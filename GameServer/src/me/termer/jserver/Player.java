package me.termer.jserver;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	private PrintWriter p = null;
	private String n = null;
	private Location l = null;
	
	public Player(PrintWriter pw, String name, Location loc) {
		p = pw;
		n = name;
		l = loc;
	}
	public Player(PrintWriter pw, String name) {
		p = pw;
		n = name;
		l = new Location();
	}
	
	public PrintWriter getOutput() {
		return p;
	}
	public String getName() {
		return n;
	}
	public Location getLocation() {
		return l;
	}
	
	public void setName(String name) {
		n = name;
	}
	@Deprecated
	public void setLocation(Location loc) {
		l = loc;
	}
	
	public void move(Location loc) {
		l = loc;
		for(Player p : Main.players) {
			p.sendPlayerMove(this, loc);
		}
	}
	
	public void sendMessage(String msg) {
		p.println("MESSAGE "+msg);
	}
	public void sendRaw(String raw) {
		p.println(raw);
	}
	public void kick() {
		p.println("KICK");
		p.flush();
		p.close();
	}
	public void sendPlayerMove(Player pl, Location loc) {
		p.println("MOVEPLAYER "+pl.getName()+":"+loc.toString());
	}
	public void sendPlayerSpawn(String name, Location loc) {
		p.println("SPAWNPLAYER "+name+":"+loc.toString());
	}
	public void sendPlayerRemove(String name) {
		p.println("REMOVEPLAYER "+name);
	}
	
	public static void broadcast(String msg) {
		for (Player p : Main.players) {
			p.sendMessage(msg);
		}
	}
	
	public static Player[] getPlayers() {
		return Main.players.toArray(new Player[0]);
	}
	public static String[] getNames() {
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i = 0; i < getPlayers().length; i++) {
			tmp.add(getPlayers()[i].getName());
		}
		return tmp.toArray(new String[0]);
	}
	public static HashSet<String> getNamesAsSet() {
		HashSet<String> tmp = new HashSet<String>();
		for(int i = 0; i < getPlayers().length; i++) {
			tmp.add(getPlayers()[i].getName());
		}
		return tmp;
	}
	public static Player getUserByName(String name) {
		Player tmp = null;
		for(Player user : Main.players) {
			if(user.getName().equalsIgnoreCase(name)) {
				tmp = user;
			}
		}
		return tmp;
	}
}