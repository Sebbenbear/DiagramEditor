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
	//                FIELDS
	//////////////////////////////////////////////////
	private String state = "Rect";
	private double pressedX,pressedY;
	private Color col = Color.black;
	private Shape currentShape = null;
	private List <Shape> shapes = new ArrayList<Shape>();
	private List <connectingLine> lines = new ArrayList<connectingLine>();
	private List <Shape> selectedShapes = new ArrayList<Shape>();

	//////////////////////////////////////////////////
	//                MAIN METHOD
	//////////////////////////////////////////////////

	public static void main(String[] args) {new DiagramEditor();}

	//////////////////////////////////////////////////
	//                CONSTRUCTOR
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
		UI.addButton("Resize",this);
		UI.addButton("Select",this);

		UI.println("Click and release on the graphics pane to start drawing a rectangle.");
	}
	//////////////////////////////////////////////////
	//                TEXTFIELD PERFORMED
	//////////////////////////////////////////////////
	public void textFieldPerformed(String name, String newText) {
		if(name.equals("Set text")){
			if(this.currentShape==null){return;}
			this.currentShape.setText(newText);
		}
		drawShapes();
	}
	//////////////////////////////////////////////////
	//                BUTTONPERFORMED
	//////////////////////////////////////////////////
	public void buttonPerformed(String name) {
		if(name.equals("Clear")){
			this.shapes = new ArrayList<Shape>();
			UI.clearGraphics();							//doesn't work when the button is pressed
		} else if(name.equals("Load")){
			UI.clearGraphics();
			loadFile();
		} else if(name.equals("Save")){
			saveFile();
			//SHAPES
		} else if(name.equals("Rect")){
			UI.println("Draw a rectangle.");
			this.state = "Rect";
			} else if(name.equals("Oval")){
			    this.state = "Oval";
			}else if(name.equals("Hexagon")){
			    this.state = "Hexagon";
			//ACTIONS
		} else if(name.equals("Move")){
			this.state = "Move";
			UI.println("Click and drag a shape to move it.");
		} else if(name.equals("Delete")){
			this.state = "Delete";
			UI.println("Click on a shape to delete it.");
		} else if(name.equals("Resize")){
			this.state = "Resize";
			UI.println("Resize a shape by clicking on the edge of it, and releasing elsewhere.");
		} else if(name.equals("Connect")){
			UI.println("Click on one shape, then release on another in order to join a line.");
			this.state = "Connect";
		} else if(name.equals("Select")){
			UI.println("Click on a shape to select it.");
			this.state = "Select";
		}
	}
	//////////////////////////////////////////////////
	//                MOUSEPERFORMED
	//////////////////////////////////////////////////
	public void mousePerformed(String action, double x, double y) {

		/** PRESSED: records the first time the button is pressed, checks if a shape has been clicked on */

		if(action.equals("pressed")){
			this.pressedX = x;
			this.pressedY = y;
			this.currentShape = findShape(x, y);
			//UI.println(currentShape);
		} else if (action.equals("released")){

			/** RELEASED: if there is a selected shape,  */

			if(this.currentShape!=null){
				if(this.state.equals("Move")){
					this.currentShape.move(x-pressedX, y-pressedY);				//these are the new values that the mouse releases on. should be the new coords of centerx and y
				} else if (this.state.equals("Delete")){    //works
					deleteShape();
					//shapes.remove(currentShape);	//this mostly works
				} 
				else if (this.state.equals("Resize"))
				{
					resizeShape(x, y);		//change in x and y values
				} 
				else if (this.state.equals("Connect"))
				{
					addConnectingLine(x, y);    //will either form a line, not form one, or remove one
				} else if (this.state.equals("Select")){
					this.currentShape.changeSelected();
				}
			} else {
				addShape(x,y);    //if nothing else, just draw the shape that is selected/ all other possible states should already be covered
			}
		}
		drawShapes();
	}
	
	public void deleteShape(){
		int shapeIndex = shapes.indexOf(currentShape);			//find the index of the shape
		int[] indices;	
		for(connectingLine l : lines){
			indices = l.getIndices();				//loop through all the lines, getting their indices
			if(indices[0] == shapeIndex || indices[1] == shapeIndex){	//if the index matches up to one of their shapes
				//currentShape.removeAllLines();			//remove the line from the arraylist
				//remove the line from the arraylist
				lines.remove(l);		//this can all go in the connectinglines removeall lines method
		}
			shapes.remove(currentShape);		//finally remove the shape from the shape array
		}
	}
	
	
	//////////////////////////////////////////////////
	//                ADD THE CONNECTING LINE
	//////////////////////////////////////////////////
	public void addConnectingLine(double x, double y){
		Shape secondShape = findShape(x, y);
		if(secondShape != null && secondShape != this.currentShape){
			this.lines.add(new connectingLine(this.currentShape, secondShape, shapes.indexOf(this.currentShape), shapes.indexOf(secondShape)));    //now it is stored
			UI.println("added line from " + this.currentShape + " to " + secondShape);
			UI.println(shapes.indexOf(this.currentShape) + " " + shapes.indexOf(secondShape));
			UI.println("no. of lines: " + lines.size());
		}
	}
	//////////////////////////////////////////////////
	//                ADD THE SHAPE
	//////////////////////////////////////////////////
	public void addShape(double x, double y){
		//take in the last 2 values, calculate height and width, draw the shape
		double height = Math.abs(y-this.pressedY);
		double width = Math.abs(x-this.pressedX);
		double xPoint = Math.min(x, this.pressedX);
		double yPoint = Math.min(y, this.pressedY);
		if (state.equals("Rect")){
			this.shapes.add(new Rectangle(xPoint,yPoint,width,height,null));
		} else if (state.equals("Oval")){
		    this.shapes.add(new Oval(xPoint,yPoint,width,height,null));
		} else if (state.equals("Hexagon"))    {
		    this.shapes.add(new Hexagon(xPoint,yPoint,width,height,col));
		}
	}

	//////////////////////////////////////////////////
	//                DRAW SHAPES
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
	//                DRAW LINES
	//////////////////////////////////////////////////
	public void drawLines(){
		
		for(connectingLine l : lines){
			l.draw();
		}
	}
	//////////////////////////////////////////////////
	//    IS X,Y ON THE SHAPE
	//////////////////////////////////////////////////
	public Shape findShape(double x, double y){
		for(Shape s : shapes){    //go through the shapes, seeing if it's on the shape
			if(s.on(x,y)){
				return s;
			}
		}
		return null;
	}
	//////////////////////////////////////////////////
	//                SAVE
	//////////////////////////////////////////////////
	public void saveFile(){		
        try{
            String fname = UIFileChooser.open("Which file would you like to save to?");

            PrintStream ps = new PrintStream(new File(fname));
            
            ps.println(shapes.size());		//number of times to interate through creating objects
			ps.println(lines.size());

			for(Shape s : shapes){
				ps.println(s.makeString());
			}
			for(connectingLine l : lines){
				ps.println(l.makeString());
			}
			ps.close();
        } 
        catch(IOException e) {UI.println("File reading failed:" + e);}	
        
        UI.println("File saved.");
	}

	

	//////////////////////////////////////////////////
	//                LOAD
	//////////////////////////////////////////////////
	public void loadFile(){
		//clear everything, make a clean state- TODO
		try{
            String fname = UIFileChooser.open("Which file would you like to save to?");

            Scanner sc = new Scanner(new File(fname));
            
            int numShapes = sc.nextInt();
            int numLines = sc.nextInt();
            
            for(int i = 0; i < numShapes; i++){
            	String shapeType = UI.next();
            	double topLeftX = UI.nextDouble();
            	double topLeftY = UI.nextDouble();
            	double wd = UI.nextDouble();
            	double ht = UI.nextDouble();
            	String text = UI.nextLine(); 		//goes up until carriage return (but will get a space if it's still null, won't be empty then
            	
            	if("Rectangle".equals(shapeType)){            	
            		shapes.add(new Rectangle(topLeftX, topLeftY, wd, ht, text));
            	} else if ("Oval".equals(shapeType)){            	
            		shapes.add(new Oval(topLeftX, topLeftY, wd, ht, text));			//must be a better way of doing this?
            	} else if ("Hexagon".equals(shapeType)){            	
                	shapes.add(new Oval(topLeftX, topLeftY, wd, ht, text));
            	}            		
            }
        sc.close();
        } 
        catch(IOException e) {UI.println("File reading failed:" + e);}	
        
		UI.println("File saved.");
	
	}
	//////////////////////////////////////////////////
	//    RESIZE THE SHAPE
	//////////////////////////////////////////////////
	private void resizeShape(double dWd, double dHt) {    //can only resize with a selected object.
		if(currentShape.onControlPoint()){			//if the shape is selected and you are on the control point, you can move it.
			UI.println("on control point");
		}
		
		
		
		
		
	}
	
	
	/*USE A BOOLEAN FOR SELECTED, NON SELECTED
	 * USE A KEYLISTNENER
	 *
	 *
	 *todo:
	 * when you delete a shape, remove all connected lines from the arraylsit.
	 * when yyou click clear, it clears the line arraylist.
	 *
	 *
	 */
}
