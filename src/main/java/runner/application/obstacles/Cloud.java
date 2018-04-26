package runner.application.obstacles;

import java.awt.geom.Point2D;

import runner.application.Vector2D;

public class Cloud extends Obstacle {

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public Cloud(Point2D.Double position, Vector2D velocity, Point2D.Double[] obstaclePoints) {
		super(position, velocity, obstaclePoints);
		// TODO Auto-generated constructor stub
	}

	public void moveCloud() {
		this.addVector(this.velocity);
	}

}
