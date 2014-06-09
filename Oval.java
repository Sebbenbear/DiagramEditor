import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class Oval implements Shape {

	private double x, y, wd, ht;
	private double centreX, centreY;
	private Color col;
	private String insideText;
	private boolean selected = false;

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
	// CHECK IF IT'S ON THE SHAPE
	////////////////////////////////////////////

	public boolean on(double x, double y) {

		//could change to selected here.
		if((y>=this.y && y < this.y + ht && x>=this.x && x < this.x + wd)){
			this.changeSelected();
			return true;
		}
		return false;

		//return (x-centreX)^2/(x radius)^2)) + ((y-centreY)/(y radius)^2) < 1
	}

	public void draw() {
		col = Color.black;	
		UI.setColor(Color.white);
		UI.fillOval(x, y, wd, ht);

		if(selected){
			col = Color.red;
		}
		UI.setColor(col);
		UI.drawOval(x,y,wd,ht);
		if(this.insideText==null){return;} //done!
		col = Color.black;
		UI.drawString(this.insideText, x+(wd/3), y+(ht/2)); //figure out a way without hardcoding it
	}

	public String makeString(){
		String s = "Oval " + this.x + " " + this.y + " " + this.wd + " " + this.ht + " " + this.insideText;
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

	public void changeSelected(){
		if(this.selected){
			this.selected = false;
		} else {
			this.selected = true;
		}
	}

	public void displayControlPoint(){	//will only display if it's selected, used for resizeing the shape
		if(selected)
			UI.fillRect(this.x + this.wd, this.y + ht, 10, 10);	//small square in the top right
	}

	public boolean onControlPoint(double x, double y) {
		return (y>=this.y+ht && y < this.y + ht + 10 && x>=this.x + wd && x < this.x + wd + 10);
	}
	
	public void setWidth(double x) {
		this.wd = x;
	}
	public void setHeight(double y) {
		this.ht = y;
	}
		
	public double getWidth() {
		return this.wd;
	}

	@Override
	public double getHeight() {
		return this.ht;
	}

	public boolean isSelected(){
		if(selected)
			return true;
		return false;
	}
	
	public void setCentreY(double y){
		this.centreY = y;
		this.y = centreY - ht/2;
	}
	public void setCentreX(double x){
		this.centreX = x;
		this.x = centreX - wd/2;
	}

}
