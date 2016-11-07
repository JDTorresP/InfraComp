package caso3;

import caso2.Cliente;
import uniandes.gload.core.Task;

public class ClientServerTask extends Task {

	private Boolean seguridadServidor = false;

	 public ClientServerTask(Boolean seg) {
		seguridadServidor=seg;
	}
	 
	@Override
	public void execute()
	{		
		try {
			Cliente client = new Cliente(seguridadServidor);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}
	}

	public void fail()
	{
		System.out.println(Task.MENSAJE_FAIL);

	}

	public void success()
	{
		System.out.println(Task.OK_MESSAGE );
	}



}
