package runner.application;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
	// **********************************************************************
	// Private Members
	// **********************************************************************

	// State (internal) variables
	private final View view;
	
	// **********************************************************************
	// Constructors and Finalizer
	// **********************************************************************
	public KeyHandler(View view) {

		// set view
		this.view = view;

		// get component
		Component component = view.getComponent();

		// add key listener
		component.addKeyListener(this);
	}

	// function to handle spacebar press
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !view.getDino().isJumping()) {
			if(!view.isSpacePressed()) view.setSuperJumpFrameLimit(view.getCounter() + 15);
			view.setSpacePressed(true);
		}
	}

	// function to handle spacebar release
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE && !view.getDino().isJumping()) {
			view.getDino().jump();
			view.setSpacePressed(false);
			view.getDino().setJumpType(0);
		}
	}

}
