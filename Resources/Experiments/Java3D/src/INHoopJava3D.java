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
 *  Notes:
 *  
 *   http://www.gamedev.net/page/resources/_/technical/game-programming/coordinates-in-hexagon-based-tile-maps-r1800
 *   http://www.calculatorsoup.com/calculators/geometry-plane/polygon.php
 *   
 *   Java3D on 64bit Windows:
 *   
 *   Download the java3d-1_5_1-windows-amd64.exe Version from the Java3D Homepage.
 *   
 *   Install this 64bit Version.
 *   
 *   Copy the .dll file from:
 *   .../Program Files/Java/Java3D/1.5.1/bin to
 *   .../Program Files/Java/jre7/bin
 *   
 *   Copy all .jar files from:
 *   .../Program Files/Java/Java3D/1.5.1/lib/ext to
 *   .../Program Files/Java/jre7/lib/ext
 *   
 */

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * http://ex.osaka-kyoiku.ac.jp/~fujii/JREC6/onlinebook_selman/Htmls/3DJava_Ch14.htm
 */
public class INHoopJava3D extends JPanel implements KeyListener
{
	private static final long serialVersionUID = 1941564561521769664L;

	static boolean application = true; // by default, don't load as an applet	
	static int primflagsx = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
	
	private PolygonAttributes pa=new PolygonAttributes();
	private Canvas3D canvas=null;
    private SimpleUniverse universe=null;
    private Appearance cubeAppearance=null;
    private Appearance polygon1Appearance=null;
    private BranchGroup rootGroup=null;
    private BoundingSphere bounds=null;
    
    private float hexHalfHeight=0.5f;
    private float hexRadius=0.866f;
       
    private String hexTexture="/assets/images/hextexture.png";
    
    /**
     * 
     */
    public INHoopJava3D () 
    {		
    	INHoopRoot.debug ("INHoopJava3D","INHoopJava3D ()");
    	
    	setFocusable(true);
    	
    	pa.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);    	
    	    	    	
    	setLayout(new BorderLayout());
    	 
    	canvas=new Canvas3D (SimpleUniverse.getPreferredConfiguration());
    	 
    	add ("North",new Label("Top Menu"));
    	add ("Center",canvas);
    	add ("South",new Label("Bottom Menu"));
    	    	
    	create ();
    	
