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

	// function to handle key presses
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			System.out.println("test");
		}
	}

}
