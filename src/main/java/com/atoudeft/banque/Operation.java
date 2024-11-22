package com.atoudeft.banque;

import java.io.Serializable;
import java.util.Date;

public abstract class Operation implements Serializable {
     TypeOperation type;
     Date date;

    public Operation() {
        this.date = new Date(System.currentTimeMillis());
    }

    public Operation(TypeOperation type) {
        this.type = type;
        this.date = new Date();  // Date actuelle lors de la création de l'opération
    }

    public TypeOperation getType() {
        return this.type;
    }

    public Date getDate() {
        return this.date;
    }

    @Override
    public abstract String toString();
}