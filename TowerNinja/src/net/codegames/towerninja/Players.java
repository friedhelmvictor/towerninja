package net.codegames.towerninja;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

/**
 * 
 * @author Frido
 *
 */
public class Players {

	private SimpleOpenNI soni;
	private Vector<Integer> actualPlayers = new Vector<Integer>();

	/**
	 * 
	 * @param applet
	 */
	public Players(PApplet applet) {

		soni = new SimpleOpenNI(applet);
		soni.enableDepth();
		soni.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);
		soni.setMirror(true);

	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	private boolean userTracked(int userId) {

		PVector pv = new PVector();
		soni.getCoM(userId, pv);
		return !(pv.x == 0 && pv.y == 0 && pv.z == 0);
	}

	/**
	 * 
	 */
	public void updateUsers() { 

		soni.update(); 

		// get current players from camera
		int[] soniPlayers = soni.getUsers();
		// add only actual tracked players to actualPlayers
		actualPlayers.clear();
		for (int i = 0; i < soniPlayers.length; i++) {
			if (userTracked(soniPlayers[i])) {
				actualPlayers.add(new Integer(soniPlayers[i]));
			}
		}
	}

}
