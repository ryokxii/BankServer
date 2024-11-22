package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire {
    private static final double FRAIS = 2.0; // Frais si solde inférieur à 1000 $
    private static final double LIMITE_SOLDE = 1000.0; // Limite pour appliquer des frais
    private double tauxInteret;

    // Constructeur pour initialiser un compte épargne
    public CompteEpargne(String numero) {
        super(numero, TypeCompte.EPARGNE); // Type compte épargne
        this.tauxInteret = 0.05; // Taux d'intérêt de 5% pour les comptes épargne
    }

    @Override
    public boolean crediter(double montant) {
        if (montant > 0) {
            setSolde(getSolde() + montant);
            return true;
        }
        return false;
    }

    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && getSolde() >= montant) {
            setSolde(getSolde() - montant);
            // Applique des frais si le solde devient inférieur à 1000 $
            if (getSolde() < LIMITE_SOLDE) {
                setSolde(getSolde() - FRAIS);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false; // Pas d'implémentation pour les factures dans le cadre des comptes épargne
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false; // Pas d'implémentation pour les transferts dans le cadre des comptes épargne
    }

    // Méthode pour ajouter des intérêts sur le solde du compte
    public void ajouterInterets() {
        double interets = getSolde() * tauxInteret;
        setSolde(getSolde() + interets);
    }
}