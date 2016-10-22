package caso2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {

	//IP y Puerto

	public final static String IP= "192.168.0.17";

	public final static int PUERTO=4444;


	//Algoritmos por defecto

	public String algoritmoSimetrico="DES";

	public String algoritmoAsimetrico="RSA";

	private String hmac = "HMACSHA1";


	//Socket, Reader y Writer

	private Socket s;

	private BufferedReader br;

	private PrintWriter pw;

	//estado
	private int estado=0;


	public Cliente() throws Exception{

		decidirAlgoritmos();
		
		conectarConServidor();
		
		manejoComunicacion();
	}

	public void decidirAlgoritmos() throws Exception
	{
		boolean ejecutar=true;
		int es=0;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;

		while (ejecutar) {
			if(es==0)
			{
				System.out.println("Defina un algoritmo Simetrico");
				fromUser = stdIn.readLine();
				
				if(!fromUser.equals("DES")&&!fromUser.equals("AES")&& !fromUser.equals("Blowfish")&&!fromUser.equals("RC4"))
				{
					System.out.println("Algoritmo "+ fromUser + " no valido");
				}
				else{
					algoritmoSimetrico=fromUser;
					es++;
				}
				
			}
			else if(es==1)
			{
				System.out.println("Defina un algoritmo Asimetrico");
				fromUser = stdIn.readLine();
				if(!fromUser.equals("RSA"))
				{
					System.out.println("Algoritmo "+ fromUser + " no valido");
				}
				else{
					algoritmoAsimetrico=fromUser;
					es++;
				}
				
			}
			else if(es==2)
			{
				System.out.println("Defina un algoritmo HMAC");
				fromUser = stdIn.readLine();
				if(!fromUser.equals("HMACMD5")&&!fromUser.equals("HMACSHA1")&&!fromUser.equals("HMACSHA256"))
				{
					System.out.print("Algoritmo "+ fromUser + " no valido");
				}
				else{
					hmac=fromUser;
					ejecutar=false;
				}
				
			}
			
		}
	}
	
	public void conectarConServidor() throws Exception
	{
		s = new Socket(IP, PUERTO);
		pw = new PrintWriter(s.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	public void manejoComunicacion() throws Exception
	{
		boolean ejecutar=true;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser="";

		while (ejecutar) {

			if(estado==0)
			{
				System.out.print("Escriba HOLA para iniciar la consulta:");
				fromUser = stdIn.readLine();
			}
			if(estado==1)
			{
				fromUser="ALGORITMOS:"+algoritmoSimetrico+":"+algoritmoAsimetrico+":"+hmac;
				ejecutar=false;

			}
			if(estado==2)
			{

			}

			if (fromUser != "") {

				System.out.println("Cliente: " + fromUser);
				pw.println(fromUser);

			}
			String res =recibirRespuesta();
		}
		stdIn.close();
		terminarComunicacion();
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
				estado++;
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
