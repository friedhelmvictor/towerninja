package net.codegames.towerninja;

public class Bomb extends Brick {

	public Bomb(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
	}

	@Override
	void draw(RendererInterface renderer) {
		renderer.drawBomb(this);

	}
}
