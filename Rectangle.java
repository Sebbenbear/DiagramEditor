
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class Rectangle implements Shape {

	private double x, y, wd, ht;
	private double centreX, centreY;	
	private Color col;
	private String insideText;		
	//private List <String> lines = new ArrayList<String>();			maybhe use a line ovbject

	////////////////////////////////////////////
	//			CONSTRUCTOR
	////////////////////////////////////////////

	public Rectangle(double x1, double y1, double w, double h, Color c, String text){
		this.x = x1;
		this.y = y1;
		this.wd = w;
		this.ht = h;
		this.col = c;

		this.centreX = x + wd/2;
		this.centreY = y + ht/2;

		this.insideText = text;

	}

	////////////////////////////////////////////
	//			GET CENTRE X AND CENTRE Y	- getter methods for better encapsulation?
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
		return (y>=this.y && y < this.y + ht  && x>=this.x && x < this.x + wd);
	}

	////////////////////////////////////////////
	//			DRAW THE SHAPE
	////////////////////////////////////////////

	public void draw() {
		//UI.println(this.insideText + "!");
		UI.setColor(this.col);							//UI.drawPolygon(xPoints, yPoints, nPoints);
														//UI.drawPolygon([x, x+wd],[y, y+ht], 4);
		UI.drawRect(x, y, wd, ht);
		//UI.println(this.insideText);
		if(this.insideText==null){return;}				//done!
			UI.drawString(this.insideText, x+(wd/3), y+(ht/2));			//figure out a way without hardcoding it
	}

	////////////////////////////////////////////
	//      RESIZE THE SHAPE
	////////////////////////////////////////////

	public void resize(double dWd, double dHt) {
		//change the wd and ht by specified amounts
		//if the amount is negative, it should get smaller in some direction
	}

	////////////////////////////////////////////
	//      TOSTRING FOR SAVING
	////////////////////////////////////////////

	public String makeString(){
		String s = "Rectangle " + this.x + " " + this.y + " " + this.wd + " " + this.ht + " " + this.col + "" + this.insideText;
		return (s);
	}

	////////////////////////////////////////////
	//      	MOVE
	////////////////////////////////////////////

	public void move(double dx, double dy) {
		//this.x += dx;
		//this.y += dy;
		this.x = dx;
		this.y = dy;
		UI.println("now:" + dx + " " + dy);

	}

	////////////////////////////////////////////
	//      SET THE TEXT INSIDE THE SHAPE
	////////////////////////////////////////////

	public void setText(String newText) {
		this.insideText = newText;
	}

	////////////////////////////////////////////
	//      ADDING A LINE (OR REMOVING)
	////////////////////////////////////////////

	public void addOrRemoveLine() {
		//

	}

	////////////////////////////////////////////
	//      REMOVES ALL LINES ASSOCIATED WITH THE SHAPE
	////////////////////////////////////////////

	public void removeAllLines() {
		// specifying the shape at the other end of the line

	}

	////////////////////////////////////////////
	//      REDRAWS ALL THE LINES
	////////////////////////////////////////////

	public void redrawLines() {
		//if it has lines connected to it,
		//then redraw them.
		//have an arraylist opf lines

	}






}
