package com.atoudeft.banque;

public class OperationTransfer extends Operation {
    private int montant = 0;
    private int numeroCompte;

    public OperationTransfer(int montant, int numeroCompte) {
        super.type = TypeOperation.TRANSFER;
        this.montant = montant;
        this.numeroCompte = numeroCompte;
    }
    
    @Override
    public String toString() {
        return super.date + "\t" + this.type + "\t" + this.montant + " $\t" + this.numeroCompte;
        
    }
}
