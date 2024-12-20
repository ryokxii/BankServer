package com.atoudeft.banque;

public class OperationRetrait extends Operation {
    private double montant = 0;

    public OperationRetrait(double montant) {
        super.type = TypeOperation.RETRAIT;
        this.montant = montant;
    }
    
    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $.";
        
    }
}
