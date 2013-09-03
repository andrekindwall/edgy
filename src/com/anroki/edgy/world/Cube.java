package com.anroki.edgy.world;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class Cube {

	public static final float SIZE = 4;
	private static final float HALF_SIZE = SIZE / 2f;
	
	private Geometry geometries[] = new Geometry[6];

	private Quad quad;
	private Material material;
	private Node parent;

	private float centerX;
	private float centerY;
	private float centerZ;
	
	public Cube(Material material, Quad quad, Node parent, float x, float y, float z){
		this.material = material;
		this.parent = parent;
		this.quad = quad;
		
		centerX = x - HALF_SIZE;
		centerY = y + HALF_SIZE;
		centerZ = z + HALF_SIZE;
	}
	
	/**
	 * Creates a face (quad) of the cube and place it in the geometry array.
	 * @param dir The face direction
	 * @return The created geometry
	 */
	private Geometry createFace(Direction dir){
		Geometry geo = new Geometry(dir.name(), quad);
		geo.setMaterial(material);
		geometries[dir.ordinal()] = geo;
		switch (dir) {
		case NORTH:
			geo.setLocalTranslation(centerX+SIZE, centerY-SIZE, centerZ-SIZE);
			geo.rotate(0, 180*FastMath.DEG_TO_RAD, 0);
			break;
		case SOUTH:
			geo.setLocalTranslation(centerX, centerY-SIZE, centerZ);
			//South is already correct rotated
			break;
		case EAST:
			geo.setLocalTranslation(centerX+SIZE, centerY-SIZE, centerZ);
			geo.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
			break;
		case WEST:
			geo.setLocalTranslation(centerX, centerY-SIZE, centerZ-SIZE);
			geo.rotate(0, -90*FastMath.DEG_TO_RAD, 0);
			break;
		case UP:
			geo.setLocalTranslation(centerX, centerY, centerZ);
			geo.rotate(-90*FastMath.DEG_TO_RAD, 0, 0);
			break;
		case DOWN:
			geo.setLocalTranslation(centerX, centerY-SIZE, centerZ-SIZE);
			geo.rotate(90*FastMath.DEG_TO_RAD, 0, 0);
			break;
		}
		return geo;
	}
	
	/**
	 * @return True if texture contains any transparent pixels, otherwise false.
	 */
	public boolean isTransparent(){
		return false;
	}
	
	/**
	 * Set visibility of a single face on the cube.
	 * @param dir Face direction
	 * @param visible true if face should be visible
	 */
	public void setFaceVisibility(Direction dir, boolean visible){
		Geometry geo = geometries[dir.ordinal()];
		if(visible){
			if(geo == null){
				parent.attachChild(createFace(dir));
			}
		} else {
			if(geo != null){
				parent.detachChild(geo);
				geo = null;
			}
		}
	}
	
	/**
	 * Directions of a cubes six sides.
	 * @author drewi
	 *
	 */
	public enum Direction{
		NORTH,
		SOUTH,
		EAST,
		WEST,
		UP,
		DOWN;
	}
	
}