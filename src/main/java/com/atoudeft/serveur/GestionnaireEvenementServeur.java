package com.atoudeft.serveur;

import com.atoudeft.banque.Banque;
import com.atoudeft.banque.CompteClient;
import com.atoudeft.banque.TypeCompte;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

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

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        this.banque = banque;
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
        Banque banque;
        ConnexionBanque cnx;
        String msg, numCompteClient, nip;
        String[] t;
        String typeEvenement = evenement.getType();
        String argument = evenement.getArgument();

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;
            System.out.println("SERVEUR: Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {
                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
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
                        cnx.setNumeroCompteActuel(numCompteClient);
                        cnx.envoyer("SELECT OK");
                    } else {
                        cnx.envoyer("SELECT NO");
                    }
                    break;

                /******************* TRAITEMENT PAR DÉFAUT *******************/
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}