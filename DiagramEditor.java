import ecs100.*;

import java.awt.Color;
import java.awt.color.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import javax.swing.JColorChooser;

public class DiagramEditor implements UIButtonListener, UIMouseListener, UITextFieldListener {
	//////////////////////////////////////////////////
	// FIELDS
	//////////////////////////////////////////////////
	private String state = "Rect";
	private double pressedX,pressedY;
	private Color col = Color.black;
	private Shape currentShape = null;
	private List <Shape> shapes = new ArrayList<Shape>();
	private List <Connection> lines = new ArrayList<Connection>();
	private boolean onControlPoint = false;
	public enum AlignmentTypes{
		TOP,
		BOT,
		LEFT,
		RIGHT,
		HORIZ,
		VERT		
	};

	//////////////////////////////////////////////////
	// MAIN METHOD
	//////////////////////////////////////////////////

	public static void main(String[] args) {new DiagramEditor();}

	//////////////////////////////////////////////////
	// CONSTRUCTOR
	//////////////////////////////////////////////////

	public DiagramEditor(){
		UI.setImmediateRepaint(false);
		UI.setMouseListener(this);
		UI.addTextField("Set text", this);
		UI.addButton("Clear", this);
		UI.addButton("Load", this);
		UI.addButton("Save", this);
		UI.addButton("Rect", this);
		UI.addButton("Oval", this);
		UI.addButton("Hexagon",this);
		UI.addButton("Delete", this);
		UI.addButton("Move", this);
		UI.addButton("Connect", this);
		UI.addButton("Align", this);
//		UI.addButton("Pick Color", this);

		UI.println("Click and release on the graphics pane \nto start drawing a rectangle.");
		UI.printf("\n ------------------------- \n");

	}
	//////////////////////////////////////////////////
	// TEXTFIELD PERFORMED
	//////////////////////////////////////////////////
	public void textFieldPerformed(String name, String newText) {
		if(name.equals("Set text")){
			if(this.currentShape==null){return;}
			this.currentShape.setText(newText);
		}
		drawShapes();
	}
	//////////////////////////////////////////////////
	// BUTTONPERFORMED
	//////////////////////////////////////////////////
	public void buttonPerformed(String name) {
		if(name.equals("Clear")){
			cleanSlate();
		} else if(name.equals("Load")){
			loadFile();
		} else if(name.equals("Save")){
			saveFile();
			//SHAPES
		} else if(name.equals("Rect")){
			UI.println("Draw a rectangle.");
			this.state = "Rect";
		} else if(name.equals("Oval")){
			UI.println("Draw an oval.");
			this.state = "Oval";
		}else if(name.equals("Hexagon")){
			UI.println("Draw a hexagon.");
			this.state = "Hexagon";
			//ACTIONS
		} else if(name.equals("Move")){
			this.state = "Move";
			UI.println("Click and drag a shape to move it.");
		} else if(name.equals("Delete")){
			this.state = "Delete";
			UI.println("Click on a shape to delete it.");
		} else if(name.equals("Connect")){
			UI.println("Click on one shape, then release on another \nin order to join a line.");
			UI.println("If a line already exists, it will be deleted.");
			this.state = "Connect";
		} else if(name.equals("Align")){
			String answer = UI.askString("Enter an alignment: T (top), L (left), B (bottom), R (right), V (Vertical), H (Horizontal)");
			if(answer.equalsIgnoreCase("T")) {
				alignShapes(AlignmentTypes.TOP);
			}
			else if(answer.equalsIgnoreCase("B")) {
				alignShapes(AlignmentTypes.BOT);		
			}
			else if(answer.equalsIgnoreCase("L")) {
				alignShapes(AlignmentTypes.LEFT);
			}
			else if(answer.equalsIgnoreCase("R")) {
				alignShapes(AlignmentTypes.RIGHT);
			}
			else if(answer.equalsIgnoreCase("H")) {
				alignShapes(AlignmentTypes.HORIZ);
			}
			else if(answer.equalsIgnoreCase("V")) {
				alignShapes(AlignmentTypes.VERT);
			}
			else {
				UI.println("Not valid");
			}
//		} else if(name.equals("Pick Color")){
//			this.col = JColorChooser.showDialog(null, "Choose Color", null);
//			UI.setColor(this.col);
			UI.printf("\n ------------------------- \n");
		}
	}
	//////////////////////////////////////////////////
	// MOUSEPERFORMED
	//////////////////////////////////////////////////
	public void mousePerformed(String action, double x, double y) {

		/** PRESSED: records the first time the button is pressed, checks if a shape has been clicked on */

		if(action.equals("pressed")){
			this.pressedX = x;
			this.pressedY = y;
			if(this.currentShape != null && this.currentShape.onControlPoint(x, y)) {
				this.onControlPoint = true;
			} else {
				this.currentShape = findShape(x, y);

				
			}
		} else if (action.equals("released")){

			/** RELEASED: if there is a selected shape or it's on the control point */

			if(this.onControlPoint) 
			{
				this.onControlPoint = false;

				double width = this.currentShape.getWidth();
				double height = this.currentShape.getHeight();

				width += x-pressedX;
				height += y-pressedY;

				this.currentShape.setWidth(width);
				this.currentShape.setHeight(height);
			} else {
				if(this.currentShape!=null){
					
					if(this.state.equals("Move")){
						this.currentShape.move(x-pressedX, y-pressedY);	
					} else if (this.state.equals("Delete")){ 
						deleteShape();
					}
					else if (this.state.equals("Connect"))
					{
						addOrRemoveConnection(x, y); //will either form a line, not form one, or remove one
					}
				} else {
					addShape(x,y); 
				}
			}
		}
		drawShapes();
	}

