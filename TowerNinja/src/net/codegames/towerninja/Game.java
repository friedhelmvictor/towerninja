package net.codegames.towerninja;

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
	private AppletRenderer mRenderer;
	private long mLastTimeStamp = System.currentTimeMillis();
	public Scoreboard scoreboard = new Scoreboard();
	public Score score;

	private static final int TOWER_HEIGHT = 8;
	private static final int TOWER_WIDTH = 6;
	/**
	 * A tower represented by a 2-dimensional array. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Brick[][] mTower = new Brick[TOWER_HEIGHT][TOWER_WIDTH];

	/**
	 * Game constructor
	 * 
	 * @param applet
	 *            <code>Main</code> instance
	 */
	public Game(PApplet applet) {
		this.mApplet = applet;
		this.mRenderer = new AppletRenderer(mApplet);
		score = new Score();
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
	 * Moves all stones for the passed number of frames.
	 */
	private void moveStones() {
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] != null) {
					if (!mTower[i][j].isOnTower()) {
						mTower[i][j].moveToDestination(mApplet.frameRate);
						if (mTower[i][j].isOnTower()) {
							score.addScore(mTower[i][j].getPoints());

							if (mTower[i][j] instanceof Bomb) {
								handleExplosion(i, j);
							}
						}
					}

				}
			}
		}
	}

	/**
	 * Handles the explosion of a bomb for a given position in the tower and
	 * triggers the removal of all stones.
	 * 
	 * @param i
	 *            row in the tower
	 * @param j
	 *            column in the tower
	 */
	private void handleExplosion(int i, int j) {
		int rowStart = (i - 1) < 0 ? 0 : (i - 1);
		int rowEnd = (i + 1) >= TOWER_HEIGHT ? TOWER_HEIGHT - 1 : (i + 1);
		int columnStart = (j - 1) < 0 ? 0 : (j - 1);
		int columnEnd = (j + 1) >= TOWER_WIDTH ? TOWER_WIDTH - 1 : (j + 1);

		for (int x = rowStart; x <= rowEnd; x++) {
			for (int y = columnStart; y <= columnEnd; y++) {
				if (mTower[x][y] != null && mTower[x][y].isOnTower()) {
					removeStone(x, y);
				}
			}
		}
		updateDestinations();
	}

	/**
	 * Checks for all flying stones if they need a new destination position, so
	 * that stones don't land "floating in the air".
	 */
	private void updateDestinations() {
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] != null && !mTower[i][j].isOnTower()) {
					int minRow = Integer.MAX_VALUE;
					for (int k = i - 1; k >= 0; k--) {
						if (mTower[k][j] == null) {
							minRow = k;
						}
					}
					if (minRow < Integer.MAX_VALUE) {
						mTower[minRow][j] = mTower[i][j];
						mTower[minRow][j].updatePathWithLastPosition(minRow, j);
						mTower[i][j] = null;
					}
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
		updateDestinations();
		for (int i = 0; i < mTower.length; i++) {
			for (int j = 0; j < mTower[0].length; j++) {
				if (mTower[i][j] == null) {
					double rand = Math.random();
					if (rand < 0.67d) {
						mTower[i][j] = new Brick(50, 5, i, j);
					} else {
						mTower[i][j] = new Bomb(50, 5, i, j);
					}
					return mTower[i][j];
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
			mApplet.stroke(color, 32);
			// left hand
			if (currentPlayer.getLeftSpeed() > MIN_SPEED) {
				// draw 6 traces of different length over each other
				for (int i = 0; i < 6; i++) {
					mApplet.beginShape();
					for (int j = 0; j < currentPlayer.getLeft().size()
							- (i + 3); j++) {
						mApplet.vertex(currentPlayer.getLeft().get(j)[0],
								currentPlayer.getLeft().get(j)[1]);
					}
					mApplet.endShape();
				}
			}
			// right hand
			if (currentPlayer.getRightSpeed() > MIN_SPEED) {
				// draw 6 traces of different length over each other
				for (int i = 0; i < 6; i++) {
					mApplet.beginShape();
					for (int j = 0; j < currentPlayer.getRight().size()
							- (i + 3); j++) {
						mApplet.vertex(currentPlayer.getRight().get(j)[0],
								currentPlayer.getRight().get(j)[1]);
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
									currentPlayer.getLeftY(),
									currentPlayer.getLastLeftX(),
									currentPlayer.getLastLeftY())
									&& !mTower[i][j].isOnTower()) {
								removeStone(i, j);
								updateDestinations();
							}
						}
						// right hand detection
						if (currentPlayer.getRightSpeed() > MIN_SPEED
								&& mTower[i][j] != null) {
							if (mTower[i][j].contains(
									currentPlayer.getRightX(),
									currentPlayer.getRightY(),
									currentPlayer.getLastRightX(),
									currentPlayer.getLastRightY())
									&& !mTower[i][j].isOnTower()) {
								removeStone(i, j);
								updateDestinations();
							}
						}
					}
				}
			}
		}
	}

	private void removeStone(int i, int j) {
		score.addScore(-1 * mTower[i][j].getPoints());
		mTower[i][j] = null;
	}
}