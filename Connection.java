import ecs100.*;

import java.awt.Color;
import java.util.*;
import java.io.*;


public class Connection {

	public final Shape shape1;
	public final Shape shape2;

	public Connection(Shape s1, Shape s2){
		this.shape1 = s1;
		this.shape2 = s2;
	}

	public void draw() {
		UI.drawLine(shape1.getCentreX(), shape1.getCentreY(), shape2.getCentreX(), shape2.getCentreY());	//wd and ht are just the other point for the line
	}
}
