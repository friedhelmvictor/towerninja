package net.codegames.towerninja;

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
	Vector<Player> players = new Vector<Player>();

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
	 * Updates the players vector for the current users tracked by the 3D camera.
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
					
					soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, spatial);
					soni.convertRealWorldToProjective(spatial, projection);
					players.get(p).setLeftX(projection.x);
					players.get(p).setLeftY(projection.y);
					
					soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, spatial);
					soni.convertRealWorldToProjective(spatial, projection);
					players.get(p).setRightX(projection.x);
					players.get(p).setRightY(projection.y);
					
					players.get(p).updateSpeed();
					
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
			Player player = new Player(userId);
						
			PVector spatial = new PVector();
			PVector projection = new PVector();
			
			soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, spatial);
			soni.convertRealWorldToProjective(spatial, projection);
			player.setLeftX(projection.x);
			player.setLeftY(projection.y);
			
			soni.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, spatial);
			soni.convertRealWorldToProjective(spatial, projection);
			player.setRightX(projection.x);
			player.setRightY(projection.y);
			
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
		
	}
	
	/**
	 * Returns a Vector of all Players including their IDs and hand positions.
	 * 
	 * @return Vector of Player objects
	 * @see    Player
	 */
	public Vector<Player> getPlayers() {
		
		updatePlayers();
		return  players;
	}
	
}