    	canvas.addKeyListener(this);
    }
    /**
     * 
     */
    protected void debug (String aMessage)
    {
    	INHoopRoot.debug ("INHoopJava3D",aMessage);
    }
    /**
     * 
     */
    public void create ()
    {    	
    	debug ("create ()");
    	
        createUniverse();
        createAppearance();
        createCubeOfCubes((float) 5.0);
        //createHexGrid ();
        //createTetrahedron ();
        createLights();
        createBehaviourInteractors();
        
        universe.getViewingPlatform().setNominalViewingTransform();       
        universe.addBranchGraph(rootGroup);
        
        //pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);               
        //pa.setBackFaceNormalFlip (true);        
        //pa.setCullFace(PolygonAttributes.CULL_NONE);
        
        createCameraPosition ();        
    }
    /**
     *
     */
    public SimpleUniverse getUniverse ()
    {
    	return (universe);
    }
    /**
     * 
     */
    private void createUniverse() 
    {
    	debug ("createUniverse ()");
    	
        universe = new SimpleUniverse (canvas);
        rootGroup = new BranchGroup ();        
        bounds = new BoundingSphere (new Point3d(0.0, 0.0, 0.0), 100.0);
    }
    /**
     * 
     */
    private void createAppearance() 
    {    	    
    	debug ("createAppearance ()");
    	
 	   	//>---------------------------------------------------
 	   	
        cubeAppearance = new Appearance();
        cubeAppearance.setPolygonAttributes (pa);
        Color3f ambientColour = new Color3f();
        ambientColour.set (Color.GRAY);
        Color3f diffuseColour = new Color3f();        
        diffuseColour.set (Color.GRAY);
        
        Color3f emissiveColour = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f specularColour = new Color3f(1.0f, 1.0f, 1.0f);

        float shininess = 20.0f;
        
        cubeAppearance.setMaterial (new Material (ambientColour, emissiveColour,diffuseColour, specularColour, shininess));
        
    	//>---------------------------------------------------
    	    	    	
    	URL imgURL=null;
    	
    	imgURL=getClass ().getResource (hexTexture);
    	
    	if (imgURL!=null)
    	{
    		debug ("Loaded " + hexTexture + ", generating texture appearance ...");
    		    		    		
 	   		Texture texImage=new TextureLoader (imgURL,this).getTexture();
 	   		
 	   		if (texImage==null)
 	   		{
 	   			debug ("Error: unable to obtain texture from image file");
 	   			return;
 	   		}
 	   		else
 	   			debug ("Texture loaded, applying to appearance");
 	   		
    		polygon1Appearance=new Appearance();
 	   		 	   		 	   		
 	   		TextureAttributes texAttrx = new TextureAttributes(); 	   
 	   		texAttrx.setTextureMode (TextureAttributes.REPLACE);
 	   		 	     	     	     	    	    	   		
 	   		polygon1Appearance.setTexture (texImage);
 	   		polygon1Appearance.setTextureAttributes(texAttrx);
 	   		polygon1Appearance.setMaterial (new Material (ambientColour, emissiveColour,diffuseColour, specularColour, shininess));
    	}
    	else
    		debug ("Unable to load texture: " + hexTexture);
    	        
        //>---------------------------------------------------
    }
    /**
     * 
     */
    public void createTetrahedron ()
    {
    	debug ("createTetrahedron ()");
    	
    	rootGroup.addChild(new Tetrahedron ());
    }
    /**
     * 
     */
    public void createHexGrid ()
    {
    	debug ("createHexGrid ()");
    	
    	int nrHex=10;
    	float hexWidth=hexRadius*2;
    	    	
    	for (int i=0;i<(nrHex+1);i++)
    	{    	
    		Shape3D shape=createHex (0.1f);
    	
    		TransformGroup tg = new TransformGroup();
    		Transform3D transform = new Transform3D();
    		Vector3f vector = new Vector3f((hexWidth*i)-((nrHex/2)*hexWidth),0.0f,0.0f);
    		transform.setTranslation(vector);
    		tg.setTransform(transform);
    		tg.addChild(shape);
    	       
    		rootGroup.addChild(tg);
    	}	
    }
    /**
     * 
     */
    private Shape3D createHex (float elevation)
    {    
    	debug ("createHex ()");
    	
	    TriangleArray tri = new TriangleArray (18,TriangleArray.COORDINATES |
	    										  GeometryArray.NORMALS |
	    										  GeometryArray.TEXTURE_COORDINATE_2);
	    
	    tri.setCoordinate (0,new Point3f(-0.866f, 0.5f, 0.0f));
	    tri.setCoordinate (1,new Point3f(-0.866f, -0.5f, 0.0f));
	    tri.setCoordinate (2,new Point3f(0.0f, 0.0f, elevation));
	    
	    tri.setCoordinate (3,new Point3f(-0.866f, -0.5f, 0.0f));
	    tri.setCoordinate (4,new Point3f( 0.0f, -0.866f, 0.0f));
	    tri.setCoordinate (5,new Point3f( 0.0f, 0.0f, elevation));
	    
	    tri.setCoordinate (6,new Point3f( 0.0f, -0.866f, 0.0f));
	    tri.setCoordinate (7,new Point3f( 0.866f,  -0.5f, 0.0f));
	    tri.setCoordinate (8,new Point3f( 0.0f, 0.0f, elevation));	  
	    
	    tri.setCoordinate (9,new Point3f( 0.866f,  -0.5f, 0.0f));
	    tri.setCoordinate (10,new Point3f( 0.866f,  0.5f, 0.0f));
	    tri.setCoordinate (11,new Point3f( 0.0f, 0.0f, elevation));
	    
	    tri.setCoordinate (12,new Point3f( 0.866f,  0.5f, 0.0f));
	    tri.setCoordinate (13,new Point3f( 0,  0.866f, 0.0f));
	    tri.setCoordinate (14,new Point3f( 0.0f, 0.0f, elevation));
	    
	    tri.setCoordinate (15,new Point3f( 0,  0.866f, 0.0f));
	    tri.setCoordinate (16,new Point3f(-0.866f,  0.5f, 0.0f));
	    tri.setCoordinate (17,new Point3f( 0.0f, 0.0f, elevation));		    
	    	    
	    GeometryInfo geometryInfo = new GeometryInfo(tri);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals (geometryInfo);	    
		
		GeometryArray result=geometryInfo.getGeometryArray();
	    
		Shape3D shape=null;
		
		if (polygon1Appearance==null)
			shape=new Shape3D (result,cubeAppearance);
		else
			shape=new Shape3D (result,polygon1Appearance);
			    
		return (shape);
    }
    /**
     * 
     */
	public void createCubeOfCubes (float multiplier) 
    {
    	debug ("createCubeOfCubes ()");
    	
        // build up a cube of 10 cube for each axis (x, y, z)
        for (float x = -.5f; x <= .5f; x = x + .1f) 
        {
            for (float y = -.5f; y <= .5f; y += .1f) 
            {
                for (float z = -.5f; z <= .5f; z += .1f) 
                {                	
                	Box box=null;
                			
                	if (polygon1Appearance!=null)
                		box=new Box ((0.02f * multiplier), (0.02f * multiplier), (0.02f * multiplier), polygon1Appearance);
                	else
                		box=new Box ((0.02f * multiplier), (0.02f * multiplier), (0.02f * multiplier), cubeAppearance);
                                        
                    TransformGroup tg = new TransformGroup();
                    Transform3D transform = new Transform3D();
                    Vector3f vector = new Vector3f ((x*multiplier),(y*multiplier),(z*multiplier));
                    transform.setTranslation(vector);
                    tg.setTransform(transform);
                    tg.addChild(box);
                    rootGroup.addChild(tg);                                        
                }
            }
        }
    }
    /**
     * 
     */
    private void createLights() 
    {
    	debug ("createLights ()");
    	
        Color3f ambientLightColour = new Color3f(0.9f, 0.9f, 0.9f);
        AmbientLight ambientLight = new AmbientLight(ambientLightColour);
        ambientLight.setInfluencingBounds(bounds);
        Color3f directionLightColour = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f directionLightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight directionLight = new DirectionalLight(directionLightColour, directionLightDir);
        directionLight.setInfluencingBounds(bounds);
        rootGroup.addChild(ambientLight);
        rootGroup.addChild(directionLight);
    }
    /**
     * 
     */
    private void createBehaviourInteractors() 
    {
    	debug ("createBehaviourInteractors ()");
    	
        TransformGroup viewTransformGroup=universe.getViewingPlatform().getViewPlatformTransform();

        //KeyNavigatorBehavior keyInteractor=new KeyNavigatorBehavior(viewTransformGroup);

        //BoundingSphere movingBounds=new BoundingSphere(new Point3d(0.0, 0.0,0.0), 1.0);
        //keyInteractor.setSchedulingBounds(movingBounds);
        //rootGroup.addChild(keyInteractor);

        //MouseRotate behavior = new MouseRotate();
        //behavior.setFactor (0.01,0.01);
        MouseTranslate behavior=new MouseTranslate ();
        behavior.setTransformGroup(viewTransformGroup);
        rootGroup.addChild(behavior);
        behavior.setSchedulingBounds(bounds);
    }
    /**
     * 
     */
    private void createCameraPosition ()
    {
    	debug ("createCameraPosition ()");
    	
    	TransformGroup VpTG = universe.getViewingPlatform().getViewPlatformTransform();

    	float Zcamera = 12; //put the camera 12 meters back
    	Transform3D Trfcamera = new Transform3D();
    	Trfcamera.setTranslation(new Vector3f(0.0f, 0.0f, Zcamera));  
    	VpTG.setTransform (Trfcamera);    	
    }
    /**
     * 
     */
	@Override
	public void keyPressed(KeyEvent e) 
	{
		debug ("keyPressed ()");
		
		//displayInfo (e,"KEY PRESSED: ");
	}
	/**
	 * 
	 */
	@Override
	public void keyReleased(KeyEvent e) 
	{
		debug ("keyReleased ()");
		
		//displayInfo (e,"KEY RELEASED: ");
	}
	/**
	 * 
	 */
	@Override
	public void keyTyped(KeyEvent e) 
	{
		debug ("keyTyped ()");
		
		//displayInfo (e,"KEY TYPED: ");
		
		if (e.getKeyChar()=='1')
		{
			pa.setPolygonMode(PolygonAttributes.POLYGON_POINT);						
		}
				
		if (e.getKeyChar()=='2')
		{
			pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		}
		
		if (e.getKeyChar()=='3')
		{
			pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		}	
		
		if (polygon1Appearance!=null)
			polygon1Appearance.setPolygonAttributes (pa);
		else
			cubeAppearance.setPolygonAttributes (pa);
	}
	/** 
	 * @param e
	 * @param keyStatus
	 */
    public void displayInfo(KeyEvent e, String keyStatus)
    {        
        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        
        if (id == KeyEvent.KEY_TYPED) 
        {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } 
        else 
        {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }
        
        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);

        if (tmpString.length() > 0) 
        {
            modString += " (" + tmpString + ")";
        } 
        else 
        {
            modString += " (no extended modifiers)";
        }
        
        String actionString = "action key? ";
        
        if (e.isActionKey()) 
        {
            actionString += "YES";
        } 
        else 
        {
            actionString += "NO";
        }
        
        String locationString = "key location: ";
        
        int location = e.getKeyLocation();
        
        if (location == KeyEvent.KEY_LOCATION_STANDARD) 
        {
            locationString += "standard";
        } 
        else if (location == KeyEvent.KEY_LOCATION_LEFT) 
        {
            locationString += "left";
        } 
        else if (location == KeyEvent.KEY_LOCATION_RIGHT) 
        {
            locationString += "right";
        } 
        else if (location == KeyEvent.KEY_LOCATION_NUMPAD) 
        {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
        
        debug (keyString + " , " + modString + " , " + actionString + " , " + locationString);
    }
    /**
     * 
     */
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(new Runnable() 
        {
            public void run() 
            {
                try 
                {
                    JFrame f = new JFrame("Hex Grid");
                    f.setDefaultCloseOperation(3);
                    INHoopJava3D square = new INHoopJava3D();
                    square.setBorder(new EmptyBorder(5, 5, 5, 5));
                    f.setContentPane (square);
                    f.pack();
                    f.setSize(640,480);
                    f.setVisible(true);
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }
        });
    }
    
}
