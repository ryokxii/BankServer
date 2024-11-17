package com.atoudeft.banque;

import java.io.Serializable;

import javafx.scene.Node;

public class Noeud implements Serializable{
	int data; 
	Node prev; 
	Node next; 

	public Noeud(int data) 
	{ 
		this.data = data; 
		this.prev = null; 
		this.next = null; 
	} 

}
