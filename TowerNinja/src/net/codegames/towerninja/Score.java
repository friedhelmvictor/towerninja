package net.codegames.towerninja;

public class Score implements Comparable<Score>, java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 301735992505840227L;
	private int score = 0;
	private String name = "Peter";
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
