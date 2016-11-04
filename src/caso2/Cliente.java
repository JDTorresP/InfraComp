package caso2;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.math.BigInteger;
import java.net.Socket;

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
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class Cliente {

	//IP y Puerto

	public final static String IP= "localhost";

	public int puerto;


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


	//Constructor
	public Cliente(boolean seguridad) throws Exception{

//		decidirAlgoritmos();
		if(seguridad)
		{
			puerto = 4443;
			generarLlaves();
			conectarConServidor();
			manejoComunicacion();
		}
		else
		{
			System.out.println("sin seguridad");
			puerto = 4444;
			conectarConServidor();
			manejoComunicacionSinSeguridad();
		}
			
		
	}

	//comunicacion

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
		s = new Socket(IP, puerto);
		pw = new PrintWriter(s.getOutputStream(), true);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}
	
	public void manejoComunicacion() throws Exception
	{

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser="";
		String fromServidor="";

		PublicKey llavePublicaServidor = null;
		SecretKey llaveSimetrica = null;

		while (ejecutar) {

			if(estado==0)
			{
//				System.out.println("Escriba HOLA para iniciar la consulta:");
				
				fromUser = "HOLA";
			}
			else if(estado==1)
			{
				fromUser="ALGORITMOS:"+algoritmoSimetrico+":"+algoritmoAsimetrico+":"+hmac;

			}
			else if(estado==2)
			{
				X509Certificate cert = generarCertificadoDigital();
				Base64 encoder = new Base64();
				String cert1 = encoder.encodeBase64String(cert.getEncoded());
				
				fromUser="-----BEGIN CERTIFICATE-----" + "\n";
				fromUser+=cert1 + "\n";
				fromUser+="-----END CERTIFICATE-----"+ "\n";

			}
			else if(estado==3)
			{

				String cert = br.readLine();
			
				String lineaSiguiente = "";
			
				while(!(lineaSiguiente = br.readLine()).equals("-----END CERTIFICATE-----")){

					cert+=lineaSiguiente;
//					System.out.println(lineaSiguiente);
					
				}
				
//				System.out.println("Ceritificado codificado en B64:" + cert);
//				System.out.println("Llegó");
				
				Base64 encoder = new Base64();
				byte[] cerb = encoder.decodeBase64(cert);
				InputStream stream = new ByteArrayInputStream(cerb);
			
				X509Certificate certServidor = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(stream);
				llavePublicaServidor = certServidor.getPublicKey();
				
				
				fromUser="OK";

			}
			else if(estado==4)
			{
				//Obtener y descifrar la llave simétrica
			
				fromServidor = br.readLine();
				String cadenaLlaveEncriptada = fromServidor;

				byte[] llaveDescifrada = descifrar(deEnterosABytes(cadenaLlaveEncriptada), privateKey, algoritmoAsimetrico);
				
				llaveSimetrica = new SecretKeySpec(llaveDescifrada, algoritmoSimetrico);

				//Cifrar la llave simétrica y enviarla de vuelta
				byte[] llaveRecifrada = cifrar(llaveDescifrada, llavePublicaServidor, algoritmoAsimetrico);
				fromUser=deBytesAEnteros(llaveRecifrada);
			}
			else if(estado==5)
			{
				//Consulta: un código de identificación de cuenta
				String consulta = "12345";
				
				byte[] consultaCifrada = cifrar(consulta.getBytes(), llaveSimetrica, algoritmoSimetrico);
				byte[] resumen = crearResumenDigital(consulta.getBytes(), llaveSimetrica);
				byte[] resumenCifrado = cifrar(resumen, llaveSimetrica, algoritmoSimetrico);			

				fromUser=deBytesAEnteros(consultaCifrada)+":"+ deBytesAEnteros(resumenCifrado);
				
				ejecutar=false;
				
			}

//			System.out.println("Cliente: " + fromUser);
			pw.println(fromUser);
			
			fromServidor=recibirRespuesta();
			
			if(!ejecutar)
			{

				byte[] rtaDescifrada = descifrar(deEnterosABytes(fromServidor), llaveSimetrica, algoritmoSimetrico);
				System.out.println("la respuesta: " + new String(rtaDescifrada));
				pw.println("Resultado recibido");
				
			}

		}
		stdIn.close();
		terminarComunicacion();
	}

	public void manejoComunicacionSinSeguridad() throws Exception
	{

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String fromUser="";
		String fromServidor="";

		PublicKey llavePublicaServidor = null;
		SecretKey llaveSimetrica = null;

		while (ejecutar) {

			if(estado==0)
			{
//				System.out.println("Escriba HOLA para iniciar la consulta:");
				
				fromUser = "HOLA";
			}
			else if(estado==1)
			{
				fromUser="ALGORITMOS:"+algoritmoSimetrico+":"+algoritmoAsimetrico+":"+hmac;

			}
			else if(estado==2)
			{							
				fromUser="CERTFICADOCLIENTE";
			}
			else if(estado==3 && fromServidor.equals("CERTIFICADOSERVIDOR"))
			{
				fromUser="OK";
			}
			else if(estado==4 && fromServidor.equals("CIFRADOKC+"))
			{
				fromUser="CIFRADOKS+";
			}
			else if(estado==5 && fromServidor.equals("OK"))
			{
				fromUser="CIFRADOLS1";
				ejecutar=false;				
			}

//			System.out.println("Cliente: " + fromUser);
			pw.println(fromUser);
			
			fromServidor=recibirRespuesta();
			
//			if(!ejecutar)
//			{
//
//				byte[] rtaDescifrada = descifrar(deEnterosABytes(fromServidor), llaveSimetrica, algoritmoSimetrico);
//				System.out.println("la respuesta: " + new String(rtaDescifrada));
//				pw.println("Resultado recibido");
//				
//			}

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
				ejecutar=false;
			}
			else
			{
//				System.out.println("Servidor: " + rta);
				estado++;
			}
		}
		else
		{
			System.out.println("no se recibio respuesta");
			ejecutar=false;
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

	public byte[] descifrar(byte[] datosADescifrar, Key llave, String algoritmo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher c = Cipher.getInstance(algoritmo); 
		c.init(Cipher.DECRYPT_MODE, llave); 
		return c.doFinal(datosADescifrar);
	}
	
	private byte[] crearResumenDigital(byte[] data, SecretKey llave) throws NoSuchAlgorithmException, InvalidKeyException
	{
		Mac mc = Mac.getInstance(hmac);
		mc.init(llave);
		mc.update(data);
		return mc.doFinal();
	}

	//certificado digital
	private X509Certificate generarCertificadoDigital() throws Exception
	{
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal nombre = new X500Principal("CN=QWERTY V1 Certificate");
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

	//encapsulamiento

	public String deBytesAEnteros( byte[] b )
	{	
		String ret = "";
		for (int i = 0 ; i < b.length ; i++) {
			String g = Integer.toHexString(((char)b[i])&0x00ff);
			ret += (g.length()==1?"0":"") + g;
		}
		return ret;
	}

	public byte[] deEnterosABytes( String ss)
	{	
		byte[] ret = new byte[ss.length()/2];
		for (int i = 0 ; i < ret.length ; i++) {
			ret[i] = (byte) Integer.parseInt(ss.substring(i*2,(i+1)*2), 16);
		}
		return ret;
	}

	//main
//	public static void main(String[] args) throws Exception {
//
//		Cliente c = new Cliente(false);
//	}
}
