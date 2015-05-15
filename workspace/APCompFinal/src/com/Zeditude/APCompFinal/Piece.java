package com.Zeditude.APCompFinal;
import java.util.ArrayList;

public class Piece {
	private Type type;
	private ArrayList<Location> moveLoc;
	private Team color;
	private boolean isSelected;
	private boolean hasMoved;
	private boolean isProtected;
	private ArrayList<Team> protectedColors;

	public Piece() {
		type = Type.BLANK;
		color = Team.BLANK;
		isSelected = false;
		moveLoc = new ArrayList<Location>();
		hasMoved = false;
		isProtected = false;
		protectedColors = new ArrayList<Team>();
	}

	public Piece(Type t, Team c) {
		type = t;
		color = c;
		moveLoc = new ArrayList<Location>();
		isSelected = false;
		hasMoved = false;
		isProtected = false;
		protectedColors = new ArrayList<Team>();
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ArrayList<Location> getMoveLoc() {
		return moveLoc;
	}

	public void addMoveLoc(Location moveLoc) {
		this.moveLoc.add(moveLoc);
	}
	
	public boolean canMoveTo(int r, int c){
		boolean flag = false;
		
		for(Location l : moveLoc)
			if(l.getRow() == r && l.getCol() == c)
				flag = true;
		
		return flag;
	}

	public Team getColor() {
		return color;
	}

	public void setColor(Team color) {
		this.color = color;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean b) {
		isSelected = b;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
	public boolean isProtected(Team c){
		boolean flag = false;
		
		for(Team t : protectedColors)
			if(t != c)
				flag = true;
		
		return isProtected && flag;
	}
	
	public void setProtected(boolean b, Team t){
		isProtected = b;
		protectedColors.add(t);
		
		if(!b)
			protectedColors = new ArrayList<Team>();
	}
	
	public void removeLocations(){
		moveLoc = new ArrayList<Location>();
	}
}
