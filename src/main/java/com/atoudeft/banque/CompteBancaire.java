package com.atoudeft.banque;

import java.io.Serializable;

public abstract class CompteBancaire implements Serializable {

    private String numero;
    private TypeCompte type;
    private double solde;
    private PileChainee historique;

    /**
     * Génère un numéro de compte bancaire aléatoirement avec le format CCC00C, où C est un caractère alphabétique
     * majuscule et 0 est un chiffre entre 0 et 9.
     * @return
     */
    public static String genereNouveauNumero() {
        char[] t = new char[6];
        for (int i=0;i<3;i++) {
            t[i] = (char)((int)(Math.random()*26)+'A');
        }
        for (int i=3;i<5;i++) {
            t[i] = (char)((int)(Math.random()*10)+'0');
        }
        t[5] = (char)((int)(Math.random()*26)+'A');
        return new String(t);
    }

    /**
     * Crée un compte bancaire.
     * @param numero numéro du compte
     * @param type type du compte
     */
    public CompteBancaire(String numero, TypeCompte type) {
        this.numero = numero;
        this.type = type;
        this.solde = 0.0;
        this.historique = new PileChainee();
    }
    public String getNumero() {
        return this.numero;
    }
    public TypeCompte getType() {
        return this.type;
    }

    public double getSolde() {
        return this.solde;
    }
    
    public PileChainee getHistorique() {
        return historique;
    }
    
    public void setHistorique(PileChainee historique) {
        this.historique = historique;
    }

    public abstract boolean crediter(double montant);
    public abstract boolean debiter(double montant);
    public abstract boolean payerFacture(String numeroFacture, double montant, String description);
    public abstract boolean transferer(double montant, String numeroCompteDestinataire);

    /**
     * Définit le solde du compte
     *
     * @param solde Le nouveau solde à attribuer à cet objet
     */
    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void sauvegarder(Object object) {
		Noeud newNode = new Noeud(object);

		if (historique.tete == null)
			historique.tete = newNode;

		Noeud curr = historique.tete;
		while (curr.next != null) {
			curr = curr.next;
		}

		curr.next = newNode;
	}

    public void setNumero(String numero) {
        this.numero = numero;
    }

}

