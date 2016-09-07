package mundo;

import java.util.ArrayList;

public class Buffer {

	//-------------------
	//----Atributos------
	//-------------------
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
	
	/**
	 * Cola de clientes que quieren enviar mensajes
	 */
	private ArrayList<Cliente> colaClientes;
	
	
	//------------------------------
	//-----Metodo Constructor-------
	//------------------------------
	public Buffer(int numC,int numS,int tam)
	{
		setNumClientes(numC);
		setNumServidores(numS);
		setTamanoBuffer(tam);
		setNumActualClientes(numC);
		clientes = new ArrayList<Cliente>(numC);
		servidores = new ArrayList<Servidor>(numS);
		listaMensajes = new ArrayList<Mensaje>(tam);
		colaClientes = new ArrayList<Cliente>(numC);
	}
	//---------------------------
	//-----Metodos Get/Set-------
	//---------------------------
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
	
	//-------------------
	//-----Metodos-------
	//-------------------
	
	/**
	 * Metodo que se encarga de crear a los clientes y servidores e iniciar el programa.
	 */
	
	private void iniciarConeccion()
	{
		
	}
}
