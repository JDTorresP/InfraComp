package mundo;

public class Mensaje {
	
	//Atributos
	
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
 
	
	//Constructores
	
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
	
	//Metodos
	
	public String getId()
	{
		return id;
	}
	
	/**
	 * recibe la respuesta al mensaje
	 * @param rta
	 */
	synchronized public void recibirRespuesta(String rta)
	{
		respuesta = rta;
		enviarRespuesta();
	}
	
	/**
	 * le avisa al cliente que ya tiene respuesta
	 */
	synchronized private void enviarRespuesta()
	{
		synchronized(this)
		{
			notify();
		}
	}
	
	synchronized public void esperar()
	{
		synchronized(this)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//metodos get/set
	public Cliente getRemitente() {
		return remitente;
	}

	public void setRemitente(Cliente remitente) {
		this.remitente = remitente;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public void setId(String id) {
		this.id = id;
	}
}
