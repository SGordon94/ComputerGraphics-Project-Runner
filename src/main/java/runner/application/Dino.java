package runner.application;

import java.awt.Point;

public class Dino {

	private Point pos;

	private boolean inJumpState;

	public Dino() {
		this.pos = new Point(0, 0);
		inJumpState = false;
	}

	public Dino(Point point) {
		this.pos = point;
		inJumpState = false;
	}

	public Dino(int x, int y) {
		this.pos = new Point(x, y);
		inJumpState = false;
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public int getX() {
		return (int) this.pos.getX();
	}

	public int getY() {
		return (int) this.pos.getY();
	}

	public void setX(int x) {
		this.pos.setLocation(x, this.getY());
	}

	public void setY(int y) {
		this.pos.setLocation(this.getX(), y);
	}

	public void setPos(int x, int y) {
		this.pos = new Point(x, y);
	}

	// public void drawDino(GL2 gl) {
	// gl.glBegin(GL2.GL_POLYGON);
	// gl.glColor3d(0.403922, 0.560784, 0);
	//
	// gl.glVertex2d(pos.getX(), pos.getY());
	// gl.glVertex2d(pos.getX(), pos.getY() + 40);
	// gl.glVertex2d(pos.getX() + 50, pos.getY() + 40);
	// gl.glVertex2d(pos.getX() + 50, pos.getY());
	//
	// gl.glEnd();
	// }

	public void jump() {
		System.out.println("Jump");
		this.setInJumpState(true);
	}

	public boolean getInJumpState() {
		return inJumpState;
	}

	public void setInJumpState(boolean inJumpState) {
		this.inJumpState = inJumpState;
	}

}
