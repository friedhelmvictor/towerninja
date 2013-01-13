package net.codegames.towerninja;

import java.util.HashMap;
import java.util.List;

import processing.core.PApplet;

//kennt alle steine
// kennt Turm

/**
 * @author chameleon
 * 
 *         This class contains a tower data structure. The method createStone
 *         allows creating new stones for free spots within the tower.
 */
public class Game {
	
	PApplet applet;

	/**
	 * A tower represented by a 2-dimensional array. the first dimension
	 * describes the towers width. The second its height. When a new stone is
	 * added to a certain location, it must not actually be located at that
	 * position already. It will fly towards that position though.
	 */
	private Stone[][] tower = new Stone[4][10];
	
	public Game(PApplet p) {
		applet = p;
	}

	public void update(List<HashMap<Character, Float>> players) {
		// createStone();
		// steine erzeugen
		// steine bewegen
		
		// display right hand
		applet.fill(255, 0, 0);
		applet.ellipseMode(applet.CENTER);
		for (int i = 0; i < players.size(); i++) {
			float x = players.get(i).get('x');
			float y = players.get(i).get('y');
			applet.ellipse(x, y, 20, 20);
		}
	}

	private void createStone() {
		towerHeightLoop: for (int i = 0; i < tower.length; i++) {
			for (int j = 0; j < tower[0].length; j++) {
				if (tower[i][j] == null) {
					tower[i][j] = new Stone(50, 5);
					break towerHeightLoop;
				}
			}
		}
	}

	/**
	 * draws every stone
	 */
	void drawStones() {

	}
}
