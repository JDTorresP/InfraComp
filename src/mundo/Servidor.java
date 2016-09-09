package mundo;

public class Servidor extends Thread {
	//-------------------
	//----Atributos------
	//-------------------
	
	/**
	 * id del servidor
	 */
	private int id;

	/**
	 * El canal del que el servidor tomara los mensajes para responderlos
	 */
	private Buffer buffer;

	/**
	 * Mensaje que el servidor esta respondiendo actualmente
	 */
	private Mensaje mensajeActual;

	/**
	 * True si hay clientes, false de lo contrario.
	 */
	private boolean hayClientes;
	
	//------------------------------
	//-----Metodo Constructor-------
	//------------------------------
	
	public Servidor(int id, Buffer buffer)
	{
		this.id = id;
		this.buffer = buffer;
		mensajeActual = null;
		hayClientes = true;
	}
	
	public void run()
	{

	}
	
}
