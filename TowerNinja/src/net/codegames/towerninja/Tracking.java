package net.codegames.towerninja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

/**
 * Provides constantly a list of all users currently tracked by the 3D camera.
 * Data is returned as a vector of {@link Player}s.
 * 
 * @author Frido
 */
public class Tracking {

	private SimpleOpenNI soni;
	private Vector<Integer> actualUsers = new Vector<Integer>();
	List<HashMap<Character, Float>> players = new ArrayList<HashMap<Character, Float>>();

	/**
	 * Initializes Player Tracking with the Main applet and SimpleOpenNi.
	 * 
	 * @param applet <code>Main</code> instance
	 * @param soni   SimpleOpenNI object
	 */
	public Tracking(PApplet applet, SimpleOpenNI soni) {

		this.soni = soni;
		this.soni.enableDepth();
		this.soni.enableUser(SimpleOpenNI.SKEL_PROFILE_HEAD_HANDS);
		this.soni.setMirror(true);

	}

	/**
	 * Determines if a user is currently visible to the 3D camera.
	 * 
	 * @param  userId ID of a user
	 * @return        true if the user is currently visible
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
	 * Returns a Vector of all Players including their IDs and hand positions.
	 * 
	 * @return Vector of Player objects
	 * @see    Player
	 */
	public List<HashMap<Character, Float>> getPlayers() {
		
		updateUsers();
		players.clear();
		
		for (int i = 0; i < actualUsers.size(); i++) {
			
			int userId = actualUsers.get(i);
			
			PVector right3d = new PVector();
			PVector right2d = new PVector();

			soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, right3d);
			soni.convertRealWorldToProjective(right3d, right2d);
			
			HashMap<Character, Float> map = new HashMap<Character, Float>();
			map.put('i', 1.0f * userId);
			map.put('x', right2d.x);
			map.put('y', right2d.y);
			players.add(map);
		}
		
		return  players;
	}
	
}
