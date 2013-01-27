package net.codegames.towerninja;

import processing.core.PApplet;
import processing.core.PImage;

public class AppletRenderer implements RendererInterface {
	
	private PApplet mApplet;
	private PImage mBatImage, mBombImage, mBrickImage, mBrickSlice, mBrickFade;

	public AppletRenderer(PApplet mApplet) {
		super();
		this.mApplet = mApplet;
		mBatImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bat2.png");
		mBombImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bomb.png");
		mBrickImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/brick.png");
		mBrickSlice = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/brick-slice.png");
		mBrickFade = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/brick-fade.png");
	}

	@Override
	public void drawBrick(Brick brick) {
		if (!brick.isDestroyed()) {
			mApplet.image(mBrickImage, brick.getxLocation(), brick.getyLocation());
		} else {
			if (brick.getDestroyTimer() > 8) {
				mApplet.image(mBrickSlice, brick.getxLocation(), brick.getyLocation());
			} else {
				mApplet.tint(255, 30 * brick.getDestroyTimer());
				mApplet.image(mBrickFade, brick.getxLocation(), brick.getyLocation());
				mApplet.noTint();
			}
			brick.decreaseDestroyTime();
		}
	}

	@Override
	public void drawBomb(Bomb bomb) {
		if (!bomb.isDestroyed()) {
			mApplet.image(mBombImage, bomb.getxLocation(), bomb.getyLocation());
		} else {
			if (bomb.getDestroyTimer() > 8) {
				mApplet.image(mBombImage, bomb.getxLocation(), bomb.getyLocation());
			} else {
				mApplet.tint(255, 30 * bomb.getDestroyTimer());
				mApplet.image(mBombImage, bomb.getxLocation(), bomb.getyLocation());
				mApplet.noTint();
			}
			bomb.decreaseDestroyTime();
		}
	}

	public void drawBat(Bat bat) {
		if (!bat.isDestroyed()) {
			mApplet.image(mBatImage, bat.getxLocation(), bat.getyLocation());
		} else {
			if (bat.getDestroyTimer() > 8) {
				mApplet.image(mBatImage, bat.getxLocation(), bat.getyLocation());
			} else {
				mApplet.tint(255, 30 * bat.getDestroyTimer());
				mApplet.image(mBatImage, bat.getxLocation(), bat.getyLocation());
				mApplet.noTint();
			}
			bat.decreaseDestroyTime();
		}
	}

}
