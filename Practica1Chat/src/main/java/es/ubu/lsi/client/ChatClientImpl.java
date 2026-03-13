package es.ubu.lsi.client;

import java.net.*;
import java.io.*;
import java.util.*;

import java.util.HashSet;
import java.util.Set;

import es.ubu.lsi.common.ChatMessage;
import es.ubu.lsi.common.ChatMessage.MessageType;

/**
 * Client.
 * 
 * @author http://www.dreamincode.net
 * @author Raúl Marticorena
 * @author Joaquin P. Seco
 *
 */
public class ChatClientImpl implements ChatClient {
	
	/** Input stream. */
	private ObjectInputStream sInput; // to read from the socket
	/** Output stream. */
	private ObjectOutputStream sOutput; // to write on the socket
	/** Socket. */
	private Socket socket;

	/** Server name/IP. */
	private String server;
	/** User name. */
	private String username;
	/** Port. */
	private int port;
	
	/** Flag to keep running main thread. */
	private boolean carryOn = true;

	/** Id. */
	private int id;
	
	/**MODIFICACION!: HashMap que contendrá los nombres e id de usuarios del chat**/
	private Map<String, Integer> nombIdUsuario = new HashMap<>();
	/**HashSet que contiene unicamente la lista de puertos baneados**/
	private Set<Integer> puertoBaneado = new HashSet<>();
	/**HahsSet que contiene nombres baneados (SOLO EN CASO QUE NO SE HAYA INICIADO AUN)**/
	private Set<String> usuarioBaneado = new HashSet<>();
	/**
	 * Constructor.
	 * 
	 * @param server server
	 * @param port port
	 * @param username user name
	 * 
	 */
	public ChatClientImpl(String server, int port, String username) {
		// which calls the common constructor with the GUI set to null
		this.server = server;
		this.port = port;
		this.username = username;
	}

	/**
	 * Starts chat.
	 * 
	 * @return true if everything goes right, false in other case
	 */
	@Override
	public boolean start() {
		// try to connect to the server
		try {
			socket = new Socket(server, port);
			String msg = "Connection accepted " + socket.getInetAddress() + ":"
					+ socket.getPort();
			display(msg); //Publica el mensaje en pantalla
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
			
		}
		catch (IOException eIO) {
			display("Exception creating new Input/output Streams: " + eIO);
			return false;
		}
		catch (Exception ec) {
			display("Error connectiong to server:" + ec);
			return false;
		}			
		// Login and receive id
		try {			
			sOutput.writeObject(username);	
			sOutput.flush();
			id = sInput.readInt();
		} catch (IOException eIO) {
			display("Exception doing login : " + eIO);
			disconnect();
			return false;
		}
		// creates the Thread to listen from the server
		new Thread(new ChatClientListener()).start();
		// success we inform the caller that it worked
		return true;
	}

	/**
	 * Displays messages.
	 * 
	 * @param msg text to show in console
	 */
	private void display(String msg) {
		System.out.println(msg); // println in console mode
	}

	/**
	 * Sends a message to the server.
	 * 
	 * @param msg message
	 */
	@Override
	public synchronized void sendMessage(ChatMessage msg) {
		try {
			if (this.carryOn) {
				sOutput.writeObject(msg);
			}
		} catch (IOException e) {
			display("Exception writing to server: " + e);
		}
	}

