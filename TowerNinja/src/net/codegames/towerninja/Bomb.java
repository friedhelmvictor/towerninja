package net.codegames.towerninja;

public class Bomb extends Brick {

	public Bomb(float xLocation, float yLocation, int iDestination,
			int jDestination) {
		super(xLocation, yLocation, iDestination, jDestination);
		setBackground("../resources/bomb.png");
	}
	
	@Override
	void draw(Renderer renderer) {
		renderer.drawBomb(this);
		
	}
}
