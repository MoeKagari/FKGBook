package patch.other.unpack;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import javax.swing.JFrame;

public class UnpackerUI {
	private JFrame frame;

	public UnpackerUI() {
		frame = new JFrame("美少女花骑士解包");
		
		frame.setSize(600, 400);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		new DropTarget(frame, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new UnpackerUI();
	}

}
