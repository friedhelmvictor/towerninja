package net.codegames.towerninja;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Vector;

import processing.core.PApplet;

// kennt alle steine
// kennt Turm

/**
 * @author chameleon
 * 
 *         This class contains a tower data structure. The method createStone
 *         allows creating new stones for free spots within the tower.
 */
public class Game {

	private final long NEW_STONE_DELAY = 500L;
	private final float MIN_SPEED = 20;
	private PApplet mApplet;
	private long mLastTimeStamp = System.currentTimeMillis();

	/**
	 * A tower represented by a 2-dimensional array. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Stone[][] tower = new Stone[10][4];

	/**
	 * Game constructor
	 * 
	 * @param applet
	 *            <code>Main</code> instance
	 */
	public Game(PApplet applet) {
		this.mApplet = applet;
	}

	/**
	 * 
	 * @param players
	 */
	public void update(Vector<Player> players) {

		if (System.currentTimeMillis() - mLastTimeStamp > NEW_STONE_DELAY) {
			mLastTimeStamp = System.currentTimeMillis();
			createStone();
		}

		drawPlayerHands(players);
		
		detectSlices(players);

		moveStones();

		drawStones();
	}

	/**
	 * Moves all stones for the passed number of frames
	 * 
	 * @param dT
	 */
	private void moveStones() {
		for (int i = 0; i < tower.length; i++) {
			for (int j = 0; j < tower[0].length; j++) {
				if (tower[i][j] != null) {
					tower[i][j].moveToDestination(mApplet.frameRate);
				}
			}
		}
	}

	/**
	 * This method loops through the tower data structure and finds the lowest
	 * leftmost free spot. It then creates a stone for that spot and returns its
	 * reference
	 * 
	 * @return a reference to the next {@link Stone}
	 */
	private Stone createStone() {
		towerHeightLoop: for (int i = 0; i < tower.length; i++) {
			for (int j = 0; j < tower[0].length; j++) {
				if (tower[i][j] == null) {
					double rand = Math.random();
					if (rand < 0.75d) {
						tower[i][j] = new Stone(50, 5, i, j);
					} else {
						tower[i][j] = new Bomb(50, 5, i, j);
					}
					return tower[i][j];
					// break towerHeightLoop;
				}
			}
		}
		return null;
	}

	/**
	 * draws every stone
	 */
	private void drawStones() {
		mApplet.fill(64);
		for (int i = 0; i < tower.length; i++) {
			for (int j = 0; j < tower[0].length; j++) {
				if (tower[i][j] != null) {
					mApplet.rect(tower[i][j].getxLocation(),
							tower[i][j].getyLocation(), tower[i][j].getWidth(),
							tower[i][j].getHeight());
				}
			}
		}
	}

	/**
	 * Draws the hand positions of every player.
	 * 
	 * @param players vector of all {@link Player}s
	 */
	private void drawPlayerHands(Vector<Player> players) {
		for (int p = 0; p < players.size(); p++) {
			Player currentPlayer = players.get(p);
			int color = currentPlayer.getColor();
			
			// left hand
			mApplet.strokeWeight(10);
			// path of last movement
			int alpha = (currentPlayer.getLeftSpeed() > MIN_SPEED) ? 96 : 64;
			mApplet.stroke(color, alpha);
			for (int i = 0; i < currentPlayer.getLeft().size() - 1; i++) {
				mApplet.line(currentPlayer.getLeft().get(i)[0], currentPlayer
						.getLeft().get(i)[1], currentPlayer.getLeft()
						.get(i + 1)[0], currentPlayer.getLeft().get(i + 1)[1]);
				mApplet.stroke(color, (int)(alpha * Math.pow(0.67, i)));
			}
			// circle for current hand position
			if (currentPlayer.getLeftSpeed() > MIN_SPEED) {
				mApplet.fill(color, 192);
			} else {
				mApplet.fill(color, 64);
			}
			mApplet.ellipseMode(mApplet.CENTER);
			mApplet.noStroke();
			mApplet.ellipse(currentPlayer.getLeftX(), currentPlayer.getLeftY(), 20, 20);
			
			// left hand
			mApplet.strokeWeight(10);
			// path of last movement
			alpha = (currentPlayer.getRightSpeed() > MIN_SPEED) ? 96 : 64;
			mApplet.stroke(color, alpha);
			for (int i = 0; i < currentPlayer.getRight().size() - 1; i++) {
				mApplet.line(currentPlayer.getRight().get(i)[0], currentPlayer
						.getRight().get(i)[1], currentPlayer.getRight()
						.get(i + 1)[0], currentPlayer.getRight().get(i + 1)[1]);
				mApplet.stroke(color, (int)(alpha * Math.pow(0.67, i)));
			}
			// circle for current hand position
			if (currentPlayer.getRightSpeed() > MIN_SPEED) {
				mApplet.fill(color, 192);
			} else {
				mApplet.fill(color, 64);
			}
			mApplet.ellipseMode(mApplet.CENTER);
			mApplet.noStroke();
			mApplet.ellipse(currentPlayer.getRightX(), currentPlayer.getRightY(), 20, 20);
		}
	}

	/**
	 * Checks for all flying stones if a player currently slices them.
	 * 
	 * @param players vector of all {@link Player}s
	 */
	private void detectSlices(Vector<Player> players) {
		for (int i = 0; i < tower.length; i++) {
			for (int j = 0; j < tower[0].length; j++) {
				if (tower[i][j] != null) {
					for (int p = 0; p < players.size(); p++) {
						Player currentPlayer = players.get(p);
						// left hand detection
						if (currentPlayer.getLeftSpeed() > MIN_SPEED
								&& tower[i][j] != null) {
							if (tower[i][j].contains(currentPlayer.getLeftX(),
									currentPlayer.getLeftY())) {
								tower[i][j] = null;
							}
						}
						// right hand detection
						if (currentPlayer.getRightSpeed() > MIN_SPEED
								&& tower[i][j] != null) {
							if (tower[i][j].contains(currentPlayer.getRightX(),
									currentPlayer.getRightY())) {
								tower[i][j] = null;
							}
						}
					}
				}
			}
		}
	}
	
}