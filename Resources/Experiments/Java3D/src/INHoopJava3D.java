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
 */

package edu.cmu.cs.in.controls.map;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.universe.*;

import edu.cmu.cs.in.base.INBase;

import java.awt.Color;

import javax.media.j3d.*;

import javax.vecmath.*;

/**
 * 
 */
public class INHoopJava3D extends INBase
{
	private PolygonAttributes pa=new PolygonAttributes();
    private SimpleUniverse universe=null;
    private Appearance cubeAppearance=null;
    private BranchGroup rootGroup=null;
    private BoundingSphere bounds=null;
    
    private float hexHalfHeight=0.5f;
    private float hexRadius=0.866f;

    /**
     * 
     */
    public INHoopJava3D () 
    {
		setClassName ("INHoopJava3D");
		debug ("INHoopJava3D ()");
		
    	pa.setCapability(PolygonAttributes.ALLOW_MODE_WRITE);    	
    }
    /**
     * 
     */
    public void create ()
    {
        createUniverse();
        createAppearance();
        //createCubeOfCubes();        
        createHexGrid ();
        createLights();
        createBehaviourInteractors();
        
        universe.getViewingPlatform().setNominalViewingTransform();       
        universe.addBranchGraph(rootGroup);
        
        //pa.setPolygonMode(PolygonAttributes.POLYGON_POINT);
        //pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        
        //pa.setBackFaceNormalFlip(true);
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
        universe = new SimpleUniverse();
        rootGroup = new BranchGroup();
        
        bounds = new BoundingSphere (new Point3d(0.0, 0.0, 0.0), 100.0);        
    }
    /**
     * 
     */
    private void createAppearance() 
    {
        cubeAppearance = new Appearance();
        cubeAppearance.setPolygonAttributes (pa);
        Color3f ambientColour = new Color3f();
        ambientColour.set (Color.GRAY);
        Color3f diffuseColour = new Color3f();        
        diffuseColour.set (Color.GRAY);
        
        Color3f emissiveColour = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f specularColour = new Color3f(1.0f, 1.0f, 1.0f);

        float shininess = 20.0f;
        cubeAppearance.setMaterial(new Material(ambientColour, emissiveColour,diffuseColour, specularColour, shininess));
    }
    /**
     * 
     */
    private void createHexGrid ()
    {
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
	    TriangleArray tri = new TriangleArray (18,TriangleArray.COORDINATES);
	    
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
		ng.generateNormals(geometryInfo);	    
		
		GeometryArray result = geometryInfo.getGeometryArray();
	    
		Shape3D shape = new Shape3D(result,cubeAppearance);
	    
		return (shape);
    }
    /**
     * 
     */
    private void createCubeOfCubes() 
    {
        // build up a cube of 10 cube for each axis (x, y, z)
        for (float x = -.5f; x <= .5f; x = x + .1f) 
        {
            for (float y = -.5f; y <= .5f; y += .1f) 
            {
                for (float z = -.5f; z <= .5f; z += .1f) 
                {
                    Box box = new Box(0.02f, 0.02f, 0.02f, cubeAppearance);
                                        
                    TransformGroup tg = new TransformGroup();
                    Transform3D transform = new Transform3D();
                    Vector3f vector = new Vector3f(x, y, z);
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
    	TransformGroup VpTG = universe.getViewingPlatform().getViewPlatformTransform();

    	float Zcamera = 12; //put the camera 12 meters back
    	Transform3D Trfcamera = new Transform3D();
    	Trfcamera.setTranslation(new Vector3f(0.0f, 0.0f, Zcamera));  
    	VpTG.setTransform (Trfcamera);    	
    }
    /**
     * 
     */
    public static void main(String[] args) 
    {
    	INHoopJava3D driver=new INHoopJava3D();
    }
}