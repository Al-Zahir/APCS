package com.Zeditude.APCompFinal;

public enum Type {
	BLANK(0), PAWN(1), KNIGHT(2), BISHOP(2), ROOK(5), QUEEN(10), KING(11);
	
	private final int value;
    private Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}