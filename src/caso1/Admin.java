package caso1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	
	private int numMensajes = 0;
	
	
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
		numMensajes = Integer.parseInt( properties.getProperty("numMensajes") );
		
		fileInput.close();
		Buffer bf = new Buffer(numClientes, numServidores, tamanoBuffer, numMensajes);
		
	}
	
	
	/**
	 * metodo main
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		Admin a = new Admin();
		
	}
}
