package caso3;

import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class Generator
{
/**
 * Carga el servicio del generador del lib
 */
	private LoadGenerator generator;
	
	public Generator()
	{
		Task work = createTask();
		int numberOfTasks = 100;
		int gapBetweenTASKS = 1000;
		generator = new LoadGenerator("Cliente - Server Load Test", numberOfTasks, work, gapBetweenTASKS);
		generator.generate();
	}
	
	private Task createTask()
	{
		return new ClientServerTask();
	}
	
	public static void main(String args)
	{
		@SuppressWarnings("unused")
		Generator gen = new Generator();
	}
}
