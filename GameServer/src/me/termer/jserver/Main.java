package me.termer.jserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import net.termer.utils.Utils;

public class Main {
	private static String IP = "";
    private static final int PORT = 9001;
    public static HashSet<Player> players = new HashSet<Player>();
    public static HashSet<String> admins = new HashSet<String>();
    public static String[] motd = {};
    public static void main(String[] args) throws Exception {
        System.out.print("Starting...");
        new EndWindow();
        ServerSocket listener = null;
        //Check files
        try {
        	//Ip file
        	File ipFile = new File("ip.txt");
        	if(!ipFile.exists()) {
        		ipFile.createNewFile();
        	}
        	try {
        		IP = Utils.getFile(new File("ip.txt"))[0];
        	} catch(Exception e) {}
        	File motdFile = new File("motd.txt");
        	if(!motdFile.exists()) {
        		motdFile.createNewFile();
        	}
        	motd = Utils.getFile(motdFile);
        } catch(Exception e){
        	System.err.println("Error while initializing files:");
        	e.printStackTrace();
        }
        if(IP.isEmpty() && IP != null) {
        	listener = new ServerSocket(PORT);
        } else {
        	try {
        		InetAddress bindIp = InetAddress.getByName(IP);
        		listener = new ServerSocket(PORT, 50, bindIp);
        	} catch(Exception e) {
        		System.out.println("Could not bind to IP: "+IP);
        		//End with error code 1
        		System.exit(1);
        	}
        }
        System.out.println("Done");
        System.out.println("Running on "+listener.getInetAddress().getHostName()+":"+listener.getLocalPort());
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    //Handler thread. This thread is spawned for each user by the listener loop.
    private static class Handler extends Thread {
        private String name = "Unknown Player";
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private Player p;
        
        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                
                System.out.println("[JOIN] "+name);
                Player.broadcast(name+" joined");
                p = new Player(out, name);
                players.add(p);
                
                //Display MotD
                for(int i = 0; i < motd.length; i++) {
                	p.sendMessage(motd[i].replaceAll("@name", name).replaceAll("@users", Integer.toString(players.size())));
                }
                p.sendRaw("NAMEACCEPTED");
                while (true) {
                	String input = "";
                	try {
                    	input = in.readLine();
                	} catch(NullPointerException e) {
                		throw new IOException("User logged out");
                	}
                    if (input == null) {
                        continue;
                    }
                    if(!input.isEmpty()) {
                    	if(!input.startsWith("/")) {
                    		System.out.println("[CHAT] "+name+": "+input.trim());
                    		Player.broadcast(name+": "+input.trim());
                    	} else {
                    		handleCommand(input.substring(1), input.substring(1).split(" ")[0]);
                    	}
                    }
                }
            } catch (IOException e) {} finally {
                if (name != null && out != null) {
                    players.remove(p);
                }
                System.out.println("[LEAVE] "+name);
                Player.broadcast(name+" left");
                try {
                    socket.close();
                } catch (IOException e) {
                	System.err.println("Internal error while closing socket for "+name);
                }
            }
        }
        private void handleCommand(String cmd, String cmdName) {
        		
        }
    }
}