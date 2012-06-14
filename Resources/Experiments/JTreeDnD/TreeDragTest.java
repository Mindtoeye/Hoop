/*
Java Swing, 2nd Edition
By Marc Loy, Robert Eckstein, Dave Wood, James Elliott, Brian Cole
ISBN: 0-596-00408-7
Publisher: O'Reilly 
*/
// TreeDragTest.java
//A simple starting point for testing the DnD JTree code.
//

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/** 
 * @author vvelsen
 */
public class TreeDragTest extends JFrame 
{
	TreeDragSource ds;
	TreeDropTarget dt;
	JTree tree;

	public TreeDragTest() 
	{
		super("Rearrangeable Tree");
		setSize(300, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// If you want autoscrolling, use this line:
		tree = new AutoScrollingJTree();
		// Otherwise, use this line:
		// tree = new JTree();
		getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

		// If we only support move operations...
		// ds = new TreeDragSource(tree, DnDConstants.ACTION_MOVE);
		ds = new TreeDragSource(tree, DnDConstants.ACTION_COPY_OR_MOVE);
		dt = new TreeDropTarget(tree);
		setVisible(true);
  }

  public class AutoScrollingJTree extends JTree implements Autoscroll 
  {
	  private int margin = 12;

	  public AutoScrollingJTree() 
	  {
		  super();
	  }

	  public void autoscroll(Point p) 
	  {
		  int realrow = getRowForLocation(p.x, p.y);
		  Rectangle outer = getBounds();
		  realrow = (p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1 : realrow < getRowCount() - 1 ? realrow + 1 : realrow);
		  scrollRowToVisible(realrow);
	  }

	  public Insets getAutoscrollInsets() 
	  {
		  Rectangle outer = getBounds();
		  Rectangle inner = getParent().getBounds();
		  return new Insets(inner.y - outer.y + margin, inner.x - outer.x
				  + margin, outer.height - inner.height - inner.y + outer.y
				  + margin, outer.width - inner.width - inner.x + outer.x
				  + margin);
	  }

	  // Use this method if you want to see the boundaries of the
	  // autoscroll active region

	  public void paintComponent(Graphics g) 
	  {
		  super.paintComponent(g);
		  Rectangle outer = getBounds();
		  Rectangle inner = getParent().getBounds();
		  g.setColor(Color.red);
		  g.drawRect(-outer.x + 12, -outer.y + 12, inner.width - 24, inner.height - 24);
	  }
  }
  /*
   * 
   */
  public static void main(String args[]) 
  {
    new TreeDragTest();
  }
}
