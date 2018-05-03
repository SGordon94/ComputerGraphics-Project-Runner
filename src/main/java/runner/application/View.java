package runner.application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
import com.jogamp.opengl.util.texture.Texture;

import runner.application.obstacles.Cloud;
import runner.application.obstacles.Fireball;
import runner.application.obstacles.Tree;

public class View implements GLEventListener {

	// **********************************************************************
	// Public Class Members
	// **********************************************************************

	public static final GLU GLU = new GLU();
	public static final GLUT GLUT = new GLUT();
	public static final Random RANDOM = new Random();
	public static final int DEFAULT_FRAMES_PER_SECOND = 60;
	public static final double FRAME_TIME_DELTA = 5.0 * 1.0 / (double) DEFAULT_FRAMES_PER_SECOND;
	private TextRenderer renderer;
	// private static final DecimalFormat FORMAT = new DecimalFormat("0.000");

	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final GLJPanel canvas;

	// dino model
	private Dino dino;
	public Random rand;

	// Just an animation counter
	private int counter = 0;

	// Limit for super jump
	private int jumpFrameLimit = 0;
	private boolean spaceIsPressed = false;

	// canvas dimension
	private int w;
	private int h;

	// font size
	private int fontSize = 25;

	// score
	private int score;

	// ground location for dino
	private double floorLocY = 80.0;

	// gravity vector
	private Vector2D gravity = new Vector2D(0.0, -15.0);

	private final FPSAnimator animator;

	// ArrayLists to hold all of the images for the parallax backgrounds
	private ArrayList<Background> spooky;
	private ArrayList<Background> city;
	private ArrayList<Background> mountain;

	// Used to decide which background to draw
	private int currentBG = 1;

	// Horizontal speed for background scrolling
	private final double hspeed = 4;
	// Different speeds for background layers
	private final double layerSpeeds[] = { 1, 2, 3, 3.2, 3.5, 3.7, 3.8, 3.9 };

