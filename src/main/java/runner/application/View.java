package runner.application;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
	private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final GLJPanel canvas;

	// dino model
	private Dino saur;

	private int counter = 0; // Just an animation counter
	private int w; // Canvas width
	private int h; // Canvas height

	// private final KeyHandler keyHandler;
	// private final MouseHandler mouseHandler;

	private final FPSAnimator animator;

	private TextRenderer renderer;

	private Point2D.Double origin; // Current origin coordinates
	private Point2D.Double cursor; // Current cursor coordinates

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************

	public View(GLJPanel canvas) {

		this.saur = new Dino(80, 360);
		this.canvas = canvas;
		cursor = null;

		// Initialize model
		origin = new Point2D.Double(0.0, 0.0);

		// Initialize rendering
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		animator.start();

		new KeyHandler(this);
		pointsList = new ArrayList<Point>();
		rand = new Random();
		// new MouseHandler(this);

		addCloud();
		try {
			for (int i = 0; i < 20; i++) {
				
				TimeUnit.SECONDS.sleep(rand.nextInt(5) + 1);
				addCloud();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		// if (player.isJumping())
		// animateJump(gl);

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
		return saur;
	}

	public void setDino(Dino saur) {
		this.saur = saur;
	}

	// **********************************************************************
	// Private Methods (Viewport)
	// **********************************************************************

	private void setProjection(GL2 gl) {
		GLU glu = new GLU();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		// upper left corner will be (0,0)
		// bottom right corner will be (1000,450)
		glu.gluOrtho2D(0.0f, 1000.0f, 450.0f, 0.0f);
	}
	// **********************************************************************
	// Private Methods (Rendering)
	// **********************************************************************

	private void update() {
		counter++; // Counters are useful, right?
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT); // Clear the buffer

		setProjection(gl);

		drawBackground(gl);
		drawGround(gl);
		drawCursorCoordinates(gl); // Draw some text
		drawDino(gl);

		for (Point pos : pointsList) {
			pos.setLocation(pos.getX() - 3, pos.getY());
			drawCloud(gl, pos);

		}

		// System.out.println(pointsList.size());
		if (saur.getInJumpState())
			animateJump(gl);

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
		Point pos = saur.getPos();
		gl.glBegin(GL2.GL_POLYGON);
		gl.glColor3d(0.403922, 0.560784, 0);

		gl.glVertex2d(pos.getX(), pos.getY());
		gl.glVertex2d(pos.getX(), pos.getY() + 40);
		gl.glVertex2d(pos.getX() + 50, pos.getY() + 40);
		gl.glVertex2d(pos.getX() + 50, pos.getY());

		gl.glEnd();

	}

	public ArrayList<Point> pointsList;
	public Random rand;
	int jumpModifier = -10;
	// Used if spacebar is let go early
	int shortJump = 0;

	public void addCloud() {
		int x = 1020;
		int y = rand.nextInt(300 + 1 - 20) + 20;

		pointsList.add(new Point(x, y));
	}

	public void drawCloud(GL2 gl, Point pos) {
		gl.glBegin(GL2.GL_POLYGON);
		gl.glColor3d(0.662745, 0.662745, 0.662745);

		// gl.glVertex2d(pos.getX(), pos.getY());
		// gl.glVertex2d(pos.getX(), pos.getY() + 10);
		// gl.glVertex2d(pos.getX() + 10, pos.getY() + 10);
		// gl.glVertex2d(pos.getX() + 10, pos.getY());

		drawEllipse(gl, (int) pos.getX() + 28, (int) pos.getY() - 5);
		drawEllipse(gl, (int) pos.getX() + 10, (int) pos.getY() + 5);
		drawEllipse(gl, (int) pos.getX() + 13, (int) pos.getY() - 13);
		drawEllipse(gl, (int) pos.getX(), (int) pos.getY());

		gl.glEnd();
	}

	public void drawEllipse(GL2 gl, int xX, int yY) {
		for (int i = 0; i < 32; i++) {
			double angle = (2 * Math.PI * i) / 32;
			double x = Math.cos(angle) * 20;
			double y = Math.sin(angle) * 10;
			gl.glVertex2d(xX + x, yY + y);
		}
	}

	int jumpModifier = -15;

	public void animateJump(GL2 gl) {
		
		if(shortJump == 1 && jumpModifier < 0){
			System.out.println("shorty");
			jumpModifier *= -1;
			shortJump = 2;
		}
		
		if (saur.getY() < 190 && jumpModifier < 0) {
				System.out.println("down");
				jumpModifier *= -1;
		}
		
		if (saur.getY() > 360) {
			saur.setInJumpState(false);
			shortJump = 0;
			saur.setY(360);
			jumpModifier = -15;
		}

		saur.setY(saur.getY() + jumpModifier);

		// if (player.getY() + player.getHeight() > jumpHeightLimit)
		// jumpModifier *= -1;
		//
		// if (player.getY() + 2 * jumpModifier < 0.0f) {
		// player.setJumpingState(false);
		// player.setY(0.0f);
		// jumpModifier *= -1;
		// }

		// player.setY(player.getY() + jumpModifier);
		// System.out.println(player.getY());

	}

}
