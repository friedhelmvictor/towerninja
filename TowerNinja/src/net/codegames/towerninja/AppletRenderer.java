package net.codegames.towerninja;

import processing.core.PApplet;
import processing.core.PImage;

public class AppletRenderer implements RendererInterface {

	private PApplet mApplet;
	private PImage mBatImage, mBombImage, mBombSlice, mBombFade, mBrickImage,
			mBrickSlice, mBrickFade;

	public AppletRenderer(PApplet mApplet) {
		super();
		this.mApplet = mApplet;
		mBatImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bat.png");
		mBombImage = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bomb.png");
		mBombSlice = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bomb-slice.png");
		mBombFade = mApplet.loadImage(mApplet.getCodeBase()
				+ "../resources/bomb-fade.png");
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
			mApplet.image(mBrickImage, brick.getxLocation(),
					brick.getyLocation());
		} else {
			drawPoints(brick);
			if (brick.getDestroyTimer() > 8) {
				mApplet.image(mBrickSlice, brick.getxLocation(),
						brick.getyLocation());
			} else {

				mApplet.tint(255, 30 * brick.getDestroyTimer());
				mApplet.image(mBrickFade, brick.getxLocation(),
						brick.getyLocation());
				mApplet.noTint();
			}
			brick.decreaseDestroyTime();
		}
		// for(int i=(int)brick.getxLocation(); i<= brick.exactXDestination();
		// i+=4) {
		// mApplet.stroke(23);
		// mApplet.point(i, brick.getPath().getY(i));
		// }
	}

	@Override
	public void drawBomb(Bomb bomb) {
		if (!bomb.isDestroyed()) {
			mApplet.image(mBombImage, bomb.getxLocation(), bomb.getyLocation());
		} else {
			drawPoints(bomb);
			if (bomb.getDestroyTimer() > 7) {
				mApplet.image(mBombSlice, bomb.getxLocation(),
						bomb.getyLocation());
			} else {
				mApplet.tint(255, 30 * bomb.getDestroyTimer());
				mApplet.image(mBombFade, bomb.getxLocation(),
						bomb.getyLocation());
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

	private void drawPoints(AbstractStone stone) {
		mApplet.textAlign(mApplet.CENTER);
		mApplet.fill(128, 0, 0);
		mApplet.textSize(36);
		if (stone.getClass().getSimpleName() == "Bomb")
			mApplet.text(stone.getPoints(),
					stone.getxLocation() + (stone.getWidth() / 2),
					stone.getyLocation());
		else {
			mApplet.text(-1 * stone.getPoints(),
					stone.getxLocation() + (stone.getWidth() / 2),
					stone.getyLocation());
		}
	}

}
