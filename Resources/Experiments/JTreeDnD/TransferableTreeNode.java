
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.tree.TreePath;

/**
*
*/
class TransferableTreeNode implements Transferable 
{
	public static DataFlavor TREE_PATH_FLAVOR = new DataFlavor(TreePath.class, "Tree Path");
	DataFlavor flavors[] = { TREE_PATH_FLAVOR };
	TreePath path;

	/** 
	 *	@param tp
	 */
	public TransferableTreeNode(TreePath tp) 
	{
		path = tp;
	}
	/*
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public synchronized DataFlavor[] getTransferDataFlavors() 
	{
		return flavors;
	}
	/**
	 *
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{
		return (flavor.getRepresentationClass() == TreePath.class);
	}
	/*
	 * (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		if (isDataFlavorSupported(flavor)) 
		{
			return (Object) path;
		} 
		else 
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}
}
