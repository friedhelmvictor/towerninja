package net.codegames.towerninja;

public class Player {
	
	private int userId;
	
	private float leftX;
	private float leftY;
	private float lastLeftX;
	private float lastLeftY;
	
	private float rightX;
	private float rightY;
	private float lastRightX;
	private float lastRightY;
	
	private float leftSpeed;
	private float rightSpeed;
	
	public Player(int userId) {
		this.userId = userId;
		leftSpeed = 0;
		rightSpeed = 0;
	}

	public float getLeftX() {
		return leftX;
	}

	public void setLeftX(float leftX) {
		lastLeftX = this.leftX;
		this.leftX = leftX;
	}

	public float getLeftY() {
		return leftY;
	}

	public void setLeftY(float leftY) {
		lastLeftY = this.leftY;
		this.leftY = leftY;
	}

	public float getRightX() {
		return rightX;
	}

	public void setRightX(float rightX) {
		lastRightX = this.rightX;
		this.rightX = rightX;
	}

	public float getRightY() {
		return rightY;
	}

	public void setRightY(float rightY) {
		lastRightY = this.rightY;
		this.rightY = rightY;
	}

	public int getUserId() {
		return userId;
	}

	public float getLeftSpeed() {
		return leftSpeed;
	}

	public float getRightSpeed() {
		return rightSpeed;
	}
	
	public void updateSpeed() {
		leftSpeed = (float)Math.sqrt(Math.pow(leftX - lastLeftX, 2) + Math.pow(leftY - lastLeftY, 2));
		rightSpeed = (float)Math.sqrt(Math.pow(rightX - lastRightX, 2) + Math.pow(rightY - lastRightY, 2));
	}

}
