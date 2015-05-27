package com.Zeditude.APCompFinal;

import java.util.ArrayList;
import java.util.List;

public class AI {
	public List<Node> start;
	int maxType;
	int minType;
	int smartIndex;
	boolean checkMate;
	int maxProtect;
	int maxRow;
	int maxCol;
	//boolean freePiece;
	//boolean protect;
	//boolean attack;

	public AI(Board b, Team t) {
		resetBoard(b, t);
	}

	/**
	 * The reset board method will recreate the board from the screen
	 * and check every possible move that the ai could do and checks to see
	 * which is the "smartest" move
	 * @param b the current board of the screen
	 * @param t the team of which is the ai's team
	 */
	
	public void resetBoard(Board b, Team t) {
		start = new ArrayList<Node>();
		smartIndex = -1;
		maxType = -1;
		minType = 7;
		checkMate = false;
		maxProtect = -1;
		maxRow = -1;
		maxCol = -1;

		Piece[][] board = b.getBoard();

		Piece[][] newBoard = new Piece[board.length][board[0].length];

		for (int r = 0; r < newBoard.length; r++)
			for (int c = 0; c < newBoard[r].length; c++)
				newBoard[r][c] = new Piece(board[r][c]);
		
		Team op = (t == Team.WHITE) ? Team.BLACK : Team.WHITE;
		
		for (int i = 0; i < newBoard.length; i++) {
			for (int j = 0; j < newBoard[i].length; j++) {
				if (newBoard[i][j].getColor() == t) {
					if (!newBoard[i][j].isProtected(t) && 
							newBoard[i][j].isProtected(op)) {
						if (newBoard[i][j].getType().getValue() > maxProtect) {
							maxProtect = newBoard[i][j].getType().getValue();
							maxRow = i;
							maxCol = j;
						}
					}
				}
			}
		}

		for (int r = 0; r < newBoard.length; r++) {
			for (int c = 0; c < newBoard[r].length; c++) {
				if (newBoard[r][c].getColor() == t) {
					Piece p = new Piece(newBoard[r][c]);
					List<Location> loc = p.getMoveLoc();
					for (Location l : loc) {
						start.add(createNode(new Board(newBoard, t), p, r, c,
								l, t));
					}
				}
			}
		}

		if (smartIndex == -1)
			smartIndex = (int) (Math.random() * start.size());
	}

	/**
	 * The create node method will recreate the recreated board from the reset board method.
	 * it will be called the exact number of times that the ai can move. It will check to see if
	 * the current move is a good one, and will set a worldly variable called smartIndex to the
	 * current move, so that when the board class asks for a move, it will give the smartIndex
	 * 
	 * @param board the recreated board of which the reset board method will use
	 * @param p the piece that is moving
	 * @param r the current row that the piece is on
	 * @param c the current col that the piece is on
	 * @param l the location of which the piece wants to move
	 * @param t the team of the ai
	 * @return a Node consisting of a recreated board with the ai's move on it
	 */
	
	private Node createNode(Board board, Piece p, int r, int c, Location l,
			Team t) {
		Team op = (t == Team.WHITE) ? Team.BLACK : Team.WHITE;
		Piece[][] currentBoard = board.getBoard();

		Piece[][] newBoard = new Piece[currentBoard.length][currentBoard[0].length];

		for (int i = 0; i < newBoard.length; i++)
			for (int j = 0; j < newBoard[i].length; j++)
				newBoard[i][j] = new Piece(currentBoard[i][j]);

		if(!checkMate){
			if (!newBoard[l.getRow()][l.getCol()].isProtected(op)
					&& newBoard[l.getRow()][l.getCol()].getType() != Type.BLANK) {
				if (newBoard[l.getRow()][l.getCol()].getType().getValue() > maxType) {
					minType = p.getType().getValue();
					maxType = newBoard[l.getRow()][l.getCol()].getType().getValue();
					smartIndex = start.size();
				}else if(newBoard[l.getRow()][l.getCol()].getType().getValue() == maxType){
					if(p.getType().getValue() < minType){
						minType = p.getType().getValue();
						smartIndex = start.size();
					}
				}
			} else if (newBoard[l.getRow()][l.getCol()].isProtected(op)
					&& newBoard[l.getRow()][l.getCol()].getType() != Type.BLANK
					&& maxType < newBoard[l.getRow()][l.getCol()].getType().getValue()) {
				if (p.getType().getValue() <= newBoard[l.getRow()][l.getCol()].getType().getValue()) {
					int rand = (int) (Math.random() * 2);
	
					if (rand == 1
							&& p.getType().getValue() == newBoard[l.getRow()][l
									.getCol()].getType().getValue()){
						smartIndex = start.size();
					}else if (p.getType().getValue() < newBoard[l.getRow()][l
							.getCol()].getType().getValue()){
						smartIndex = start.size();
					}
				}
			}
		}

		newBoard[l.getRow()][l.getCol()] = p;
		newBoard[r][c] = new Piece();
		p.setHasMoved(true);
		
		if (p.getType() == Type.KING && Math.abs(c - l.getCol()) > 1) {
			if (c < l.getCol()) {
				Piece rook = newBoard[l.getRow()][l.getCol() + 1];
				newBoard[l.getRow()][l.getCol() - 1] = rook;
				newBoard[l.getRow()][l.getCol() + 1] = new Piece();
			} else {
				Piece rook = newBoard[r][l.getCol() - 2];
				newBoard[l.getRow()][l.getCol() + 1] = rook;
				newBoard[l.getRow()][l.getCol() - 2] = new Piece();
			}
		}
		
		Board b = new Board(newBoard, op);
		b.setProtections();
		
		if(!checkMate){
			if (maxProtect > maxType){
				if (b.getBoard()[maxRow][maxCol].isProtected(t) || !b.getBoard()[maxRow][maxCol].isProtected(op)){
					if(!newBoard[l.getRow()][l.getCol()].isProtected(op))
						smartIndex = start.size();
					else if(newBoard[l.getRow()][l.getCol()].getType().getValue() <= maxProtect)
						smartIndex = start.size();
				}
			}
		}
		
		b.setLocations();
		
		if(b.isInCheck(op)){
			if(b.isMate(op)){
				smartIndex = start.size();
				checkMate = true;
			}
		}

		return new Node(b, null);
	}

	public Piece[][] getBoard() {
		return start.get(smartIndex).getBoard().getBoard();
	}
}
