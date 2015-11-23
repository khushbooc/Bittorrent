package client;
import division.filedivide;
import java.net.*;
import java.io.*;

public class client5 {
	Socket requestSocket;           //socket connect to the server
	PrintStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	String message;                //message send to the server
	String MESSAGE; //capitalized message read from the server
    String message2;
    
    static int chunkcount;
    static int k=0;
    static int[] array  = new int[100];

	void run()
	{
		try{
			//create a socket to connect to the server
			requestSocket = new Socket("localhost", 8000);
			System.out.println("Connected to localhost in port 8000");
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
					out.println("client5");
					chunkcount=Integer.parseInt( br.readLine());
					int i;
					chunkcount=chunkcount-1;
					System.out.println("The total number of chunks the file has are"+chunkcount);
					for(i=5;i<chunkcount;i+=5)
					{
						receiveFile(filename);
						array[k++]=i;
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
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	//send a message to the output stream
	
	
	public  void receiveFile(String fileName) {
	    try {
	        int bytesRead;
	        InputStream in = requestSocket.getInputStream();
	        DataInputStream clientData = new DataInputStream(in);
	        fileName = clientData.readUTF();
	        OutputStream output = new FileOutputStream(
	                ("src\\client\\recieved_forClien1_from_server_" + fileName));
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
	    
	}
	
	
	//main method
	public static void main(String args[])
	{
		//filedivide fd = new filedivide();
		//String message2;
		//BufferedReader in=null;
	   // chunkcount=fd.spFile("src/CNHW1.pdf");
		client5 client = new client5();
		client.run();
	}


}
