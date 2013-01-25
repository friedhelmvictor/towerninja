package net.codegames.towerninja;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import processing.core.PApplet;


public class Scoreboard extends PApplet implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
		scores.add(score);
		Collections.sort(scores);
		return scores.indexOf(score);
	}
	
	/**
	 * Gives the Screboard as an array
	 * 
	 * @return scoreboard, array over Score
	 */
	public Score[] giveScoreboard(){
		return scores.toArray(new Score[scores.size()]);
	}
	
	public void save(String filename)
	{
		
		try
		{
			
			
		// Write to disk with FileOutputStream
		FileOutputStream f_out = new FileOutputStream(filename);

		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);

		// Write object out to disk
		obj_out.writeObject ( this );
		}
		catch(Exception e)
		{}		
	}
	
	public Scoreboard load(String file)
	{
		try
		{
			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream(file);

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = 
				new ObjectInputStream (f_in);

			// Read an object
			Object obj = obj_in.readObject();

			if (obj instanceof Scoreboard)
			{
				
				Scoreboard scoreboard_new = (Scoreboard) obj;
				return scoreboard_new;
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		return null;
	}
	
	
}
