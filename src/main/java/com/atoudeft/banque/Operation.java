package com.atoudeft.banque;

import java.io.Serializable;
import java.sql.Date;

public abstract class Operation implements Serializable {
    TypeOperation type;
    Date date;

    public Operation() {
        this.date = new Date(System.currentTimeMillis());
    }

}
