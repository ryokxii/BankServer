package com.atoudeft.banque;

public class OperationTransfer extends Operation {
    private double montant = 0;
    private String numeroCompte;

    public OperationTransfer(double montant, String numeroCompte) {
        super.type = TypeOperation.TRANSFER;
        this.montant = montant;
        this.numeroCompte = numeroCompte;
    }
    
    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $\t" + this.numeroCompte;
        
    }
}
