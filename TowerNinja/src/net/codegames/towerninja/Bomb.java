package net.codegames.towerninja;

public class Bomb extends Stone {

	private float width = 50;
	
	public Bomb(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
		// TODO Auto-generated constructor stub
	}
	
	public float getWidth() {
		return width;
	}


}
