package patch.other.unpack;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

class DropTargetAdapter implements DropTargetListener {
	public void drop(DropTargetDropEvent dtde) {
		try {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Object object = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
				List<?> list = null;
				if (object instanceof List<?>) {
					list = (List<?>) (object);
				} else {
					return;
				}
				for (Object obj : list) {
					if (obj instanceof File) {
						File file = (File) obj;
						ZlibUnpacker.getThread(file, null).start();
					}
				}
				dtde.dropComplete(true);
			} else {
				dtde.rejectDrop();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (UnsupportedFlavorException ufe) {
			ufe.printStackTrace();
		}
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	public void dragOver(DropTargetDragEvent dtde) {
	}

	public void dragExit(DropTargetEvent dte) {
	}

	public void dragEnter(DropTargetDragEvent dtde) {
	}
}
