package com.anroki.edgy.objects;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;


public class Player extends MovingObject {
	
	private Node world;
	Material material;
	public Player(Node world, Material mat) {
		super();
		this.world = world;
		this.material = mat;
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
	
	public boolean onGround() {
		return getCharacterControl().onGround();
//		Vector3f origin = getPhysicsLocation();
//		origin.setY(origin.y- getHeight()/2f);
//        Vector3f direction = origin.clone();
//        direction.setY(direction.y-10);
//        direction.normalizeLocal();
//        
//        //TODO: Remove this line drawing
//        Mesh mesh = new Mesh();
//        mesh.setMode(Mesh.Mode.Lines);
//        mesh.setBuffer(VertexBuffer.Type.Position, 3, new float[]{ direction.x, direction.y, direction.z, origin.x, origin.y, origin.z });
//        mesh.setBuffer(VertexBuffer.Type.Index, 2, new short[]{ 0, 1 });
//        Geometry geo = new Geometry("line", mesh);
//        geo.setMaterial(material);
//        world.attachChild(geo);
//
//        Ray ray = new Ray(origin, direction);
//        CollisionResults results = new CollisionResults();
//        int numCollisions = world.collideWith(ray, results);
//        if (numCollisions > 0) {
//            CollisionResult hit = results.getClosestCollision();
//            System.out.println("collide " + hit.getContactPoint());
//            return true;
//        }
//        return false;
	}
	
}
