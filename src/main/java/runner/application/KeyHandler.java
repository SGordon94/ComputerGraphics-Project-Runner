package runner.application;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

//import com.jogamp.opengl.awt.GLJPanel;

public class KeyHandler{
	private static Runner instance;
	private final String JUMP = "jump";

	public KeyHandler(Runner runner) {
		instance = runner.getInstance();

		//uses bindings to capture key events
		runner.getCanvas().getInputMap().put(KeyStroke.getKeyStroke("SPACE"), JUMP);
		runner.getCanvas().getActionMap().put(JUMP, new JumpAction(runner));
	}
	
	//Jump action for Player
	class JumpAction extends AbstractAction{
		private Runner runner;
		JumpAction(Runner runner){
			this.runner = runner;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Player player = runner.getPlayer();
			if(player == null) return;
			else if(!player.isJumping()) {
				player.jump(runner);
			}
		}	
	}
}
