package mundo;

import java.util.Random;

public class Cliente extends Thread implements Comparable<Cliente>{

	//-------------------------------------------------------------------------------
	//Constantes
	//-------------------------------------------------------------------------------

	/**
	 * Constante maximo de mensajes
	 */
	private final static int MAX_MENSAJES = 10;

	//-------------------
	//----Atributos------
	//-------------------

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

	//------------------------------
	//-----Metodo Constructor-------
	//------------------------------
	/**
	 * @param id id del cliente, no es necesario en la arquitectura actual (caso 1), pero se agrega por seguridad.
	 * @param canal
	 */
	public Cliente(int id, Buffer buffer)
	{
		super();
		termino = false;
		this.id = id;
		this.buffer = buffer;
		msRespondidos = 0;
		msEnviados = 0;
		numeroMsAEnviar = ( new Random() ).nextInt(MAX_MENSAJES) + 1;
	}

	public void run()
	{
		if(!termino)
		{
			terminar();
		}
		while(msRespondidos < numeroMsAEnviar)
		{
			//respuesta al ultimo mensaje enviado
			boolean msenv = false;

			while( !msenv )
			{
				msenv = enviarMensaje();
				yield();
			}
			synchronized(this)
			{
				try 
				{
					wait();
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			synchronized(this)
			{
				recibirRespuestaMensaje();
			}

			System.out.println("Se han respondido " + msRespondidos + " para el cliente de id " + id); //TODO
		}
	}

	private void terminar() {
		synchronized(this)
		{
			buffer.retirarCliente(this);
			termino = true;
		}
	}

	private void recibirRespuestaMensaje() {

		synchronized(this)
		{
			msRespondidos++;

			if (numeroMsAEnviar == msRespondidos)
			{
				terminar();
			}
		}

	}

	private boolean enviarMensaje() {
		Mensaje mensaje = new Mensaje(id + ":" + msEnviados, this);
		boolean rta = buffer.recibirMensaje(mensaje);
		if(rta)
		{
			msEnviados++;
		}
		return rta;
	}

	@Override
	public int compareTo(Cliente o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
