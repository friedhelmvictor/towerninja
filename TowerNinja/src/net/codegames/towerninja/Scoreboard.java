package net.codegames.towerninja;

import java.util.*;
import processing.core.PApplet;


public class Scoreboard extends PApplet {
	private static int lenght = 10;
	private List<Score> scores = new ArrayList<Score>();
	
	/**
	 * Adds a score to the Scoreboard
	 * and returns the position.
	 * 
	 * @param score
	 * @return score position or -1 if not in scoreboard 
	 */
	public int addScore(Score score)
	{
		int position = 0;
		for (int i = scores.size()-1; i > 0; i++) {
			if(scores.get(i).score() > score.score()){
				position = i;
				break;
			}
		}
		scores.add(position, score);
		return position;
	}
	
	/**
	 * Gives the Screboard as an array
	 * 
	 * @return scoreboard, array over Score
	 */
	public Score[] giveScoreboard(){
		return scores.toArray(new Score[scores.size()]);
	}
	
	public void display() {
		Score[] scoreboard = this.giveScoreboard();
		for(int i = 0; i < scoreboard.length; i++){
			text(scoreboard[i].getName(), 10, 20 + i*30);
			text(scoreboard[i].getScore(),200,20 + i*30);
		}		
	}
	
}
