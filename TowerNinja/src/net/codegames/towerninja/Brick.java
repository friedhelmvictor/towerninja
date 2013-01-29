package net.codegames.towerninja;

public class Brick extends AbstractStone {

	public Brick(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		setxLocation(xLocation);
		setyLocation(yLocation);
		setDestination(iDestination, jDestination);
		setxVelocity((exactXDestination() - xLocation) / 5000);
		setyVelocity((exactYDestination() - yLocation) / 5000);
		setPoints(5);
		
	}
	

	void draw(RendererInterface renderer) {
		renderer.drawBrick(this);
		
	}
}
