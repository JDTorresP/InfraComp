package mundo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Admin {
	
	//Constantes
	
	/**
	 *Constante que modela el nombre del archivo properties
	 **/
	private static final String NOMBRE_ARCHIVO = "config.properties";
	/**
	 *Constante que modela la ubicacion del archivo properties
	 **/
	private static final String UBI_ARCHIVO = "data/";
	
	
	//Atributos
	
	private int numClientes = 0;
	
	private int numServidores = 0;
	
	private int tamanoBuffer = 0;
	
	/**
	 * Lista de clientes inicializados por el buffer
	 */
	private ArrayList<Cliente> clientes;
	
	
	//Metodos
	
	/**
	 * Metodo que se encarga de leer el archivo de configuracion con los parametros de inicializacion del buffer
	 * Pos: Los parametros del archivo de configuracion inicializan el buffer y quedan almacenados
	 * @throws IOException 
	 */
	
	public Admin() throws IOException
	{
		File file = new File( UBI_ARCHIVO+ NOMBRE_ARCHIVO);
		FileInputStream fileInput = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInput);
		System.out.println("lee los archivos");
		
		numClientes = Integer.parseInt( properties.getProperty("numClientes") );
		numServidores = Integer.parseInt( properties.getProperty("numServidores") );
		tamanoBuffer = Integer.parseInt( properties.getProperty("tamanoBuffer") );
		Buffer bf = new Buffer(numClientes, numServidores, tamanoBuffer);
		
		for(int i = 0; i < numClientes; i++)
		{
			new Cliente(i, bf).start();
			System.out.println("Se creó cliente " + i);
		}

		fileInput.close();

		
	}
	
	
	
	public static void main(String[] args) throws IOException {
		
		Admin a = new Admin();
		
	}
	
	
	//Metodos get/set
	
	
	public int getNumClientes() {
		return numClientes;
	}
	public void setNumClientes(int numClientes) {
		this.numClientes = numClientes;
	}
	public int getNumServidores() {
		return numServidores;
	}
	public void setNumServidores(int numServidores) {
		this.numServidores = numServidores;
	}
	public int getTamanoBuffer() {
		return tamanoBuffer;
	}
	public void setTamanoBuffer(int tamanoBuffer) {
		this.tamanoBuffer = tamanoBuffer;
	}

}
