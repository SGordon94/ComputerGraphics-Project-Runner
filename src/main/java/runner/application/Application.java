package runner.application;

import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.JFrame;

//******************************************************************************

public final class Application {
	// **********************************************************************
	// Public Class Members
	// **********************************************************************

	public static final Rectangle DEFAULT_BOUNDS = new Rectangle(0, 0, 1000, 450);

	// **********************************************************************
	// Main
	// **********************************************************************

	public static void main(String[] args) {
		GLProfile profile = GLProfile.getDefault();
		GLCapabilities capabilities = new GLCapabilities(profile);
		// GLCanvas canvas = new GLCanvas(capabilities);
		GLJPanel canvas = new GLJPanel(capabilities);
		JFrame frame = new JFrame("Application");

		frame.setBounds(DEFAULT_BOUNDS);
		frame.getContentPane().add(canvas);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		View view = new View(canvas);
	}
}

// ******************************************************************************
