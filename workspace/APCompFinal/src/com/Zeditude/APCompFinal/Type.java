package com.Zeditude.APCompFinal;

public enum Type {
	BLANK(0), PAWN(1), KNIGHT(2), BISHOP(2), ROOK(4), QUEEN(5), KING(6);
	
	private final int value;
    private Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}