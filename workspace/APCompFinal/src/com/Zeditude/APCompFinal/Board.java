package com.Zeditude.APCompFinal;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

public class Board implements Cloneable {
	private Context context;
	private Piece[][] board;
	private Team turn;
	private AI ai;

	public Board(Context con, int rows, int cols) {
		board = new Piece[rows][cols];
		context = con;

		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				board[i][j] = new Piece();

		for (int c = 0; c < board.length; c++) {
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

		turn = Team.WHITE;
		
		setLocations();
		setProtections();
		
		ai = new AI(this, turn);
	}

	public Board(Piece[][] b, Team t) {
		this.board = b;
		this.turn = t;
		setProtections();
	}

	public int getRow() {
		return board.length;
	}

	public int getCol() {
		return board[0].length;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public Piece getPiece(int r, int c) {
		return board[r][c];
	}

	public boolean isValid(int row1, int col1, int row2, int col2) {
		Piece[][] board2 = new Piece[board.length][board[0].length];

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board2[r][c] = new Piece(board[r][c]);

		Piece p = board2[row1][col1];
		board2[row2][col2] = p;
		board2[row1][col1] = new Piece();

		Board b2 = new Board(board2, turn);
		return !b2.isInCheck(p.getColor());
	}

	public boolean isInCheck(Team t) {
		Piece king = null;

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				if (board[r][c].getType() == Type.KING
						&& board[r][c].getColor() == t)
					king = board[r][c];
		Team op = king.getColor();

		if (op == Team.WHITE)
			op = Team.BLACK;
		else
			op = Team.WHITE;

		return king.isProtected(op);
	}
	
	public boolean isStaleMate(){
		boolean kingsOnly = true;
		
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				if(board[r][c].getType() != Type.BLANK && board[r][c].getType() != Type.KING)
					kingsOnly = false;
		
		boolean noMovesWhite = true;
		boolean noMovesBlack = true;
		
		for (int r = 0; r < board.length; r++){
			for (int c = 0; c < board[r].length; c++){
				Piece p = board[r][c];
				
				if(p.getColor() == Team.WHITE){
					if(p.getMoveLoc().size() > 0)
						noMovesWhite = false;
				}else if(p.getColor() == Team.BLACK){
					if(p.getMoveLoc().size() > 0)
						noMovesBlack = false;
				}
			}
		}
		
		return (noMovesWhite && !isInCheck(Team.WHITE)) || (noMovesBlack && !isInCheck(Team.BLACK)) || kingsOnly;
				
	}

	public boolean isMate(Team t) {
		boolean flag = true;

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				if (board[r][c].getColor() == t)
					if (board[r][c].getMoveLoc().size() != 0)
						flag = false;

		return flag;

	}

	public void nextTurn() {
		if (turn == Team.WHITE)
			turn = Team.BLACK;
		else
			turn = Team.WHITE;
	}

	public void select(int r, int c) {
		int oldR = -1, oldC = -1;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j].isSelected()) {
					oldR = i;
					oldC = j;
				}

				board[i][j].setSelected(false);
			}
		}

		Piece s;
		if (oldR == -1 && oldC == -1)
			s = board[r][c];
		else
			s = board[oldR][oldC];

		if (r == oldR && c == oldC)
			board[r][c].setSelected(false);
		else if (oldR == -1 && oldC == -1) {
			if (s.getType() != Type.BLANK && s.getColor() == turn)
				s.setSelected(true);
		} else if (s.canMoveTo(r, c))
			movePiece(s, oldR, oldC, r, c);
		else if (board[r][c].getType() != Type.BLANK && board[r][c].getColor() == turn)
			board[r][c].setSelected(true);
		else
			board[r][c].setSelected(false);
	}

	public void movePiece(Piece s, int oldR, int oldC, int r, int c) {
		s.setHasMoved(true);
		board[r][c] = s;
		board[oldR][oldC] = new Piece();
		
		if(s.getType() == Type.KING && Math.abs(oldC - c) > 1){
			if(oldC < c){
				Piece rook = board[r][c + 1];
				board[r][c - 1] = rook;
				board[r][c + 1] = new Piece();
			}else{
				Piece rook = board[r][c - 2];
				board[r][c + 1] = rook;
				board[r][c - 2] = new Piece();
			}
		}

		setLocations();
		setProtections();

		Team op = s.getColor();
		if (op == Team.WHITE)
			op = Team.BLACK;
		else
			op = Team.WHITE;

		if (isInCheck(op)) {
			if (isMate(op))
				Toast.makeText(context, "Check mate " + s.getColor() + " wins",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(context, op + " is in check", Toast.LENGTH_LONG)
						.show();
		}

		setLocations();
		setProtections();

		nextTurn();
		
		ai.resetBoard(this, turn);
		this.board = ai.getBoard();
		nextTurn();
	}

	public void setLocations() {
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c].removeLocations();

		ArrayList<Piece> kings = new ArrayList<Piece>();
		ArrayList<Integer> row = new ArrayList<Integer>();
		ArrayList<Integer> col = new ArrayList<Integer>();

		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				Piece p = board[r][c];

				if (p.getType() != Type.BLANK && p.getType() != Type.KING)
					setPieceLocation(p, r, c);
				else if (p.getType() == Type.KING) {
					kings.add(p);
					row.add(r);
					col.add(c);
				}

			}
		}

		for (int i = 0; i < kings.size(); i++)
			setPieceLocation(kings.get(i), row.get(i), col.get(i));

	}

	public void setProtections() {
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[r].length; c++)
				board[r][c].setProtected(false, Team.BLANK);

		ArrayList<Piece> kings = new ArrayList<Piece>();
		ArrayList<Integer> row = new ArrayList<Integer>();
		ArrayList<Integer> col = new ArrayList<Integer>();

		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				Piece p = board[r][c];

				if (p.getType() != Type.BLANK && p.getType() != Type.KING)
					setPieceProtection(p, r, c);
				else if (p.getType() == Type.KING) {
					kings.add(p);
					row.add(r);
					col.add(c);
				}

			}
		}

		for (int i = 0; i < kings.size(); i++)
			setPieceProtection(kings.get(i), row.get(i), col.get(i));
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

	public void setPieceProtection(Piece p, int r, int c) {
		switch (p.getType()) {
		case PAWN:
			setPawnProtect(p, r, c);
			break;

		case KNIGHT:
			setKnightProtect(p, r, c);
			break;

		case BISHOP:
			setBishopProtect(p, r, c);
			break;

		case ROOK:
			setRookProtect(p, r, c);
			break;

		case QUEEN:
			setQueenProtect(p, r, c);
			break;

		case KING:
			setKingProtect(p, r, c);
			break;
		}
	}

	public void setPawnProtect(Piece p, int r, int c) {
		if (p.getColor() == Team.WHITE) {
			if (r - 1 >= 0) {
				if (c + 1 < board[r].length)
					board[r - 1][c + 1].setProtected(true, p.getColor());

				if (c - 1 >= 0)
					board[r - 1][c - 1].setProtected(true, p.getColor());
			}
		} else {
			if (r + 1 < board.length) {
				if (c + 1 < board.length)
					board[r + 1][c + 1].setProtected(true, p.getColor());
				if (c - 1 >= 0)
					board[r + 1][c - 1].setProtected(true, p.getColor());
			}
		}
	}

	public void setKnightProtect(Piece p, int r, int c) {
		if (r + 1 < board.length) {
			if (c + 2 < board[r].length)
				board[r + 1][c + 2].setProtected(true, p.getColor());

			if (c - 2 >= 0)
				board[r + 1][c - 2].setProtected(true, p.getColor());

			if (r + 2 < board.length) {
				if (c + 1 < board[r].length)
					board[r + 2][c + 1].setProtected(true, p.getColor());

				if (c - 1 >= 0)
					board[r + 2][c - 1].setProtected(true, p.getColor());
			}
		}

		if (r - 1 >= 0) {
			if (c + 2 < board[r].length)
				board[r - 1][c + 2].setProtected(true, p.getColor());

			if (c - 2 >= 0)
				board[r - 1][c - 2].setProtected(true, p.getColor());

			if (r - 2 >= 0) {
				if (c + 1 < board[r].length)
					board[r - 2][c + 1].setProtected(true, p.getColor());

				if (c - 1 >= 0)
					board[r - 2][c - 1].setProtected(true, p.getColor());
			}
		}
	}

	public void setBishopProtect(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c + 1;
		while (tempR < board.length && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
			tempC++;
		}

		if (tempR < board.length && tempC < board[tempR].length)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r - 1;
		tempC = c + 1;
		while (tempR >= 0 && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
			tempC++;
		}

		if (tempR >= 0 && tempC < board[tempR].length)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r + 1;
		tempC = c - 1;
		while (tempR < board.length && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
			tempC--;
		}

		if (tempR < board.length && tempC >= 0)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r - 1;
		tempC = c - 1;
		while (tempR >= 0 && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
			tempC--;
		}

		if (tempR >= 0 && tempC >= 0)
			board[tempR][tempC].setProtected(true, p.getColor());
	}

	public void setRookProtect(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c;
		while (tempR < board.length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR++;
		}

		if (tempR < board.length)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r - 1;
		tempC = c;
		while (tempR >= 0 && board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
		}

		if (tempR >= 0)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r;
		tempC = c + 1;
		while (tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC++;
		}

		if (tempC < board[tempR].length)
			board[tempR][tempC].setProtected(true, p.getColor());

		tempR = r;
		tempC = c - 1;
		while (tempC >= 0 && board[tempR][tempC].getType() == Type.BLANK) {
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC--;
		}

		if (tempC >= 0)
			board[tempR][tempC].setProtected(true, p.getColor());
	}

	public void setQueenProtect(Piece p, int r, int c) {
		setBishopProtect(p, r, c);
		setRookProtect(p, r, c);
	}

	public void setKingProtect(Piece p, int r, int c) {
		if (r + 1 < board.length) {
			board[r + 1][c].setProtected(true, p.getColor());

			if (c + 1 < board[r].length)
				board[r + 1][c + 1].setProtected(true, p.getColor());

			if (c - 1 >= 0)
				board[r + 1][c - 1].setProtected(true, p.getColor());
		}

		if (c + 1 < board[r].length)
			board[r][c + 1].setProtected(true, p.getColor());

		if (c - 1 >= 0)
			board[r][c - 1].setProtected(true, p.getColor());

		if (r - 1 >= 0) {
			board[r - 1][c].setProtected(true, p.getColor());

			if (c + 1 < board[r].length)
				board[r - 1][c + 1].setProtected(true, p.getColor());

			if (c - 1 >= 0)
				board[r - 1][c - 1].setProtected(true, p.getColor());
		}
	}

	public void setPawnLoc(Piece p, int r, int c) {
		if (p.getColor() == Team.WHITE) {
			if (r - 1 >= 0) {
				if (board[r - 1][c].getType() == Type.BLANK
						&& isValid(r, c, r - 1, c)) {
					p.addMoveLoc(new Location(r - 1, c));

					if (!p.hasMoved())
						if (r - 2 >= 0
								&& board[r - 2][c].getType() == Type.BLANK
								&& isValid(r, c, r - 2, c))
							p.addMoveLoc(new Location(r - 2, c));
				}

				if (c + 1 < board[r].length)
					if (board[r - 1][c + 1].getColor() == Team.BLACK
							&& isValid(r, c, r - 1, c + 1))
						p.addMoveLoc(new Location(r - 1, c + 1));

				if (c - 1 >= 0)
					if (board[r - 1][c - 1].getColor() == Team.BLACK
							&& isValid(r, c, r - 1, c - 1))
						p.addMoveLoc(new Location(r - 1, c - 1));
			}
		} else {
			if (r + 1 < board.length) {
				if (board[r + 1][c].getType() == Type.BLANK
						&& isValid(r, c, r + 1, c)) {
					p.addMoveLoc(new Location(r + 1, c));

					if (!p.hasMoved())
						if (r + 2 < board.length
								&& board[r + 2][c].getType() == Type.BLANK
								&& isValid(r, c, r + 2, c))
							p.addMoveLoc(new Location(r + 2, c));
				}

				if (c + 1 < board.length)
					if (board[r + 1][c + 1].getColor() == Team.WHITE
							&& isValid(r, c, r + 1, c + 1))
						p.addMoveLoc(new Location(r + 1, c + 1));

				if (c - 1 >= 0)
					if (board[r + 1][c - 1].getColor() == Team.WHITE
							&& isValid(r, c, r + 1, c - 1))
						p.addMoveLoc(new Location(r + 1, c - 1));
			}
		}
	}

	public void setKnightLoc(Piece p, int r, int c) {
		if (r + 1 < board.length) {
			if (c + 2 < board[r].length)
				if (board[r + 1][c + 2].getColor() != p.getColor()
						&& isValid(r, c, r + 1, c + 2))
					p.addMoveLoc(new Location(r + 1, c + 2));

			if (c - 2 >= 0)
				if (board[r + 1][c - 2].getColor() != p.getColor()
						&& isValid(r, c, r + 1, c - 2))
					p.addMoveLoc(new Location(r + 1, c - 2));

			if (r + 2 < board.length) {
				if (c + 1 < board[r].length)
					if (board[r + 2][c + 1].getColor() != p.getColor()
							&& isValid(r, c, r + 2, c + 1))
						p.addMoveLoc(new Location(r + 2, c + 1));

				if (c - 1 >= 0)
					if (board[r + 2][c - 1].getColor() != p.getColor()
							&& isValid(r, c, r + 2, c - 1))
						p.addMoveLoc(new Location(r + 2, c - 1));
			}
		}

		if (r - 1 >= 0) {
			if (c + 2 < board[r].length)
				if (board[r - 1][c + 2].getColor() != p.getColor()
						&& isValid(r, c, r - 1, c + 2))
					p.addMoveLoc(new Location(r - 1, c + 2));

			if (c - 2 >= 0)
				if (board[r - 1][c - 2].getColor() != p.getColor()
						&& isValid(r, c, r - 1, c - 2))
					p.addMoveLoc(new Location(r - 1, c - 2));

			if (r - 2 >= 0) {
				if (c + 1 < board[r].length)
					if (board[r - 2][c + 1].getColor() != p.getColor()
							&& isValid(r, c, r - 2, c + 1))
						p.addMoveLoc(new Location(r - 2, c + 1));

				if (c - 1 >= 0)
					if (board[r - 2][c - 1].getColor() != p.getColor()
							&& isValid(r, c, r - 2, c - 1))
						p.addMoveLoc(new Location(r - 2, c - 1));
			}
		}
	}

	public void setBishopLoc(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c + 1;
		while (tempR < board.length && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR++;
			tempC++;
		}

		if (tempR < board.length && tempC < board[tempR].length)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r - 1;
		tempC = c + 1;
		while (tempR >= 0 && tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR--;
			tempC++;
		}

		if (tempR >= 0 && tempC < board[tempR].length)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r + 1;
		tempC = c - 1;
		while (tempR < board.length && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR++;
			tempC--;
		}

		if (tempR < board.length && tempC >= 0)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r - 1;
		tempC = c - 1;
		while (tempR >= 0 && tempC >= 0
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR--;
			tempC--;
		}

		if (tempR >= 0 && tempC >= 0)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
	}

	public void setRookLoc(Piece p, int r, int c) {
		int tempR = r + 1, tempC = c;
		while (tempR < board.length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR++;
		}

		if (tempR < board.length)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r - 1;
		tempC = c;
		while (tempR >= 0 && board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempR--;
		}

		if (tempR >= 0)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r;
		tempC = c + 1;
		while (tempC < board[tempR].length
				&& board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempC++;
		}

		if (tempC < board[tempR].length)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));

		tempR = r;
		tempC = c - 1;
		while (tempC >= 0 && board[tempR][tempC].getType() == Type.BLANK) {
			if (isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
			tempC--;
		}

		if (tempC >= 0)
			if (board[tempR][tempC].getColor() != p.getColor()
					&& isValid(r, c, tempR, tempC))
				p.addMoveLoc(new Location(tempR, tempC));
	}

	public void setQueenLoc(Piece p, int r, int c) {
		setBishopLoc(p, r, c);
		setRookLoc(p, r, c);
	}

	public void setKingLoc(Piece p, int r, int c) {
		Team op = p.getColor();

		if (op == Team.WHITE)
			op = Team.BLACK;
		else
			op = Team.WHITE;

		if (r + 1 < board.length) {
			if (!board[r + 1][c].isProtected(op)
					&& (board[r + 1][c].getType() == Type.BLANK || board[r + 1][c]
							.getColor() != p.getColor())
					&& isValid(r, c, r + 1, c))
				p.addMoveLoc(new Location(r + 1, c));

			if (c + 1 < board[r].length) {
				if (!board[r + 1][c + 1].isProtected(op)
						&& (board[r + 1][c + 1].getType() == Type.BLANK || board[r + 1][c + 1]
								.getColor() != p.getColor())
						&& isValid(r, c, r + 1, c + 1))
					p.addMoveLoc(new Location(r + 1, c + 1));
			}

			if (c - 1 >= 0) {
				if (!board[r + 1][c - 1].isProtected(op)
						&& (board[r + 1][c - 1].getType() == Type.BLANK || board[r + 1][c - 1]
								.getColor() != p.getColor())
						&& isValid(r, c, r + 1, c - 1))
					p.addMoveLoc(new Location(r + 1, c - 1));
			}
		}

		if (c + 1 < board[r].length) {
			if (!board[r][c + 1].isProtected(op)
					&& (board[r][c + 1].getType() == Type.BLANK || board[r][c + 1]
							.getColor() != p.getColor())
					&& isValid(r, c, r, c + 1))
				p.addMoveLoc(new Location(r, c + 1));
		}

		if (c - 1 >= 0) {
			if (!board[r][c - 1].isProtected(op)
					&& (board[r][c - 1].getType() == Type.BLANK || board[r][c - 1]
							.getColor() != p.getColor())
					&& isValid(r, c, r, c - 1))
				p.addMoveLoc(new Location(r, c - 1));
		}

		if (r - 1 >= 0) {
			if (!board[r - 1][c].isProtected(op)
					&& (board[r - 1][c].getType() == Type.BLANK || board[r - 1][c]
							.getColor() != p.getColor())
					&& isValid(r, c, r - 1, c))
				p.addMoveLoc(new Location(r - 1, c));

			if (c + 1 < board[r].length) {
				if (!board[r - 1][c + 1].isProtected(op)
						&& (board[r - 1][c + 1].getType() == Type.BLANK || board[r - 1][c + 1]
								.getColor() != p.getColor())
						&& isValid(r, c, r - 1, c + 1))
					p.addMoveLoc(new Location(r - 1, c + 1));
			}

			if (c - 1 >= 0) {
				if (!board[r - 1][c - 1].isProtected(op)
						&& (board[r - 1][c - 1].getType() == Type.BLANK || board[r - 1][c - 1]
								.getColor() != p.getColor())
						&& isValid(r, c, r - 1, c - 1))
					p.addMoveLoc(new Location(r - 1, c - 1));
			}
		}

		if (!p.hasMoved() && !isInCheck(p.getColor())) {
			int tempR = r, tempC = c + 1;
			op = p.getColor();
			
			if(op == Team.WHITE)
				op = Team.BLACK;
			else
				op = Team.WHITE;
			
			while (board[tempR][tempC].getType() == Type.BLANK
					&& tempC < board[r].length
					&& !board[tempR][tempC].isProtected(op))
				tempC++;

			if (tempC < board[r].length
					&& board[tempR][tempC].getType() == Type.ROOK)
				if (!board[tempR][tempC].hasMoved())
					p.addMoveLoc(new Location(tempR, tempC - 1));
			
			tempR = r;
			tempC = c - 1;
			while (board[tempR][tempC].getType() == Type.BLANK
					&& tempC > 0
					&& !board[tempR][tempC].isProtected(op))
				tempC--;

			if (tempC >= 0
					&& board[tempR][tempC].getType() == Type.ROOK)
				if (!board[tempR][tempC].hasMoved())
					p.addMoveLoc(new Location(tempR, tempC + 2));
		}
	}
}
