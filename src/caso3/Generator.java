package caso3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class Generator
{
	private final static Boolean SERVIDOR_CON_SEGURIDAD = true;
	private final static Boolean SERVIDOR_SIN_SEGURIDAD = false;
	public static final String DEVOLVER_ATENDIDAS="devolver";

	/**
	 * Carga el servicio del generador del lib
	 */
	private LoadGenerator generator;
	public static Long tiempoAutenticacion=0L;
	public static Long tiempoConsulta=0L;
	public static int numClientes =0 ;

	public Generator(int ntask, int gap, int numThrea, boolean seguridad)
	{
		Task work = createTask(seguridad);
		int numberOfTasks = ntask;
		int gapBetweenTASKS = gap;
		generator = new LoadGenerator("Cliente - Server Load Test", numberOfTasks, work, gapBetweenTASKS);
		generator.generate();

	}

	private Task createTask(boolean seguridad)
	{
		if(seguridad){return new ClientServerTask(SERVIDOR_CON_SEGURIDAD,this);}
		return new ClientServerTask(SERVIDOR_SIN_SEGURIDAD,this);
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{
		// variables de coneccion para llenar por el cliente
		boolean eje=true;
		int es=0;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		int nThreads=0;
		int carga=0;
		int retardo=0;
		boolean seguridad = false;
		int puerto = 4444;

		//manejo del cliente
		while (eje) {
			if(es==0)
			{
				System.out.println("Indique el numero de threads"); fromUser = stdIn.readLine();
				nThreads=Integer.valueOf(fromUser); es++;
			}
			else if(es==1)
			{
				System.out.println("Indique el numero consultas o carga"); fromUser = stdIn.readLine();
				carga=Integer.valueOf(fromUser); es++;
			}
			else if(es==2)
			{
				System.out.println("Indique el tiempo de retardo en milisegundos"); fromUser = stdIn.readLine();
				retardo=Integer.valueOf(fromUser); es++;
			}
			else if(es==3)
			{
				System.out.println("Presione Y para conectarse al servidor con seguridad /n" +
			"Presione N de lo contrario"); fromUser = stdIn.readLine();
				if(fromUser.equalsIgnoreCase("y")){seguridad=true;} eje=false;
			}
		}
		if(seguridad){puerto=4443;}
		
		//primera coneccion con el servidor para asignacion de threads en el pool
		Socket s = new Socket("172.24.42.139", puerto);
		PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
		pw.println(nThreads); System.out.println("se inicializo con "+nThreads+ " threads el executor del servidor");
		pw.close();
		s.close();
		
		//se realizan las pruebas con los datos ingresados
		Generator gen = new Generator(carga,retardo,nThreads,seguridad);
		
		// Tiempo de espera por resultados, se aconseja entre mas threads menor tiempo
		TimeUnit.MINUTES.sleep(1);
		TimeUnit.SECONDS.sleep(10);
		
		System.out.println("tiempo de autenticacion promedio = "+tiempoAutenticacion/numClientes+ " miliseg");
		System.out.println("tiempo de consulta promedio = "+tiempoConsulta/numClientes+ " miliseg");
		
		// se pide por ultimo el numero de respuestas atendidas por el servidor, esto genera exception en el
		//protocolo del servidor, pero como es la ultima peticion no importa.
		s = new Socket("172.24.42.139", puerto);
		pw = new PrintWriter(s.getOutputStream(), true);
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pw.println(DEVOLVER_ATENDIDAS);
		System.out.println("Numero de transacciones perdidas: "+ String.valueOf(carga-Integer.valueOf(br.readLine())));
	}
}
