package runner.application.obstacles;

import java.awt.geom.Point2D;
import runner.application.ImageResource;
import runner.application.Vector2D;

public class Fireball extends Obstacle {
	// **********************************************************************
	// Private Members
	// **********************************************************************

	// array of 2 fireball sprites to simulate a firey fireball
	private ImageResource[] sprites;

	// index to keep track of which sprite the fireball is using
	private int currentSpriteIndex;

	// counter for index incrementing logic
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

		// init counter and index
		currentSpriteIndex = 0;
		fireballCounter = 0;
	}

	// **********************************************************************
	// Public methods
	// **********************************************************************
	public void moveFireball() {
		// increment counter
		fireballCounter++;

		// add vector to fireball's position
		this.addVector(this.velocity);
	}

	public ImageResource getSprite() {
		// this is a dumb way to switch between the 2 sprite images
		if (fireballCounter % 25 == 3) {
			currentSpriteIndex = (currentSpriteIndex + 1) % 2;
		}

		return sprites[currentSpriteIndex];
	}

}
