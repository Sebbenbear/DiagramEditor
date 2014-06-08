
import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class connectingLine {

	private Shape shape1;
	private Shape shape2;
	private int index1;
	private int index2;
	
	public connectingLine(Shape s1, Shape s2, int sIndex1, int sIndex2){		//TODO find these on creation of the connecting line
		this.shape1 = s1;
		this.shape2 = s2;
		index1 = sIndex1;
		index2 = sIndex2;
	}

	public void draw() {
		UI.drawLine(shape1.getCentreX(), shape1.getCentreY(), shape2.getCentreX(), shape2.getCentreY());	//wd and ht are just the other point for the line
	}

	public String makeString(){
		String s = index1 + " " + index2;	//this may need adjusting
		return s;
	}
	
	public int[] getIndices(){					//returns an array of size 2, for both indices
		int [] indices = {index1, index2};		//only needed for deletion, tostring
		UI.printf("getting indices %d : %d\n", indices[0], indices[1]);
		return indices;
	}
	

	public void addOrRemoveLine() {
		// TODO Auto-generated method stub
	}

	public void removeAllLines() {
		
	}

	public void redrawLines() {
		// TODO Auto-generated method stub
	}


}
