package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

public class TestJavaServer extends Thread {
	
	private static final int portNumber = 1234;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private DBOperation dbService;
	
	public TestJavaServer(Socket socket) {
		try {
			this.socket = socket;
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			closeAll();
		}
	}
	
	public void run() {
		try {
			String response = "";
			String[] orderInfo;
			boolean available = true;
			System.out.println("\nA User Accessed!");
			
			while(available) {
				byte[] bytes = new byte[1024];
	        	in.read(bytes, 0, 1024);
	        	String request = new String(bytes, "UTF-8");
	        	request = request.split("\n")[0];
	        	orderInfo = request.split(" ");
	        	
	        	if(orderInfo[0].equals("order1")) {
					response = "order1-response";
	        	}
				else if(orderInfo[0].equals("order2")) {
					response = "order2-response";
	        	}
				else if(orderInfo[0].equals("exit")) {
					response = orderInfo[0];
					available = false;
	        	}
	        	
	        	response += "\n";
        		out.write(response.getBytes("UTF-8"));
    			out.flush();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			closeAll();
			System.out.println("\nA Client has exited...");
        }
	}
	
	public void closeAll() {
		try {
			if(socket != null)
				socket.close();
			if(in != null)
				in.close();
			if(out != null)
				out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		ServerSocket serverSocket = null;
		
		try {
			System.out.println("This server has been started");
			System.out.println("IP Address: " + Inet4Address.getLocalHost().getHostAddress());
			System.out.println("Port Number: " + portNumber);
			serverSocket = new ServerSocket(portNumber);
			
			while(true) {
				Socket socket = serverSocket.accept();
				TestJavaServer m = new TestJavaServer(socket);
				m.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(serverSocket != null);
					serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
