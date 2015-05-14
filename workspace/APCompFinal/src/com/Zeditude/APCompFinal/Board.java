package com.Zeditude.APCompFinal;
import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

public class Board {
	private Context context;
	private Piece[][] board;
	private Team turn;

	public Board(Context con, int rows, int cols) {
		board = new Piece[rows][cols];
		context = con;
		
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[i].length; j++)
				board[i][j] = new Piece();
		
		for(int c = 0; c < board.length; c++){
			board[1][c] = new Piece(Type.PAWN, Team.BLACK);
			board[6][c] = new Piece(Type.PAWN, Team.WHITE);
		}
		
		board[0][0] = new Piece(Type.ROOK, Team.BLACK);
		board[0][7] = new Piece(Type.ROOK, Team.BLACK);
		board[7][0] = new Piece(Type.ROOK, Team.WHITE);
		board[7][7] = new Piece(Type.ROOK, Team.WHITE);
		
		board[0][1] = new Piece(Type.KNIGHT, Team.BLACK);
		board[0][6] = new Piece(Type.KNIGHT, Team.BLACK);
		board[7][1] = new Piece(Type.KNIGHT, Team.WHITE);
		board[7][6] = new Piece(Type.KNIGHT, Team.WHITE);
		
		board[0][2] = new Piece(Type.BISHOP, Team.BLACK);
		board[0][5] = new Piece(Type.BISHOP, Team.BLACK);
		board[7][2] = new Piece(Type.BISHOP, Team.WHITE);
		board[7][5] = new Piece(Type.BISHOP, Team.WHITE);
		
		board[0][3] = new Piece(Type.QUEEN, Team.BLACK);
		board[0][4] = new Piece(Type.KING, Team.BLACK);
		board[7][3] = new Piece(Type.QUEEN, Team.WHITE);
		board[7][4] = new Piece(Type.KING, Team.WHITE);
		
