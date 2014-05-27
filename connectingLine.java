
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class connectingLine {				//doesn't have to implement shape

	private Shape shape1;
	private Shape shape2;

	public connectingLine(Shape s1, Shape s2){
		this.shape1 = s1;
		this.shape2 = s2;
	}

	public void draw() {
		UI.drawLine(shape1.getCentreX(), shape1.getCentreY(), shape2.getCentreX(), shape2.getCentreY());	//wd and ht are just the other point for the line
	}

	public String makeString(){
		String s = "Line " + shape1.toString() + " : " + shape2.toString();	//this may need adjusting
		return (s);
	}

	public void addOrRemoveLine() {
		// TODO Auto-generated method stub
	}

	public void removeAllLines() {
		// TODO Auto-generated method stub
	}

	public void redrawLines() {
		// TODO Auto-generated method stub
	}


}
