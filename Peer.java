package ss.week7.cmdline;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Peer for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Peer implements Runnable {
    public static final String EXIT = "exit";
    protected String name;
    protected Socket sock;
    protected BufferedReader in;
    protected BufferedWriter out;


    /*@
       requires (nameArg != null) && (sockArg != null);
     */
    /**P7.12
     * Constructor. creates a peer object based in the given parameters.
     * @param   nameArg name of the Peer-proces
     * @param   sockArg Socket of the Peer-proces
     */
    public Peer(String nameArg, Socket sockArg) throws IOException
    { 
    	name=nameArg; 
    	sock=sockArg;
    	//P 7.21
    	InputStream inputStream=sock.getInputStream();
    	InputStreamReader inReader=new InputStreamReader(inputStream);
    	in=new BufferedReader(inReader);
    	
    	OutputStream outputStream=sock.getOutputStream();
    	OutputStreamWriter outReader=new OutputStreamWriter(outputStream);
    	out=new BufferedWriter(outReader);
    } 

    /**P 7.12 //read messages from BufferedReader in and prints them on Terminal
     * Reads strings of the stream of the socket-connection and
     * writes the characters to the default output.
     */
    public void run()
    { 
			String sCurrentLine;
			try{
				while ((sCurrentLine = in.readLine()) != null) {
					System.out.println(sCurrentLine);
				}
				
    		} catch (IOException e) {
    			e.printStackTrace();
    		} finally {
    			try {
    				if (in != null)
    					in.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    }


    /**P7.12
     * Reads a string from the console and sends this string over
     * the socket-connection to the Peer process.
     * On Peer.EXIT the method ends
     * @throws IOException 
     */
    public void handleTerminalInput() throws IOException {
    	
/* do i need to use sock.getInputStream() and sock.getOutputStream() ?
 * Scanner sc = new Scanner (sock.getInputStream());
   PrintStream p = new PrintStream (sock.getOutputStream());*/
		String sCurrentLine;
		try{
			sCurrentLine = in.readLine();
			while ((sCurrentLine!=null)&&(sCurrentLine.equals(EXIT))) 
			{
				out.write(sCurrentLine);
				out.newLine(); //P7.21
				out.flush(); //has to be here. make sure all the buffers are cleared, everything is written
			}
			
		} catch (IOException e)//in or out does not exist 
		{
			e.printStackTrace();
			this.shutDown();
		} finally {
			
			try{
				out.flush();//flush() method: if any bytes previously written have been buffered by the implementation of the output stream, such bytes should immediately be written to their intended destination.
			}catch (IOException e){
				e.printStackTrace();
			}
			this.shutDown();//P 7.24
		}
    }

    /**P7.12 //close stream, close socket
     * Closes the connection, the sockets will be terminated
     * @throws IOException 
     */
    public void shutDown() throws IOException {
    	try 
    	{
    		sock.close();
    	}catch (IOException e){
    		e.printStackTrace();// tells you what happened and where in the code this happened.
    	}
    }

    /**  returns name of the peer object*/
    public String getName() {
        return name;
    }

    /** read a line from the default input */
    static public String readString(String tekst) 
    {
        System.out.print(tekst);
        String antw = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    System.in));
            antw = in.readLine();
        } catch (IOException e) {
        }

        return (antw == null) ? "" : antw;
    }
}