	public void alignShapes(AlignmentTypes type){
		//change the x values here/ if they are selected
		double sumOfYs = 0;
		double sumOfXs = 0;
		int shapeCount = 0;
		
		for(Shape s : shapes){
			if(s.isSelected()){
				sumOfXs += s.getCentreX();
				sumOfYs += s.getCentreY();
				shapeCount++;
			}
		}
		double newCenterX = sumOfXs/shapeCount;
		double newCenterY =sumOfYs/shapeCount;
		
		for(Shape s : shapes){
			if(s.isSelected()){
				//now to decide where to put them all
				switch(type){
				case TOP:
					s.setCentreY(newCenterY + s.getHeight() / 2);
					break;
				case BOT:
					s.setCentreY(newCenterY - s.getHeight() / 2);
					break;
				case LEFT:
					s.setCentreX(newCenterX + s.getWidth() / 2);
					break;
				case RIGHT:
					s.setCentreX(newCenterX - s.getWidth() / 2);
					break;
				case HORIZ:
					s.setCentreY(newCenterY);
					break;
				case VERT:
					s.setCentreX(newCenterX);
					break;
				}
			}
		}
		
		drawShapes();
	}
		


	public void deleteShape(){
		ArrayList<Connection> connectionsToRemove = new ArrayList<Connection>();
		for(Connection l : lines){
			if(l.shape1 == currentShape || l.shape2 == currentShape){	//if the index matches up to one of their shapes
				//add the line to a list to be removed from lines, but DONT remove it now
				//otherwise we will get a ConcurrentModificationException, instead we remove
				//it outside the loop
				connectionsToRemove.add(l);
			}
		}
		//NOW we actually remove the lines to avoid exception we would otherwise get
		lines.removeAll(connectionsToRemove);

		shapes.remove(currentShape);	//finally remove the shape from the shape array
	}

	public boolean removeConnection(Shape s1, Shape s2){
		for(Connection c : lines){
			if(s1 == c.shape1 && s2 == c.shape2 || s2 == c.shape1 && s1 == c.shape2)  {		//then it' the same line
				lines.remove(c);
				return true;
			}
		}
		return false;
	}

	//////////////////////////////////////////////////
	// ADD THE CONNECTING LINE
	//////////////////////////////////////////////////
	public void addOrRemoveConnection(double x, double y){
		Shape secondShape = findShape(x, y);
		if(secondShape != null && secondShape != this.currentShape){
			if(!removeConnection(this.currentShape, secondShape)){	//removes connection if there is one
				this.lines.add(new Connection(this.currentShape, secondShape)); //now it is stored	
			}
		}
	}
	//////////////////////////////////////////////////
	// ADD THE SHAPE
	//////////////////////////////////////////////////
	public void addShape(double x, double y){
		double height = Math.abs(y-this.pressedY);
		double width = Math.abs(x-this.pressedX);
		double xPoint = Math.min(x, this.pressedX);
		double yPoint = Math.min(y, this.pressedY);
		if (state.equals("Rect")){
			this.shapes.add(new Rectangle(xPoint,yPoint,width,height,null));
		} else if (state.equals("Oval")){
			this.shapes.add(new Oval(xPoint,yPoint,width,height,null));
		} else if (state.equals("Hexagon")) {
			this.shapes.add(new Hexagon(xPoint,yPoint,width,height,null));
		}
	}

