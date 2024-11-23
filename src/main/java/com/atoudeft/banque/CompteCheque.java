package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire{
    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */
    public CompteCheque(String numero, TypeCompte type) {
        super(numero, TypeCompte.CHEQUE);
    }

    /**
     * Crédite un montant au solde actuel.
     * Ajoute le montant au solde, si le montant est positif
     *
     * @param montant Le montant à créditer (positif)
     * @return ture si le montant est crédité, sinon false
     */
    @Override
    public boolean crediter(double montant) {
        if (montant >= 0){
            setSolde(getSolde() + montant);
            super.sauvegarder(new OperationDepot(montant));
            return true;
        }
        return false;
    }

    /**
     * Débite un montant au solde actuel.
     * Soustrait si le montant est positif et si le solde
     * est suffisant pour le montant à débiter.
     *
     * @param montant Le montant à débiter (positif)
     * @return true si le montant est débité, sinon false.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && getSolde()>=montant){
            setSolde(getSolde() - montant);
            super.sauvegarder(new OperationRetrait(montant));
            return true;
        }
        return false;
    }

    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        super.sauvegarder(new OperationFacture(montant, numeroFacture, description));
        return false;
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        super.sauvegarder(new OperationTransfer(montant, numeroCompteDestinataire));
        return false;
    }
}
