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
		private int numeroMensajesAEnviar;

		/**
		 * Cantidad de mensajes respondidos por el servidor.
		 */
		private int mensajesRespondidos;

		/**
		 * Cantidad de mensajes que han sido enviados al servidor en espera de respuesta.
		 */
		private int mensajesEnviados;

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
		public Cliente(int id, Buffer canal)
		{
			super();
			termino = false;
			this.id = id;
			this.buffer = buffer;
			mensajesRespondidos = 0;
			mensajesEnviados = 0;
			numeroMensajesAEnviar = ( new Random() ).nextInt(MAX_MENSAJES) + 1;
		}
		
		public void run()
		{
			while(mensajesRespondidos < numeroMensajesAEnviar)
			{
				boolean respuestaAUltimoMensajeEnviado = false;

				while( !respuestaAUltimoMensajeEnviado )
				{
					respuestaAUltimoMensajeEnviado = enviarMensaje();
					yield();
				}

				//wait() debe usarse en un bloque de codigo sincronizado
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

				System.out.println("Se han respondido " + mensajesRespondidos + " para el cliente de id " + id); //TODO
			}

			if(!termino)
			{
				terminar();
			}
		}
		
	private void terminar() {
			// TODO Auto-generated method stub
			
		}

	private void recibirRespuestaMensaje() {
			// TODO Auto-generated method stub
			
		}

	private boolean enviarMensaje() {
			// TODO Auto-generated method stub
			return false;
		}

	@Override
	public int compareTo(Cliente o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
