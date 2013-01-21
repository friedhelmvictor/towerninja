package net.codegames.towerninja;

abstract public class AbstractStone {

	// Current location of the stone
	private float xLocation, yLocation;
	// Relative destination in the tower where the Stone is to be put.
	private float iDestination, jDestination;
	// Width and height of the stone
	private float width = 70;
	private float height = 40;
	// variables for movement
	private float yVelocity = 0.2f;
	private float xVelocity = 0.1f;
	// default image file for drawing
	private String background = "../resources/brick.png";

	protected float getxLocation() {
		return xLocation;
	}

	protected void setxLocation(float xLocation) {
		this.xLocation = xLocation;
	}

	protected float getyLocation() {
		return yLocation;
	}

	protected void setyLocation(float yLocation) {
		this.yLocation = yLocation;
	}

	protected float getiDestination() {
		return iDestination;
	}

	protected void setiDestination(float iDestination) {
		this.iDestination = iDestination;
	}

	protected float getjDestination() {
		return jDestination;
	}

	protected void setjDestination(float jDestination) {
		this.jDestination = jDestination;
	}

	protected float getWidth() {
		return width;
	}

	protected void setWidth(float width) {
		this.width = width;
	}

	protected float getHeight() {
		return height;
	}

	protected void setHeight(float height) {
		this.height = height;
	}

	protected float getyVelocity() {
		return yVelocity;
	}

	protected void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}

	protected float getxVelocity() {
		return xVelocity;
	}

	protected void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}

	protected float exactXDestination() {
		return 90 + (getjDestination() * getWidth());
	}

	protected float exactYDestination() {
		return 400 - (getiDestination() * getHeight());
	}

	protected void setBackground(String background) {
		this.background = background;
	}
	
	public String getBackground() {
		return background;
	}
	
	public void moveToDestination(float dT) {
		if (Math.abs(getxLocation() - exactXDestination()) < 1
				&& Math.abs(getyLocation() - exactYDestination()) < 1)
			;
		else {
			setxLocation(getxLocation() + dT * getxVelocity());
			setyLocation(getyLocation() + dT * getyVelocity());
		}
	}

	public boolean contains(float x, float y) {
		return (x <= getxLocation() + getWidth() && x >= getxLocation()
				&& y <= getyLocation() + getHeight() && y >= getyLocation());
	}
	
	
	

	abstract void draw(Renderer renderer);
}
