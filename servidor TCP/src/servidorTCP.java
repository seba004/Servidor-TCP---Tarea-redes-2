import java.io.*;
import java.net.*;

public class servidorTCP implements Runnable {
	
	static final int puerto  = 8086;
	Socket con;
	
	
	public servidorTCP(Socket con) {
		this.con = con;
	}

	public static void main(String argv[]) throws Exception{ 	 	
			
			ServerSocket socketservidor = new ServerSocket(puerto);
			
			while(true){
				servidorTCP cliente = new servidorTCP(socketservidor.accept());
				Thread hilo = new Thread(cliente);
				hilo.start();
				
				}
			}
		
	
	
	public void hi_back() throws IOException{
		DataOutputStream outToClient = new DataOutputStream(this.con.getOutputStream());
		//String message_final = aca estoy trabajandooo!!!
	}
	
	
	
	
	
	
	@Override
	public void run()
	{
		String clientSentence ;
		
		try {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(con.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(this.con.getOutputStream());
				clientSentence = inFromClient.readLine(); 
				
				
				
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

