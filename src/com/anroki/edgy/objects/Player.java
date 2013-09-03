package com.anroki.edgy.objects;


public class Player extends MovingObject {

	public Player() {
		super();
	}
	
	/**
	 * @return The speed of the player. (not the current speed)
	 */
	public float getSpeed(){
		return 0.4f;
	}

	@Override
	protected float getRadius() {
		return 1.5f;
	}

	@Override
	public float getHeight() {
		return 6f;
	}

	@Override
	protected int getAxis() {
		return 1;
	}

	@Override
	protected float getJumpSpeed() {
		return 15f;
	}

	@Override
	protected float getFallSpeed() {
		return 30f;
	}

	@Override
	protected float getGravity() {
		return 30f;
	}
	
}
