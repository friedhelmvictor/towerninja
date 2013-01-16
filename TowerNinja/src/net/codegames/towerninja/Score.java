package net.codegames.towerninja;

public class Score {
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
}
