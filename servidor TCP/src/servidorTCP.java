import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class servidorTCP implements Runnable {
	
	static final int puerto  = 8086;
	Socket con;
	String protocol_set[] ={"hi_back","dispach_message","save_message"};
	
	public servidorTCP(Socket con) {
		this.con = con;
	}

	public void hi_back() throws IOException{
		DataOutputStream outClient =new DataOutputStream(this.con.getOutputStream());
		outClient.writeBytes(protocol_set[0]+"¬¬"+"BUENOS  DIAS BUENAS TARDES"+'\n');
		outClient.flush();
	}
	
	public void dispach_message ( String ip_contacto) throws IOException{
		DataOutputStream outClient =new DataOutputStream(this.con.getOutputStream());
		String fichero ="src/"+ip_contacto+".txt";
		File archivo = new File (fichero);
		FileReader fr = new FileReader (archivo);
		BufferedReader br = new BufferedReader(fr);
		String linea;
		
		while((linea=br.readLine())!=null){
			 String mensaje = protocol_set[1]+"##"+linea+"\n";
	         outClient.writeBytes(mensaje);
	         outClient.flush();
		}
		archivo.delete();
	}
	
	public void save_message(String IP,String message) throws IOException{
		DataOutputStream outClient =new DataOutputStream(this.con.getOutputStream());
		String fichero =IP+".txt";
		FileWriter escri = new FileWriter("src/" + fichero,true);
		escri.write(IP+":"+message+"\r\n");
		escri.close();
		
		//File chat = new File(IP+puerto+ip_contacto,port_contacto+".txt");
		//FileWriter escribir = new FileWriter(chat, true);
		//BufferedWriter escritor2 =new BufferedWriter(escribir);
		//PrintWriter escritor3 = new PrintWriter(escritor2);
		//escritor3.append(IP+":"+message+"\r\n");
		//escritor3.close();
		//escritor2.close();
		
		outClient.writeBytes(protocol_set[2]+"¬¬"+"mensaje recivido"+'\n');
		outClient.flush();
		
	}
	public static void main(String argv[]) throws Exception{ 	 	
			
			ServerSocket socketservidor = new ServerSocket(puerto);
			
			while(true){
				servidorTCP cliente = new servidorTCP(socketservidor.accept());
				Thread hilo = new Thread(cliente);
				hilo.start();
				
				}
			}
	
	
	@Override
	public void run()
	{
		String clientSentence ;
		
		try {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(con.getInputStream()));
				clientSentence = inFromClient.readLine(); 
				while(clientSentence != null){
					StringTokenizer tokens = new StringTokenizer(clientSentence,"¬¬");
					String Protocolo =tokens.nextToken();
					switch(Protocolo){
					case("say_hi"):
						hi_back();
						clientSentence = inFromClient.readLine();
						System.out.println(clientSentence);
						break;
					case("ask_message"):
						String ip_salida =tokens.nextToken();
						String puerto_salida=tokens.nextToken();
						String ip_llegada=tokens.nextToken();
						String puerto_llegada=tokens.nextToken();
						String message=tokens.nextToken();
						dispach_message(ip_salida);
						break;
					case("send_message"):
						String ip_salida2 =tokens.nextToken();
						String puerto_salida2=tokens.nextToken();
						String ip_llegada2=tokens.nextToken();
						String puerto_llegada2=tokens.nextToken();
						String message2=tokens.nextToken();
						save_message(ip_salida2,message2);
						break;
						
					}
				}

				//outToClient.writeBytes(capitalizedSentence)   ca lo que se envia al clietne
			}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			//aca va codigo java simeple gatita 1313
		}
		
	}
}

