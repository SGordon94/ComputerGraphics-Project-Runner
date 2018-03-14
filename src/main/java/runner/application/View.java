package runner.application;

import java.awt.Component;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Random;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

public class View implements GLEventListener {

	// **********************************************************************
	// Public Class Members
	// **********************************************************************

	public static final GLU GLU = new GLU();
	public static final GLUT GLUT = new GLUT();
	public static final Random RANDOM = new Random();
	public static final int DEFAULT_FRAMES_PER_SECOND = 60;
	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final GLJPanel canvas;

	private int counter = 0; // Just an animation counter
	private int w; // Canvas width
	private int h; // Canvas height

	// private final KeyHandler keyHandler;
	private final MouseHandler mouseHandler;

	private final FPSAnimator animator;

	private TextRenderer renderer;

	private Point2D.Double origin; // Current origin coordinates
	private Point2D.Double cursor; // Current cursor coordinates

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public View(GLJPanel canvas) {
		this.canvas = canvas;
		cursor = null;
		// Initialize model
		origin = new Point2D.Double(0.0, 0.0);

		// Initialize rendering
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		// animator.start();

		// keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		w = drawable.getWidth();
		h = drawable.getHeight();

		renderer = new TextRenderer(new Font("Monospaced", Font.PLAIN, 25), true, true);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		updateProjection(drawable);

		update(drawable);
		render(drawable);

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	// **********************************************************************
	// Getters and Setters
	// **********************************************************************
	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public Point2D.Double getOrigin() {
		return new Point2D.Double(origin.x, origin.y);
	}

	public void setOrigin(Point2D.Double origin) {
		this.origin.x = origin.x;
		this.origin.y = origin.y;
		canvas.repaint();
	}

	public Point2D.Double getCursor() {
		return cursor;
	}

	public void setCursor(Point2D.Double cursor) {
		this.cursor = cursor;
		canvas.repaint();
	}

	// **********************************************************************
	// Private Methods (Viewport)
	// **********************************************************************

	private void updateProjection(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();

//		float xmin = (float) (origin.x - 1.0);
//		float xmax = (float) (origin.x + 1.0);
//		float ymin = (float) (origin.y - 1.0);
//		float ymax = (float) (origin.y + 1.0);
//
//		gl.glMatrixMode(GL2.GL_PROJECTION); // Prepare for matrix xform
//		gl.glLoadIdentity(); // Set to identity matrix
//		glu.gluOrtho2D(xmin, xmax, ymin, ymax); // 2D translate and scale

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();

		glu.gluOrtho2D(0.0f, 1000.0f, 0.0f, 450.0f);
	}
	// **********************************************************************
	// Private Methods (Rendering)
	// **********************************************************************

	private void update(GLAutoDrawable drawable) {
		counter++; // Counters are useful, right?
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT); // Clear the buffer
		drawCursorCoordinates(drawable); // Draw some text
		drawAxes(gl);
	}

	// **********************************************************************
	// Private Methods (Scene)
	// **********************************************************************
	private void drawCursorCoordinates(GLAutoDrawable drawable) {
		if (cursor == null)
			return;

		String sx = FORMAT.format(new Double(cursor.x));
		String sy = FORMAT.format(new Double(cursor.y));
		String s = "(" + sx + "," + sy + ")";

		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(1.0f, 1.0f, 0, 1.0f);
		renderer.draw(s, 2, 2);
		renderer.endRendering();
	}

	private void drawAxes(GL2 gl) {
		gl.glBegin(GL.GL_LINES);

		gl.glColor3f(0.25f, 0.25f, 0.25f);
		gl.glVertex2d(-10.0, 0.0);
		gl.glVertex2d(10.0, 0.0);

		gl.glVertex2d(0.0, -10.0);
		gl.glVertex2d(0.0, 10.0);

		gl.glEnd();
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public Component getComponent() {
		// TODO Auto-generated method stub
		return (Component) canvas;
	}

}
