package caso3;

import uniandes.gload.core.Task;
import uniandes.gload.examples.clientserver.Client;

public class ClientServerTask extends Task {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		//Aquí iría el cliente del caso2, este es el ejemplo
		Client client = new Client();
		client.sendMessageToServer("Hi! i'm a client");
		client.waitForMessageFromServer();
		
	}
	
	public void fail() {
		// TODO Auto-generated method stub
		System.out.println(Task.MENSAJE_FAIL);

	}

	public void success() {
		// TODO Auto-generated method stub
		System.out.println(Task.OK_MESSAGE);
	}

	

}
