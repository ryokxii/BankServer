package com.atoudeft.serveur;

import com.atoudeft.banque.*;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

import java.util.Stack;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    private Banque banque;
    private CompteBancaire compteSelectionne; // Variable pour stocker le compte sélectionné
    private Stack<Operation> historique = new Stack<>(); // Historique des opérations

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        this.banque = ((ServeurBanque) serveur).getBanque();
    }

    /**
     * Permet de sélectionner un compte pour effectuer des opérations.
     *
     * @param compte Le compte bancaire à sélectionner.
     */
    public void selectionnerCompte(CompteBancaire compte) {
        this.compteSelectionne = compte;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque) serveur;
        ConnexionBanque cnx;
        String msg, numCompteClient, nip;
        String[] t;
        String typeEvenement = evenement.getType();
        String argument = evenement.getArgument();

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Reçu : " + typeEvenement + " " + argument);
            cnx.setTempsDerniereOperation(System.currentTimeMillis());

            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;

                case "LIST":
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;

                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "NOUVEAU": //Crée un nouveau compte-client :
                    if (cnx.getNumeroCompteClient() != null) {
                        cnx.envoyer("NOUVEAU NO deja connecte");
                        break;
                    }
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length < 2) {
                        cnx.envoyer("NOUVEAU NO");
                    } else {
                        numCompteClient = t[0];
                        nip = t[1];
                        banque = serveurBanque.getBanque();
                        if (banque.ajouter(numCompteClient, nip)) {
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");
                        } else
                            cnx.envoyer("NOUVEAU NO " + t[0] + " existe");
                    }
                    break;

                case "CONNECT":
                    argument = evenement.getArgument();
                    t = argument.split(":");

                    if (t.length < 2) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }
                    numCompteClient = t[0];
                    nip = t[1];
                    banque = serveurBanque.getBanque();

                    if (cnx.getNumeroCompteClient() != null) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }

                    CompteClient client = banque.getCompteClient(numCompteClient);
                    if (client == null || !client.getNip().equals(nip)) {
                        cnx.envoyer("CONNECT NO");
                        break;
                    }

                    cnx.setNumeroCompteClient(numCompteClient);
                    cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                    cnx.envoyer("CONNECT OK ");
                    break;

                case "SELECT":
                    if(cnx.getNumeroCompteClient() == null){
                        cnx.envoyer("SELECT NO");
                        break;
                    }
                    argument = evenement.getArgument().trim();
                    numCompteClient =null;
                    banque = serveurBanque.getBanque();

                    if (TypeCompte.CHEQUE.name().equalsIgnoreCase(argument)) {
                        numCompteClient = banque.getNumeroCompteType(cnx.getNumeroCompteClient(), TypeCompte.CHEQUE);
                    } else if (TypeCompte.EPARGNE.name().equalsIgnoreCase(argument)) {
                        numCompteClient = banque.getNumeroCompteType(cnx.getNumeroCompteClient(), TypeCompte.EPARGNE);
                    }

                    if (numCompteClient != null) {
                        // Mise à jour de la connexion
                        cnx.setNumeroCompteActuel(numCompteClient);

                        // Récupération du compte correspondant
                        CompteBancaire compte = banque.getCompte(numCompteClient);
                        if (compte != null) {
                            this.selectionnerCompte(compte); // Sélectionne le compte
                            cnx.envoyer("SELECT OK"); // Confirme la sélection
                        } else {
                            cnx.envoyer("SELECT NO"); // Compte introuvable
                        }
                    } else {
                        cnx.envoyer("SELECT NO"); // Aucun compte associé trouvé
                    }
                    break;

                /******************* OPÉRATIONS SPÉCIFIQUES AUX COMPTES *******************/
                case "DEPOT":
                    if (compteSelectionne == null) {
                        cnx.envoyer("Aucun compte sélectionné.");
                        break;
                    }
                    double montantDepot = Double.parseDouble(argument);
                    if (compteSelectionne.crediter(montantDepot)) {
                        if (compteSelectionne instanceof CompteEpargne) {
                            ((CompteEpargne) compteSelectionne).ajouterInterets();
                        }
                        historique.push(new OperationDepot((int)montantDepot));
                        cnx.envoyer("DEPOT OK");
                    } else {
                        cnx.envoyer("DEPOT NO");
                    }
                    break;

                case "RETRAIT":
                    if (compteSelectionne == null) {
                        cnx.envoyer("Aucun compte sélectionné.");
                        break;
                    }
                    double montantRetrait = Double.parseDouble(argument);
                    if (compteSelectionne.debiter(montantRetrait)) {
                        if (compteSelectionne instanceof CompteEpargne && compteSelectionne.getSolde() < 1000) {
                            ((CompteEpargne) compteSelectionne).setSolde(compteSelectionne.getSolde() - 2);
                        }
                        historique.push(new OperationRetrait((int)montantRetrait));
                        cnx.envoyer("RETRAIT OK");
                    } else {
                        cnx.envoyer("RETRAIT NO");
                    }
                    break;

                case "FACTURE":
                    String[] argsFacture = argument.split(" ");
                    double montantFacture = Double.parseDouble(argsFacture[0]);
                    String numeroFacture = argsFacture[1];
                    String descriptionFacture = argsFacture[2];
                    if (compteSelectionne.payerFacture(descriptionFacture, montantFacture, String.valueOf(numeroFacture))) {
                        OperationFacture operationFacture = new OperationFacture((int)montantFacture, numeroFacture, descriptionFacture);
                        historique.push(operationFacture);
                        cnx.envoyer("FACTURE OK");
                    } else {
                        cnx.envoyer("FACTURE NO");
                    }
                    break;

                case "TRANSFER":
                    String[] argsTransfer = argument.split(" ");
                    double montantTransfer = Double.parseDouble(argsTransfer[0]);
                    String compteDestinataire = argsTransfer[1];
                    if (compteSelectionne.transferer(montantTransfer, compteDestinataire)) {
                        OperationTransfer operationTransfer = new OperationTransfer((int) montantTransfer, compteDestinataire);
                        historique.push(operationTransfer);
                        cnx.envoyer("TRANSFER OK");
                    } else {
                        cnx.envoyer("TRANSFER NO");
                    }
                    break;

                case "EPARGNE":
                    if (compteSelectionne.getType() == TypeCompte.EPARGNE) {
                        cnx.envoyer("Compte EPARGNE sélectionné et géré.");
                    }
                    break;

                default:
                    msg = (typeEvenement + " " + argument).toUpperCase();
                    cnx.envoyer(msg);
                    break;
            }
        }
    }
}
