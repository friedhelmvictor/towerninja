package net.codegames.towerninja;

public class Brick extends AbstractStone {

	public Brick(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		setxLocation(xLocation);
		setyLocation(yLocation);
		setiDestination(iDestination);
		setjDestination(jDestination);
		setxVelocity((exactXDestination() - xLocation) / 5000);
		setyVelocity((exactYDestination() - yLocation) / 5000);
	}

	@Override
	void draw(RendererInterface renderer) {
		renderer.drawBrick(this);
		
	}
}