	/**
	 * Disconnect client closing resources.
	 */
	@Override
	public void disconnect() {
		
		try {
			display("Trying to disconnect and close client with username " + username);
			if (sInput != null)  {
				sInput.close();
				sInput = null;
			}
			if (sOutput != null) {
				sOutput.close();
				sOutput = null;
			}
			if (socket != null && !socket.isClosed()) {
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			
			display("Disconnect with error, closing resources, closed previously.");
		}
		finally{
			display("Bye!");
			carryOn = false;
		}
	}

	/**
	 * Starts the client.
	 * 
	 * To start the Client in console mode use one of the following command >
	 * java Client > java Client username > java Client username portNumber >
	 * java Client username portNumber serverAddress at the console prompt If
	 * the portNumber is not specified 1500 is used If the serverAddress is not
	 * specified "localHost" is used If the username is not specified
	 * "Anonymous" is used > java Client is equivalent to > java Client
	 * Anonymous 1500 localhost are equivalent.
	 * 
	 * In console mode, if an error occurs the program simply stops when a GUI
	 * id used, the GUI is informed of the disconnection
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		// default values
		int portNumber = 1500;
		String serverAddress = "localhost";
		String userName = "Anonymous";

		// depending of the number of arguments provided we fall through
		switch (args.length) {
		// > javac Client username portNumber serverAddr
		case 3:
			serverAddress = args[2];
			// > javac Client username portNumber
		case 2:
			try {
				portNumber = Integer.parseInt(args[1]);
			} catch (Exception e) {
				System.out.println("Invalid port number.");
				System.out
						.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
				return;
			}
			// > javac Client username
		case 1:
			userName = args[0];
			// > java Client
		case 0:
			break;
		// invalid number of arguments
		default:
			System.err.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		// create the Client object
		ChatClient client = new ChatClientImpl(serverAddress, portNumber, userName);
		// test if we can start the connection to the Server
		// if it failed nothing we can do
		if (!client.start()) {
			System.err.println("Error connecting server. Check network and server status.");
			return;
		}

		// wait for messages from user
		ChatClientImpl clientChat = ((ChatClientImpl) client);
		try (Scanner scan = new Scanner(System.in)) {
			// loop forever for message from the user
			while (clientChat.carryOn) {
				System.out.print("> ");
				// read message from user
				String userMsg = scan.nextLine();		/// RECIBO MENSAJE ///
				/*IMPLEMENTO FUNCIONALIDAD DE BAN - UNBAN a partir del mensaje*/
				if (userMsg.startsWith("ban ")) { //BAN
					String usuario = userMsg.substring(4).toLowerCase();
					//guardo nombre de usuairo por si aun no esta conectado
					clientChat.usuarioBaneado.add(usuario);
					//Guardo numero de puerto
					Integer puerto= clientChat.nombIdUsuario.get(usuario);
					
					if(puerto != null) {
						clientChat.puertoBaneado.add(puerto);
					} 
					System.out.println("Usuario " + usuario + " baneado de este chat.");
					
					//Mensaje al servidor indicando que hemos baneado
					client.sendMessage(new ChatMessage(clientChat.id, MessageType.MESSAGE, 
							clientChat.username + " ha baneado a " + usuario));
					continue;
				}
				if (userMsg.startsWith("unban ")) { //UNBAN
					String usuario = userMsg.substring(6).toLowerCase();
					//posibilidad de desbanear si aun no se ha conectado
					clientChat.usuarioBaneado.remove(usuario);
					Integer puerto = clientChat.nombIdUsuario.get(usuario);
					
					if(puerto != null) {
						clientChat.puertoBaneado.remove(puerto);
					}
					System.out.println("Usuario "+usuario+" desbloqueado.");
					
					//Avisamos al servidor que hemos desbaneado al usuario
					client.sendMessage(new ChatMessage(clientChat.id, MessageType.MESSAGE, 
							clientChat.username + " ha desbloqueado a " + usuario));
					continue;
				} ///// fin BAN/UNBAN
				// logout if message is LOGOUT
				if (userMsg.equalsIgnoreCase(MessageType.LOGOUT.toString())) {
					client.sendMessage(new ChatMessage(clientChat.id, MessageType.LOGOUT,
							MessageType.LOGOUT.toString()));
					// break to do the disconnect
					break;
					
				} else if (userMsg.equalsIgnoreCase(MessageType.SHUTDOWN.toString())) {
					client.sendMessage(new ChatMessage(clientChat.id, MessageType.SHUTDOWN,
							MessageType.SHUTDOWN.toString()));
					// break to do the disconnect
					break;
				
				} else { // default to ordinary message
					String autoria = "Alberto patrocina el mensaje: " +userMsg;
					client.sendMessage(new ChatMessage(clientChat.id, MessageType.MESSAGE, autoria));
				}
				System.out.println();
			} // try with resources
		}
		// done disconnect by logout, not shutdown
		client.disconnect();		
	}

	/**
	 * Client listener for messages from server.
	 * 
	 */
	class ChatClientListener implements Runnable {
		
		/**
		 * Run.
		 */
		public void run() {
			while (true) {
				try {
					ChatMessage msg = (ChatMessage) sInput.readObject(); //recibo mensaje y guardo emisor
					String mensaje = msg.getMessage();		//Extraigo texto msg
					if (msg.getId() != id) {
						int puertoId = msg.getId();
						
						if(mensaje.contains(":")) {
							String[] divide = mensaje.split(" ");
							
							if (divide.length >= 2) {
								String emisor = divide[1].replace(":", "").toLowerCase(); //Divido mensaje + lowercase
								nombIdUsuario.put(emisor,  puertoId);
								//Agregamos usuarios baneados en caso de que no haya iniciado aun 
								if (usuarioBaneado.contains(emisor)) {
									puertoBaneado.add(puertoId);
								}
							}
						} else if (mensaje.contains("now connected")) {
							//En caso de que nunca haya escrito
							String[] divide = mensaje.split(" ");
							
							if(divide.length >= 2) {
								String emisor = divide[1].toLowerCase(); //divido mensaje + lowercase
								nombIdUsuario.put(emisor,  puertoId);
								//Agregamos usuarios baneados en caso de que no haya iniciado aun 
								if (usuarioBaneado.contains(emisor)) {
									puertoBaneado.add(puertoId);
								}
							}
						}				
					}
					
					if (msg.getId() != id && !puertoBaneado.contains(msg.getId())) { //IMPORTANTE si esta ban no se imprime
						// if console mode print the message and add back the prompt
						System.out.println(msg.getMessage());
						System.out.print("\n> ");
					}
						
				} catch (IOException e) {
					display("Server has closed the connection. ");
					carryOn = false;
					break;
				} catch (ClassNotFoundException e2) {
					throw new RuntimeException("Wrong message type", e2);
				}
			} // while
		} // run
	} // ChatClientListener

} // ChatClient
