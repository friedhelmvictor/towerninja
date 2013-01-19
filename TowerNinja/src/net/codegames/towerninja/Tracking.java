package net.codegames.towerninja;

import java.awt.Color;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

/**
 * Provides constantly a list of all users currently tracked by the 3D camera.
 * Data is returned as a vector of {@link Player}s.
 */
public class Tracking {

	private SimpleOpenNI soni;

	private PApplet mApplet;

	private Vector<Integer> actualUsers = new Vector<Integer>();
	Vector<Player> players = new Vector<Player>();

	private Color[] colors = { new Color(79, 12, 77), // violett
			new Color(27, 64, 120), // blue
			new Color(45, 120, 33) // green
	};

	private int drawShadows = 0;
	private int[] userMap;
	private PImage userImage;

	/**
	 * Initializes Player Tracking with the Main applet and SimpleOpenNi.
	 * 
	 * @param applet
	 *            <code>Main</code> instance
	 * @param soni
	 *            SimpleOpenNI object
	 */
	public Tracking(PApplet applet, SimpleOpenNI soni) {

		if (Main.KINECT_PRESENT) {
			this.soni = soni;
			this.soni.enableDepth();
			this.soni.enableUser(SimpleOpenNI.SKEL_PROFILE_HEAD_HANDS);
			this.soni.setMirror(true);
		}

		mApplet = applet;
		userMap = new int[soni.depthWidth() * soni.depthHeight()];
		userImage = mApplet.createImage(mApplet.width, mApplet.height,
				mApplet.ARGB);
	}

	/**
	 * Determines if a user is currently visible to the 3D camera.
	 * 
	 * @param userId
	 *            ID of a user
	 * @return true if the user is currently visible
	 */
	private boolean userTracked(int userId) {

		PVector pv = new PVector();
		soni.getCoM(userId, pv);
		return !(pv.x == 0 && pv.y == 0 && pv.z == 0);
	}

	/**
	 * Updates the list of users that are currently visible to the 3D camera.
	 */
	private void updateUsers() {

		soni.update();

		// get current players from camera
		int[] soniPlayers = soni.getUsers();
		// add only actual tracked players to actualPlayers
		actualUsers.clear();
		for (int i = 0; i < soniPlayers.length; i++) {
			if (userTracked(soniPlayers[i])) {
				actualUsers.add(new Integer(soniPlayers[i]));
			}
		}
	}

	/**
	 * Updates the players vector for the current users tracked by the 3D
	 * camera.
	 */
	private void updatePlayers() {

		Vector<Integer> currentUsers = new Vector<Integer>();
		updateUsers();

		// first loop for all user already stored as players
		for (int p = 0; p < players.size(); p++) {
			for (int u = 0; u < actualUsers.size(); u++) {
				if (players.get(p).getUserId() == actualUsers.get(u)) {

					int userId = players.get(p).getUserId();

					PVector spatial = new PVector();
					PVector projection = new PVector();

					soni.getJointPositionSkeleton(userId,
							SimpleOpenNI.SKEL_LEFT_HAND, spatial);
					soni.convertRealWorldToProjective(spatial, projection);
					players.get(p).setLeft(projection.x, projection.y);

					soni.getJointPositionSkeleton(userId,
							SimpleOpenNI.SKEL_RIGHT_HAND, spatial);
					soni.convertRealWorldToProjective(spatial, projection);
					players.get(p).setRight(projection.x, projection.y);

					// remove handled user from actualUsers for second loop
					actualUsers.remove(u);

					// save userID for third loop
					currentUsers.add(userId);
				}
			}
		}

		// second loop for all new users not yet stored as players
		for (int u = 0; u < actualUsers.size(); u++) {

			int userId = actualUsers.get(u);
			Player player = new Player(userId, colors[userId % colors.length]);

			PVector spatial = new PVector();
			PVector projection = new PVector();

			soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND,
					spatial);
			soni.convertRealWorldToProjective(spatial, projection);
			player.setLeft(projection.x, projection.y);

			soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND,
					spatial);
			soni.convertRealWorldToProjective(spatial, projection);
			player.setRight(projection.x, projection.y);

			players.add(player);

			// remove handled user from actualUsers
			actualUsers.remove(u);

			// save userID for third loop
			currentUsers.add(userId);
		}

		// third loop to remove all untracked players
		for (int p = 0; p < players.size(); p++) {
			boolean delete = true;
			for (int i = 0; i < currentUsers.size(); i++) {
				if (players.get(p).getUserId() == currentUsers.get(i)) {
					delete = false;
				}
			}
			if (delete) {
				players.remove(p);
			}
		}

		drawShadows();
	}

	/**
	 * Returns a Vector of all Players including their IDs and hand positions.
	 * 
	 * @return Vector of Player objects
	 * @see Player
	 */
	public Vector<Player> getPlayers() {
		if (!Main.KINECT_PRESENT) {
			return new Vector<Player>();
		}
		updatePlayers();
		return players;
	}

	/**
	 * Draws the shadow images of currently tracked players in their respective
	 * colors on the background.
	 */
	private void drawShadows() {
		// refresh the image only every third frame
		drawShadows = (drawShadows + 1) % 3;
		if ((drawShadows % 3) != 0 && players.size() > 0) {
			mApplet.image(userImage, 0, 0);
			return;
		}
		
		userImage = mApplet.createImage(soni.depthWidth(), soni.depthHeight(),
				mApplet.ARGB);
		soni.getUserPixels(SimpleOpenNI.USERS_ALL, userMap);
		for (int y = 0; y < soni.depthHeight(); y += 2) {
			for (int x = 0; x < soni.depthWidth(); x += 2) {
				int i = x + y * soni.depthWidth();
				for (int p = 0; p < players.size(); p++) {
					if (userMap[i] == players.get(p).getUserId()) {
						userImage.pixels[i] = players.get(p).getColor() & 0x80FFFFFF;
					}
				}
			}
		}
		userImage.updatePixels();
		mApplet.image(userImage, 0, 0);
	}

}
