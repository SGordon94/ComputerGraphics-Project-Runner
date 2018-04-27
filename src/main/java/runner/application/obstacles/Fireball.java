package runner.application.obstacles;

import java.awt.geom.Point2D;

import runner.application.ImageResource;
import runner.application.Vector2D;

public class Fireball extends Obstacle {
	// **********************************************************************
	// Static Members
	// **********************************************************************

	final static int DEFAULT_NUMBER_OF_SIDES = 4;
	final static int DEFAULT_WIDTH = 60;
	final static int DEFAULT_HEIGHT = 60;

	// **********************************************************************
	// Private Members
	// **********************************************************************
	private ImageResource[] sprites;
	private int currentSpriteIndex;
	private int fireballCounter;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	// default constructor
	public Fireball(Point2D.Double position, Vector2D velocity, Point2D.Double[] obstaclePoints) {
		super(position, velocity, obstaclePoints);

		// init fire ball images
		sprites = new ImageResource[2];
		sprites[0] = new ImageResource("sprites/fiya_0.png");
		sprites[1] = new ImageResource("sprites/fiya_1.png");

		currentSpriteIndex = 0;
		fireballCounter = 0;
	}

	// **********************************************************************
	// Public methods
	// **********************************************************************
	public void moveFireball() {
		fireballCounter++;
		this.addVector(this.velocity);
	}

	public ImageResource getSprite() {
		if (fireballCounter % 25 == 3) {
			currentSpriteIndex = (currentSpriteIndex + 1) % 2;
		}

		return sprites[currentSpriteIndex];
	}

}
