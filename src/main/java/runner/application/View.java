package runner.application;

import java.awt.Component;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
	public static final double FRAME_TIME_DELTA = 5.0 * 1.0/(double)DEFAULT_FRAMES_PER_SECOND;
	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final GLJPanel canvas;

	// dino model
	// private Dino saur;
	private Dino dino;
	// public ArrayList<Point> pointsList;
	public Random rand;
	//int jumpModifier = -10;
	// Used if spacebar is let go early
	//int shortJump = 0;

	private int counter = 0; // Just an animation counter
	private int jumpFrameLimit = 0;
	private boolean spaceIsPressed = false;
	private int w; // Canvas width
	private int h; // Canvas height
	private double floorLocY = 80.0;
	private Vector2D gravity = new Vector2D(0.0, -15.0);

	// private final KeyHandler keyHandler;
	// private final MouseHandler mouseHandler;

	private final FPSAnimator animator;

	private TextRenderer renderer;

	private Point2D.Double origin; // Current origin coordinates
	private Point2D.Double cursor; // Current cursor coordinates

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	ArrayList<Cloud> cloudList;

	public View(GLJPanel canvas) {

		// init dino model info
		// set position
		Point2D.Double position = new Point2D.Double(200.0, floorLocY);
		// TODO: make this not a simple polygon
		// generate polygon points
		Point2D.Double[] polygonPoints = generatePolygon(position, Dino.DEFAULT_NUMBER_OF_SIDES, Dino.DEFAULT_HEIGHT, Dino.DEFAULT_HEIGHT);
		this.dino = new Dino(position, polygonPoints);

		this.canvas = canvas;

		cursor = null;

		// Initialize model
		origin = new Point2D.Double(0.0, 0.0);

		// Initialize rendering
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		animator.start();

		new KeyHandler(this);

		this.cloudList = new ArrayList<Cloud>();
		rand = new Random();
		// new MouseHandler(this);

		addCloud();

		try {
			for (;;) {

				TimeUnit.SECONDS.sleep(rand.nextInt(5) + 1);
				addCloud();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Point2D.Double[] generatePolygon(Point2D.Double center, int sides, int w, int h) {
		Point2D.Double[] polygonPoints = new Point2D.Double[sides];

		// generate points based off circle method
		for (int i = 0; i < sides; i++) {
			double angle = (2 * Math.PI * i) / sides;

			double x = Math.cos(angle) * w;
			double y = Math.sin(angle) * h;

			// add points to array
			polygonPoints[i] = new Point2D.Double(x + center.getX(), y + center.getY());
		}

		return polygonPoints;
	}

	// **********************************************************************
	// Override Methods (GLEventListener)
	// **********************************************************************

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
		update();
		render(drawable);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
		this.w = w;
		this.h = h;
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

	public int getCounter() {
		return counter;
	}

	public boolean isSpacePressed() {
		if(spaceIsPressed) return true;
		return false;
	}

	public void setSpacePressed(boolean state) {
		spaceIsPressed = state;
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

	public Dino getDino() {
		return dino;
	}

	public void setDino(Dino saur) {
		this.dino = saur;
	}

	public void setSuperJumpFrameLimit(int frameLimit) {
		jumpFrameLimit = frameLimit;
	}

	// **********************************************************************
	// Private Methods (Viewport)
	// **********************************************************************

	private void setProjection(GL2 gl) {
		GLU glu = new GLU();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// bottom left corner will be (0,0)
		// top right corner will be (1000,450)
		glu.gluOrtho2D(0.0f, 1000.0f, 0.0f, 450.0f);

	}
	// **********************************************************************
	// Private Methods (Rendering)
	// **********************************************************************

	private void update() {
		updateDino();
		counter++; // Counters are useful, right?
	}

	private void updateDino() {
		if(!spaceIsPressed) jumpFrameLimit = counter;
		if(counter > jumpFrameLimit) { System.out.println("super jump ready"); dino.setJumpType(2); }
		if(counter <= jumpFrameLimit) dino.setJumpType(1);
		
		//if jumping, update dino position and new polygon points
		if(dino.isJumping()) {
			double newYPosition = dino.getY() + FRAME_TIME_DELTA * dino.getJumpVelocity().getY(); //new_position = old_position + delta_time * current_velocity
			double newYVelocity = dino.getJumpVelocity().getY()  + FRAME_TIME_DELTA * gravity.y; //new_velocity = old_velocity + delta_time * gravity
			dino.setPosition(new Point2D.Double(dino.getX(), newYPosition));
			dino.setJumpVelocity(new Vector2D(dino.getJumpVelocity().getX(), newYVelocity));
			Point2D.Double[] polygonPoints = generatePolygon(dino.getPosition(), Dino.DEFAULT_NUMBER_OF_SIDES, Dino.DEFAULT_HEIGHT, Dino.DEFAULT_HEIGHT);
			dino.setPoints(polygonPoints);
		}
		
		if(dino.getPosition().y - dino.getHeight() < floorLocY) {
			dino.setY(floorLocY);
			Point2D.Double[] polygonPoints = generatePolygon(dino.getPosition(), Dino.DEFAULT_NUMBER_OF_SIDES, Dino.DEFAULT_HEIGHT, Dino.DEFAULT_HEIGHT);
			dino.setPoints(polygonPoints);
			dino.setInJumpState(false);
		}
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT); // Clear the buffer

		setProjection(gl);

		drawBackground(gl);
		drawGround(gl);
		drawCursorCoordinates(gl); // Draw some text
		drawDino(gl);

		// for (Point pos : pointsList) {
		// if (pos.getX() <= 0) {
		// pointsList.remove(pos);
		// break;
		// }
		// pos.setLocation(pos.getX() - 2, pos.getY());

		drawCloud(gl);
		// }

		for (Cloud cloud : cloudList) {
			// if (cloud.getX() <= 0) {
			// cloudList.remove(cloud);
			// break;
			// }
			//
			// cloud.setX(cloud.getX() - cloud.getSpeed());
			cloud.moveCloud();
			// cloud.addVector(new Vector2D(-1, 0));
		}

		// TODO: polish jump
		//if (dino.getInJumpState()) animateJump(gl);
	}

	// **********************************************************************
	// Private Methods (Scene)
	// **********************************************************************
	private void drawCursorCoordinates(GL2 gl) {
		if (cursor == null)
			return;

		String sx = FORMAT.format(new Double(cursor.x));
		String sy = FORMAT.format(new Double(cursor.y));
		String s = "(" + sx + "," + sy + ")";

		renderer.beginRendering(this.getWidth(), this.getHeight());
		renderer.setColor(1.0f, 1.0f, 0, 1.0f);
		renderer.draw(s, 2, 2);
		renderer.endRendering();
	}

	private void drawBackground(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);

		gl.glColor3d(0.0470588, 0.192157, 0.4);
		gl.glVertex2f(0, 0);
		gl.glVertex2f(this.getWidth(), 0);

		gl.glColor3d(0.992157, 0.490196, 0.00392157);

		gl.glVertex2f(this.getWidth(), this.getHeight());

		gl.glVertex2f(0, this.getHeight());

		gl.glEnd();
	}

	private void drawGround(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);

		int yo = 2 * (this.getHeight() / 3); // 2/3 the height

		gl.glColor3d(0.392157, 0.247059, 0.0470588f);
		gl.glVertex2i(0, yo);
		gl.glVertex2i(this.getWidth(), yo);
		gl.glVertex2i(this.getWidth(), this.getHeight());
		gl.glVertex2i(0, this.getHeight());

		gl.glColor3f(0, 0, 0);

		gl.glEnd();

		int offsetX = 0;

		for (int i = 0; i < 27; i++) {
			offsetX = ((i * 20) - counter % 20) * 2;
			// System.out.println(offsetX);
			drawGroundLine(gl, offsetX, yo);
		}
	}

	private void drawGroundLine(GL2 gl, int dx, int yo) {
		gl.glBegin(GL2.GL_QUADS);

		gl.glColor3d(0.278431, 0.392157, 0.0470588);
		gl.glVertex2i(dx + 30, yo);
		gl.glVertex2i(dx + (-20), this.getHeight());
		gl.glVertex2i(dx + (-10), this.getHeight());
		gl.glVertex2i(dx + 40, yo);

		gl.glEnd();
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public Component getComponent() {
		// TODO Auto-generated method stub
		return (Component) canvas;
	}

	private void drawDino(GL2 gl) {

		// TODO: make this a dino
		gl.glBegin(GL2.GL_POLYGON);

		gl.glColor3d(0.403922, 0.560784, 0);

		for (Point2D.Double point : dino.getPoints()) {
			gl.glVertex2d(point.getX(), point.getY());
		}

		gl.glEnd();

	}

	public void addCloud() {
		Random rand = new Random();
		double x = 1020.0;
		double y = (double) (rand.nextInt(300 + 1 - 20) + 20);

		Point2D.Double position = new Point2D.Double(x, y);
		Vector2D velocity = new Vector2D(-(rand.nextInt(5) + 1), 0);
		Point2D.Double[] cloudPoints = generatePolygon(position, 32, 30, 20);
		cloudList.add(new Cloud(position, velocity, cloudPoints));
	}

	// public void drawCloud(GL2 gl, Point pos) {
	public void drawCloud(GL2 gl) {

		for (Cloud cloud : cloudList) {

			gl.glBegin(GL2.GL_POLYGON);

			gl.glColor3d(0.662745, 0.662745, 0.662745);
			for (Point2D.Double point : cloud.getCloudPoints()) {
				gl.glVertex2d(point.getX(), point.getY());
			}

			gl.glEnd();
		}

		// // gl.glVertex2d(pos.getX(), pos.getY());
		// // gl.glVertex2d(pos.getX(), pos.getY() + 10);
		// // gl.glVertex2d(pos.getX() + 10, pos.getY() + 10);
		// // gl.glVertex2d(pos.getX() + 10, pos.getY());
		//
		// drawEllipse(gl, (int) pos.getX() + 28, (int) pos.getY() - 5);
		// drawEllipse(gl, (int) pos.getX() + 10, (int) pos.getY() + 5);
		// drawEllipse(gl, (int) pos.getX() + 13, (int) pos.getY() - 13);
		// drawEllipse(gl, (int) pos.getX(), (int) pos.getY());

	}

	// public void drawEllipse(GL2 gl, int xX, int yY) {
	// for (int i = 0; i < 32; i++) {
	// double angle = (2 * Math.PI * i) / 32;
	// double x = Math.cos(angle) * 20;
	// double y = Math.sin(angle) * 10;
	// gl.glVertex2d(xX + x, yY + y);
	// }
	// }

	// public void animateJump(GL2 gl) {
	// 	if (shortJump == 1 && jumpModifier < 0) {
	// 		// System.out.println("shorty");
	// 		jumpModifier *= -1;
	// 		shortJump = 2;
	// 	}

	// 	if (this.dino.getY() < 190 && jumpModifier < 0) {
	// 		// System.out.println("down");
	// 		jumpModifier *= -1;
	// 	}

	// 	if (this.dino.getY() > 360) {
	// 		this.dino.setInJumpState(false);
	// 		shortJump = 0;
	// 		this.dino.setY(360);
	// 		jumpModifier = -10;
	// 	}

	// 	this.dino.setY(this.dino.getY() + jumpModifier);

	// 	// try {
	// 	// // collision bitch
	// 	// for (Cloud cloud : cloudList) {
	// 	// boolean isAbove = (this.dino.getY() < cloud.getY() + 50);
	// 	// boolean isBetweenL = (this.dino.getX() < cloud.getX());
	// 	// boolean isBetweenR = (this.dino.getX() + this.dino.getWidth() >
	// 	// cloud.getXOffset());
	// 	//
	// 	// if (isAbove && isBetweenL && isBetweenR) {
	// 	// cloudList.remove(cloud);
	// 	// // System.out.println("no");
	// 	// }
	// 	//
	// 	// }
	// 	// } catch (Exception e) {
	// 	// // lol
	// 	// }

	// 	// collision

	// 	// if (player.getY() + player.getHeight() > jumpHeightLimit)
	// 	// jumpModifier *= -1;
	// 	//
	// 	// if (player.getY() + 2 * jumpModifier < 0.0f) {
	// 	// player.setJumpingState(false);
	// 	// player.setY(0.0f);
	// 	// jumpModifier *= -1;
	// 	// }

	// 	// player.setY(player.getY() + jumpModifier);
	// 	// System.out.println(player.getY());

	// }

}
