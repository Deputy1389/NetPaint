package server;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import paint.PaintObject;
import view.RunGUI;

public class Client {
	private static final String ADDRESS = "localhost";

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client();
	}

	private static RunGUI view;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	@SuppressWarnings("unchecked")
	public Client() throws UnknownHostException, IOException {
		view = new RunGUI();
		view.setVisible(true);
		try {
			// Connect to the Server (construct a new Socket object)
			socket = new Socket(ADDRESS, Server.SERVER_PORT);
			// Get the server's input and output streams for reads and writes
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			this.cleanUpAndQuit("Couldn't connect to the server");
		}
		// Listen for clients to send messages
		Client.view.getDrawingPanel().addMouseListener(new mouseListener());
		Client.view.getDrawingPanel().addMouseMotionListener(new mouseMotionListener());
		try {																		//to initialize the gui
			Client.view.setPaintObject((Vector<PaintObject>) ois.readObject());		//set the paintobject vector to the one given by the server
			Client.view.repaint();													//repaint the gui
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		ServerListener serverListener = new ServerListener();
		serverListener.start();
	}

	private class ServerListener extends Thread {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			// Repeatedly accept objects from the server and add
			// them to our model.
			try {
				while (true) {
					Client.view.setPaintObject((Vector<PaintObject>) ois.readObject());	//set the paintobject vector to the one given by the server
					Client.view.repaint();												//repaint the gui
				}	
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void cleanUpAndQuit(String message) {
		JOptionPane.showMessageDialog(Client.view, message);
		try {
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			// Couldn't close the socket, we are in deep trouble. Abandon ship.
			e.printStackTrace();
		}
		Client.view.dispatchEvent(new WindowEvent(Client.view, WindowEvent.WINDOW_CLOSING));
	}

	private Point p1, p2;

	private class mouseMotionListener implements MouseMotionListener {
		//Listens to the mouse movement
		@Override
		public void mouseMoved(MouseEvent e) {	
			if (p1 != null && p2 == null) {								//if you click once but not twice
				view.getDrawingPanel().tempPaint(p1, e.getPoint());		//Sends draws a ghost image
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {}
	}

	private class mouseListener implements MouseListener {
		//Listens for mouse clicks
		@Override
		public void mouseClicked(MouseEvent e) {
			if (p1 == null) {								//if the panel is blank
				p1 = new Point(e.getX(), e.getY());			//get the first click as a point
			} else if (p1 != null && p2 == null) {			//if its a second click
				p2 = new Point(e.getX(), e.getY());			//get the second click as a point
				view.TwoPoints(p1, p2);						//draw
				try {
					oos.writeObject(view.getPaintObject());	//send the object to the server
					view.repaint();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					Client.this.cleanUpAndQuit("Couldn't send a message to the server");
				}
			} else {										//if its after a second click
				p1 = null;									//reset points 1 and 2
				p2 = null;
				p1 = new Point(e.getX(), e.getY());			//point 1 is where the user clicked
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
}