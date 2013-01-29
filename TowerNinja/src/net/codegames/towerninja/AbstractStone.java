package net.codegames.towerninja;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

abstract public class AbstractStone {

	// Current location of the stone
	private float xLocation, yLocation;
	// Relative destination in the tower where the Stone is to be put.
	private float iDestination, jDestination;
	// Width and height of the stone
	private float width = 84;
	private float height = 60;
	// variables for movement
	private float yVelocity = 0.2f;
	private float xVelocity = 0.1f;
	private Parabole mPath;
	private boolean isOnTower = false;
	private boolean isDestroyed = false;
	private int destroyTimer = 10;
	// points a stone gives when hit tower or loose when sliced
	private int points = 0;
	private float startPositionX;

	protected float getStartPositionX() {
		return startPositionX;
	}

	protected void setStartPositionX(float startPositionX) {
		this.startPositionX = startPositionX;
	}

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

	protected void setDestination(float iDestination, float jDestination) {
		this.iDestination = iDestination;
		this.jDestination = jDestination;
		this.mPath = new Parabole(startPositionX, Main.height, startPositionX
				+ (exactXDestination() - startPositionX) / 2, 50,
				exactXDestination(), exactYDestination());
	}

	protected void updatePathWithLastPosition(float iDestination,
			float jDestination) {
		this.iDestination = iDestination;
		this.jDestination = jDestination;
		this.mPath = new Parabole(startPositionX, Main.height, getxLocation(),
				getyLocation(), exactXDestination(), exactYDestination());
	}

	protected float getjDestination() {
		return jDestination;
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
		return (Main.width / 2) - (2.5f * (getWidth()))
				+ (getjDestination() * (getWidth()));
	}

	protected float exactYDestination() {
		return Main.height + 6 - ((getiDestination() + 1) * (getHeight()));
	}

	protected Parabole getPath() {
		return mPath;
	}

	public void moveToDestination(float dT) {
		if (isOnTower())
			;
		else if ((Math.abs(getxLocation() - exactXDestination()) < 10)
				&& (getPath().getY(getxLocation() + dT * getxVelocity()) > exactYDestination())) {
			setxLocation(exactXDestination());
			setyLocation(exactYDestination());
			setIsOnTower();
		} else {
			setxLocation(getxLocation() + dT * getxVelocity());
			setyLocation(getPath().getY(getxLocation()));
		}
	}

	protected void setIsOnTower() {
		isOnTower = true;
	}

	public boolean isOnTower() {
		return isOnTower;
	}

	/**
	 * Checks if a line with given start and end point intersects with the
	 * rectangle represented by the stone.
	 * 
	 * @param x1
	 *            x-coordinate of starting point
	 * @param y1
	 *            y-coordinate of starting point
	 * @param x2
	 *            x-coordinate of ending point
	 * @param y2
	 *            y-coordinate of ending point
	 * @return true if line intersects with stone
	 */
	public boolean contains(float x1, float y1, float x2, float y2) {
		Rectangle2D rect = new Rectangle.Float(getxLocation(), getyLocation(),
				getWidth(), getHeight());
		return rect.intersectsLine(new Line2D.Float(x1, y1, x2, y2));
	}

	abstract void draw(RendererInterface renderer);

	protected int getPoints() {
		return points;
	}

	protected void setPoints(int points) {
		this.points = points;
	}

	protected void setDestroyed(boolean destroyed) {
		this.isDestroyed = destroyed;
	}

	protected boolean isDestroyed() {
		return isDestroyed;
	}

	protected int getDestroyTimer() {
		return destroyTimer;
	}

	protected void decreaseDestroyTime() {
		destroyTimer--;
	}
	
	public void putOnTower(){
		setxLocation(exactXDestination());
		setyLocation(exactYDestination());
		isOnTower();
	}
}
