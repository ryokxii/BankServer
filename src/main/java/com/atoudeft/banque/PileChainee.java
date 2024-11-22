package com.atoudeft.banque;

import java.io.Serializable;

public class PileChainee implements Serializable {
	Object object;
	Noeud tete; 

	public PileChainee() { 
		this.tete = null; 
	} 
	
	public void sauvegarder(Object object) {
		Noeud newNode = new Noeud(object);

		if (tete == null)
			tete = newNode;

		Noeud curr = tete;
		while (curr.next != null) {
			curr = curr.next;
		}

		curr.next = newNode;
	}

	
}
