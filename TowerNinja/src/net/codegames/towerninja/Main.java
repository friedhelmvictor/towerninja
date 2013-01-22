package net.codegames.towerninja;

import processing.core.PApplet;
import processing.core.PImage;
import SimpleOpenNI.SimpleOpenNI;

public class Main extends PApplet {

	/**
	 * Set the value of KINECT_PRESENT to true if you actually have one
	 * connected. Otherwise you won't be able to start the program.
	 */
	public static final boolean KINECT_PRESENT = false;

	/**
	 * When enabled, displays a frame counter
	 */
	private static final boolean DEV_MODE = true;
	
	/**
	 * 
	 */
	private static final float SIZE_MULTIPLIER = 1.5f;
	
	/**
	 * This is the main class. It should just handle the basic setup, creating
	 * the game and updating it regularly. Apart from that, the {@link Game}
	 * Object should take care of things.
	 */
	private static final long serialVersionUID = 1L;
	private int width = (int)(SIZE_MULTIPLIER * 640);
	private int height = (int)(SIZE_MULTIPLIER * 480);
	private Game game;
	private Tracking tracking;

	private SimpleOpenNI soni;
	
	PImage bg;

	/**
	 * Initial setup of the Applet. Also creating the {@link Game} and
	 * {@link Tracking} object.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {

		size(width, height);
		frameRate(30);
		smooth();

		game = new Game(this);

		soni = new SimpleOpenNI(this);
		tracking = new Tracking(this, soni);

		bg = loadImage(this.getCodeBase() + "../resources/background.png");
	}

	/**
	 * Called [frameRate] number of times per second. Updating the gamestate.
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(bg);
		game.update(tracking.getPlayers());

		if (DEV_MODE) {
			displayDevOutput();
		}
	}

	private void displayDevOutput() {
		fill(64);
		noStroke();
		text("FPS: " + (int) frameRate, 10, 20);
		
		Runtime rt = Runtime.getRuntime();
		text("Memory: " + Math.round((rt.totalMemory() - rt.freeMemory())/(1024*1024)) + " MB", 10, 35);
	}

	/**
	 * Callback for SimpleOpenNI's automatic user calibration.
	 * 
	 * @param userId
	 */
	public void onNewUser(int userId) {
		soni.requestCalibrationSkeleton(userId, true);
	}

	/**
	 * Callback for SimpleOpenNI's automatic user tracking.
	 * 
	 * @param userId
	 * @param successfull
	 */
	public void onEndCalibration(int userId, boolean successfull) {
		if (successfull) {
			soni.startTrackingSkeleton(userId);
		}
	}

	public static void main(String[] args) {
		PApplet.main(new String[] { "net.codegames.towerninja.Main" });
	}

}
