package runner.application;

import java.awt.Dimension;
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
		GLJPanel canvas = new GLJPanel(capabilities);
		JFrame frame = new JFrame("Application");
		
		canvas.setPreferredSize(new Dimension(1000, 450));

		frame.setBounds(DEFAULT_BOUNDS);
		frame.getContentPane().add(canvas);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
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
