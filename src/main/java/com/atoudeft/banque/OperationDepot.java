package com.atoudeft.banque;

public class OperationDepot extends Operation {
    private double montant = 0;

    public OperationDepot(double montant2) {
        super.type = TypeOperation.DEPOT;
        this.montant = montant2;
    }
    
    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $.";
        
    }
}
