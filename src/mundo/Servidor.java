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
				synchronized(this)
				{
					try {
						System.out.println("zzz");
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("he despertado");
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
		mensajeActual.recibirRespuesta(RTA);
		System.out.println(mensajeActual.getRespuesta());
		mensajeActual = null;
	}
}