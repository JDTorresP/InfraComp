package mundo;

public class Servidor extends Thread {
	
	
	/**
	 * Respuesta por defecto del servidor en caso de no haber otra especificada
	 */
	private final static String RTA = "OK";
	
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

	
	//------------------------------
	//-----Metodo Constructor-------
	//------------------------------
	
	public Servidor(int id, Buffer buffer)
	{
		this.id = id;
		this.buffer = buffer;
		mensajeActual = null;
	}
	
	public void run()
	{
		
		while(buffer.getNumActualClientes()>0)
		{
			
			synchronized(this)
			{
				while((buffer.getNumActualClientes()>0)&&mensajeActual==null)
				{
					//System.out.println("Servidor "+id+" intenta solicitar mensaje");
					solicitarMensaje();
					yield();
				}
			}
			if((buffer.getNumActualClientes()>0)&&mensajeActual!=null)
			{
				//System.out.println("Servidor "+id+" tiene mensaje");
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
	 * solicita un mensaje al buffer
	 */
	synchronized private void solicitarMensaje()
	{
		mensajeActual = buffer.pedirMensaje();
		//System.out.println(mensajeActual);
	}
	
	/**
	 * manda la respuesta al mensaje actual
	 */
	synchronized private void responderMensaje()
	{
		mensajeActual.recibirRespuesta(RTA);
		mensajeActual = null;
	}
	
}
