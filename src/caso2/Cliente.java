package caso2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

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

	private boolean ejecutar=true;

	//llaves

	private KeyPair parLlaves;

	private PublicKey publicKey;

	private PrivateKey privateKey;


	public Cliente() throws Exception{

		//		generarLlaves();
		//		ver();
		decidirAlgoritmos();
		generarLlaves();
		conectarConServidor();
		manejoComunicacion();
	}

	//comunicacion
	public void ver()throws Exception
	{
		X509Certificate cert = generarCertificadoDigital();
		String[] envio = cert.toString().split("\n");
		for(int i=0;i<envio.length;i++)
		{
			System.out.print(envio[i]);
		}

	}


	public void decidirAlgoritmos() throws Exception
	{
		boolean eje=true;
		int es=0;

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;

		while (eje) {
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
					eje=false;
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

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser="";

		while (ejecutar) {

			if(estado==0)
			{
				System.out.print("Escriba HOLA para iniciar la consulta:");
				fromUser = stdIn.readLine();
			}
			else if(estado==1)
			{
				fromUser="ALGORITMOS:"+algoritmoSimetrico+":"+algoritmoAsimetrico+":"+hmac;

			}
			else if(estado==2)
			{
				X509Certificate cert = generarCertificadoDigital();

//				String[] envio = cert.toString().split("\n");
//				for(int i=0;i<envio.length-1;i++)
//				{
//					pw.println(envio[i]);
//				}
				fromUser = "CERTFICADOCLIENTE";

			}
			else if(estado==3)
			{
//				PublicKey llavePublicaServidor = null;
//				X509Certificate certServidor = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(s.getInputStream());
//				llavePublicaServidor = certServidor.getPublicKey();

				fromUser="OK";

			}
			else if(estado==4)
			{
				fromUser="CIFRADOKS+";
			}
			else if(estado==5)
			{
				fromUser="CIFRADOLS1";
				ejecutar=false;
			}

			System.out.println("Cliente: " + fromUser);
			pw.println(fromUser);

			recibirRespuesta();
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
				System.out.println("Servidor: " + rta);
				estado++;
			}
		}
		else
		{
			System.out.println("no se recibio respuesta");
		}
		return rta;
	}

	private void terminarComunicacion() throws Exception
	{
		ejecutar=false;
		pw.close();
		br.close();
		s.close();
		System.out.println("Se terminó la conexión con el servidor");
	}

	//cifrados

	private void generarLlaves() throws Exception
	{
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(algoritmoAsimetrico);
		keygen.initialize(1024);
		parLlaves = keygen.generateKeyPair();
		publicKey = parLlaves.getPublic();
		privateKey = parLlaves.getPrivate();
	}

	public byte[] cifrar(byte[] datosACifrar, Key llave, String algoritmo) throws Exception
	{
		Cipher c = Cipher.getInstance(algoritmo);
		c.init(Cipher.ENCRYPT_MODE, llave);
		return c.doFinal(datosACifrar);
	}

	public byte[] descifrar(byte[] datosADescifrar, Key llave, String algoritmo) throws Exception
	{
		Cipher c = Cipher.getInstance(algoritmo); 
		c.init(Cipher.DECRYPT_MODE, llave); 
		return c.doFinal(datosADescifrar);
	}


	//certificado digital
	private X509Certificate generarCertificadoDigital() throws Exception
	{
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal nombre = new X500Principal("CN=Aasdas");
		BigInteger serialAleatorio = new BigInteger( 10, new Random() );


		//fecha actual
		Date fechaActual = new Date();

		//Configuración del generador del certificado
		certGen.setSerialNumber(serialAleatorio);
		certGen.setIssuerDN(nombre);
		certGen.setSubjectDN(nombre);
		certGen.setNotBefore(fechaActual);
		certGen.setNotAfter(new Date(2017,1,1));
		certGen.setPublicKey(publicKey);
		certGen.setSignatureAlgorithm(hmac.split("HMAC")[1]+"with"+algoritmoAsimetrico);

		X509Certificate cert = certGen.generate(privateKey);

		return cert;
	}


	//main
	public static void main(String[] args) throws Exception {

		Cliente c = new Cliente();
	}
}
