package mundo;

import java.util.ArrayList;

public class Buffer {

	//Atributos

	private int numClientes = 0 ;
	private int numServidores = 0;
	private int tamanoBuffer = 0;
	private int numActualClientes= 0;

	
	/**
	 * Lista de clientes inicializados por el buffer
	 */
	private ArrayList<Cliente> clientes;
	
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
		
		clientes = new ArrayList<Cliente>(numC);
		servidores = new ArrayList<Servidor>(numS);
		listaMensajes = new ArrayList<Mensaje>(tam);


		for(int i = 0; i < numServidores; i++)
		{
			servidores.add( new Servidor(i, this) );
			servidores.get(i).start();
		}

		
		for(int i = 0; i < numClientes; i++)
		{
			clientes.add( new Cliente(i, this) );
			clientes.get(i).start();
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
				if (listaMensajes.size() > 0)
				{
					mensaje=listaMensajes.get(0);
					listaMensajes.remove(0);
				}
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
			for (int i = 0; i < clientes.size(); i++)
			{
				Cliente c1 = clientes.get(i);
				if (c1.compareTo(c) == 0)
				{
					clientes.remove(i);
					numActualClientes--;
				}
			}
			
			System.out.println("numero actual clientes: " + numActualClientes); 

		}
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
}
