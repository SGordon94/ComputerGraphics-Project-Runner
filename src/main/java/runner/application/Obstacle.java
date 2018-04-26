package runner.application;

import java.awt.geom.Point2D;

abstract class Obstacle {

	// **********************************************************************
	// Private Members
	// **********************************************************************
	Point2D.Double position;

	Vector2D velocity;

	private Point2D.Double[] obstaclePoints;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public Obstacle(Point2D.Double position, Vector2D velocity, Point2D.Double[] obstaclePoints) {
		this.position = position;
		this.velocity = velocity;
		this.obstaclePoints = obstaclePoints;
	}

	// **********************************************************************
	// Getters and Setters
	// **********************************************************************

	public Point2D.Double getPosition() {
		return position;
	}

	public void setPosition(Point2D.Double position) {
		this.position = position;
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}

	public Point2D.Double[] getPoints() {
		return obstaclePoints;
	}

	public void setPoints(Point2D.Double[] obstaclePoints) {
		this.obstaclePoints = obstaclePoints;
	}

	// **********************************************************************
	// movement methods
	// **********************************************************************
	public void setX(double x) {

		double initX = this.position.x;
		double deltaX = x - initX;

		// apply delta to all x's
		for (Point2D.Double point : obstaclePoints) {
			point.setLocation(point.x + deltaX, point.y);
		}

		// update position
		this.position.setLocation(x, this.position.getY());
	}

	public void addVector(Vector2D v) {
		// add vector to all cloud points
		for (Point2D.Double point : obstaclePoints) {
			point.setLocation(point.getX() + v.getX(), point.getY() + v.getY());
		}

		// also update origin
		this.position.setLocation(this.position.getX() + v.getX(), this.position.getY() + v.getY());
	}

}
