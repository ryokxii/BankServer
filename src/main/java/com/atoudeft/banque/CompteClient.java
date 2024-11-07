package com.atoudeft.banque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompteClient implements Serializable {
    private String numero;
    private String nip;
    private List<CompteBancaire> comptes;

    /**
     * Crée un compte-client avec un numéro et un nip.
     *
     * @param numero le numéro du compte-client
     * @param nip le nip
     */
    public CompteClient(String numero, String nip) {
        this.numero = numero;
        this.nip = nip;
        comptes = new ArrayList<>();
    }

    /**
     * Ajoute un compte bancaire au compte-client.
     *
     * @param compte le compte bancaire
     * @return true si l'ajout est réussi
     */
    public boolean ajouter(CompteBancaire compte) {
        return this.comptes.add(compte);
    }

    /**
     * Renvoie une liste de tous les comptes bancaires associés au compte-client.
     * Renvoie une copie pour préserver l'encalpsulation.
     *
     * @return Une liste qu'on ne peut pas modifier des comptes bancaires du client
     */
    public List<CompteBancaire> getComptes() {
        return new ArrayList<>(comptes);
    }

    /**
     * Retourne le nip du compte client.
     *
     * @return Le nip du compte client.
     */
    public String getNip() {
        return nip;
    }

    /**
     * Définit le nip du compte client.
     *
     * @param nip Le nouveau nip à définir pour le compte client.
     */
    public void setNip(String nip) {
        this.nip = nip;
    }
}