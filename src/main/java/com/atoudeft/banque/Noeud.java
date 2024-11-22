package com.atoudeft.banque;

import java.io.Serializable;

public class Noeud implements Serializable{
	Object data; 
	Noeud next; 

	public Noeud(Object data) 
	{ 
		this.data = data; 
		this.next = null; 
	} 

}
