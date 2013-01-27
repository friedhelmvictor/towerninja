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

	private final long NEW_STONE_DELAY = 1000L;
	private final float MIN_SPEED = 20;
	private PApplet mApplet;
	private AppletRenderer mRenderer;
	private long mLastTimeStamp = System.currentTimeMillis();
	private static boolean startScreen = true;

	public Scoreboard scoreboard = new Scoreboard();
	public Score score;
	
	private static final int TOWER_HIGHT = 8;
	private static final int TOWER_WIDTH = 6;
	/**
	 * A tower represented by a 2-dimensional array. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Brick[][] mTower = new Brick[TOWER_HIGHT][TOWER_WIDTH];

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

	}

	public void update(Vector<Player> players) {
			if(!lost()){
				if (!startScreen && System.currentTimeMillis() - mLastTimeStamp > NEW_STONE_DELAY) {
					mLastTimeStamp = System.currentTimeMillis();
					createStone();
				}
				
				drawPlayerHands(players);
				detectSlices(players);
				if(!startScreen){
					moveStones();
				}
				else{
					drawStartScreen();
				}
				drawStones();
				displayScore();
			}
			else{
				drawScoreboard();
				scoreboard.save("scores.txt");
			}
		
		
		
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
					if (!mTower[i][j].isOnTower()){
						mTower[i][j].moveToDestination(mApplet.frameRate);
						if (mTower[i][j].isOnTower()){
							score.addScore(mTower[i][j].getPoints());
						
							if(mTower[i][j].getClass().getName() == "net.codegames.towerninja.Bomb"){
								System.out.println("Bomb "+ i + ";" +j);
								explode(i,j);
							}
						}
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
		for (int i = 0; i < mTower.length; i++) {
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
			mApplet.stroke(color, 32);
			// left hand
			if (currentPlayer.getLeftSpeed() > MIN_SPEED) {
				// draw 3 traces of different length over each other
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
				// draw 3 traces of different length over each other
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
									&& !mTower[i][j].isOnTower()
									) {
								removeStone(i, j);
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
									&& !mTower[i][j].isOnTower()
									) {
								removeStone(i, j);
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
		if(startScreen){
			startScreen = false;
		}
		if (mTower[i + 1][j] != null) {
			mTower[i + 1 ][j].moveToDestination(j);
		}
	}
	
	/**
	 * Displays the Score in game
	 * 
	 */
	private void displayScore() {
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
		mTower[0][0] = new Bat(400,500,400,500);
		mApplet.text("Slice the stone to start the game!", 390, 500);
	}
	
	
	/**
	 * Does actually the opposite than what we intended to do, but for testing that we can loose.
	 * 
	 * @return true when tower is full, false else
	 */
	private boolean lost(){
		for (int rowIndex = 0; rowIndex < mTower.length; rowIndex++ ) {
		       Object[] row = mTower[rowIndex];
		       if (row != null) {
		          for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
		             if (null == row[columnIndex]) {
		                 return false;
		             }
		          }
		       }
		       else
		    	   return false;
		    }
		return true;
	}
	
	private void explode(int i, int j){
		System.out.println("Bomb "+ i + ";" +j);
		for(int x = i-1; x <= i+1 && x <= TOWER_WIDTH; x++){
			if(!(x<0)){
				for(int y = j-1; y <= j+1 && y <= TOWER_HIGHT; y++){
					if(!(y<0)){
						if(mTower[x][y] != null && mTower[x][y].isOnTower()){
							removeStone(x,y);
							System.out.println("Remove "+ x + ";" +y);
						}
					}
				}
			}
		}
		
	}
}