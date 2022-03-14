package pt.tecnico.sockets;

import java.io.*;
import java.net.*;

public class SocketServer {

	public static void main(String[] args) throws IOException {
		// Check arguments
		if (args.length < 1) {
			System.err.println("Argument(s) missing!");
			System.err.printf("Usage: java %s port%n", SocketServer.class.getName());
			return;
		}

		// Convert port from String to int
		final int port = Integer.parseInt(args[0]);

		// Create server socket
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.printf("Server accepting connections on port %d %n", port);

		// wait for and then accept client connection
		// a socket is created to handle the created connection
		Socket clientSocket = serverSocket.accept();
		final String clientAddress = clientSocket.getInetAddress().getHostAddress();
		final int clientPort = clientSocket.getPort();
		System.out.printf("Connected to client %s on port %d %n", clientAddress, clientPort);

		// Create buffered stream to receive data from client, one line at a time
		DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

		// Receive data until client closes the connection
		String response;
		while (true) {
			// Read a line of text.
			// A line ends with a line feed ('\n').
			response = in.readLine();
			if (response == null) {
				break;
			}
			System.out.printf("Received message with content: '%s'%n", response);
			String[] answer = response.split(" ");
			Integer answer1 = Integer.parseInt(answer[1]);
			answer1++;
			out.writeBytes(Integer.toString(answer1) + "\n");
			System.out.printf("Transmiting new int: %s \n", Integer.toString(answer1));
		}

		// Close connection to current client
		clientSocket.close();
		System.out.println("Closed connection with client");

		// Close server socket
		serverSocket.close();
		System.out.println("Closed server socket");
	}

}
