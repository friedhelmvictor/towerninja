package net.codegames.towerninja;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
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
		
		//Delete if tempFile exists
		File fileTemp = new File("../resources/" + filename);
		if (fileTemp.exists()){
		    fileTemp.delete();
		}
		try
		{
			final ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream("../resources/" + filename, true));
			fos.writeObject(scores);
		    fos.close();
		}
		catch(Exception e)
		{}		
	}
	
	public List<Score> load(String filename)
	{
		 try{
		      //use buffering
		      InputStream file = new FileInputStream( "../resources/" +  filename);
		      InputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try{
		        //deserialize the List
		        List<Score> scores_new = (List<Score>)input.readObject();
		        scores = scores_new;
		        return scores_new;
		      }
		      finally{
		        input.close();
		      }
		    }
		    catch(ClassNotFoundException ex){
		      
		    }
		    catch(IOException ex){
		     
		    }	
		 return null;
	}
	
	
}
