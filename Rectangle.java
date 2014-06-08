
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class Rectangle implements Shape {

	private double x, y, wd, ht;
	private double centreX, centreY;
	private Color col;
	private String insideText;
	private boolean selected = false;
	//private List <String> lines = new ArrayList<String>(); maybhe use a line ovbject

	////////////////////////////////////////////
	// CONSTRUCTOR
	////////////////////////////////////////////

	public Rectangle(double x1, double y1, double w, double h, String text){
		this.x = x1;
		this.y = y1;
		this.wd = w;
		this.ht = h;

		this.centreX = x + wd/2;
		this.centreY = y + ht/2;

		this.insideText = text;
	}

	public void changeSelected(){
		if(this.selected){
			this.selected = false;
			UI.println("changed");
		} else {
			this.selected = true;
		}
		UI.println("Shape is now " + selected);
	}
	
	////////////////////////////////////////////
	// GET CENTRE X AND CENTRE Y - getter methods for better encapsulation?
	////////////////////////////////////////////

	public double getCentreX() {
		return (this.centreX);
	}

	public double getCentreY() {
		return (this.centreY);
	}

	////////////////////////////////////////////
	// CHECK IF IT'S ON THE SHAPE
	////////////////////////////////////////////

	public boolean on(double x, double y) {
		return (y>=this.y && y < this.y + ht && x>=this.x && x < this.x + wd);
	}

	////////////////////////////////////////////
	// DRAW THE SHAPE
	////////////////////////////////////////////

	public void draw() {
		col = Color.black;					//UI.drawPolygon([x, x+wd],[y, y+ht], 4);
		UI.setColor(Color.white);
		UI.fillRect(x, y, wd, ht);
		
		if(selected){
			col = Color.red;
		}
		UI.setColor(col);
		//UI.drawRect(x, y, wd, ht);
		UI.drawPolygon(new double[]{x, x+wd, x+wd, x}, new double []{y, y, y+ht, y+ht}, 4);
		if(this.insideText==null){return;}    //done!
		col = Color.black;
		UI.drawString(this.insideText, x+(wd/3), y+(ht/2));    //figure out a way without hardcoding it
	}

	////////////////////////////////////////////
	// RESIZE THE SHAPE
	////////////////////////////////////////////

	public void resize(double x, double y) {
		double changeWd = centreX-x;
		double changeHt = centreY-y;
		
		
		
		this.x += x-this.x;
		this.y += y-this.y;
		
		
		
		

		//TODO
		//FIX THE LINES LIKE IN THE OTHER PROGRAM

	}

	////////////////////////////////////////////
	// TOSTRING FOR SAVING
	////////////////////////////////////////////

	public String makeString(){
		//get the index of it before you save it. concatenate it on in the next method?
		String s = "Rectangle" + " " + this.x + " " + this.y + " " + this.wd + " " + this.ht + " " + this.insideText;
		return (s);
	}

	////////////////////////////////////////////
	// MOVE
	////////////////////////////////////////////

	public void move(double dx, double dy) {		//released xy is the arg
		this.centreX += dx;
		this.centreY += dy;
		this.x = centreX - wd/2;
		this.y = centreY - ht/2;
	}

	////////////////////////////////////////////
	// SET THE TEXT INSIDE THE SHAPE
	////////////////////////////////////////////

	public void setText(String newText) {
		this.insideText = newText;
	}

	////////////////////////////////////////////
	// ADDING A LINE (OR REMOVING)
	////////////////////////////////////////////

	public void addOrRemoveLine() {
		//
	}

	////////////////////////////////////////////
	// REMOVES ALL LINES ASSOCIATED WITH THE SHAPE
	////////////////////////////////////////////

	public void removeAllLines() {
		
	}

	////////////////////////////////////////////
	// REDRAWS ALL THE LINES
	////////////////////////////////////////////

	public void redrawLines() {
		//if it has lines connected to it,
		//then redraw them.
		//have an arraylist opf lines

	}
	
	public void displayControlPoint(){		//will only display if it's selected, used for resizeing the shape
		if(selected)
			UI.fillRect(this.x-10, this.y-10, 10, 10);	//small square in the top right
	}
	
	
	

}
