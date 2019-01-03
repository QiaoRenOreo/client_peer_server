
package ss.week7.cmdline;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    private static final String USAGE
        = "usage: " + Server.class.getName() + " <name> <port>";
 
    /** Starts a Server-application. */
    public static void main(String[] args) 
    {
        if (args.length < 2) 
        { 
            System.out.println(USAGE);
            System.exit(0);
        } 
    	//check exists or not
        String name = args[0];
        InetAddress addr = null;
        int port = 0;
        ServerSocket serverSock = null;
        Socket sock=null;
 
        // parse args[1] - the port
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: port " + args[1]
            		           + " is not an integer");
            System.exit(0);
        }

        // try to open a Socket to the server
        try {
        	serverSock=new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("ERROR: could not create a socket on " + addr
                    + " and port " + port);
        }
        
       
        try {
            addr = InetAddress.getLocalHost();//get IP address of server
        } catch (UnknownHostException e) {
            System.out.println(USAGE);
            System.out.println("ERROR: host " + args[1] + " unknown");
            System.exit(0);
        }
        try { 
        	Socket socket=serverSock.accept();
        	//while() //P 7.22 the method should WAIT until the client wants to connect to the server
            //wait();
        	Peer client = new Peer(name, socket); // ServerSocket socket has been defined, but Socket socket should be in the Peer
            Thread streamInputHandler = new Thread(client);
            streamInputHandler.start();
            client.handleTerminalInput(); 
            client.shutDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

} // end of class Server
