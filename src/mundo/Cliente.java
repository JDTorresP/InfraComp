package mundo;

public class Cliente extends Thread implements Comparable<Cliente>{


	//Atributos

	/**
	 * Identificador entero del Cliente
	 */
	private int id;

	/**
	 * Numero de mensajes que debe enviar el cliente
	 */
	private int numeroMsAEnviar;

	/**
	 * Cantidad de mensajes respondidos por el servidor.
	 */
	private int msRespondidos;

	/**
	 * Cantidad de mensajes que han sido enviados al servidor en espera de respuesta.
	 */
	private int msEnviados;

	/**
	 * Buffer de envio de mensajes
	 */
	private Buffer buffer;
	/**
	 * modela si se enviaron todos los mensajes
	 */
	boolean termino;

	//Constructor
	/**
	 * metodo que crea un nuevo cliente, para facilitar las pruebas en numero de mensajes a 
	 * enviar es el id del cliente +1.
	 * @param id
	 * @param buffer
	 */
	public Cliente(int id, Buffer buffer)
	{
		termino = false;
		this.id = id;
		this.buffer = buffer;
		
		msRespondidos = 0;
		msEnviados = 0;
		numeroMsAEnviar = id+10;
		//numeroMsAEnviar = (int)((Math.random()*15)+1);
	}

	//Metodos
	
	public void run()
	{
		while(msRespondidos < numeroMsAEnviar)
		{
			boolean msenv = false;

			while( !msenv )
			{
				msenv = enviarMensaje();
				yield();
			}
			

			synchronized(this)
			{
				recibirRespuesta();
			}

			System.out.println("Se han respondido " + msRespondidos + " para el cliente de id " + id);
		}
	}

	/**
	 * metodo que recibe respuesta a un mensaje
	 */
	private void recibirRespuesta() {

		synchronized(this)
		{
			msRespondidos++;

			if (numeroMsAEnviar == msRespondidos)
			{
				buffer.retirarCliente(this);
				termino = true;
			}
		}
	}

	/**
	 * metodo que envia un mensaje al buffer,
	 * @return true si se envia con exito el mensaje, false de lo contrario
	 */
	private boolean enviarMensaje() {
		Mensaje mensaje = new Mensaje(id + ":" + msEnviados, this);
		boolean rta = buffer.recibirMensaje(mensaje);
		if(rta)
		{
			msEnviados++;
			mensaje.esperar();
		}
		return rta;
	}
	
	public int darId()
	{
		return id;
	}

	public int compareTo(Cliente o) {
		return id - o.darId();
	}

}
