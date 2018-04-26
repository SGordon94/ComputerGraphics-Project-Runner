package runner.application;

import java.awt.geom.Point2D;

import runner.application.obstacles.Cloud;

public class Dino {

	// **********************************************************************
	// Static Members
	// **********************************************************************

	final static int DEFAULT_NUMBER_OF_SIDES = 6;
	final static int DEFAULT_WIDTH = 50;
	final static int DEFAULT_HEIGHT = 50;

	// **********************************************************************
	// Private Members
	// **********************************************************************
	private int width;
	private int height;

	// current position of dino (maybe use to change Y positions?)
	private Point2D.Double position;

	// list of points
	private Point2D.Double points[];

	// TODO: jump
	private Vector2D jumpVelocity;
	private boolean inJumpState;
	private int currentJumpType; //0 - not jumping, 1 - normal jump, 2 - super jump

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public Dino() {
		width = 60;
		height = 60;
		position = new Point2D.Double(0.0, 0.0);
		points = null;
		inJumpState = false;
		currentJumpType = 0;
		jumpVelocity = new Vector2D(0.0, 0.0);
	}

	public Dino(Point2D.Double position, Point2D.Double points[]) {
		this.position = position;
		this.points = points;
		inJumpState = false;
		currentJumpType = 0;
		jumpVelocity = new Vector2D(0.0, 0.0);
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
		double initY = this.position.getY();

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
		if(inJumpState) return true;
		return false;
	}

	public void setInJumpState(boolean inJumpState) {
		this.inJumpState = inJumpState;
	}

	public void setJumpType(int type) {
		currentJumpType = type;
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public void jump() {
		switch(currentJumpType) {
			case 1:
				this.setInJumpState(true);
				jumpVelocity = new Vector2D(0.0, 50.0);
				break;
			case 2:
				this.setInJumpState(true);
				jumpVelocity = new Vector2D(0.0, 75.0);
				break;
			default:
				this.setInJumpState(false);
				jumpVelocity = new Vector2D(0.0, 0.0);
				break;
		}
	}

	//
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

	public boolean isPointOnLine(Point2D.Double a, Point2D.Double b, Point2D.Double p) {
		// shift points / vector
		Point2D.Double shiftB = new Point2D.Double(b.getX() - a.getX(), b.getY() - a.getY());
		Point2D.Double shiftP = new Point2D.Double(p.getX() - a.getX(), p.getY() - a.getY());

		// find cross product
		double crossProduct = (shiftB.getX() * shiftP.getY() - shiftP.getX() * shiftB.getY());

		// if crossproduct < 0 then given point is on segment
		return Math.abs(crossProduct) < 0.000001;
	}

	public boolean lineIntersect(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		// is point c on line seg ab
		boolean isPointOnLineTest1 = isPointOnLine(a, b, c);
		// is point d on line seg ab
		boolean isPointOnLineTest2 = isPointOnLine(a, b, d);

		// test cd intersects ab by checking if ab are on different sides of cd
		boolean test = (sideTest(a, b, c) > 0) ^ (sideTest(a, b, d) > 0);

		return isPointOnLineTest1 || isPointOnLineTest2 || test;

	}

	public boolean doLinesIntersect(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double d) {
		// check if line intersects
		boolean result = lineIntersect(a, b, c, d) && lineIntersect(c, d, a, b);

		return result;
	}

	public boolean collides(Point2D.Double[] pointsList) {
		for (int i = 0; i < points.length; i++) {
			Point2D.Double a = points[i];
			Point2D.Double b = points[(i + 1) % points.length];
			// Point2D.Double b = points[(i + 1) % points.length];
			for (int j = 0; j < pointsList.length; j++) {
				Point2D.Double c = pointsList[j];
				Point2D.Double d = pointsList[(j + 1) % pointsList.length];

				if (doLinesIntersect(a, b, c, d)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean collides(Cloud cloud) {
		Point2D.Double[] cloudPoints = cloud.getPoints();

		return collides(cloudPoints);
	}
}
