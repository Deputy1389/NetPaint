package view;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JColorChooser;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paint.Line;
import paint.Oval;
import paint.PaintObject;
import paint.Pic;
import paint.Rectangle;

public class RunGUI extends JFrame {

	public static void main(String[] args) {
		RunGUI view = new RunGUI();
		view.setVisible(true);
	}

	private Image image;
	private DrawingPanel drawingPanel;
	private JColorChooser colorChooser;
	private static Vector<PaintObject> allPaintObjects;
	private String style;				//This is the shape
	private Color color;	//The shapes color

	public RunGUI() {

		try {
			image = ImageIO.read(new File("images/PIA19048.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GridBagConstraints c = new GridBagConstraints();
		JPanel bottom = new JPanel();
		setColor(Color.MAGENTA);	//initializes the color
		setStyle("Image");			//initializes the paint object 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(300, 10);
		setSize(700, 750);
		setDrawingPanel(new DrawingPanel());
		setLayout(new GridLayout(0, 1));
		getDrawingPanel().setPreferredSize(new Dimension(700, 1000));
		JScrollPane scrollPane = new JScrollPane(getDrawingPanel());
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollPane);

		JPanel buttons = new JPanel();
		ButtonGroup group = new ButtonGroup();
		JRadioButton line = new JRadioButton("Line");
		line.addActionListener(new RadioListener());
		JRadioButton rectangle = new JRadioButton("Rectangle");
		rectangle.addActionListener(new RadioListener());
		JRadioButton oval = new JRadioButton("Oval");
		oval.addActionListener(new RadioListener());
		JRadioButton image = new JRadioButton("Image", true);
		image.addActionListener(new RadioListener());
		group.add(line);
		group.add(rectangle);
		group.add(oval);
		group.add(image);
		buttons.add(line);
		buttons.add(rectangle);
		buttons.add(oval);
		buttons.add(image);
		c.gridx = 0;
		c.gridy = 0;
		bottom.add(buttons, c);

		colorChooser = new JColorChooser();
		colorChooser.getSelectionModel().addChangeListener(new ColorListener());
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 1;
		bottom.add(colorChooser, c);
		add(bottom);

		allPaintObjects = new Vector<>();
	}

	public class ColorListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			Color newColor = getColorChooser().getColor();
			setColor(newColor);
		}

	}

	public class RadioListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			switch (command) {
			case "Line":
				setStyle("Line");
				break;
			case "Rectangle":
				setStyle("Rectangle");
				break;
			case "Oval":
				setStyle("Oval");
				break;
			case "Image":
				setStyle("Image");
				break;
			}
		}

	}

	/*
	 * TwoPoints takes two points and creates the correct PaintObject
	 */
	public void TwoPoints(Point p1, Point p2) {
		PaintObject l = null;
		switch (getStyle()) {		//gets the current drawing style
		case ("Rectangle"):	
			l = new Rectangle(color, p1, p2);	//draws the selected style with the given two points
			break;
		case ("Line"):
			l = new Line(color, p1, p2);
			break;
		case ("Oval"):
			l = new Oval(color, p1, p2);
			break;
		case ("Image"):
			l = new Pic(p1, p2);
			break;
		}
		allPaintObjects.add(l);
	}

	//This is where the drawings go
	public class DrawingPanel extends JPanel {
		private PaintObject temp;

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g2);
			if (allPaintObjects != null)
				for (PaintObject ob : allPaintObjects) {	//iterates over the paintobject vector
					ob.draw(g2, image);						//draws each object
				}
			if (temp != null) {
				temp.draw(g2, image);
			}
		}

		//Draws the ghost image
		//takes two points as parameters
		public void tempPaint(Point p1, Point p2) {
			switch (getStyle()) {	//based on the current style
			case ("Rectangle"):		
				temp = new Rectangle(color, p1, p2);	//draws the current style from two points
				break;
			case ("Line"):
				temp = new Line(color, p1, p2);
				break;
			case ("Oval"):
				temp = new Oval(color, p1, p2);
				break;
			case ("Image"):
				temp = new Pic(p1, p2);
				break;
			}
			repaint();	
		}
	}
	//Getter for the paintobject vector
	public Vector<PaintObject> getPaintObject() {
		return allPaintObjects;
	}
	//setter for the paintobject vector
	public void setPaintObject(Vector<PaintObject> paint) {
		allPaintObjects = paint;
	}
	//getter for the drawing panel
	public DrawingPanel getDrawingPanel() {
		return drawingPanel;
	}
	//setter for the drawing panel
	public void setDrawingPanel(DrawingPanel drawingPanel) {
		this.drawingPanel = drawingPanel;
	}
	//getter for the paintobject style
	public String getStyle() {
		return style;
	}
	//gett for the color chooser
	public JColorChooser getColorChooser(){
		return colorChooser;
	}
	//setter for the paintobject style
	public void setStyle(String string) {
		this.style = string;
	}
	//getter for the color
	public Color getColor() {
		return color;
	}
	//setter for the color
	public void setColor(Color color) {
		this.color = color;
	}

}
