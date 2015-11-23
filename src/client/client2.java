package client;
import division.filedivide;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class client2 {
		Socket requestSocket; //socket connect to the server
		static Socket newrequestsocket;
		PrintStream out;         //stream write to the socket
	 	ObjectInputStream in;          //stream read from the socket
		String message;                //message send to the server
		String MESSAGE; //capitalized message read from the server
	    String message2;
	    ArrayList<String> abc=new ArrayList<String>();
	    static int chunkcount;
	    static int k=0;
	    static int[] array  = new int[100];

		void run()
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
						out.println("client2");
						chunkcount=Integer.parseInt( br.readLine());
						int i;
						chunkcount=chunkcount-1;
						for(i=0;i<chunkcount;i++)
							abc.add(i,"0");
						System.out.println("The total number of chunks the file has are"+chunkcount);
						for(i=2;i<chunkcount;i+=5){
							receiveFile(filename,requestSocket);
							//array[k++]=i;
							abc.set(i,"1");
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
			
			receivepeer();
		}
		//send a message to the output stream
		
		
		public  void receiveFile(String fileName,Socket r1) {
		    try {
		        int bytesRead;
		        InputStream in = r1.getInputStream();
		        DataInputStream clientData = new DataInputStream(in);
		        fileName = clientData.readUTF();
		        System.out.println(fileName);
		        OutputStream output = new FileOutputStream(
		                ("src/client/FOR_client2_" + fileName));
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
			client2 client = new client2();
			client.run();
		}
		public void receivepeer() 
		{
			String temp;
			int temp1;
			while(true)
			{
			try {
				newrequestsocket = new Socket("localhost", 6000);
				System.out.println("Connected to localhost in port 6000");
				receiveFile("abc",newrequestsocket);
				
				ObjectOutputStream out= new ObjectOutputStream(newrequestsocket.getOutputStream());
				BufferedReader in= new BufferedReader(new InputStreamReader(newrequestsocket.getInputStream()));
				out.writeObject(abc);
				 while(true)
		            {
		                temp=in.readLine();
		                if(temp.equalsIgnoreCase("exit"))
		                    break;
		                else
		                {
		                	temp1=Integer.parseInt(in.readLine());
		                	abc.set(temp1, "1");
		                	receiveFile(temp,newrequestsocket);
		                }
		            }
				
				
				
				
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Requesting client connection");
			}
			
			}
			
		}


	}



