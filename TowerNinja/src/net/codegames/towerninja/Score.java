package net.codegames.towerninja;

import java.io.Serializable;

public class Score implements Comparable<Score>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 301735992505840227L;
	private int score = 0;
	private String name = "Ninja";
	public int addScore(int n)
	{
		this.score += n;  
		return this.score;
	}
	
	public int score()
	{
		return this.score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int compareTo(Score compareObject) {
		if (this.getScore() < compareObject.getScore())
            return 1;
        else if (this.getScore() == compareObject.getScore())
            return 0;
        else
            return -1;
	}
}
