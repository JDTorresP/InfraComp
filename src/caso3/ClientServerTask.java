package caso3;

import caso2.Cliente;
import uniandes.gload.core.Task;
import uniandes.gload.examples.clientserver.Client;

public class ClientServerTask extends Task {

	@Override
	public void execute()
	{		
		//Aquí iría el cliente del caso2, este es el ejemplo
		try {
			Cliente client = new Cliente(true);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void fail()
	{
		System.out.println(Task.MENSAJE_FAIL);

	}

	public void success()
	{
		System.out.println(Task.OK_MESSAGE);
	}

	

}
