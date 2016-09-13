package mundo;

import java.util.ArrayList;

public class Buffer {

	//Atributos

	private int numClientes = 0 ;
	private int numServidores = 0;
	private int tamanoBuffer = 0;
	private int numActualClientes= 0;




	/**
	 * Lista de servidores inicializados por el Buffer
	 */
	private ArrayList<Servidor> servidores;

	/**
	 * Lista de mensajes pendientes de respuesta recibidos por el buffer
	 */
	private ArrayList<Mensaje> listaMensajes;


	//Costructor

	public Buffer(int numC,int numS,int tam)
	{
		setNumClientes(numC);
		setNumServidores(numS);
		setTamanoBuffer(tam);
		setNumActualClientes(numC);

		servidores = new ArrayList<Servidor>(numS);
		listaMensajes = new ArrayList<Mensaje>(tam);

		for(int i = 0; i < numServidores; i++)
		{
			new Servidor(i,this).start();
			System.out.println("servidor " + i);
		}
	}

	//Metodos


	/**
	 * Metodo que llama un Cliente para enviar un mensaje al buffer
	 * @param mensaje objeto de tipo Mensaje que envia el cliente
	 * @return true si fue posible almacenar el mensaje, false de lo contrario
	 */
	synchronized public boolean recibirMensaje(Mensaje mensaje)
	{
		synchronized(listaMensajes)
		{
			synchronized(this)
			{
				if (listaMensajes.size() < tamanoBuffer)
				{
					listaMensajes.add(mensaje);
					System.out.println("cantidad mensajes guardados: " + listaMensajes.size());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * metodo que llama un servidor para pedir un mensaje.
	 * @param mensaje
	 * @return el mensaje en la posicion 0 de la cola de mensajes
	 */
	synchronized public Mensaje pedirMensaje()
	{
		Mensaje mensaje=null;
		synchronized(listaMensajes)
		{
			synchronized(this)
			{

				mensaje=listaMensajes.get(0);
				listaMensajes.remove(0);

			}
		}
		return mensaje;
	}

	/**
	 * Metodo que llama un cliente para terminar su ejecucion porque resolvio todos los mensajes correctamente
	 */
	synchronized public void retirarCliente(Cliente c)
	{
		synchronized(this)
		{

			numActualClientes--;

		}

		System.out.println("numero actual clientes: " + numActualClientes); 

	}


	/**
	 * le avisa al servidor que ya hay mensajes disponibles
	 */
	synchronized private void hayMensajes()
	{
		synchronized(this)
		{
			
				servidores.notify();
			
		}

	}

	synchronized public void esperar()
	{
		System.out.println("me estoy durmiendo");
		try
		{
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("me desperté");

	}

	//Metodos get/set

	public int getNumClientes() {
		return numClientes;
	}
	public void setNumClientes(int numClientes) {
		this.numClientes = numClientes;
	}
	public int getNumServidores() {
		return numServidores;
	}
	public void setNumServidores(int numServidores) {
		this.numServidores = numServidores;
	}
	public int getTamanoBuffer() {
		return tamanoBuffer;
	}
	public void setTamanoBuffer(int tamanoBuffer) {
		this.tamanoBuffer = tamanoBuffer;
	}
	public int getNumActualClientes() {
		return numActualClientes;
	}
	public void setNumActualClientes(int numActualClientes) {
		this.numActualClientes = numActualClientes;
	}
	public ArrayList<Mensaje> getListaMensajes() {
		return listaMensajes;
	}
}
