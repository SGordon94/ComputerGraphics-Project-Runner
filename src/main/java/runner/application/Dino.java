package runner.application;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Hashtable;

public class Dino {

	// **********************************************************************
	// Static Members
	// **********************************************************************

	final static int DEFAULT_NUMBER_OF_SIDES = 4;
	final static int DEFAULT_WIDTH = 42;
	final static int DEFAULT_HEIGHT = 48;

	// **********************************************************************
	// Private Members
	// **********************************************************************
	private int width;
	private int height;

	// current position of dino (maybe use to change Y positions?)
	private Point2D.Double position;

	// list of points
	private Point2D.Double points[];

	private Vector2D jumpVelocity;
	private boolean inJumpState;
	private int currentJumpType; // 0 - not jumping, 1 - normal jump, 2 - super jump
	private Hashtable<String, ImageResource> sprites; // holds all sprites for dino
	private String currentSprite; // current sprite's key for sprites hashtable

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public Dino() {
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		position = new Point2D.Double(0.0, 0.0);
		points = null;
		inJumpState = false;
		currentJumpType = 0;
		jumpVelocity = new Vector2D(0.0, 0.0);
		currentSprite = "";
		loadSprites();
	}

	public Dino(Point2D.Double position, Point2D.Double points[]) {
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
		this.position = position;
		this.points = points;
		inJumpState = false;
		currentJumpType = 0;
		jumpVelocity = new Vector2D(0.0, 0.0);
		currentSprite = "";
		loadSprites();
	}

	// **********************************************************************
	// Getters and Setters
	// **********************************************************************
	public double getX() {
		return this.position.getX();
	}

	public void setX(double x) {
		this.position.setLocation(x, this.position.getY());
	}

	public double getY() {
		return this.position.getY();
	}

	public void setY(double y) {
		// get initial y position
		double initY = this.position.getY();

		// calculate difference in Y init and Y after
		double deltaY = y - initY;

		// apply delta to all y's
		for (Point2D.Double point : points) {
			point.setLocation(point.x, point.y + deltaY);
		}

		this.position.setLocation(this.position.getX(), y);
	}

	public Point2D.Double[] getPoints() {
		return points;
	}

	public void setPoints(Point2D.Double points[]) {
		this.points = points;
	}

	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Vector2D getJumpVelocity() {
		return jumpVelocity;
	}

	public void setJumpVelocity(Vector2D newVelocity) {
		this.jumpVelocity = newVelocity;
	}

	public boolean isJumping() {
		if (inJumpState)
			return true;
		return false;
	}

	public void setInJumpState(boolean inJumpState) {
		this.inJumpState = inJumpState;
	}

	public void setJumpType(int type) {
		currentJumpType = type;
	}

	public void setCurrentSprite(String key) {
		if (key == "crouch0" || key == "crouch1" || key == "run0" || key == "run1" || key == "jump") {
			currentSprite = key;
		}
	}

	public String getCurrentSprite() {
		return currentSprite;
	}

	public ImageResource getCurrentSpriteImage() {
		return sprites.getOrDefault(currentSprite, new ImageResource("sprites/dino_jump.png"));
	}

	// **********************************************************************
	// Private Methods
	// **********************************************************************
	private void loadSprites() {
		// load sprites from sprites folder and store into has table
		sprites = new Hashtable<String, ImageResource>();
		sprites.put("crouch0", new ImageResource("sprites/dino_crouch_0.png"));
		sprites.put("crouch1", new ImageResource("sprites/dino_crouch_1.png"));
		sprites.put("jump", new ImageResource("sprites/dino_jump.png"));
		sprites.put("run0", new ImageResource("sprites/dino_run_0.png"));
		sprites.put("run1", new ImageResource("sprites/dino_run_1.png"));
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	// set jump velocities and jump state
	public void jump() {
		switch (currentJumpType) {
		case 1: // normal jump
			this.setInJumpState(true);
			jumpVelocity = new Vector2D(0.0, 45.0);
			break;
		case 2: // super jump
			this.setInJumpState(true);
			jumpVelocity = new Vector2D(0.0, 60.0);
			break;
		default: // not jumping
			this.setInJumpState(false);
			jumpVelocity = new Vector2D(0.0, 0.0);
			break;
		}
	}

	// check to see what side point is on for line segment
	public int sideTest(Point2D.Double a, Point2D.Double b, Point2D.Double p) {
		// cross product
		double result = (b.getX() - a.getX()) * (p.getY() - a.getY()) - (b.getY() - a.getY()) * (p.getX() - a.getX());

		// right
		if (result > 0) {
			return 1;
		} else if (result == 0) {
			// On the line
			return 0;
		}
		// Left
		return -1;
	}

	// check to see if 2 given lines are intersecting
	public boolean lineIntersect(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		// is point c on line seg ab
		int isPointCOnLineAB = sideTest(a, b, c);

		// is point d on line seg ab
		int isPointDOnLineAB = sideTest(a, b, c);

		// if results are 0 then points are on the line
		boolean isPointOnLineTest1 = isPointCOnLineAB == 0 ? true : false;
		boolean isPointOnLineTest2 = isPointDOnLineAB == 0 ? true : false;

		// test cd intersects ab by checking if ab are on different sides of cd
		boolean test = (sideTest(a, b, c) > 0) ^ (sideTest(a, b, d) > 0);

		// return result
		return isPointOnLineTest1 || isPointOnLineTest2 || test;

	}

	public boolean doLinesIntersect(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		// check if line intersects
		boolean result = lineIntersect(a, b, c, d) && lineIntersect(c, d, a, b);

		return result;
	}

	// collision detection function * adapted from homework *
	public boolean collides(Point2D.Double[] pointsList) {
		// iterate through list of dinosaur point
		for (int i = 0; i < points.length; i++) {

			// get point and next point
			Point2D.Double a = points[i];
			Point2D.Double b = points[(i + 1) % points.length];

			// iterate through list of obstacle points
			for (int j = 0; j < pointsList.length; j++) {
				Point2D.Double c = pointsList[j];
				Point2D.Double d = pointsList[(j + 1) % pointsList.length];

				// check if lines intersect
				// if (doLinesIntersect(a, b, c, d)) {

				// last resort to get collisions working propery :c
				if (doLinesIntersect(a, b, c, d) && Line2D.linesIntersect(a.getX(), a.getY(), b.getX(), b.getY(),
						c.getX(), c.getY(), d.getX(), d.getY())) {
					return true;
				}
			}
		}
		return false;
	}
}
