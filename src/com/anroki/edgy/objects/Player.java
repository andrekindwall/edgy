package com.anroki.edgy.objects;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class Player extends MovingObject {
	
	private Node world;
	
	public Player(Node world) {
		super();
		this.world = world;
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
		return 5f;
	}

	@Override
	protected Axis getAxis() {
		return Axis.Y;
	}

	@Override
	protected float getJumpSpeed() {
		return 15f;
	}

	@Override
	protected float getFallSpeed() {
		return 60f;
	}

	@Override
	protected float getGravity() {
		return 50f;
	}
	
	public float getFlyspeed() {
		return 0.8f;
	}
	
	@Override
	public void jump() {
		if(onGround()){
			super.jump();
		}
	}
	
	public void flyMode(boolean on){
		if (on) this.getCharacterControl().setFallSpeed(0);
		else if (on == false) this.getCharacterControl().setFallSpeed(30);
	}
	
	public boolean onGround() {
		Vector3f origin = getPhysicsLocation();
		origin.setY(origin.y- getHeight()/2f);
        Vector3f direction = new Vector3f(0, -1, 0);

        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
        world.collideWith(ray, results);
        if (results.size() > 0) {
            CollisionResult hit = results.getClosestCollision();
            if(hit.getDistance() < 1.55f){
            	return true;
            }
        }
        return false;
	}
	
}
