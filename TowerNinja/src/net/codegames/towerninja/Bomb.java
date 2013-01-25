package net.codegames.towerninja;

public class Bomb extends Brick {

	public Bomb(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
		setPoints(-5);
	}

	@Override
	void draw(RendererInterface renderer) {
		renderer.drawBomb(this);

	}
}
