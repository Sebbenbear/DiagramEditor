
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class Oval implements Shape {

	private double x, y, wd, ht;
	private double centreX, centreY;
	private Color col;
	private String insideText;

	public Oval(double x1, double y1, double w, double h, String text){
		this.x = x1;
		this.y = y1;
		this.wd = w;
		this.ht = h;

		this.centreX = x + wd/2;
		this.centreY = y + ht/2;

		this.insideText = text;
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
	//			CHECK IF IT'S ON THE SHAPE
	////////////////////////////////////////////

	public boolean on(double x, double y) {
		return (y>=this.y && y < this.y + ht  && x>=this.x && x < this.x + wd);		//bounding box
		//USE THE EQUATION HERE.

		//return (x-centreX)^2/(x radius)^2)) + ((y-centreY)/(y radius)^2) < 1
	}

	public void draw() {		
		UI.setColor(this.col);			//set it to the right color (take col as a param
		UI.setColor(Color.white);
		UI.fillOval(x, y, wd, ht);
		UI.setColor(Color.black);
		UI.drawOval(x, y, wd, ht);
		if(this.insideText==null){return;}    //done!
		UI.drawString(this.insideText, x+(wd/3), y+(ht/2));    //figure out a way without hardcoding it
	}

	public void resize(double dWd, double dHt) {
		this.ht = dHt;
		this.wd = dWd;
	}

	public String makeString(){
		String s = "Oval " + this.x + " " + this.y + " " + this.wd + " " + this.ht + " " + this.col;
		return (s);
	}

	public void move(double dx, double dy) {
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

	public void addOrRemoveLine() {
		//

	}

	////////////////////////////////////////////
	// REMOVES ALL LINES ASSOCIATED WITH THE SHAPE
	////////////////////////////////////////////

	public void removeAllLines() {
		// specifying the shape at the other end of the line

	}

	////////////////////////////////////////////
	// REDRAWS ALL THE LINES
	////////////////////////////////////////////

	public void redrawLines() {
		//if it has lines connected to it,
		//then redraw them.
		//have an arraylist opf lines

	}


}
