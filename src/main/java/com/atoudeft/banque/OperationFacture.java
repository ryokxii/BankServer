package com.atoudeft.banque;

public class OperationFacture extends Operation {
    private double montant = 0;
    private String numeroFacture;
    private String description = "";

    public OperationFacture(double montant, String numeroFacture, String description) {
        super.type = TypeOperation.FACTURE;
        this.montant = montant;
        this.numeroFacture = numeroFacture;
        this.description = description;
    }

    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $\t" + this.numeroFacture + "\n" + this.description;
        
    }
}
