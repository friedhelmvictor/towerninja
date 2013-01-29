package net.codegames.towerninja;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;

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
	private static boolean startScreen = true;
	private boolean gameover = false;
	
	private static final int TOWER_HEIGHT = 8;
	private static final int TOWER_WIDTH = 5;
	/**
	 * A tower represented by a 2-dimensional data strucure. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Vector<Brick[]> tower = new Vector<Brick[]>();

	/**
	 * Game constructor
	 * 
	 * @param applet
	 *            <code>Main</code> instance
	 */
	public Game(PApplet applet) {
		this.mApplet = applet;
		this.mRenderer = new AppletRenderer(mApplet);
		scoreboard.load("scores.txt");
		score = new Score();
		scoreboard.addScore(score);
		for (int i = 0; i < TOWER_HEIGHT; i++) {
			tower.add(new Brick[TOWER_WIDTH]);
		}
	}

	public void update(Vector<Player> players) {
		//startscreen
		if(startScreen)
		{
			drawStartScreen();
			drawPlayerHands(players);
			detectSlices(players);
			removeStones();
			drawStones();
		}
		else
		{
			//Game running
			if(!gameover){
				if (System.currentTimeMillis() - mLastTimeStamp > NEW_STONE_DELAY) {
					mLastTimeStamp = System.currentTimeMillis();
					createStone();
				}
				drawPlayerHands(players);
				detectSlices(players);
				removeStones();
				moveStones();
				drawStones();
				displayScore();
				checkGameover();
			}
			//gameover
			else{
				drawScoreboard();
				scoreboard.save("scores.txt");
			}
			
		}
	}

	/**
	 * Moves all stones for the passed number of frames.
	 */
	private void moveStones() {
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(i).length; j++) {
				if (tower.get(i)[j] != null) {
					if (!tower.get(i)[j].isOnTower()) {
						tower.get(i)[j].moveToDestination(mApplet.frameRate);
						if (tower.get(i)[j].isOnTower()) {
							score.addScore(tower.get(i)[j].getPoints());

							if (tower.get(i)[j] instanceof Bomb) {
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
				if (tower.get(x)[y] != null && tower.get(x)[y].isOnTower()) {
					destroyStone(x, y);
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
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(i).length; j++) {
				if (tower.get(i)[j] != null && !tower.get(i)[j].isOnTower()) {
					int minRow = Integer.MAX_VALUE;
					for (int k = i - 1; k >= 0; k--) {
						if (tower.get(k)[j] == null) {
							minRow = k;
						}
					}
					if (minRow < Integer.MAX_VALUE) {
						tower.get(minRow)[j] = tower.get(i)[j];
						tower.get(minRow)[j].updatePathWithLastPosition(minRow,
								j);
						tower.get(i)[j] = null;
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
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(i).length; j++) {
				if (tower.get(i)[j] == null) {
					double rand = Math.random();
					if (rand < 0.65d) {
						tower.get(i)[j] = new Brick(50, 5, i, j);
					} else {
						tower.get(i)[j] = new Bomb(50, 5, i, j);
					}
					return tower.get(i)[j];
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
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(0).length; j++) {
				if (tower.get(i)[j] != null) {
					tower.get(i)[j].draw(mRenderer);
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
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(0).length; j++) {
				if (tower.get(i)[j] != null) {
					for (int p = 0; p < players.size(); p++) {
						Player currentPlayer = players.get(p);
						// left hand detection
						if (currentPlayer.getLeftSpeed() > MIN_SPEED
								&& tower.get(i)[j] != null) {
							if (tower.get(i)[j].contains(
									currentPlayer.getLeftX(),
									currentPlayer.getLeftY(),
									currentPlayer.getLastLeftX(),
									currentPlayer.getLastLeftY())
									&& !tower.get(i)[j].isOnTower()) {
								destroyStone(i, j);
								updateDestinations();
							}
						}
						// right hand detection
						if (currentPlayer.getRightSpeed() > MIN_SPEED
								&& tower.get(i)[j] != null) {
							if (tower.get(i)[j].contains(
									currentPlayer.getRightX(),
									currentPlayer.getRightY(),
									currentPlayer.getLastRightX(),
									currentPlayer.getLastRightY())
									&& !tower.get(i)[j].isOnTower()) {
								destroyStone(i, j);
								updateDestinations();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sets sliced or exploded stones as destroyed.
	 * 
	 * @param i
	 *            tower row
	 * @param j
	 *            tower column
	 */
	private void destroyStone(int i, int j) {
		score.addScore(-1 * tower.get(i)[j].getPoints());
		tower.get(i)[j].setDestroyed(true);
		if(startScreen){
			startScreen = false;
			
			tower.get(0)[0] = new Brick(50, 5, 0, 0);
			tower.get(0)[0].putOnTower();
		}
		
		// tower.get(i)[j] = null;
	}

	/**
	 * Loops through the tower and removes all stones with finished destroy
	 * animation.
	 */
	private void removeStones() {
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(0).length; j++) {
				if (tower.get(i)[j] != null && tower.get(i)[j].isDestroyed()) {
					if (tower.get(i)[j].getDestroyTimer() == 0) {
						tower.get(i)[j] = null;
					}
				}
			}
		}
	}
	

	/**
	 * Displays the Score in game
	 * 
	 */
	private void displayScore() {
		mApplet.textSize(32);
		mApplet.text("Score: " + this.score.getScore(), 10, 60);
	}

	/**
	 * Draw the Scoreboard
	 * 
	 */
	private void drawScoreboard() {
		Score[] scoreboardArray = this.scoreboard.giveScoreboard();
		mApplet.text("Highscore" , 10, 50);
		for(int i = 0; i < scoreboardArray.length && i <= 10; i++){
			mApplet.text(i+1 , 10, 80 + i*30);
			mApplet.text(scoreboardArray[i].getName(), 100, 80 + i*30);
			mApplet.text(scoreboardArray[i].getScore(), 300, 80 + i*30);
		}
	}
	
	/**
	 * Draw the start screen.
	 */
	private void drawStartScreen(){
		tower.get(0)[0] = new Bat(400,500,400,500);
		PImage bubble = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/speech-bubble-small.png");
		mApplet.image(bubble, 420, 500-bubble.height );
		//mApplet.text("Slice the Bat to start the game!", 500, 480-bubble.height/2);
	}
	
	private boolean checkGameover(){
		for (int i = 0; i < tower.size(); i++) {
			for (int j = 0; j < tower.get(0).length; j++) {
				if (tower.get(i)[j] != null && tower.get(i)[j].isOnTower()) {
					return false;
				}
			}
		}
		gameover = true;
		return true;
	}
}