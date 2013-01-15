package net.codegames.towerninja;

public class Stone {

	private float xLocation, yLocation;
	// Destination in the tower where the Stone is to be put.
	private float iDestination, jDestination;
	private float width = 100;
	private float height = 20;
	

	// variables for movement
	public float yVelocity = 0.2f;
	public float xVelocity = 0.1f;
	
	
	public Stone(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.iDestination = iDestination;
		this.jDestination = jDestination;
	
		this.xVelocity = 30 + (jDestination * width) - xLocation;
		System.out.println("xVelocity: "+xVelocity+"= 30 + ("+jDestination+" * "+width+") - "+xLocation);
		
		this.yVelocity = 300 - (iDestination * height) - yLocation;
		System.out.println("yVelocity: "+yVelocity+"= 500 - ("+iDestination+" * "+height+") - "+yLocation);
		this.xVelocity /= 5000;
		this.yVelocity /= 5000;
		
	}
	
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
	
	
	public void moveToDestination(float dT) {
		xLocation += dT*xVelocity;
		yLocation += dT*yVelocity;
	}
}
