package com.Zeditude.APCompFinal;

import java.util.List;

public class Node {
	private Board currentBoard;
	private List<Node> nodes;
	
	public Node(Board b, List<Node> n){
		currentBoard = b;
		nodes = n;
	}
}
