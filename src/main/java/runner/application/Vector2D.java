package runner.application;

import java.awt.geom.Point2D;

/*
 * 
 *  Vector class (wrapper for Point2D.Double) to differentiate
 *  between vectors and points
 * 
 * */
public class Vector2D extends Point2D.Double {

	/*
	 * auto generated serial?
	 */
	private static final long serialVersionUID = -8850240991984891965L;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	// default constructor
	public Vector2D() {
		super();
	}

	public Vector2D(double x, double y) {
		super(x, y);
	}

	public Vector2D(Point2D.Double a, Point2D.Double b) {
		this(b.getX() - a.getX(), b.getY() - a.getY());
	}

	// copy constructor
	public Vector2D(Vector2D v) {
		x = v.x;
		y = v.y;
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public String toString() {
		return "(" + this.x + "," + this.y + ")";
	}

}