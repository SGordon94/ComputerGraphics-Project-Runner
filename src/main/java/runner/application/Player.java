package runner.application;

public class Player {
	
	private static Runner instance;
	private float x;
	private float y;
	private float width;
	private float height;
	
	private float red;
	private float green;
	private float blue;
	
	private boolean inJumpState;
	
	public Player(Runner runner) {
		instance = runner.getInstance();
		x = 0.0f;
		y = 0.0f;
		width = 0.1f;
		height = 0.1f;
		
		red = 1.0f;
		green = 0.0f;
		blue = 0.0f;
		
		inJumpState = false;
	}
	
	public boolean isJumping() {
		return inJumpState;
	}
	
	public void setJumpingState(boolean state) {
		inJumpState = state;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float newY) {
		y = newY;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getRedValue() {
		return red;
	}
	
	public float getGreenValue() {
		return green;
	}
	
	public float getBlueValue() {
		return blue;
	}
	
	public void setColor(float red, float green, float blue) {
		
	}
	
	public void jump(Runner runner) {
		System.out.println("Jump");
		inJumpState = true;
//		int begin = runner.getCount();
//		int end = begin + 36000;
//		float jumpModifier = 0.0001f;
//		while(begin < end) {
//			begin++;
//			y += jumpModifier;
//			System.out.println(y);
//		}
	}
}


