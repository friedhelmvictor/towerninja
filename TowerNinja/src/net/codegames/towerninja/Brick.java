package net.codegames.towerninja;

public class Brick extends AbstractStone {

	public Brick(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		setStartPositionX(xLocation);
		setxLocation(xLocation);
		setyLocation(yLocation);
		setDestination(iDestination, jDestination);
		if (getStartPositionX() > exactXDestination())
			setxVelocity((getStartPositionX() - exactXDestination()) / -5000);
		else
			setxVelocity((exactXDestination() - getStartPositionX()) / 5000);
		setyVelocity((exactYDestination() - yLocation) / 5000);
		setPoints(5);
		
	}
	

	public void draw(RendererInterface renderer) {
		renderer.drawBrick(this);

	}
}
