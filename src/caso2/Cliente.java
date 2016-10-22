package caso2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

	public final static String IP= "192.168.0.17";
	
	public final static int PUERTO=4444;
	
	private Socket s;
	
	private BufferedReader br;
	
	private PrintWriter pw;

	
	public Cliente() throws Exception{
		
		s = new Socket(IP, PUERTO);
		pw = new PrintWriter(s.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
		manejoComunicacion();
	}
	
	public void manejoComunicacion() throws Exception
	{
		boolean ejecutar = true;
		
		while (ejecutar) {

			pw.println("HOLA");

			if (! recibirRespuesta().equals("OK") )
			{
				System.err.println("Error en protocolo");
				terminarComunicacion();
			}

			//pw.println("ALGORITMOS:" +  + ":RSA:" + );

			//recibirRespuesta(); 
		}
	}
	
	private String recibirRespuesta() throws Exception
	{
		String rta = br.readLine();

		if(rta != null)
		{
			if(rta.equals("ERROR"))
			{
				System.err.println("Error reportado por el servidor. Terminando conexión.");
				terminarComunicacion();
			}
			else
			{
				System.out.println("Respuesta recibida: " + rta);
			}
		}
		else
		{
			System.out.println("No se ha recibido ninguna respuesta");
		}
		return rta;
	}
	
	
	private void terminarComunicacion() throws Exception
	{
		pw.close();
		br.close();
		s.close();
		System.out.println("Se terminó la conexión con el servidor");
	}

	public static void main(String[] args) throws Exception {
		
		Cliente c = new Cliente();
	}
}
