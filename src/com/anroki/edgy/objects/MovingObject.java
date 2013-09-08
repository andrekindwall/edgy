package com.anroki.edgy.objects;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;

/**
 * Helper method that holds a moving object.
 * @author drewi
 *
 */
public abstract class MovingObject {
	
	private CharacterControl controller;
	
	/**
	 * @param name The name of the scene element. This is required for identification and comparision purposes.
	 */
	protected MovingObject() {
		
	}
	
	/**
	 * Creates object
	 * @param x local x translation
	 * @param y local y translation
	 * @param z local z translation
	 */
	public void createObject(float x, float y, float z){
		createObject(new Vector3f(x, y, z));
	}
	
	/**
	 * Creates object
	 * @param localTranslation Local translation
	 */
	public void createObject(Vector3f localTranslation) {
		CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(getRadius(), getHeight(), getAxis().getValue());
		controller = new CharacterControl(capsuleShape, 0.01f);
		controller.setJumpSpeed(getJumpSpeed());
		controller.setFallSpeed(getFallSpeed());
	    controller.setGravity(getGravity());
	    controller.setPhysicsLocation(localTranslation);
		
	}
	
	/**
	 * Makes the character jump with the set jump force.
	 */
	public void jump() {
		controller.jump();
	}
	
	public CharacterControl getCharacterControl(){
		return controller;
	}
	
	/**
	 * Sets the walk direction of the character. This parameter is framerate independent and the character will move
	 * continuously in the direction given by the vector with the speed given by the vector length in m/s.
	 * @param walkDirection The movement direction and speed in m/s
	 */
	public void setWalkDirection(Vector3f walkDirection) {
		controller.setWalkDirection(walkDirection);
	}
	
	public Vector3f getPhysicsLocation() {
		return controller.getPhysicsLocation();
	}

	/**
	 * @return The radius of the object.
	 */
	protected abstract float getRadius();
	
	/**
	 * @return The height of the object.
	 */
	protected abstract float getHeight();
	
	/**
	 * @return Set Axis.
	 */
	protected abstract Axis getAxis();
	
	/**
	 * @return The jump speed of the object.
	 */
	protected abstract float getJumpSpeed();
	
	/**
	 * @return The fall speed of the object.
	 */
	protected abstract float getFallSpeed();
	
	/**
	 * @return The object gravity.
	 */
	protected abstract float getGravity();
	
	protected enum Axis{
		X(0),
		Y(1),
		Z(2);
		private final int axis;
		private Axis(int axis){
			this.axis = axis;
		}
		public int getValue() {
			return axis;
		}
	}
	
}
