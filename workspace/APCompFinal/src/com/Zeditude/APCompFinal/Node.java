package com.Zeditude.APCompFinal;

import java.util.List;

public class Node {
	private Board currentBoard;
	private List<Node> nodes;
	private boolean current;
	
	public Node(Board b, List<Node> n){
		currentBoard = b;
		nodes = n;
	}
	
	public Board getBoard(){
		return currentBoard;
	}
	
	public List<Node> getNodes(){
		return nodes;
	}
	
	public boolean isCurrent(){
		return current;
	}
}
