package net.codegames.towerninja;

public class Bomb extends Stone {

	private float width = 70;
	private float height = 40;
	
	private String background = "resources/bomb.png";
	
	public Bomb(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
		// TODO Auto-generated constructor stub
	}
	
	public float getWidth() {
		return width;
	}
	
	public String getBackground() {
		return background;
	}


}
