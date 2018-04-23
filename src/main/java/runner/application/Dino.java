package runner.application;

import java.awt.geom.Point2D;

public class Dino {

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
	// private Vector2D.Double jumpVelocity;
	private boolean inJumpState;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public Dino() {
		width = 60;
		height = 60;
		position = new Point2D.Double(0.0, 0.0);
		inJumpState = false;
		points = null;
	}

	public Dino(Point2D.Double position, Point2D.Double points[]) {
		this.position = position;
		this.points = points;
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

	public boolean getInJumpState() {
		return inJumpState;
	}

	public void setInJumpState(boolean inJumpState) {
		this.inJumpState = inJumpState;
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public void jump() {
		this.setInJumpState(true);
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

	// check what side a point c is on given a line segment ab
	private int sideTest(Point2D.Double a, Point2D.Double b, Point2D.Double p) {
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
}
