package server;
import division.filedivide;

import java.net.*;
import java.io.*;

public class Server {

	private static final int sPort = 5000;   //The server will be listening on this port number
	public static int chunkcount;
	public static OutputStream os;
	public static void main(String[] args) throws Exception {
		filedivide fd = new filedivide();
		//String message2;
		//BufferedReader in=null;
	    chunkcount=fd.spFile("src/CNHW1.pdf");
		System.out.println("The server is running."); 
        	ServerSocket listener = new ServerSocket(sPort);
		 int clientNum=1;
        	try {
            		while(true) {
                		
                		Thread xyz=new Thread(new Handler(listener.accept(),clientNum));
                		xyz.start();
				System.out.println("Client "  + clientNum + " is connected!");
				clientNum++;
			
            			}
        	} finally {
            		listener.close();
        	} 
 
    	}

	

	/**
     	* A handler thread class.  Handlers are spawned from the listening
     	* loop and are responsible for dealing with a single client's requests.
     	*/
    	private static class Handler implements Runnable {
        	private String message;//message received from the client
		    private String message2;
        	//uppercase message send to the client
		private static Socket connection;
        	private BufferedReader in=null;	//stream read from the socket
        	private PrintStream out;    //stream write to the socket
		private int no;		//The index number of the client
		//private int numbe;
        	public Handler(Socket connection, int no) {
            		Handler.connection = connection;
	    		this.no = no;
        	}
        		
        public void run() {
 		try{
			//initialize Input and Output streams
			out= new PrintStream(connection.getOutputStream());
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			try{
				while(true)
				{
					
					//receive the message sent from the client
					message = in.readLine();
					message2 = in.readLine();
					while(message!=null)
					{
						out.println(chunkcount);
						if(message.equalsIgnoreCase("send"))
						{   
							if(message2.equalsIgnoreCase("client1"))
							{
							//String filename="CNHW1.pdf";
							sendfile(1);
							
							}
							if(message2.equalsIgnoreCase("client2"))
							{
							//String filename="CNHW1.pdf";
							sendfile(2);
							
							}
							if(message2.equalsIgnoreCase("client3"))
							{
							//String filename="CNHW1.pdf";
							sendfile(3);
							
							}
							if(message2.equalsIgnoreCase("client4"))
							{
							//String filename="CNHW1.pdf";
							sendfile(4);
							
							}
							if(message2.equalsIgnoreCase("client5"))
							{
							//String filename="CNHW1.pdf";
							sendfile(5);
							
							}
						}
					}
					
			}
			}
			catch(IOException er){
					System.err.println("Data received in unknown format");
				}
		}
		catch(IOException ioException){
			System.out.println("Disconnect with Client " + no);
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				connection.close();
			}
			catch(IOException ioException){
				System.out.println("Disconnect with Client " + no);
			}
		}
	}

	public static void sendfile(int i)
	{
		String s;
		String clientnum = "client"+ i;
		for(;i<chunkcount;i+=5)
		{
			s="src\\chunk."+i;
			sendFileToClient(s,clientnum);
	
		}
	}
    	
    	public static void sendFileToClient(String fileName,String clientname) {
    	    try {
    	        File myFile = new File(fileName);
    	        byte[] mybytearray = new byte[(int) myFile.length()];
    	        FileInputStream fis = new FileInputStream(myFile);
    	        BufferedInputStream bis = new BufferedInputStream(fis);
    	        DataInputStream dis = new DataInputStream(bis);
    	        dis.readFully(mybytearray, 0, mybytearray.length);
    	        os = connection.getOutputStream();
    	       // os.write(chunkcount);
    	        DataOutputStream out = new DataOutputStream(os);
    	        out.writeUTF(myFile.getName());
    	        out.writeLong(mybytearray.length);
    	        out.write(mybytearray, 0, mybytearray.length);
    	        out.flush();
    	        System.out.println("File " + fileName + " sent to"+clientname);
    	        dis.close();
    	    } catch (Exception e) {
    	        System.err.println("Error! " + e);
    	    }
    	}

}
}