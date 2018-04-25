package runner.application;

import java.awt.geom.Point2D;

public class Cloud {

	// **********************************************************************
	// Private Members
	// **********************************************************************
	Point2D.Double position;
	Vector2D velocity;

	// idk

	private Point2D.Double[] cloudPoints;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public Cloud(Point2D.Double position, Vector2D velocity, Point2D.Double[] cloudPoints) {
		this.position = position;
		this.velocity = velocity;
		this.cloudPoints = cloudPoints;
	}

	// **********************************************************************
	// Getters and Setters
	// **********************************************************************

	public Point2D.Double[] getCloudPoints() {
		return cloudPoints;
	}

	public void setCloudPoints(Point2D.Double[] cloudPoints) {
		this.cloudPoints = cloudPoints;
	}

	public void setX(double x) {

		double initX = this.position.x;
		double deltaX = x - initX;

		// apply delta to all x's
		for (Point2D.Double point : cloudPoints) {
			point.setLocation(point.x + deltaX, point.y);
		}

		// update position
		this.position.setLocation(x, this.position.getY());
	}

	// **********************************************************************
	// movement methods
	// **********************************************************************
	public void addVector(Vector2D v) {
		// add vector to all cloud points
		for (Point2D.Double point : cloudPoints) {
			point.setLocation(point.getX() + v.getX(), point.getY() + v.getY());
		}

		// also update origin
		this.position.setLocation(this.position.getX() + v.getX(), this.position.getY() + v.getY());
	}

	public void moveCloud() {
		this.addVector(this.velocity);
	}

}
