import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

public class servidorTCP implements Runnable {
	
	static final int puerto  = 9046;
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
		if (archivo.exists()){
			
			FileReader fr = new FileReader (archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea;
		
			while((linea=br.readLine())!=null){
				String mensaje = protocol_set[1]+"¬¬"+linea+"\n";
				outClient.writeBytes(mensaje);
				outClient.flush();
			}
			outClient.writeBytes("end"+"¬¬"+"chau"+"\n");
			outClient.flush();
			archivo.delete();
		}	
		else{
			outClient.writeBytes("end"+"¬¬"+"chau"+"\n");
			outClient.flush();
			
		}
	}
	
	public void save_message(String IP,String message) throws IOException{
		DataOutputStream outClient =new DataOutputStream(this.con.getOutputStream());
		String fichero =IP+".txt";
		FileWriter escri = new FileWriter("src/" + fichero,true);
		escri.write(IP+":"+message+"\r\n");
		escri.close();
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
					
					System.out.println(clientSentence);
					
					StringTokenizer tokens = new StringTokenizer(clientSentence,"¬¬");
					String Protocolo =tokens.nextToken();
					switch(Protocolo){
					case("say_hi"):
						hi_back();
						clientSentence = inFromClient.readLine();
						System.out.println(clientSentence);
						
						break;
					case("ask_message"):
						
						String ip_contacto=tokens.nextToken();
						
						//String message=tokens.nextToken();
						System.out.println(ip_contacto);
						dispach_message(ip_contacto);
						 clientSentence = null;
						System.out.println("guardado:"+clientSentence);
		                   
						
						
						
						break;
					case("send_message"):
						
						String ip_salida2 =tokens.nextToken();
						String message2=tokens.nextToken();
						save_message(ip_salida2,message2);
						clientSentence = "rady";
						System.out.println("ok"+clientSentence);
						//System.out.println("se cae en el server");
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

