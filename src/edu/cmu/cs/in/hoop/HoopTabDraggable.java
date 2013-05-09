/** 
 * Author: Martin van Velsen <vvelsen@cs.cmu.edu>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation, either version 3 of the 
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package edu.cmu.cs.in.hoop;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.cmu.cs.in.base.HoopRoot;
import edu.cmu.cs.in.controls.base.HoopJPanel;
import edu.cmu.cs.in.controls.templates.HoopAquaBarTabbedPaneUI;

public class HoopTabDraggable extends JTabbedPane implements ChangeListener, WindowListener, ComponentListener
{	
	private static final long serialVersionUID = 1L;	
	private static final int LHoopEWIDTH = 3;
	private static final String NAME = "test";
	private final GhostGlassPane glassPane = new GhostGlassPane();
	private final Rectangle lineRect  = new Rectangle();
	//private final Color lineColor = new Color(0, 100, 255);
	private final Color lineColor = new Color(255,0,0);
	private int dragTabIndex = -1;
	private boolean hasGhost = true;
	private boolean isPaintScrollArea = true;
	private static Rectangle rBackward = new Rectangle();
	private static Rectangle rForward  = new Rectangle();
	private static int rwh = 20;
	private static int buttonsize = 30; //XXX: magic number of scroll button size
	
	private Boolean useBasicStyle=false;
	  
	/** 
	 * 
	 */	  
	public HoopTabDraggable() 
	{
		super();
		
		this.addChangeListener(this);
		
		if (useBasicStyle==false)
		{
			//this.setUI(new HoopTabUI(this));			
			//this.setUI(HoopCWTabbedPaneUI.createUI(this));
			this.setUI(HoopAquaBarTabbedPaneUI.createUI(this));
			
		}	
	    
	    final DragSourceListener dsl = new DragSourceListener() 
	    {
	    	/**
	    	 * 	
	    	 */
	    	@Override public void dragEnter(DragSourceDragEvent e) 
	    	{
	    		e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
	    	}
	    	/**
	    	 * 	
	    	 */	      
	    	@Override public void dragExit(DragSourceEvent e) 
	    	{
	    		e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
	    		lineRect.setRect(0,0,0,0);
	    		glassPane.setPoint(new Point(-1000,-1000));
	    		glassPane.repaint();
	    	}
	    	/**
	    	 * 	
	    	 */
	    	@Override public void dragOver(DragSourceDragEvent e) 
	    	{
	    		Point glassPt = e.getLocation();
	    		SwingUtilities.convertPointFromScreen(glassPt, glassPane);
	    		int targetIdx = getTargetTabIndex(glassPt);
	        
	    		//	if(getTabAreaBounds().contains(tabPt) && targetIdx>=0 &&
	        
	    		if(getTabAreaBounds().contains(glassPt) && targetIdx>=0 && targetIdx!=dragTabIndex && targetIdx!=dragTabIndex+1) 
	    		{
	    			e.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
	    			glassPane.setCursor(DragSource.DefaultMoveDrop);
	    		}
	    		else
	    		{
	    			e.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
	    			glassPane.setCursor(DragSource.DefaultMoveNoDrop);
	    		}
	    	}
	    	/**
	    	 * 	
	    	 */
	    	@Override public void dragDropEnd(DragSourceDropEvent e) 
	    	{
	    		lineRect.setRect(0,0,0,0);
	    		dragTabIndex = -1;
	    		glassPane.setVisible(false);
	    	  
	    		if(hasGhost()) 
	    		{	    		  
	    			glassPane.setVisible(false);
	    			glassPane.setImage(null);
	    		}
	    	}
	    	/**
	    	 * 	
	    	 */	      
	    	@Override public void dropActionChanged(DragSourceDragEvent e) 
	    	{
	    	  
	    	}
	    };
	    
	    final Transferable t = new Transferable() 
	    {
	    	/**
	    	 * 	
	    	 */
	    	private final DataFlavor FLAVOR = new DataFlavor (DataFlavor.javaJVMLocalObjectMimeType, NAME);
	    	@Override public Object getTransferData(DataFlavor flavor) 
	    	{
	    		return HoopTabDraggable.this;
	    	}
	    	/**
	    	 * 	
	    	 */
	    	@Override public DataFlavor[] getTransferDataFlavors() 
	    	{
	    		DataFlavor[] f = new DataFlavor[1];
	    		f[0] = this.FLAVOR;
	    		return f;
	    	}
	    	/**
	    	 * 	
	    	 */
	    	@Override public boolean isDataFlavorSupported(DataFlavor flavor) 
	    	{
	    		return flavor.getHumanPresentableName().equals(NAME);
	    	}
	    };
	    
    	/**
    	 * 	
    	 */
	    final DragGestureListener dgl = new DragGestureListener() 
	    {
	    	/**
	    	 * 	
	    	 */
	    	@Override public void dragGestureRecognized(DragGestureEvent e) 
	    	{
	    		if(getTabCount() <= 1)
	    			return;
	        
	    		Point tabPt = e.getDragOrigin();
	    		dragTabIndex = indexAtLocation(tabPt.x, tabPt.y);
	    		//"disabled tab problem".
	        
	    		if(dragTabIndex < 0 || !isEnabledAt(dragTabIndex)) 
	    			return;
	        
	    		initGlassPane(e.getComponent(), e.getDragOrigin());
	        
	    		try
	    		{
	    			e.startDrag(DragSource.DefaultMoveDrop, t, dsl);
	    		}
	    		catch(InvalidDnDOperationException idoe) 
	    		{
	    			idoe.printStackTrace();
	    		}
	    	}
	    };
	    
	    new DropTarget(glassPane, DnDConstants.ACTION_COPY_OR_MOVE,new CDropTargetListener(), true);
	    
	    new DragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, dgl);

	    this.addComponentListener(this);
	}
	/**
	 * 
	 */
	private void debug (String aMessage)
	{
		HoopRoot.debug ("HoopTabDraggable",aMessage);
	}
	/**
	  * 
	  */
	public Boolean getUseBasicStyle() 
	{
		return useBasicStyle;
	}
	/**
	  * 
	  */	
	public void setUseBasicStyle(Boolean useBasicStyle) 
	{
		this.useBasicStyle = useBasicStyle;
	}	
	/**
	  * Let the UI style class do this instead if we can (HoopTabUI)
	  */
	@Override 
	public void paintComponent(Graphics g) 
	{		
		if (useBasicStyle==true) 
		{
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(new Color (200,200,200));		
			g2.fillRect (0,0,this.getWidth()-2,this.getHeight()-2);
			
			g2.setColor(new Color (0,0,0));		
			g2.drawRect (0,0,this.getWidth()-2,this.getHeight()-2);
		}	
		
		super.paintComponent(g);
	}
	/** 
	 * 
	 */
	private void clickArrowButton(String actionKey) 
	{
	    ActionMap map = getActionMap();
	    
	    if(map != null) 
	    {
	      Action action = map.get(actionKey);
	      
	      if (action != null && action.isEnabled()) 
	      {
	        action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null, 0, 0));
	      }
	    }
	}	  
	/** 
	 * 
	 */	  
	private void autoScrollTest(Point glassPt) 
	{
		Rectangle r = getTabAreaBounds();
		int tabPlacement = getTabPlacement();
		
		if(tabPlacement==TOP || tabPlacement==BOTTOM) 
		{
			rBackward.setBounds(r.x, r.y, rwh, r.height);
			rForward.setBounds(r.x+r.width-rwh-buttonsize, r.y, rwh+buttonsize, r.height);
		}
		else if(tabPlacement==LEFT || tabPlacement==RIGHT) 
		{
			rBackward.setBounds(r.x, r.y, r.width, rwh);
			rForward.setBounds(r.x, r.y+r.height-rwh-buttonsize, r.width, rwh+buttonsize);
		}
    
		rBackward = SwingUtilities.convertRectangle(getParent(), rBackward, glassPane);
		rForward  = SwingUtilities.convertRectangle(getParent(), rForward,  glassPane);
		  
		if(rBackward.contains(glassPt)) 
		{ 
			//System.out.println(new java.util.Date() + "Backward");
			clickArrowButton("scrollTabsBackwardAction");
		}
		else if(rForward.contains(glassPt)) 
		{
			//System.out.println(new java.util.Date() + "Forward");
			clickArrowButton("scrollTabsForwardAction");
		}
	}	

	class CDropTargetListener extends HoopRoot implements DropTargetListener
	{
		public CDropTargetListener ()
		{
			setClassName ("CDropTargetListener");
			debug ("CDropTargetListener ()");
		}
		  
		@Override public void dragEnter(DropTargetDragEvent e) 
		{
			if (isDragAcceptable(e)) 
				e.acceptDrag(e.getDropAction());
			else 
				e.rejectDrag();
		}
		  
		@Override public void dragExit(DropTargetEvent e) {}
		@Override public void dropActionChanged(DropTargetDragEvent e) {}

		  private Point _glassPt = new Point();
		  
		  @Override public void dragOver(final DropTargetDragEvent e) 
		  {
			  Point glassPt = e.getLocation();
			  if(getTabPlacement()==JTabbedPane.TOP || getTabPlacement()==JTabbedPane.BOTTOM) 
			  {
				  initTargetLeftRightLine(getTargetTabIndex(glassPt));
			  }
			  else
			  {
				  initTargetTopBottomLine(getTargetTabIndex(glassPt));
			  }
			  
			  if(hasGhost()) 
			  {
				  glassPane.setPoint(glassPt);
			  }
			  
			  if(!_glassPt.equals(glassPt)) 
				  glassPane.repaint();
	      
			  _glassPt = glassPt;
			  autoScrollTest(glassPt);
		  }

		  @Override public void drop(DropTargetDropEvent e) 
		  {
			  if(isDropAcceptable(e)) 
			  {
				  convertTab(dragTabIndex, getTargetTabIndex(e.getLocation()));
				  e.dropComplete(true);
			  }
			  else
			  {
				  e.dropComplete(false);
			  }
			  
			  repaint();
		  }
		  
		  private boolean isDragAcceptable(DropTargetDragEvent e) 
		  {
			  Transferable t = e.getTransferable();
			  
			  if(t==null) 
				  return false;
			  
			  DataFlavor[] f = e.getCurrentDataFlavors();
			  
			  if(t.isDataFlavorSupported(f[0]) && dragTabIndex>=0) 
			  {
				  return true;
			  }
			  
			  return false;
	    }
		  
	    private boolean isDropAcceptable(DropTargetDropEvent e) 
	    {
	    	Transferable t = e.getTransferable();
	      
	    	if(t==null) 
	    		return false;
	      
	    	DataFlavor[] f = t.getTransferDataFlavors();
	      
	    	if(t.isDataFlavorSupported(f[0]) && dragTabIndex>=0) 
	    	{
	    		return true;
	    	}
	      
	    	return false;
	    	}
	  }
	  
	  public void setPaintGhost(boolean flag) 
	  {
	    hasGhost = flag;
	  }
	  
	  public boolean hasGhost() 
	  {
	    return hasGhost;
	  }
	  	  
	  public void setPaintScrollArea(boolean flag) 
	  {
	    isPaintScrollArea = flag;
	  }
	  
	  public boolean isPaintScrollArea() 
	  {
	    return isPaintScrollArea;
	  }

	  private int getTargetTabIndex(Point glassPt) 
	  {
		  Point tabPt = SwingUtilities.convertPoint (glassPane, glassPt, HoopTabDraggable.this);
	    
		  boolean isTB = getTabPlacement()==JTabbedPane.TOP || getTabPlacement()==JTabbedPane.BOTTOM;
	    
		  for(int i=0;i < getTabCount();i++) 
		  {
			  Rectangle r = getBoundsAt(i);
			  if(isTB) r.setRect(r.x-r.width/2, r.y,  r.width, r.height);
			  else   r.setRect(r.x, r.y-r.height/2, r.width, r.height);
			  if(r.contains(tabPt)) return i;
		  }
	    
		  Rectangle r = getBoundsAt(getTabCount()-1);
	    
		  if(isTB) 
			  r.setRect(r.x+r.width/2, r.y,  r.width, r.height);
		  else   
			  r.setRect(r.x, r.y+r.height/2, r.width, r.height);
	    
		  return   r.contains(tabPt)?getTabCount():-1;
	  }
	  /** 
	   * @param prev
	   * @param next
	   */
	  private void convertTab(int prev, int next) 
	  {
		  if(next < 0 || prev==next) 
		  {
			  return;
		  }
	  		
	  		Component cmp = getComponentAt(prev);
	  		Component tab = getTabComponentAt(prev);
	  		String str  = getTitleAt(prev);
	  		Icon icon   = getIconAt(prev);
	  		String tip  = getToolTipTextAt(prev);
	  		boolean flg   = isEnabledAt(prev);
	  		int tgtindex  = prev>next ? next : next-1;
	  		remove(prev);
	  		insertTab(str, icon, cmp, tip, tgtindex);
	  		setEnabledAt(tgtindex, flg);
	  		
	  		//When you drag'n'drop a disabled tab, it finishes enabled and selected.
	  		//pointed out by dlorde
	  		if(flg) 
	  			setSelectedIndex(tgtindex);

	  		//I have a component in all tabs (jlabel with an X to close the tab)
	  		//and when i move a tab the component disappear.
	  		//pointed out by Daniel Dario Morales Salas
	  		setTabComponentAt(tgtindex, tab);
	  }
	  /** 
	   * @param next
	   */
	  private void initTargetLeftRightLine(int next) 
	  {
		  if(next < 0 || dragTabIndex==next || next-dragTabIndex==1) 
		  {
			  lineRect.setRect(0,0,0,0);
		  }
		  else if(next==0) 
		  {
			  Rectangle r = SwingUtilities.convertRectangle (this, getBoundsAt(0), glassPane);
			  
			  lineRect.setRect(r.x-LHoopEWIDTH/2,r.y,LHoopEWIDTH,r.height);
		  }
		  else
		  {
			  Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(next-1), glassPane);
			  lineRect.setRect(r.x+r.width-LHoopEWIDTH/2,r.y,LHoopEWIDTH,r.height);
		  }
	  }
	  /** 
	   * @param next
	   */
	  private void initTargetTopBottomLine(int next) 
	  {
		  HoopRoot.debug ("HoopTabDraggable","initTargetTopBottomLine ()");
		  
		  if(next < 0 || dragTabIndex==next || next-dragTabIndex==1) 
		  {
			  lineRect.setRect(0,0,0,0);
		  }
		  else if(next==0) 
		  {
			  Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(0), glassPane);
			  lineRect.setRect(r.x,r.y-LHoopEWIDTH/2,r.width,LHoopEWIDTH);
		  }
		  else
		  {
			  Rectangle r = SwingUtilities.convertRectangle(this, getBoundsAt(next-1), glassPane);
			  lineRect.setRect(r.x,r.y+r.height-LHoopEWIDTH/2,r.width,LHoopEWIDTH);
		  }
	  }
	  /** 
	   * @param c
	   * @param tabPt
	   */
	  private void initGlassPane(Component c, Point tabPt) 
	  {
		  HoopRoot.debug ("HoopTabDraggable","initGlassPane ()");
		  
		  getRootPane().setGlassPane(glassPane);
	    
		  if(hasGhost()) 
		  {
			  Rectangle rect = getBoundsAt(dragTabIndex);
			  BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
			  Graphics g = image.getGraphics();
			  c.paint(g);
			  rect.x = rect.x < 0?0:rect.x;
			  rect.y = rect.y < 0?0:rect.y;
			  image = image.getSubimage(rect.x,rect.y,rect.width,rect.height);
			  glassPane.setImage(image);
		  }
		  
		  Point glassPt = SwingUtilities.convertPoint(c, tabPt, glassPane);
		  glassPane.setPoint(glassPt);
		  glassPane.setVisible(true);
	  }
	  /** 
	   * @return
	   */
	  private Rectangle getTabAreaBounds() 
	  {
		  HoopRoot.debug ("HoopTabDraggable","getTabAreaBounds ()");
		  
		  Rectangle tabbedRect = getBounds();
		  //pointed out by daryl. NullPointerException: i.e. addTab("Tab",null)
		  //Rectangle compRect   = getSelectedComponent().getBounds();
		  Component comp = getSelectedComponent();
		  
		  int idx = 0;
		  
		  while(comp==null && idx < getTabCount()) 
			  comp = getComponentAt(idx++);
		  
		  Rectangle compRect = (comp==null)?new Rectangle():comp.getBounds();
		  
		  int tabPlacement = getTabPlacement();
		  
		  if(tabPlacement==TOP) 
		  {
			  tabbedRect.height = tabbedRect.height - compRect.height;
		  }
		  else if(tabPlacement==BOTTOM) 
		  {
			  tabbedRect.y = tabbedRect.y + compRect.y + compRect.height;
			  tabbedRect.height = tabbedRect.height - compRect.height;
		  }
		  else if(tabPlacement==LEFT) 
		  {
			  tabbedRect.width = tabbedRect.width - compRect.width;
		  }
		  else if(tabPlacement==RIGHT) 
		  {
			  tabbedRect.x = tabbedRect.x + compRect.x + compRect.width;
			  tabbedRect.width = tabbedRect.width - compRect.width;
		  }
		  
		  tabbedRect.grow(2, 2);
		  
		  HoopRoot.debug ("HoopTabDraggable","w: " + tabbedRect.width + " h: " + tabbedRect.height);
		  
		  return tabbedRect;
	  }
	  
	class GhostGlassPane extends HoopJPanel 
	{
		  private static final long serialVersionUID = 1L;		
		  private final AlphaComposite composite;
		  private Point location = new Point(0, 0);
		  private BufferedImage draggingGhost = null;
		  
		  public GhostGlassPane() 
		  {
			  setClassName ("GhostGlassPane");
			  debug ("GhostGlassPane ()");
				
			  setOpaque(false);
			  
			  composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
			  
			  //http://bugs.sun.com/view_bug.do?bug_id=6700748
			  //setCursor(null);
		  }
		  /** 
		   * @param draggingGhost
		   */
		  public void setImage(BufferedImage draggingGhost) 
		  {
			  debug ("setImage ()");
			  
			  this.draggingGhost = draggingGhost;
		  }
		  /** 
		   * @param location
		   */
		  public void setPoint(Point location) 
		  {
			  this.location = location;
		  }		  
		  /**
		   * 
		   */
		  @Override 
		  public void paintComponent(Graphics g) 
		  {
			  //debug ("paintComponent ()");
			  			  
			  Graphics2D g2 = (Graphics2D) g;
			  g2.setComposite(composite);
			  
			  if(isPaintScrollArea() && getTabLayoutPolicy()==SCROLL_TAB_LAYOUT) 
			  {
				  g2.setPaint(Color.RED);
				  g2.fill(rBackward);
				  g2.fill(rForward);
			  }
			  
			  if(draggingGhost != null) 
			  {
				  double xx = location.getX() - (draggingGhost.getWidth(this) /2d);
				  double yy = location.getY() - (draggingGhost.getHeight(this)/2d);
				  g2.drawImage(draggingGhost, (int)xx, (int)yy , null);
			  }
			  
			  if(dragTabIndex>=0) 
			  {
				  g2.setPaint(lineColor);
				  g2.fill(lineRect);
			  }
		  }
  	}
	/**
	 * 
	 */
	@Override
	public void stateChanged(ChangeEvent e) 
	{
		debug ("stateChanged ()");
		
        JTabbedPane pane = (JTabbedPane) e.getSource();
        int selectedIndex = pane.getSelectedIndex();
        
        debug ("selectedIndex = " + selectedIndex);		
        
        int totalTabs = this.getTabCount();
        for(int i = 0; i < totalTabs; i++)
        {
        	HoopTabPane aTab = (HoopTabPane) this.getTabComponentAt(i);
           
        	if (aTab!=null)
        	{
        		if (i==selectedIndex)
        		{
        			aTab.setSelected (true);
        		}
        		else
        		{
        			aTab.setSelected (false);
        		}
        	}	
        }
	}
	@Override
	public void windowActivated(WindowEvent arg0) 
	{
		debug ("windowActivated ()");
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) 
	{
		debug ("windowClosed ()");
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		debug ("windowClosing ()");
		
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) 
	{
		debug ("windowDeactivated ()");
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) 
	{
		debug ("windowDeiconified ()");
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) 
	{
		debug ("windowIconified ()");
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) 
	{
		debug ("windowOpened ()");
		
	}
	@Override
	public void componentHidden(ComponentEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentMoved(ComponentEvent arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		//debug ("componentResize ("+this.getWidth()+","+this.getHeight()+")");		
	}
	@Override
	public void componentShown(ComponentEvent arg0) 
	{
			
	}
}
