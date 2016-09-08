package mundo;

public class Mensaje {
	
	
	/**
	 * Mensaje enviado entre todos los clientes
	 */
	private final static String MENSAJE_POR_DEFECTO = "crack";
	
	/**
	 * id del mensaje
	 */
	private String id;

	/**
	 * Cliente propietario del envio del mensaje
	 */
	private Cliente remitente;

	/**
	 * Mensaje general hacia el servidor
	 */
	private String mensaje;

	/**
	 * Respuesta que fue recibida por parte del servidor
	 */
	private String respuesta;
 
	/**
	 * Constructor que asigna al mensaje del objeto el mensaje por defecto.
	 * @param remitente creador del objeto Mensaje
	 */
	public Mensaje(String id, Cliente remitente)
	{
		this.id = id;
		this.remitente = remitente;
		mensaje = MENSAJE_POR_DEFECTO;
		respuesta = "";
	}

	/**
	 * Constructor que recibe el remitente y su propio mensaje
	 * @param remitente creador del objeto Mensaje
	 * @param mensaje mensaje que contendra el objeto
	 */
	public Mensaje(String id, Cliente remitente, String mensaje)
	{
		this.id = id;
		this.remitente = remitente;
		this.mensaje = mensaje;
		respuesta = "";
	}

}
