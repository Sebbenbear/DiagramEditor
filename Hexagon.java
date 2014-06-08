
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;

public class Hexagon implements Shape{

	private double x, y, wd, ht;
	private Color col;

	public Hexagon(double x1, double y1, double w, double h, Color c) {
		this.x = x1;
		this.y = y1;
		this.wd = w;
		this.ht = h;
		this.col = c;
	}

	public void draw() {
		//do the calculations here
		//points are dependent on ht and wd

		/*	  __
		 *   /	\	start from top left
		 * 	 \__/
		 */
		double HleftX = x;
		double HtopLeftX = x + wd/3;
		double HtopRightX = x + 2*(wd/3);
		double HrightX = x + wd;
		double HbotRightX = x + 2*(wd/3);
		double HbotLeftX = x + wd/3;
		
		double HleftY = y + ht/2;
		double HtopLeftY = y;
		double HtopRightY = y;
		double HrightY = y + ht/2;
		double HbotRightY = y + ht;
		double HbotLeftY = y + ht;

		UI.drawPolygon(new double[]{HleftX, HtopLeftX, HtopRightX , HrightX, HbotRightX, HbotLeftX}, new double []{HleftY, HtopLeftY, HtopRightY , HrightY, HbotRightY, HbotLeftY}, 6);
	}

	public void resize(double dWd, double dHt) {
		// TODO Auto-generated method stub

	}

	public void move(double dx, double dy) {
		// TODO Auto-generated method stub

	}

	public boolean on(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	public void addOrRemoveLine() {
		// TODO Auto-generated method stub

	}

	public void removeAllLines() {
		// TODO Auto-generated method stub

	}

	public String makeString() {
		// TODO Auto-generated method stub
		return null;
	}

	public void redrawLines() {
		// TODO Auto-generated method stub

	}

	public void setText(String t) {
		// TODO Auto-generated method stub

	}

	public double getCentreX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCentreY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
