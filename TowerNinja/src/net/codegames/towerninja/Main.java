package net.codegames.towerninja;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
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
	private static final boolean DEV_MODE = false;
	
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
	public static int width = (int)(SIZE_MULTIPLIER * 640);
	public static int height = (int)(SIZE_MULTIPLIER * 480);
	private Game game;
	private Tracking tracking;

	private SimpleOpenNI soni;
	
	PImage bg;
	PImage fg;
	
	private PFont katana;

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
		
		katana = loadFont(getCodeBase()
					+ "../resources/Katana-54.vlw");
		textFont(katana);

		game = new Game(this);

		soni = new SimpleOpenNI(this);
		tracking = new Tracking(this, soni);
		bg = loadImage(getCodeBase()
				+ "../resources/background.png");
		fg = loadImage(getCodeBase()
				+ "../resources/foreground.png");
	}

	/**
	 * Called [frameRate] number of times per second. Updating the gamestate.
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		image(bg, 0, height - bg.height);
		if(game.restart){
			game = new Game(this);
			game.setStartScreen(false);
		}
			
		
		game.update(tracking.getPlayers());
		
		
		if (DEV_MODE) {
			displayDevOutput();
		}
		
		image(fg, 0, height - fg.height);
	}

	private void displayDevOutput() {
		fill(64);
		noStroke();
		textAlign(LEFT);
		textSize(12);
		text("FPS: " + (int) frameRate, 10, 60);
		
		Runtime rt = Runtime.getRuntime();
		text("Memory: " + Math.round((rt.totalMemory() - rt.freeMemory())/(1024*1024)) + " MB", 10, 75);
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
