package net.codegames.towerninja;

import java.awt.Color;
import java.util.Vector;

/**
 * A Player represents all Date about a tracked user playing this game. Hand
 * positions are stored and returned, additionaly the hands speed is always
 * updated.
 */
public class Player {

	private int userId;
	private Color color;

	private Vector<Float[]> left = new Vector<Float[]>();
	private Vector<Float[]> right = new Vector<Float[]>();

	private float leftSpeed;
	private float rightSpeed;

	private static final int NUMBER_OF_COORDS = 10;

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
	
	public float getLastLeftX() {
		return left.get(1)[0];
	}

	public float getLastLeftY() {
		return left.get(1)[1];
	}

	public Vector<Float[]> getLeft() {
		return left;
	}

	public void setLeft(float x, float y) {
		if (left.isEmpty()) {
			for (int i = 0; i < NUMBER_OF_COORDS; i++) {
				left.add(0, new Float[] { x, y });
			}
		}

		left.add(0, new Float[] { x, y });
		left.remove(left.size() - 1);

		leftSpeed = (float) Math.sqrt(Math.pow(left.get(0)[0] - left.get(1)[0],
				2) + Math.pow(left.get(0)[1] - left.get(1)[1], 2));
	}

	public float getRightX() {
		return right.get(0)[0];
	}

	public float getRightY() {
		return right.get(0)[1];
	}
	
	public float getLastRightX() {
		return right.get(1)[0];
	}

	public float getLastRightY() {
		return right.get(1)[1];
	}

	public Vector<Float[]> getRight() {
		return right;
	}

	public void setRight(float x, float y) {
		if (right.isEmpty()) {
			for (int i = 0; i < NUMBER_OF_COORDS; i++) {
				right.add(0, new Float[] { x, y });
			}
		}

		right.add(0, new Float[] { x, y });
		right.remove(right.size() - 1);

		rightSpeed = (float) Math.sqrt(Math.pow(right.get(0)[0]
				- right.get(1)[0], 2)
				+ Math.pow(right.get(0)[1] - right.get(1)[1], 2));
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
