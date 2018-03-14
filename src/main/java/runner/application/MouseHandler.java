package runner.application;

//import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

//******************************************************************************

/**
 * The <CODE>MouseHandler</CODE> class.
 * <P>
 *
 * @author Chris Weaver
 * @version %I%, %G%
 */
public final class MouseHandler extends MouseAdapter {
	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final View view;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public MouseHandler(View view) {
		this.view = view;

		Component component = view.getComponent();

		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);
	}

	// **********************************************************************
	// Override Methods (MouseMotionListener)
	// **********************************************************************
	public void mouseMoved(MouseEvent e) {
		Point2D.Double v = calcCoordinatesInView(e.getX(), e.getY());

		view.setCursor(v);
	}

	// **********************************************************************
	// Private Methods
	// **********************************************************************

	private Point2D.Double calcCoordinatesInView(int sx, int sy) {
		int w = sx;
		int h = sy;
		//Point2D.Double p = view.getOrigin();
		//double vx = p.x + (sx * 2.0) / w - 1.0;
		//double vy = p.y - (sy * 2.0) / h + 1.0;

		return new Point2D.Double(w, h);
	}

}

// ******************************************************************************
