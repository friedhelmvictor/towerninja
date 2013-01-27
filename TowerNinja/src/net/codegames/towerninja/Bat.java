package net.codegames.towerninja;

public class Bat extends Brick {
	
	
	public Bat(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
		setPoints(0);
	}

	void draw(RendererInterface renderer) {
		renderer.drawBat(this);
		
	}
}
