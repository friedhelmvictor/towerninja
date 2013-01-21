package net.codegames.towerninja;

import processing.core.PApplet;
import processing.core.PImage;

public class AppletRenderer implements Renderer {
	private PApplet mApplet;
	private PImage mBombImage, mBrickImage;

	public AppletRenderer(PApplet mApplet) {
		super();
		this.mApplet = mApplet;
		mBombImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bomb.png");
		mBrickImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/brick.png");
	}

	@Override
	public void drawBrick(Brick brick) {
		mApplet.image(mBrickImage, brick.getxLocation(), brick.getyLocation());
	}

	@Override
	public void drawBomb(Bomb bomb) {
		mApplet.image(mBombImage, bomb.getxLocation(), bomb.getyLocation());
	}

}
