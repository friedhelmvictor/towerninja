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
	private Parabole mPath;

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
		return 200 + (getjDestination() * getWidth());
	}

	protected float exactYDestination() {
		return 430 - (getiDestination() * getHeight());
	}
	
	protected Parabole getPath() {
		return mPath;
	}

	protected void setTrajectoryTipY(float y) {
		this.mPath = new Parabole(10, 400, 10+(exactXDestination()-10)/2, y, exactXDestination(), exactYDestination());
		setxLocation(10);
		setyLocation(400);
	}

	public void moveToDestination(float dT) {
		if (Math.abs(getxLocation() - exactXDestination()) < 5
				&& Math.abs(getyLocation() - exactYDestination()) < 5)
			;
		else {
			setxLocation(getxLocation() + dT * getxVelocity());
//			setyLocation(getyLocation() + dT * getyVelocity());
			setyLocation(getPath().getY(getxLocation()));
		}
	}

	public boolean contains(float x, float y) {
		return (x <= getxLocation() + getWidth() && x >= getxLocation()
				&& y <= getyLocation() + getHeight() && y >= getyLocation());
	}
	
	
	

	abstract void draw(RendererInterface renderer);
}
