package com.atoudeft.banque;

import java.io.Serializable;

import javafx.scene.Node;

public class PileChainee implements Serializable{
	Node head; 
	Node tail; 

	public PileChainee() { 
		this.head = null; 
		this.tail = null; 
	} 

}
