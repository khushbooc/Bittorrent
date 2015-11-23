package client;
import division.filedivide;

import java.net.*;
import java.util.ArrayList;
import java.io.*;
public class Client1  {
	Socket requestSocket;           //socket connect to the server
	PrintStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE; //capitalized message read from the server
    String message2;
    static int Listenport = 6000;
    public static OutputStream os;
    public static Socket connection;
    static byte cc;
    static int chunkcount;
    static int k=0;
    public static ArrayList<String> abc=new ArrayList<String>();
    static int[] array  = new int[100];
    ServerSocket ss;
	public void run()
	{
		
		try{
			
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", 5000);
			System.out.println("Connected to localhost in port 5000");
			//initialize inputStream and outputStream
			out = new PrintStream(requestSocket.getOutputStream());
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader br = new BufferedReader(new InputStreamReader(requestSocket.getInputStream()));
			while(true)
			{
				try{
					String a,filename = null;
					System.out.println("ENTER CHOICE :\n1.Get FILE\n2.exit");
					a=bufferedReader.readLine();
					if(a.equalsIgnoreCase("1"))
					{
					//System.out.println("ENTER FILE NAME:");
					//filename=bufferedReader.readLine();
					out.println("send");
					out.println("client1");
					chunkcount=Integer.parseInt( br.readLine());
					
					int i;
					chunkcount=chunkcount-1;
					for(i=0;i<chunkcount;i++)
						abc.add(i,"0");
					System.out.println("The total number of chunks the file has are "+chunkcount);
					for(i=1;i<=chunkcount;i+=5)
					{
						receiveFile(filename);
					   abc.set(i, "1");
					}
					
					}
					else if(a.equalsIgnoreCase("2"))
					{
						System.out.print("exit command");
						System.exit(0);
					}
				}
				
				catch(Exception e)
				{
					System.out.println("ERROR");
				}
			/*for(int i=0;i<k;i++)
				{
				System.out.println("I have these chunks :" +array[i]);
				}*/
				break;
			}
		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		/*finally{
			//Close connections
			try{
				//in.close();
				//out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}*/
		try {
			sendpeer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//send a message to the output stream
	
	
	public  String receiveFile(String fileName) {
	    try {
	    	
	        int bytesRead;
	        InputStream in = requestSocket.getInputStream();
	        DataInputStream clientData = new DataInputStream(in);
	        fileName = clientData.readUTF();
	        OutputStream output = new FileOutputStream(
	                ("src\\client\\file_recieved_" + fileName));
	        
	        long size = clientData.readLong();
	        byte[] buffer = new byte[1024];
	        while (size > 0
	                && (bytesRead = clientData.read(buffer, 0,
	                (int) Math.min(buffer.length, size))) != -1) {
	            output.write(buffer, 0, bytesRead);
	            size -= bytesRead;
	        }
	        
	        output.flush();
	        
	        System.out.println("File " + fileName + " received from Server.");
	        
	        
	    } catch (IOException ex) {
	        
	        System.out.println("ERRORRR!");
	    }
	    return fileName;
	    
	}
	public static void sendpeer() throws IOException
	{
		ServerSocket listener = new ServerSocket(Listenport);
		try {
			

				System.out.println("Client 1 awaiting connection");
        		
        		Thread xyz=new Thread(new Handler(listener.accept()));
        		xyz.start();
		//System.out.println("Client "  + clientNum + " is connected!");
		//clientNum++;
	
    			
	} 
		finally {
    		listener.close();
	} 
		
	
	}
	
	
	//main method
	public static void main(String args[]) throws ClassNotFoundException
	{   
		Client1 client = new Client1();
		client.run();
	}
	private static class Handler  implements Runnable
	{
	private static Socket connection;
    	private BufferedReader in=null;	//stream read from the socket
    	private PrintStream out;    //stream write to the socket
	private int no;		//The index number of the client
    	public Handler(Socket connection) {
        		Handler.connection = connection;
    		//this.no = no;
    	}
	public static void clientsender(String fileName) throws IOException
	{
		File myFile = new File(fileName);
		System.out.println("file" + fileName +"is sent from client 1");
	        byte[] mybytearray = new byte[(int) myFile.length()];
	        FileInputStream fis = new FileInputStream(myFile);
	        BufferedInputStream bis = new BufferedInputStream(fis);
	        DataInputStream dis = new DataInputStream(bis);
	        dis.readFully(mybytearray, 0, mybytearray.length);
	        os = connection.getOutputStream();
	      
	        DataOutputStream out = new DataOutputStream(os);
	        out.writeUTF(myFile.getName());
	        out.writeLong(mybytearray.length);
	        out.write(mybytearray, 0, mybytearray.length);
	        out.flush();
	        System.out.println("file" + fileName +"is sent from client 1");
	        dis.close();
		
		
	}
	@Override
	public void run() 
	{
		ArrayList<String> checkchunk=new ArrayList<String>();
		
		//in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while(true)
		{
		try {
			ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
			out= new PrintStream(connection.getOutputStream());
			checkchunk= (ArrayList<String>)in.readObject();
			String clientnum;
			for(int i=0;i<checkchunk.size();i++)
			{
				if(checkchunk.get(i).equals("0") && abc.get(i).equals("1") )
				{
					clientnum = "src/client/file_recieved_chunk."+i;
					out.println(clientnum);
					out.println(i);
					clientsender(clientnum);
				}
			}
			out.println("exit");
			System.out.println("Sent Some files");
            break;
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	

	}
}

