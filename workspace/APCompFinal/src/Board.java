import java.util.ArrayList;

public class Board {
	private Piece[][] board;
	private Team turn;

	public Board(int rows, int cols) {
		board = new Piece[rows][cols];
	}

	public void nextTurn() {
		if (turn == Team.WHITE)
			turn = Team.BLACK;
		else
			turn = Team.WHITE;
	}

	public void select() {

	}

	public void setLocations() {
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[r].length; c++) {
				Piece p = board[r][c];
				p.setProtected(false, Team.BLANK);
				
				if (p.getType() != Type.BLANK)
					setPieceLocation(p, r, c);
			}
		}
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
			p.addMoveLoc(new Location(r + 1, c));

			if (!p.hasMoved())
				p.addMoveLoc(new Location(r + 2, c));

			if (c + 1 < board[r].length){
				if (board[r + 1][c + 1].getColor() == Team.BLACK)
					p.addMoveLoc(new Location(r + 1, c + 1));
				
				board[r + 1][c + 1].setProtected(true, p.getColor());
			}

			if (c - 1 > 0){
				if (board[r + 1][c - 1].getColor() == Team.BLACK)
					p.addMoveLoc(new Location(r + 1, c - 1));
				
				board[r + 1][c - 1].setProtected(true, p.getColor());
			}
		} else {
			p.addMoveLoc(new Location(r - 1, c));

			if (!p.hasMoved())
				p.addMoveLoc(new Location(r - 2, c));

			if (c + 1 < board.length){
				if (board[r - 1][c + 1].getColor() == Team.WHITE)
					p.addMoveLoc(new Location(r - 1, c + 1));
				
				board[r - 1][c + 1].setProtected(true, p.getColor());
			}

			if (c - 1 > 0){
				if (board[r - 1][c - 1].getColor() == Team.WHITE)
					p.addMoveLoc(new Location(r - 1, c - 1));
			
				board[r - 1][c - 1].setProtected(true, p.getColor());
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
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
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
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
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
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
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
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
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
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
		}
		
		
		tempR = r - 1;
		tempC = c;
		while(tempR >= 0 && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempR--;
		}
		
		if(tempR >= 0){
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
		}
		
		
		tempR = r;
		tempC = c + 1;
		while(tempC < board[tempR].length && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC++;
		}
		
		if(tempC < board[tempR].length){
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
		}
		
		
		tempR = r;
		tempC = c - 1;
		while(tempC >= 0 && board[tempR][tempC].getType() == Type.BLANK){
			p.addMoveLoc(new Location(tempR, tempC));
			board[tempR][tempC].setProtected(true, p.getColor());
			tempC--;
		}
		
		if(tempC >= 0){
			if(board[tempR][tempC].getColor() != p.getColor()){
				p.addMoveLoc(new Location(tempR, tempC));
				board[tempR][tempC].setProtected(true, p.getColor());
			}
		}
	}

	public void setQueenLoc(Piece p, int r, int c) {
		setBishopLoc(p, r, c);
		setRookLoc(p, r, c);
	}

	public void setKingLoc(Piece p, int r, int c) {
		
	}
}