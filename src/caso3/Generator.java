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

	/**
	 * Carga el servicio del generador del lib
	 */
	private LoadGenerator generator;
	public static Long tiempoAutenticacion=0L;
	public static Long tiempoConsulta=0L;
	public static int numClientes =0 ;

	public Generator(int ntask, int gap, int numThrea)
	{
		Task work = createTask();
		int numberOfTasks = ntask;
		int gapBetweenTASKS = gap;
		generator = new LoadGenerator("Cliente - Server Load Test", numberOfTasks, work, gapBetweenTASKS);
		generator.generate();

	}

	private Task createTask()
	{
		return new ClientServerTask(SERVIDOR_CON_SEGURIDAD,this);
	}

	public static void main(String[] args) throws IOException, InterruptedException
	{
		boolean eje=true;
		int es=0;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		int nThreads=0;
		int carga=0;
		int retardo=0;

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
				retardo=Integer.valueOf(fromUser); eje=false;
			}
		}
		
		Socket s = new Socket("172.24.42.139", 4443);
		PrintWriter pw = new PrintWriter(s.getOutputStream(), true);
		pw.println(nThreads); System.out.println("se inicializo con "+nThreads+ " threads el executor del servidor");
		pw.close();
		s.close();
		Generator gen = new Generator(carga,retardo,nThreads);
		
		TimeUnit.SECONDS.sleep(20);
		System.out.println("tiempo de autenticacion promedio = "+tiempoAutenticacion/numClientes+ " miliseg");
		System.out.println("tiempo de consulta promedio = "+tiempoConsulta/numClientes+ " miliseg");
	}
}
