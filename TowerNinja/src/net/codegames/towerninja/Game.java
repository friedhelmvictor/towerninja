package net.codegames.towerninja;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;

// kennt alle steine
// kennt Turm

/**
 * @author chameleon
 * 
 *         This class contains a tower data structure. The method createStone
 *         allows creating new stones for free spots within the tower.
 */
public class Game {

	private final long NEW_STONE_DELAY = 1000L;
	private final float MIN_SPEED = 20;
	private PApplet mApplet;
	private AppletRenderer mRenderer;
	private long mLastTimeStamp = System.currentTimeMillis();

	/**
	 * A tower represented by a 2-dimensional array. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Brick[][] mTower = new Brick[8][6];

	/**
	 * Game constructor
	 * 
	 * @param applet
	 *            <code>Main</code> instance
	 */
	public Game(PApplet applet) {
		this.mApplet = applet;
		this.mRenderer = new AppletRenderer(mApplet);
	}

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
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] != null) {
					mTower[i][j].moveToDestination(mApplet.frameRate);
				}
			}
		}
	}

	/**
	 * This method loops through the tower data structure and finds the lowest
	 * leftmost free spot. It then creates a stone for that spot and returns its
	 * reference
	 * 
	 * @return a reference to the next {@link Brick}
	 */
	private Brick createStone() {
		towerHeightLoop: for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] == null) {
					double rand = Math.random();
					if (rand < 0.75d) {
						mTower[i][j] = new Brick(50, 5, i, j);
					} else {
						mTower[i][j] = new Bomb(50, 5, i, j);
					}
					return mTower[i][j];
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
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] != null) {
					mTower[i][j].draw(mRenderer);
				}
			}
		}
	}

	/**
	 * Draws the hand positions of every player.
	 * 
	 * @param players
	 *            vector of all {@link Player}s
	 */
	private void drawPlayerHands(Vector<Player> players) {
		for (int p = 0; p < players.size(); p++) {
			Player currentPlayer = players.get(p);
			int color = currentPlayer.getColor();

			mApplet.noFill();
			mApplet.strokeWeight(12);
			mApplet.strokeJoin(mApplet.MITER);
			mApplet.strokeCap(mApplet.PROJECT);
			mApplet.stroke(color, 64);
			// left hand
			if (currentPlayer.getLeftSpeed() > MIN_SPEED) {
				// draw 3 traces of different length over each other
				for (int i = 0; i < 3; i++) {
					mApplet.beginShape();
					for (int j = 0; j < currentPlayer.getLeft().size() - (i * 2 + 3); j++) {
						mApplet.vertex(currentPlayer.getLeft().get(j)[0], currentPlayer.getLeft().get(j)[1]);
					}
					mApplet.endShape();
				}
			}
			// right hand
			if (currentPlayer.getRightSpeed() > MIN_SPEED) {
				// draw 3 traces of different length over each other
				for (int i = 0; i < 3; i++) {
					mApplet.beginShape();
					for (int j = 0; j < currentPlayer.getRight().size() - (i * 2 + 3); j++) {
						mApplet.vertex(currentPlayer.getRight().get(j)[0], currentPlayer.getRight().get(j)[1]);
					}
					mApplet.endShape();
				}
			}
		}
	}

	/**
	 * Checks for all flying stones if a player currently slices them.
	 * 
	 * @param players
	 *            vector of all {@link Player}s
	 */
	private void detectSlices(Vector<Player> players) {
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] != null) {
					for (int p = 0; p < players.size(); p++) {
						Player currentPlayer = players.get(p);
						// left hand detection
						if (currentPlayer.getLeftSpeed() > MIN_SPEED
								&& mTower[i][j] != null) {
							if (mTower[i][j].contains(currentPlayer.getLeftX(),
									currentPlayer.getLeftY())) {
								removeStone(i, j);
							}
						}
						// right hand detection
						if (currentPlayer.getRightSpeed() > MIN_SPEED
								&& mTower[i][j] != null) {
							if (mTower[i][j].contains(currentPlayer.getRightX(),
									currentPlayer.getRightY())) {
								removeStone(i, j);
							}
						}
					}
				}
			}
		}
	}

	private void removeStone(int i, int j) {
		mTower[i][j] = null;
		if(mTower[i+1][j] != null) {
			mTower[i][j+1].setjDestination(j);
		}
	}

}