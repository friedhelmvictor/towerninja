package net.codegames.towerninja;

public class Stone {

	private float xLocation, yLocation;
	// Destination in the tower where the Stone is to be put.
	private float iDestination, jDestination;
	private float width = 100;
	public float getxLocation() {
		return xLocation;
	}

	public float getyLocation() {
		return yLocation;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	private float height = 20;
	
	// variables for movement
	private float xVelocity = 0;
	private float yVelocity = 0.2f;
	
	public void moveToDestination(float dT) {
		xLocation += dT*xVelocity;
		yLocation += dT*yVelocity;
	}
	
	public Stone(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.iDestination = iDestination;
		this.jDestination = jDestination;
	}
}
