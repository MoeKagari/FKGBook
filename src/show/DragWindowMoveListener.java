package show;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JWindow;

public class DragWindowMoveListener extends MouseAdapter {
	private JWindow window;
	private Point oldLocation;
	private Point newLocation;
	private boolean closeWindow = false;
	private boolean allowDrag = false;

	public DragWindowMoveListener(JWindow window) {
		this.window = window;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			this.allowDrag = true;
			this.closeWindow = true;
			this.oldLocation = e.getPoint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (this.allowDrag) {
			this.closeWindow = false;
			this.newLocation = e.getPoint();
			this.window.setLocation(//
					(int) (this.window.getLocation().getX() + this.newLocation.getX() - this.oldLocation.getX()), //
					(int) (this.window.getLocation().getY() + this.newLocation.getY() - this.oldLocation.getY())//
			);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && this.closeWindow) {
			this.window.setVisible(false);
		}

		this.closeWindow = false;
		this.allowDrag = false;
	}
}
