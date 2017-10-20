package server;

//Sean Gallagher, Mark Hunter
//Alex's Section
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import paint.PaintObject;

public class Server {
	public static final int SERVER_PORT = 5001;
	private static ServerSocket sock;
	private static List<ObjectOutputStream> clients = Collections
			.synchronizedList(new ArrayList<>());
	private static ArrayList<ClientHandler> cHandler = new ArrayList<ClientHandler>();
	
	public static void main(String[] args) throws IOException {
		sock = new ServerSocket(SERVER_PORT);
		System.out.println("Server started on port " + SERVER_PORT);
		Vector<PaintObject> allPaintObjects = new Vector<PaintObject>();	//holds the paint objects for all clients
		while (true) {
			//Accept a connection from the ServerSocket.
			Socket s = sock.accept();
			ObjectInputStream is = new ObjectInputStream(s.getInputStream());
			ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
			//Add the output stream to our list of clients
			//so we can write to all clients output streams later.
			clients.add(os);
			// Start a new ClientHandler thread for this client.
			for(int i=0; i<cHandler.size(); i++)
				if(cHandler.size()>0 && allPaintObjects.size()<=cHandler.get(i).getPaintObjects().size()){
					allPaintObjects = cHandler.get(i).getPaintObjects();	//get the updated allpaintobjects vector
				}	
			ClientHandler c = new ClientHandler(is, clients, allPaintObjects);	
			//add the new clientHandler to the list
			cHandler.add(c);			
			c.start();	//start the thread
			allPaintObjects = cHandler.get(cHandler.size()-1).getPaintObjects();
			System.out.println("Accepted a new connection from " + s.getInetAddress());
		}
	}
}
class ClientHandler extends Thread {
	private Vector<PaintObject> allPaintObjects;
	private ObjectInputStream input;
	private List<ObjectOutputStream> clients;

	//Constructor for the ClientHandler
	//Takes an input stream, a list of outputstreams and a paint object vector as parameters
	public ClientHandler(ObjectInputStream input, List<ObjectOutputStream> clients, Vector<PaintObject> allPaintObjects2) {
		this.input = input;
		this.clients = clients;
		allPaintObjects = allPaintObjects2;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Vector<PaintObject> tempPaint;	//temporary paint object
		this.writeToClients(allPaintObjects);
		while (true) {
			try {
				tempPaint = (Vector<PaintObject>) input.readObject();
				allPaintObjects = tempPaint;
				this.writeToClients(allPaintObjects);
			} catch (IOException e) {
				/* Client left -- clean up and let the thread die */
				this.cleanUp();
				return;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				this.cleanUp();
				return;
			}
		}
	}

	private void writeToClients(Vector<PaintObject> paint) {
		synchronized (clients) {
			// Use an enhanced for loop to Send a string to all clients
			// the client list by iterating over all ObjectOutputStreams in
			// clients
			for (ObjectOutputStream client : clients) {
				try {
					client.reset();
					client.writeObject(paint);
					client.reset();
				} catch (IOException e) {
				}
			}
		}
	}

	// Write a method that closes all the
	// resources of a ClientHandler and logs a message, and call it from every
	// place that a fatal error occurs in ClientHandler (the catch blocks that
	// you can't recover from).
	private void cleanUp() {
		try {
			this.input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Getter for the paint object vector
	public Vector<PaintObject> getPaintObjects(){
		return allPaintObjects;
	}
}