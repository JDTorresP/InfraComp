package mundo;

public class Servidor extends Thread {

	//Constantes

	/**
	 * Respuesta por defecto del servidor en caso de no haber otra especificada
	 */
	private final static String RTA = "OK";

	//Atributos

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


	//Constructor

	public Servidor(int id, Buffer buffer)
	{
		this.id = id;
		this.buffer = buffer;
		mensajeActual = null;
	}

	//metodos

	public void run()
	{

		while(buffer.getNumActualClientes()>0)
		{
			while(buffer.getNumActualClientes()>0&&mensajeActual==null)
			{
				buffer.esperar();
				mensajeActual=buffer.pedirMensaje();
			}
			
			if(mensajeActual!=null)
			{
				synchronized(this)
				{
					responderMensaje();
				}
			}
		}
		System.out.println("termine");
		buffer.despertarTodos();

	}

	public Mensaje getMensajeActual()
	{
		return mensajeActual;
	}
	/**
	 * manda la respuesta al mensaje actual
	 */
	synchronized private void responderMensaje()
	{
		System.out.println(mensajeActual.getRespuesta());
		mensajeActual.recibirRespuesta(RTA);
		mensajeActual = null;
	}
}