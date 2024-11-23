package com.atoudeft.banque;

public class OperationDepot extends Operation {
    private double montant = 0;

    public OperationDepot(double montant) {
        super.type = TypeOperation.DEPOT;
        this.montant = montant;
    }
    
    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $.";
        
    }
}