	//////////////////////////////////////////////////
	// DRAW SHAPES
	//////////////////////////////////////////////////
	public void drawShapes(){
		UI.clearGraphics();
		drawLines();
		for(Shape s : shapes){
			s.draw();
			s.displayControlPoint();
		}
		UI.repaintGraphics();
	}
	//////////////////////////////////////////////////
	// DRAW LINES
	//////////////////////////////////////////////////
	public void drawLines(){
		for(Connection l : lines){
			l.draw();
		}
	}
	//////////////////////////////////////////////////
	// IS X,Y ON THE SHAPE
	//////////////////////////////////////////////////
	public Shape findShape(double x, double y){
		for(Shape s : shapes){ //go through the shapes, seeing if it's on the shape
			if(s.on(x,y)){
				return s;
			}
		}
		return null;
	}
	//////////////////////////////////////////////////
	// SAVE
	//////////////////////////////////////////////////
	public void saveFile(){	
		try{
			String fname = UIFileChooser.open("Which file would you like to save to?");

			PrintStream ps = new PrintStream(new File(fname));

			ps.println(shapes.size());	//number of times to interate through creating objects
			ps.println(lines.size());

			for(Shape s : shapes){
				ps.println(s.makeString());
			}
			for(Connection l : lines){
				ps.println(shapes.indexOf(l.shape1) + " " + shapes.indexOf(l.shape2));
			}
			ps.close();
		}
		catch(IOException e) {UI.println("File reading failed:" + e);}	

		UI.println("File saved.");
	}

	//////////////////////////////////////////////////
	// 			CLEANSLATE RESETS ALL VARIABLES
	//////////////////////////////////////////////////

	public void cleanSlate(){
		state = "Rect";
		currentShape = null;
		shapes = new ArrayList<Shape>();
		lines = new ArrayList<Connection>();

		UI.clearGraphics();
		UI.repaintGraphics();

	}
	//////////////////////////////////////////////////
	// LOAD
	//////////////////////////////////////////////////
	public void loadFile(){
		cleanSlate();
		try{
			String fname = UIFileChooser.open("Which file would you like to save to?");

			Scanner sc = new Scanner(new File(fname));

			int numShapes = sc.nextInt();
			int numLines = sc.nextInt();

			UI.printf("%d : %d\n",numShapes,numLines);	//this works
			//draw the shapes first
			for(int i = 0; i < numShapes; i++){
				String shapeType = sc.next();
				double topLeftX = sc.nextDouble();
				double topLeftY = sc.nextDouble();
				double wd = sc.nextDouble();
				double ht = sc.nextDouble();
				String text = sc.nextLine(); //goes up until carriage return (but will get a space if it's still null, won't be empty then
				if(text.contains("null"))	//if there was nothing in the string
					text = null;
				if("Rectangle".equals(shapeType)){
					shapes.add(new Rectangle(topLeftX, topLeftY, wd, ht, text));
				} else if ("Oval".equals(shapeType)){
					shapes.add(new Oval(topLeftX, topLeftY, wd, ht, text));	//must be a better way of doing this?
				} else if ("Hexagon".equals(shapeType)){
					shapes.add(new Hexagon(topLeftX, topLeftY, wd, ht, text));
				}
				UI.println("Shape added: " + shapeType);
			}

			for(int i = 0; i < numLines; i++){	
				int idx1 = sc.nextInt();
				int idx2 = sc.nextInt();
				//using the indexes, construct the shapes
				Shape s1 = shapes.get(idx1);
				Shape s2 = shapes.get(idx2);
				lines.add(new Connection(s1, s2));
			}	
			sc.close();
		}
		catch(IOException e) {UI.println("File reading failed:" + e);}	

		UI.println("File loaded.");
		drawShapes();
	}
}
