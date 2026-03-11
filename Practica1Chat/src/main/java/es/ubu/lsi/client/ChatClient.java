package es.ubu.lsi.client;

import es.ubu.lsi.common.ChatMessage;

/**
 * Chat client.
 * Interfaz del cliente del chat 1.0
 * @author ALBERTO RAFAEL MUÑOZ MORENO
 *
 */
public interface ChatClient {

	/**
	 * Starts chat.
	 * conectarse al servicor
	 * @return true if everything goes right, false in other case
	 */
	public abstract boolean start();

	/**
	 * Sends a message to the server.
	 * Envio de mensajes
	 * @param msg message
	 */
	public abstract void sendMessage(ChatMessage msg);

	/**
	 * Disconnect client closing resources.
	 * Desconectarse o salir del servidor
	 */
	public abstract void disconnect();

}