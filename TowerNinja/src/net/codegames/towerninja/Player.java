package net.codegames.towerninja;

import java.awt.Color;
import java.util.Vector;

public class Player {

	private int userId;
	private Color color;

	private Vector<Float[]> left = new Vector<Float[]>();
	private Vector<Float[]> right = new Vector<Float[]>();

	private float leftSpeed;
	private float rightSpeed;

	public Player(int userId, Color color) {
		this.userId = userId;
		this.color = color;
		leftSpeed = 0;
		rightSpeed = 0;
	}

	public float getLeftX() {
		return left.get(0)[0];
	}

	public float getLeftY() {
		return left.get(0)[1];
	}

	public void setLeft(float x, float y) {
		left.add(0, new Float[] { x, y });
		if (left.size() > 5) {
			left.remove(left.size() - 1);
		}
		if (left.size() > 1) {
			leftSpeed = (float) Math.sqrt(Math.pow(left.get(0)[0]
					- left.get(1)[0], 2)
					+ Math.pow(left.get(0)[1] - left.get(1)[1], 2));
		}
	}

	public float getRightX() {
		return right.get(0)[0];
	}

	public float getRightY() {
		return right.get(0)[1];
	}

	public void setRight(float x, float y) {
		right.add(0, new Float[] { x, y });
		if (right.size() > 5) {
			right.remove(right.size() - 1);
		}
		if (right.size() > 1) {
			rightSpeed = (float) Math.sqrt(Math.pow(right.get(0)[0]
					- right.get(1)[0], 2)
					+ Math.pow(right.get(0)[1] - right.get(1)[1], 2));
		}
	}

	public int getUserId() {
		return userId;
	}

	public int getColor() {
		return color.getRGB();
	}

	public float getLeftSpeed() {
		return leftSpeed;
	}

	public float getRightSpeed() {
		return rightSpeed;
	}

}
