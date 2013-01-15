package net.codegames.towerninja;

import processing.core.PApplet;
import SimpleOpenNI.SimpleOpenNI;

public class Main extends PApplet {

	/**
	 * Set the value of KINECT_PRESENT to true if you actually have one
	 * connected. Otherwise you won't be able to start the program.
	 */
	public static final boolean KINECT_PRESENT = true;

	/**
	 * When enabled, displays a frame counter
	 */
	private static final boolean DEV_MODE = true;

	/**
	 * This is the main class. It should just handle the basic setup, creating
	 * the game and updating it regulary. Apart from that, the {@link Game}
	 * Object should take care of things.
	 */
	private static final long serialVersionUID = 1L;
	private int width = 640;
	private int height = 480;
	private Game game;
	private Tracking tracking;

	private SimpleOpenNI soni;

	/**
	 * Initial setup of the Applet. Also creating the {@link Game} and
	 * {@link Tracking} object.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {

		size(width, height);
		frameRate(30);

		game = new Game(this);

		soni = new SimpleOpenNI(this);
		tracking = new Tracking(this, soni);

	}

	/**
	 * Called [frameRate] number of times per second. Updating the gamestate.
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(128);
		game.update(tracking.getPlayers());

		if (DEV_MODE) {
			displayFramerate();
		}
	}

	private void displayFramerate() {
		fill(255);
		noStroke();
		text((int) frameRate, 10, 20);
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
