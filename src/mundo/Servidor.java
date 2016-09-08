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
	private Buffer canal;

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
	
	public Servidor(int id, Buffer canal)
	{
		this.id = id;
		this.canal = canal;
		mensajeActual = null;
		hayClientes = true;
	}
	
}
