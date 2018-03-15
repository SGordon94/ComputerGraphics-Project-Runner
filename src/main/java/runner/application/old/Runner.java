//package runner.application;
//
//import java.awt.Font;
//import java.awt.geom.Rectangle2D;
//import javax.media.opengl.GL;
//import javax.media.opengl.GL2;
//import javax.media.opengl.GLAutoDrawable;
//import javax.media.opengl.GLCapabilities;
//import javax.media.opengl.GLEventListener;
//import javax.media.opengl.GLProfile;
//import javax.media.opengl.awt.GLJPanel;
//import javax.media.opengl.glu.GLU;
//import javax.swing.JFrame;
//// import com.jogamp.opengl.awt.*;
//// import com.jogamp.opengl.glu.*;
//import com.jogamp.opengl.util.FPSAnimator;
//import com.jogamp.opengl.util.awt.TextRenderer;
//
//public class Runner extends JFrame implements GLEventListener {
//	private static final long serialVersionUID = 1L;
//	private static Runner instance = null;
//	private final GLJPanel canvas;
//	private final int width = 600;
//	private final int height = 600;
//	private final KeyHandler keyHandler;
//
//	private final Player player;
//	private int counter = 0;
//	private float jumpHeightLimit = 0.5f;
//	private float jumpModifier = 0.04f;
//	private FPSAnimator animator;
//	private TextRenderer textRenderer;
//
//	private boolean frameCounterOn = true;
//
//	public Runner() {
//		super("Runner");
//		GLProfile profile = GLProfile.getDefault();
//		GLCapabilities capabilities = new GLCapabilities(profile);
//		canvas = new GLJPanel(capabilities);
//		canvas.addGLEventListener(this);
//		keyHandler = new KeyHandler(this);
//		player = new Player(this);
//		instance = this;
//
//		this.setName("Runner");
//		this.getContentPane().add(canvas);
//		this.setSize(width, height);
//		this.setLocationRelativeTo(null);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setVisible(true);
//		this.setResizable(false);
//		canvas.requestFocusInWindow();
//	}
//
//	public void start() {
//		// starts game
//		System.out.println("Starting game");
//		animator = new FPSAnimator(this.getCanvas(), 60);
//		animator.start();
//	}
//
//	public Runner getInstance() {
//		return instance;
//	}
//
//	public int getWidth() {
//		return width;
//	}
//
//	public int getHeight() {
//		return height;
//	}
//
//	public int getCount() {
//		return counter;
//	}
//
//	public GLJPanel getCanvas() {
//		return (GLJPanel) canvas;
//	}
//
//	public Player getPlayer() {
//		return (Player) player;
//	}
//
//	private void updateProjection(GLAutoDrawable drawable) {
//		GL2 gl = drawable.getGL().getGL2();
//		GLU glu = new GLU();
//
//		// float xmin = (float) (0.0 - 1.0);
//		// float xmax = (float) (0.0 + 1.0);
//		// float ymin = (float) (0.0 - 1.0);
//		// float ymax = (float) (0.0 + 1.0);
//		//
//		// gl.glMatrixMode(GL2.GL_PROJECTION); // Prepare for matrix xform
//		// gl.glLoadIdentity(); // Set to identity matrix
//		// glu.gluOrtho2D(xmin, xmax, ymin, ymax); // 2D translate and scale
//		
//		gl.glMatrixMode(GL2.GL_PROJECTION);
//		
//		gl.glLoadIdentity();
//		
//		glu.gluOrtho2D(0.0f, 1280.0f, 0.0f, 720.0f);
//	}
//
//	private void drawFrameCount(GLAutoDrawable drawable) {
//		if (!frameCounterOn)
//			return;
//		String text = "" + counter;
//		Rectangle2D fcBounds = textRenderer.getBounds(text);
//
//		textRenderer.beginRendering(width, height);
//		textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
//		textRenderer.draw(text, (int) (width - fcBounds.getWidth() - 2), height - 15);
//		textRenderer.endRendering();
//	}
//
//	private void drawPlayer(GL2 gl) {
//		gl.glBegin(GL2.GL_POLYGON);
//		gl.glColor3f(0.0f, 1.0f, 0.0f);
//		gl.glVertex2f(player.getX(), player.getY());
//		gl.glVertex2f(player.getX(), player.getY() + player.getHeight());
//		gl.glVertex2f(player.getX() + player.getWidth(), player.getY() + player.getHeight());
//		gl.glVertex2f(player.getX() + player.getWidth(), player.getY());
//		gl.glEnd();
//	}
//
//	public void animateJump(GL2 gl) {
//		if (player.getY() + player.getHeight() > jumpHeightLimit)
//			jumpModifier *= -1;
//		if (player.getY() + 2 * jumpModifier < 0.0f) {
//			player.setJumpingState(false);
//			player.setY(0.0f);
//			jumpModifier *= -1;
//		}
//		player.setY(player.getY() + jumpModifier);
//		System.out.println(player.getY());
//	}
//
//	@Override
//	public void display(GLAutoDrawable drawable) {
//		updateProjection(drawable);
//		counter++;
//		GL2 gl = drawable.getGL().getGL2();
//		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//		drawFrameCount(drawable);
//		drawPlayer(gl);
//		if (player.isJumping())
//			animateJump(gl);
//	}
//
//	@Override
//	public void dispose(GLAutoDrawable drawable) {
//		textRenderer = null;
//	}
//
//	@Override
//	public void init(GLAutoDrawable drawable) {
//		textRenderer = new TextRenderer(new Font("Monospaced", Font.BOLD, 16), true, true);
//	}
//
//	@Override
//	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {
//		// TODO Auto-generated method stub
//
//	}
//}
