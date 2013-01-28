package net.codegames.towerninja;

public class Parabole {
	private float aFactor, bFactor, c;

	public Parabole(float x1, float y1, float x2, float y2, float x3, float y3) {
		float a1 = x1 * x1;
		float b1 = x1;
		float d1 = y1;

		float a2 = x2 * x2;
		float b2 = x2;
		float d2 = y2;

		float a3 = x3 * x3;
		float b3 = x3;
		float d3 = y3;

		float a4 = a2 - a1;
		float b4 = b2 - b1;
		float d4 = d2 - d1;

		float a5 = a3 - a2;
		float b5 = b3 - b2;
		float d5 = d3 - d2;

		float fktr = b4 / b5;

		float a6 = a4 + -fktr * a5;
		float d6 = d4 + -fktr * d5;
		aFactor = d6 / a6;

		bFactor = (d4 - aFactor * a4) / b4;
		c = d1 - (aFactor * a1 + bFactor * b1);
//		System.out.println(aFactor + "x�+" + bFactor + "x+" + c);

//		float a1 = x1 * x1;
//		float a2 = x2 * x2;
//		float fktr = (x2 - x1) / (x3 - x2);
//		aFactor = ((a2 - a1) + -fktr * ((x3 * x3) - a2))
//				/ ((y2 - y1) + -fktr * (y3 - y2));
//		bFactor = ((y2 - y1) - aFactor * (a2 - a1)) / (x2 - x1);
//		c = y1 - (aFactor * a1 + bFactor * x1);
//		System.out.println(aFactor + "x�+" + bFactor + "x+" + c);

		// http://www.ina-de-brabandt.de/analysis/qf/parabel-aus-3punkten.html
		// http://en.wikipedia.org/wiki/Projectile_motion

	}

	// public float getX(float y) {
	// float p = bFactor / aFactor;
	// float q = c / aFactor;
	// return 0;
	// }

	public float getY(float x) {
		return (aFactor * x * x) + (bFactor * x) + c;
	}

	public float slopeAtX(float x) {
		return 2 * aFactor * x + bFactor;
	}
}
