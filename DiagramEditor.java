
import ecs100.*;

import java.awt.Color;
import java.awt.color.*;
import java.util.*;

import javax.swing.JColorChooser;

public class DiagramEditor implements UIButtonListener, UIMouseListener, UITextFieldListener {

	//////////////////////////////////////////////////
	//                FIELDS
	//////////////////////////////////////////////////

	private String state = "Rect";		//deault is line when you start using the mouse on the graphics pane
	private double pressedX,pressedY;		//need to remember the previous values
	
	private Color col = Color.black;		//make this a new color
	private Shape currentShape = null;		//selected shape
	
	private List <Shape> shapes = new ArrayList<Shape>(); 	
	private List <connectingLine> lines = new ArrayList<connectingLine>();		//list of line objects

    //////////////////////////////////////////////////
    //                MAIN METHOD
    //////////////////////////////////////////////////

    public static void main(String[] args) {new DiagramEditor();}

    //////////////////////////////////////////////////
    //                CONSTRUCTOR
    //////////////////////////////////////////////////

    public DiagramEditor(){
        UI.setMouseListener(this);

        UI.addTextField("Set text", this);

        UI.addButton("Clear", this);
        UI.addButton("Line", this);
        UI.addButton("Rect", this);
        UI.addButton("Oval", this);
        UI.addButton("Hexagon",this);
        UI.addButton("Delete", this);
        UI.addButton("Move", this);
        UI.addButton("Connect", this);
        UI.addButton("Pick Color", this);
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
        	UI.clearGraphics();
        } else if(name.equals("Load")){
        	UI.clearGraphics();
        	loadFile();
        } else if(name.equals("Save")){
        	saveFile();
        } else if(name.equals("Pick Color")){
        	this.col = JColorChooser.showDialog(null, "Choose Color", null);
        	UI.setColor(this.col);

        	//SHAPES
        //} else if(name.equals("Line")){
        	//this.state = "Line";
        } else if(name.equals("Rect")){				
        	this.state = "Rect";
        //} else if(name.equals("Oval")){
        //	this.state = "Oval";
        //}else if(name.equals("Hexagon")){
        //	this.state = "Hexagon";

        	//ACTIONS
        } else if(name.equals("Move")){
        	this.state = "Move";
        } else if(name.equals("Delete")){
        	this.state = "Delete";
        } else if(name.equals("Resize")){
        	this.state = "Resize";
        } else if(name.equals("Connect")){
        	this.state = "Connect";
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

			UI.println(currentShape);

		} else if (action.equals("released")){

			/** RELEASED: if there is a selected shape,  */

			if(this.currentShape!=null){
				if(this.state.equals("Move")){
					this.currentShape.move(x,y);					//this works. make it better
					//double dx = Math.abs(pressedX-changeX);
					//double dy = Math.abs(pressedY-changeY);
					//UI.println("Moving" + this.currentShape);
				} else if (this.state.equals("Delete")){			//works
					shapes.remove(currentShape);
				} else if (this.state.equals("Resize")){
					resizeShape(Math.abs(pressedX-x),Math.abs(pressedY-y) );
				} else if (this.state.equals("Connect")){
					addConnectingLine(x, y);	//will either form a line, not form one, or remove one
				}
				//draw a shape if no other actions are possible

			} else {
				addShape(x,y);			//if nothing else, just draw the shape that is selected/ all other possible states should already be covered
			}
		}
		drawLines();
		drawShapes();
	}

	//////////////////////////////////////////////////
	//                ADD THE CONNECTING LINE
	//////////////////////////////////////////////////

	public void addConnectingLine(double x, double y){
		Shape secondShape = findShape(x, y);
		if(secondShape != null || !secondShape.equals(this.currentShape)){					//if there isn't a pair of different shapes, return. 2nd part isn't working. NOT WORKING
			this.lines.add(new connectingLine(this.currentShape, secondShape));		//now it is stored
			UI.println("added line from " + this.currentShape + " to " + secondShape);
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
			this.shapes.add(new Rectangle(xPoint,yPoint,width,height,this.col, null));
		} else if (state.equals("Oval")){
			this.shapes.add(new Oval(x,y,this.pressedX,this.pressedY,this.col));
		} else if (state.equals("Hexagon"))	{
			this.shapes.add(new Hexagon(x,y,this.pressedX,this.pressedY,this.col));
		}
	}

	//////////////////////////////////////////////////
	//                DRAW SHAPES
	//////////////////////////////////////////////////

	public void drawShapes(){
		//if the arraylist is not null
		//loop through the arraylist and call the draw method on all the shapes.
		//UI.clearGraphics();						//ONLY NEED CLEARGRAPHICS WHEN DRAWING THE LINES FIRST
		if(this.shapes.size() == 0){return;}	//return if no elements in array
		for(Shape s : shapes){
			s.draw();
		}
		//UI.repaintGraphics();
	}

	//////////////////////////////////////////////////
	//                DRAW LINES
	//////////////////////////////////////////////////

	public void drawLines(){
		//if the arraylist is not null
		//loop through the arraylist and call the draw method on all the shapes.
		UI.clearGraphics();
		if(this.lines.size() == 0){return;}	//return if no elements in array
		for(connectingLine l : lines){
			l.draw();
		}
		//UI.repaintGraphics();			//keep or remove?
	}

	//////////////////////////////////////////////////
	//			IS X,Y ON THE SHAPE
	//////////////////////////////////////////////////

	public Shape findShape(double x, double y){
		for(Shape s : shapes){			//go through the shapes, seeing if it's on the shape
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
		//call the toString method on every object of the arraylist, should be all info needed to redraw. what about the order
	}

	//////////////////////////////////////////////////
	//                LOAD
	//////////////////////////////////////////////////

	public void loadFile(){
		//
	}

	//////////////////////////////////////////////////
	//				RESIZE THE SHAPE
	//////////////////////////////////////////////////

	private void resizeShape(double abs, double abs2) {




	}



	/*USE A BOOLEAN FOR SELECTED, NON SELECTED
	 * USE A KEYLISTNENER
	 *
	 *
	 */

}