		setLocations();
	}
	
	public int getRow(){
		return board.length;
	}
	
	public int getCol(){
		return board[0].length;
	}
	
	public Piece getPiece(int r, int c){
		return board[r][c];
	}

	public void nextTurn() {
		if (turn == Team.WHITE)
			turn = Team.BLACK;
		else
			turn = Team.WHITE;
	}

	public void select(int r, int c) {
		for(int i = 0; i < board.length; i++)
			for(int j = 0; j < board[i].length; j++)
				board[i][j].setSelected(false);
		
		Piece s = board[r][c];
		
		if(s.getType() != Type.BLANK){
			s.setSelected(true);
			Toast.makeText(context, "" + s.getMoveLoc(), Toast.LENGTH_LONG).show();
		}
	}

	public void setLocations() {
		for(int r = 0; r < board.length; r++)
			for(int c = 0; c < board[r].length; c++)
				board[r][c].setProtected(false, Team.BLANK);
		
		ArrayList<Piece> kings = new ArrayList<Piece>();
		ArrayList<Integer> row = new ArrayList<Integer>();
		ArrayList<Integer> col = new ArrayList<Integer>();
		
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				Piece p = board[r][c];
				
				if (p.getType() != Type.BLANK && p.getType() != Type.KING)
					setPieceLocation(p, r, c);
				else if(p.getType() == Type.KING){
					kings.add(p);
					row.add(r);
					col.add(c);
				}
					
			}
		}
		
		for(int i = 0; i < kings.size(); i++)
			setPieceLocation(kings.get(i), row.get(i), col.get(i));
		
	}

	public void setPieceLocation(Piece p, int r, int c) {
		switch (p.getType()) {
		case PAWN:
			setPawnLoc(p, r, c);
			break;

		case KNIGHT:
			setKnightLoc(p, r, c);
			break;

		case BISHOP:
			setBishopLoc(p, r, c);
			break;

		case ROOK:
			setRookLoc(p, r, c);
			break;

		case QUEEN:
			setQueenLoc(p, r, c);
			break;

		case KING:
			setKingLoc(p, r, c);
			break;
		}
	}

	public void setPawnLoc(Piece p, int r, int c) {
		if (p.getColor() == Team.WHITE) {
			p.addMoveLoc(new Location(r - 1, c));

			if (!p.hasMoved())
				p.addMoveLoc(new Location(r - 2, c));

			if (c + 1 < board[r].length){
				if (board[r - 1][c + 1].getColor() == Team.BLACK)
					p.addMoveLoc(new Location(r - 1, c + 1));
				
				board[r - 1][c + 1].setProtected(true, p.getColor());
			}

			if (c - 1 > 0){
				if (board[r - 1][c - 1].getColor() == Team.BLACK)
					p.addMoveLoc(new Location(r - 1, c - 1));
				
				board[r - 1][c - 1].setProtected(true, p.getColor());
			}
		} else {
			p.addMoveLoc(new Location(r + 1, c));

			if (!p.hasMoved())
				p.addMoveLoc(new Location(r + 2, c));

			if (c + 1 < board.length){
				if (board[r + 1][c + 1].getColor() == Team.WHITE)
					p.addMoveLoc(new Location(r + 1, c + 1));
				
				board[r + 1][c + 1].setProtected(true, p.getColor());
			}

			if (c - 1 > 0){
				if (board[r + 1][c - 1].getColor() == Team.WHITE)
					p.addMoveLoc(new Location(r + 1, c - 1));
			
				board[r + 1][c - 1].setProtected(true, p.getColor());
			}
		}
	}

	public void setKnightLoc(Piece p, int r, int c) {

	}

	public void setBishopLoc(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c + 1;
		while (tempR < board.length && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
			tempC++;
		}
		
		if(tempR < board.length && tempC < board[tempR].length){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}

		tempR = r - 1;
		tempC = c + 1;
		while (tempR >= 0 && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
			tempC++;
		}
		
		if(tempR >= 0 && tempC < board[tempR].length){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}

		tempR = r + 1;
		tempC = c - 1;
		while (tempR < board.length && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
			tempC--;
		}
		
		if(tempR < board.length && tempC >= 0){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}

		tempR = r - 1;
		tempC = c - 1;
		while (tempR >= 0 && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
			tempC--;
		}
		
		if(tempR >= 0 && tempC >= 0){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}
	}

	public void setRookLoc(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c;
		while(tempR < board.length && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
		}
		
		if(tempR < board.length){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}
		
		
		tempR = r - 1;
		tempC = c;
		while(tempR >= 0 && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
		}
		
		if(tempR >= 0){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}
		
		
		tempR = r;
		tempC = c + 1;
		while(tempC < board[tempR].length && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC++;
		}
		
		if(tempC < board[tempR].length){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}
		
		
		tempR = r;
		tempC = c - 1;
		while(tempC >= 0 && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC--;
		}
		
		if(tempC >= 0){
			if(board[tempR][tempC].getColor() != p.getColor())
				p.addMoveLoc(new Location(tempR, tempC));
			
			board[tempR][tempC].setProtected(true, p.getColor());
		}
	}

	public void setQueenLoc(Piece p, int r, int c) {
		setBishopLoc(p, r, c);
		setRookLoc(p, r, c);
	}

	public void setKingLoc(Piece p, int r, int c) {
		if(r + 1 < board.length){
			if(board[r + 1][c].getType() == Type.BLANK || 
					(board[r + 1][c].getColor() != p.getColor() && !board[r + 1][c].isProtected()))
				p.addMoveLoc(new Location(r + 1, c));
			
			board[r + 1][c].setProtected(true, p.getColor());
			
			if(c + 1 < board[r].length){
				if(board[r + 1][c + 1].getType() == Type.BLANK || 
						(board[r + 1][c + 1].getColor() != p.getColor() && !board[r + 1][c + 1].isProtected()))
					p.addMoveLoc(new Location(r + 1, c + 1));
				
				board[r + 1][c + 1].setProtected(true, p.getColor());
			}
			
			if(c - 1 >= 0){
				if(board[r + 1][c - 1].getType() == Type.BLANK || 
						(board[r + 1][c - 1].getColor() != p.getColor() && !board[r + 1][c - 1].isProtected()))
					p.addMoveLoc(new Location(r + 1, c - 1));
				
				board[r + 1][c - 1].setProtected(true, p.getColor());
			}
		}
		
		if(c + 1 < board[r].length){
			if(board[r][c + 1].getType() == Type.BLANK || 
					(board[r][c + 1].getColor() != p.getColor() && !board[r][c + 1].isProtected()))
				p.addMoveLoc(new Location(r, c + 1));
			
			board[r][c + 1].setProtected(true, p.getColor());
		}
		
		
		if(c - 1 >= 0){
			if(board[r][c - 1].getType() == Type.BLANK || 
					(board[r][c - 1].getColor() != p.getColor() && !board[r][c - 1].isProtected()))
				p.addMoveLoc(new Location(r, c - 1));
			
			board[r][c - 1].setProtected(true, p.getColor());
		}
		
		if(r - 1 >= 0){
			if(board[r - 1][c].getType() == Type.BLANK || 
					(board[r - 1][c].getColor() != p.getColor() && !board[r - 1][c].isProtected()))
				p.addMoveLoc(new Location(r - 1, c));
			
			board[r - 1][c].setProtected(true, p.getColor());
			
			if(c + 1 < board[r].length){
				if(board[r - 1][c + 1].getType() == Type.BLANK || 
						(board[r - 1][c + 1].getColor() != p.getColor() && !board[r - 1][c + 1].isProtected()))
					p.addMoveLoc(new Location(r - 1, c + 1));
				
				board[r - 1][c + 1].setProtected(true, p.getColor());
			}
			
			if(c - 1 >= 0){
				if(board[r - 1][c - 1].getType() == Type.BLANK || 
						(board[r - 1][c - 1].getColor() != p.getColor() && !board[r - 1][c - 1].isProtected()))
					p.addMoveLoc(new Location(r - 1, c - 1));
				
				board[r - 1][c - 1].setProtected(true, p.getColor());
			}
		}
	}
}