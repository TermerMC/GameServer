package me.termer.jserver;

public class Location {
	private int x = 0;
	private int y = 0;
	public Location(int xx, int yy) {
		x = xx;
		y = yy;
	}
	public Location() {}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public void setX(int nx) {
		x = nx;
	}
	public void setY(int ny) {
		y = ny;
	}
	
	@Override
	public String toString() {
		return Integer.toString(x)+","+Integer.toString(y);
	}
	
	public static Location parseLocation(String l) {
		Location f = new Location();
		try {
			f.setX(Integer.parseInt(l.split(",")[0]));
			f.setY(Integer.parseInt(l.split(",")[1]));
		} catch(Exception e) {
			throw new IllegalArgumentException("Invalid location");
		}
		return f;
	}
}