	// obstacle lists
	ArrayList<Cloud> cloudList;
	ArrayList<Tree> treeList;
	List<Fireball> fireballList;

	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public View(GLJPanel canvas) {

		// init dino model info
		Point2D.Double position = new Point2D.Double(200.0, floorLocY + Dino.DEFAULT_HEIGHT);

		// generate dino's bounding box
		Point2D.Double[] polygonPoints = generateDinoPoints(position, Dino.DEFAULT_WIDTH, Dino.DEFAULT_HEIGHT);
		
		// init backgrounds
		spooky = new ArrayList<Background>();
		city = new ArrayList<Background>();
		mountain = new ArrayList<Background>();

		// init dino
		this.dino = new Dino(position, polygonPoints);

		// sets initial sprite to run0
		dino.setCurrentSprite("run0");

		// Initialize model
		this.canvas = canvas;

		// Initialize rendering
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		animator.start();

		// init key handler
		new KeyHandler(this);

		// init random object
		rand = new Random();

		// init obstacle lists
		this.cloudList = new ArrayList<Cloud>();
		this.treeList = new ArrayList<Tree>();
		this.fireballList = new ArrayList<Fireball>();

		// add first obstacle to view
		addTree();
		addFireball();

		// TODO: GAME LOOP
		try {
			for (;;) {

				TimeUnit.SECONDS.sleep(rand.nextInt(7) + 1);
				// addCloud();
				addFireball();
				addTree();
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

		// Init the "spooky" parallax backdrop
		spooky.add(new Background(new ImageResource("sprites/parallax/snowGround.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/littleTrees.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/bigTrees.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/bigTrees2.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/bigTrees3.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/stars.png"), this.getWidth()));
		spooky.add(new Background(new ImageResource("sprites/parallax/Moon.png"), this.getWidth()));
		
		// City
		city.add(new Background(new ImageResource("sprites/parallax/cityGround.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityTrees.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityColorBuildings.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityFarBuildings1.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityFarBuildings2.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityFarBuildings3.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/citySun.png"), this.getWidth()));
		city.add(new Background(new ImageResource("sprites/parallax/cityBG.png"), this.getWidth()));
		
		// Mountain
		mountain.add(new Background(new ImageResource("sprites/parallax/mountainGround.png"), this.getWidth()));
		mountain.add(new Background(new ImageResource("sprites/parallax/mountainTrees.png"), this.getWidth()));
		mountain.add(new Background(new ImageResource("sprites/parallax/mountains1.png"), this.getWidth()));
		mountain.add(new Background(new ImageResource("sprites/parallax/mountainClouds.png"), this.getWidth()));
		mountain.add(new Background(new ImageResource("sprites/parallax/mountainBG.png"), this.getWidth()));
		

		/*groundtexture = new Background(new ImageResource("sprites/parallax/snowGround.png"), this.getWidth());
		background1 = new Background(new ImageResource("sprites/parallax/littleTrees.png"), this.getWidth());
		background2 = new Background(new ImageResource("sprites/parallax/bigTrees.png"), this.getWidth());
		background3 = new Background(new ImageResource("sprites/parallax/bigTrees2.png"), this.getWidth());
		background4 = new Background(new ImageResource("sprites/parallax/bigTrees3.png"), this.getWidth());
		background5 = new Background(new ImageResource("sprites/parallax/Moon.png"), this.getWidth());*/

		score = 0;
		renderer = new TextRenderer(new Font("Monospaced", Font.PLAIN, fontSize), true, true);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		renderer = null;
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
		return spaceIsPressed ? true : false;
	}

	public void setSpacePressed(boolean state) {
		spaceIsPressed = state;
	}

	public Dino getDino() {
		return dino;
	}

	public void setDino(Dino saur) {
		this.dino = saur;
	}
	
	public void incrementCurrentBG() {
		if(currentBG < 3) 
			currentBG++;
		else
			currentBG = 1;
	}
	
	public void decrementCurrentBG() {
		if(currentBG > 1) 
			currentBG--;
		else
			currentBG = 3;
	}

	// set frame limit for super jump
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

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.setSwapInterval(100);

		// Clear the buffer
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

		// Updates window projection
		setProjection(gl);

		// TODO: BACKGROUND PLEASE
		// drawBackground(gl, new Color(0,0,0));


		// Decide which parallax background to display
		switch (currentBG) {
		// Draw the spooky background
		case 1:					
			for (int i = spooky.size()-1; i > 0; i--) {
				if (i < 2) {
					drawGroundTexture(gl, spooky.get(i), layerSpeeds[i], this.getHeight() / 4.5);
				} else {
					drawSkyTexture(gl, spooky.get(i), layerSpeeds[i], this.getHeight() / 4.5);
				}

			}
			drawGroundTexture(gl, spooky.get(0), layerSpeeds[0], -this.getHeight() / 10);
			break;
		// Draw the city background
		case 2:		
			for (int i = city.size()-1; i > 0; i--) {
					drawSkyTexture(gl, city.get(i), layerSpeeds[i], (this.getHeight() / 4.5));	

			}
			drawGroundTexture(gl, city.get(0), layerSpeeds[0], -this.getHeight() / 10);
			break;
		// Draw the mountain background
		case 3:					
			for (int i = mountain.size()-1; i > 0; i--) {
				if (i < 2) {
					drawGroundTexture(gl, mountain.get(i), layerSpeeds[i], this.getHeight() / 4.5);
				} else {
					drawSkyTexture(gl, mountain.get(i), layerSpeeds[i], this.getHeight() / 4.5);
				}

			}
			drawGroundTexture(gl, mountain.get(0), layerSpeeds[0], -this.getHeight() / 10);
			break;
		default:
			break;
		}

		// draw dino character
		drawDino(gl);

		// draw / move fireballs
		drawFireBalls(gl);
		moveFireBalls();

		// draw / move trees
		drawTrees(gl);
		moveTrees();

		// draw the score
		drawScore(drawable);

	}

	// generate fractal tree
	public List<Point2D.Double> generateTree(double x1, double y1, double angle, double depth, double scale,
			List<Point2D.Double> test) {

		// recursive base case - return when depth is 0
		if (depth == 0)
			return null;

		// calculate x2,y2 points relative to given x1, y1
		double x2 = x1 + (Math.cos(Math.toRadians(angle)) * depth * scale);
		double y2 = y1 + (Math.sin(Math.toRadians(angle)) * depth * scale);

		// add pair of points to list
		test.add(new Point2D.Double(x1, y1));
		test.add(new Point2D.Double(x2, y2));

		// recursively generate more points but at a higher depth
		generateTree(x2, y2, angle - 20, depth - 1, scale, test);
		generateTree(x2, y2, angle + 20, depth - 1, scale, test);

		// return list of generated points
		return test;
	}

	// convert list to array
	public Point2D.Double[] generateTreePoints(List<Point2D.Double> test) {
		Point2D.Double[] ok = test.toArray(new Point2D.Double[test.size()]);
		return ok;
	}

	// **********************************************************************
	// Private Methods (Dino Logic)
	// **********************************************************************

	private Point2D.Double[] generateDinoPoints(Point2D.Double center, int w, int h) {
		// generate bounding box for dino
		Point2D.Double[] points = new Point2D.Double[4];
		points[0] = new Point2D.Double(center.x - w / 2, center.y - h / 2);
		points[1] = new Point2D.Double(center.x - w / 2, center.y + h / 2);
		points[2] = new Point2D.Double(center.x + w / 2, center.y + h / 2);
		points[3] = new Point2D.Double(center.x + w / 2, center.y - h / 2);
		return points;
	}

	private void updateDino() {
		if (!spaceIsPressed)
			jumpFrameLimit = counter; // as long as space is not pressed update jump frame limit to be equal to
										// current frame count
		if (counter > jumpFrameLimit) {
			// when the current frame count passes the frame limit for the super jump, set
			// dino to super jump mode and update sprite to crouch
			dino.setJumpType(2);
			dino.setWidth(46);
			dino.setHeight(44);
			Point2D.Double[] polygonPoints = generateDinoPoints(dino.getPosition(), dino.getWidth(), dino.getHeight());
			dino.setPoints(polygonPoints);
			// current sprite determines which crouch frame to start on
			String sprite = dino.getCurrentSprite();
			if (sprite == "run0")
				dino.setCurrentSprite("crouch0");
			else if (sprite == "run1")
				dino.setCurrentSprite("crouch1");
		}
		if (counter <= jumpFrameLimit)
			dino.setJumpType(1); // reset to normal jump

		// if jumping, update dino position and new polygon points
		if (dino.isJumping()) {
			// new_position = old_position + delta_time * current_velocity
			// new_velocity = old_velocity + delta_time * gravity
			double newYPosition = dino.getY() + FRAME_TIME_DELTA * dino.getJumpVelocity().getY();
			double newYVelocity = dino.getJumpVelocity().getY() + FRAME_TIME_DELTA * gravity.y;
			dino.setPosition(new Point2D.Double(dino.getX(), newYPosition));
			dino.setJumpVelocity(new Vector2D(dino.getJumpVelocity().getX(), newYVelocity));
			dino.setWidth(Dino.DEFAULT_WIDTH);
			dino.setHeight(Dino.DEFAULT_HEIGHT);
			Point2D.Double[] polygonPoints = generateDinoPoints(dino.getPosition(), dino.getWidth(), dino.getHeight());
			dino.setPoints(polygonPoints);
			dino.setCurrentSprite("jump");
		}

		// if dino goes below floor location then stop dino and set dino to running
		if (dino.getPosition().y - dino.getHeight() < floorLocY) {
			dino.setY(floorLocY + dino.getHeight());
			dino.setWidth(Dino.DEFAULT_WIDTH);
			dino.setHeight(Dino.DEFAULT_HEIGHT);
			Point2D.Double[] polygonPoints = generateDinoPoints(dino.getPosition(), dino.getWidth(), dino.getHeight());
			dino.setPoints(polygonPoints);
			dino.setInJumpState(false);
			dino.setCurrentSprite("run0");
		}

		// update dino sprite every 5 frames
		// Change mod from 25 to 5 to speed up dino run
		if (counter % 5 == 0) {
			String currentSprite = dino.getCurrentSprite();
			if (currentSprite == "crouch0")
				dino.setCurrentSprite("crouch1");
			if (currentSprite == "crouch1")
				dino.setCurrentSprite("crouch0");
			if (currentSprite == "run0")
				dino.setCurrentSprite("run1");
			if (currentSprite == "run1")
				dino.setCurrentSprite("run0");
		}
	}

	// **********************************************************************
	// Private Methods (Scene)
	// **********************************************************************

	private void drawBackground(GL2 gl, Color color) {
		gl.glBegin(GL2.GL_POLYGON);

		double red = color.getRed()/255;
		double green = color.getGreen()/255;
		double blue = color.getBlue()/255;
		
		gl.glColor3d(red, green, blue);
		gl.glVertex2f(0, 0);
		gl.glVertex2f(this.getWidth(), 0);
		gl.glVertex2f(this.getWidth(), this.getHeight());
		gl.glVertex2f(0, this.getHeight());

		gl.glEnd();
	}

	private void drawGround(GL2 gl) {
		gl.glBegin(GL2.GL_POLYGON);

		int yo = 0; // 2/3 the height

		gl.glColor3d(0.22, 0.36, 0.22);
		gl.glVertex2i(0, yo);
		gl.glVertex2i(this.getWidth(), yo);
		gl.glVertex2i(this.getWidth(), this.getHeight() / 3);
		gl.glVertex2i(0, this.getHeight() / 3);

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
		gl.glVertex2i(dx + (-20), this.getHeight() / 3);
		gl.glVertex2i(dx + (-10), this.getHeight() / 3);
		gl.glVertex2i(dx + 40, yo);

		gl.glEnd();
	}

	private void drawScore(GLAutoDrawable drawable) {

		// if score is 999999(max) just keep it at max
		// score is calculated using time passed based on the counter/75
		// 75 was chosen just so score doesn't increase too quickly
		score = (score < 999999) ? (int) Math.ceil(counter / 75) : score;

		// Use time passed AKA counter to calculate the score
		String s = "Score: " + score;

		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(1.0f, 1.0f, 0, 1.0f);
		// 7 is the length of the string 'Score: '
		// 6 is the max length of score 999999
		// 16 was found from trial and error starting from 25(font size) and gradually
		// decreasing
		renderer.draw(s, this.getWidth() - (7 + 6) * 16, this.getHeight() - 25);
		renderer.endRendering();
	}

	private void drawDino(GL2 gl) {
		// enable blending to allow for png transparency (for texture drawing only)
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// enable texture drawing
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

		// get texture (the current dino sprite) and bind it to set it as the current
		// texture to draw
		Texture texture = dino.getCurrentSpriteImage().getTexture();
		if (texture != null) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
		}

		gl.glBegin(GL2.GL_QUADS); // use quads to draw bounding box

		gl.glColor3d(0.403922, 0.560784, 0);

		Point2D.Double[] points = dino.getPoints();

		// texture coordinates are relative to texture's bounding box and takes values
		// between 0.0 and 1.0
		// so to use full bounding box width and height, texture coordinates must span 0
		// and 1
		// for sprite facing left, texture coordinate starts in top right and goes
		// counter-clockwise so that sprite is facing right in program
		gl.glTexCoord2d(1, 1);
		gl.glVertex2d(points[0].getX(), points[0].getY());

		gl.glTexCoord2d(1, 0);
		gl.glVertex2d(points[1].getX(), points[1].getY());

		gl.glTexCoord2d(0, 0);
		gl.glVertex2d(points[2].getX(), points[2].getY());

		gl.glTexCoord2d(0, 1);
		gl.glVertex2d(points[3].getX(), points[3].getY());

		gl.glEnd();
		gl.glFlush();
		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); // unbind any textures
		gl.glDisable(GL2.GL_TEXTURE_2D); // disable texture drawing
		gl.glDisable(GL2.GL_BLEND); // disable blending
	}

	private void drawSkyTexture(GL2 gl, Background ground, double layer, double offsetY) {

		int yo = 0;

		int width = ground.GetImg().getTexture().getImageWidth();
		// int height = ground.GetImg().getTexture().getImageHeight();
		int height = (int) (this.getHeight());

		// enable blending to allow for png transparency (for texture drawing only)
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// get texture to draw
		Texture texture = ground.GetImg().getTexture();
		if (texture != null) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
		}

		// enable texture drawing
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

		for (int i = 0; i < ground.GetX().size(); i++) {

			gl.glBegin(GL2.GL_QUADS);

			gl.glColor3d(1, 1, 1);

			// texture coordinates are relative to texture's bounding box and takes values
			// between 0.0 and 1.0
			// so to use full bounding box width and height, texture coordinates must span 0
			// and 1
			// for sprite facing left, texture coordinate starts in top right and goes
			// counter-clockwise so that sprite is facing right in program
			gl.glTexCoord2d(0, 1);
			gl.glVertex2d(ground.GetX().get(i), yo + offsetY);

			gl.glTexCoord2d(1, 1);
			gl.glVertex2d(ground.GetX().get(i) + width, yo + offsetY);

			gl.glTexCoord2d(1, 0);
			gl.glVertex2d(ground.GetX().get(i) + width, height);

			gl.glTexCoord2d(0, 0);
			gl.glVertex2d(ground.GetX().get(i), height);

			if (ground.GetX().get(i) < width * -1) {
				if (ground.GetLast() == ground.GetX().size() - 1) {
					ground.GetX().set(i, ground.GetX().get(ground.GetLast()) + (double) width - (hspeed - layer));
				} else {
					ground.GetX().set(i, ground.GetX().get(ground.GetLast()) + (double) width);
				}
				ground.SetLast(i);
				System.out.println("Last: " + ground.GetLast());
			} else {
				ground.GetX().set(i, ground.GetX().get(i) - (hspeed - layer));
			}

			gl.glEnd();
			// gl.glFlush();

		}

		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); // unbind any textures
		gl.glDisable(GL2.GL_TEXTURE_2D); // disable texture drawing
		gl.glDisable(GL2.GL_BLEND); // disable blending

	}

	private void drawGroundTexture(GL2 gl, Background ground, double layer, double offsetY) {

		int yo = 0;

		int width = ground.GetImg().getTexture().getImageWidth();
		// int height = ground.GetImg().getTexture().getImageHeight();
		int height = this.getHeight() / 3;

		// enable blending to allow for png transparency (for texture drawing only)
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

		// get texture to draw
		Texture texture = ground.GetImg().getTexture();
		if (texture != null) {
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
		}

		// enable texture drawing
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

		for (int i = 0; i < ground.GetX().size(); i++) {

			gl.glBegin(GL2.GL_QUADS);

			gl.glColor3d(1, 1, 1);

			// texture coordinates are relative to texture's bounding box and takes values
			// between 0.0 and 1.0
			// so to use full bounding box width and height, texture coordinates must span 0
			// and 1
			// for sprite facing left, texture coordinate starts in top right and goes
			// counter-clockwise so that sprite is facing right in program
			gl.glTexCoord2d(0, 1);
			gl.glVertex2d(ground.GetX().get(i), yo + offsetY);

			gl.glTexCoord2d(1, 1);
			gl.glVertex2d(ground.GetX().get(i) + width, yo + offsetY);

			gl.glTexCoord2d(1, 0);
			gl.glVertex2d(ground.GetX().get(i) + width, height + offsetY);

			gl.glTexCoord2d(0, 0);
			gl.glVertex2d(ground.GetX().get(i), height + offsetY);

			if (ground.GetX().get(i) < width * -1) {
				if (ground.GetLast() == ground.GetX().size() - 1) {
					ground.GetX().set(i, ground.GetX().get(ground.GetLast()) + (double) width - (hspeed - layer));
				} else {
					ground.GetX().set(i, ground.GetX().get(ground.GetLast()) + (double) width);
				}
				ground.SetLast(i);
				System.out.println("Last: " + ground.GetLast());
			} else {
				ground.GetX().set(i, ground.GetX().get(i) - (hspeed - layer));
			}

			gl.glEnd();
			// gl.glFlush();

		}

		gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); // unbind any textures
		gl.glDisable(GL2.GL_TEXTURE_2D); // disable texture drawing
		gl.glDisable(GL2.GL_BLEND); // disable blending

	}

	private void drawFireBalls(GL2 gl) {

		ListIterator<Fireball> iterator = fireballList.listIterator();

		while (iterator.hasNext()) {
			Fireball fiyaball = iterator.next();
			// enable blending to allow for png transparency (for texture drawing only)
			gl.glEnable(GL2.GL_BLEND);
			gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

			// enable texture drawing
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
			gl.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

			// get texture (the current dino sprite) and bind it to set it as the current
			// texture to draw
			Texture texture = fiyaball.getSprite().getTexture();
			if (texture != null) {
				gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
			}

			gl.glBegin(GL2.GL_QUADS); // use quads to draw bounding box

			// gl.glColor3d(0.403922, 0.560784, 0);
			gl.glColor3d(1, 1, 1);

			Point2D.Double[] points = fiyaball.getPoints();

			// texture coordinates are relative to texture's bounding box and takes values
			// between 0.0 and 1.0
			// so to use full bounding box width and height, texture coordinates must span 0
			// and 1
			// for sprite facing left, texture coordinate starts in top right and goes
			// counter-clockwise so that sprite is facing right in program
			gl.glTexCoord2d(1, 1);
			gl.glVertex2d(points[0].getX(), points[0].getY());

			gl.glTexCoord2d(1, 0);
			gl.glVertex2d(points[1].getX(), points[1].getY());

			gl.glTexCoord2d(0, 0);
			gl.glVertex2d(points[2].getX(), points[2].getY());

			gl.glTexCoord2d(0, 1);
			gl.glVertex2d(points[3].getX(), points[3].getY());

			gl.glEnd();
			gl.glFlush();
			gl.glBindTexture(GL2.GL_TEXTURE_2D, 0); // unbind any textures
			gl.glDisable(GL2.GL_TEXTURE_2D); // disable texture drawing
			gl.glDisable(GL2.GL_BLEND); // disable blending
		}

	}

	private void drawTrees(GL2 gl) {
		for (Tree tree : treeList) {
			Point2D.Double[] please = tree.getPoints();
			gl.glLineWidth((float) 2.0);
			gl.glBegin(GL2.GL_LINES);
			gl.glColor3d(0.392157, 0.247059, 0.0470588f);
			for (int i = 0; i < please.length; i = i + 2) {
				// 70 is chosen to change the height closer to the player model
				gl.glVertex2d(please[i].getX(), please[i].getY() + 70);
				gl.glVertex2d(please[(i + 1) % please.length].getX(), please[(i + 1) % please.length].getY() + 70);

			}
			gl.glEnd();
		}
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************
	public Component getComponent() {
		// TODO Auto-generated method stub
		return (Component) canvas;
	}

	// **********************************************************************
	// Public Methods / add obstacles to view
	// **********************************************************************

	/* add fireball to view */
	public void addFireball() {
		// init random object
		rand = new Random();

		// initial fireball position (outside the view)
		// with random height position
		double y = (double) (rand.nextInt(300 + 1 - 20) + 20);
		Point2D.Double fiya = new Point2D.Double(500.0, y);

		// generate random velocity
		Vector2D velocity = new Vector2D(-(rand.nextInt(4) + 1), 0);

		// generate fireball points
		Point2D.Double[] points = new Point2D.Double[4];
		points[0] = new Point2D.Double(1100, y + 20);
		points[1] = new Point2D.Double(1060, y + 20);
		points[2] = new Point2D.Double(1060, y - 20);
		points[3] = new Point2D.Double(1100, y - 20);

		// init list iterator
		ListIterator<Fireball> iterator = fireballList.listIterator();

		// add new fire ball to list
		iterator.add(new Fireball(fiya, velocity, points));
	}

	public void addTree() {
		List<Point2D.Double> treePoints = new ArrayList<Point2D.Double>();
		Point2D.Double[] treePointsArray;

		Point2D.Double position = new Point2D.Double(1100, 35.0);

		// TODO: parameterize tree generation
		treePointsArray = generateTreePoints(generateTree(position.getX(), position.getY(), 90, 9, 2.0, treePoints));

		// init list iterator
		ListIterator<Tree> iterator = treeList.listIterator();

		// add new fire ball to list
		iterator.add(new Tree(position, new Vector2D(-3, 0), treePointsArray));
	}

	// **********************************************************************
	// Public Methods / obstacle movement
	// **********************************************************************

	public void moveFireBalls() {
		// init list iterator
		ListIterator<Fireball> iterator = fireballList.listIterator();

		// iterate through fireball list
		while (iterator.hasNext()) {
			Fireball fiyaBall = iterator.next();

			// move fireball
			fiyaBall.moveFireball();

			// remove fireballs that are off the view
			if (fiyaBall.getPosition().getX() <= -700) {
				iterator.remove();
			}

			// TODO: collision end game
			if (dino.collides(fiyaBall.getPoints())) {

				// remove all obstacles
				fireballList = new ArrayList<Fireball>();
				treeList = new ArrayList<Tree>();
				this.counter = 0;
			}
		}
	}

	public void moveTrees() {
		// init list iterator
		ListIterator<Tree> iterator = treeList.listIterator();

		// iterator through tree list
		while (iterator.hasNext()) {
			Tree tree = iterator.next();

			// move tree
			tree.moveTree();

			// TODO: collision end game
			if (dino.collides(tree.getPoints())) {

				// remove all obstacles
				fireballList = new ArrayList<Fireball>();
				treeList = new ArrayList<Tree>();
				this.counter = 0;
			}
		}
	}
}
