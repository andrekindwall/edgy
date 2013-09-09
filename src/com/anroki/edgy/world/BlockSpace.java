package com.anroki.edgy.world;

import com.anroki.edgy.world.Block.Direction;

public class BlockSpace {

	private Block cube;

	private int x;
	private int y;
	private int z;
	
	public BlockSpace(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * True if BlockSpace contains air or any block that has any transparent texture.
	 * @return
	 */
	public boolean isTransparent(){
		return cube == null || cube.isTransparent();
	}
	
	/**
	 * @see {@link com.anroki.edgy.world.Block#setFaceVisibility(Direction, boolean) Cube.setFaceVisibility}
	 */
	public void setFaceVisibility(Direction dir, boolean visible){
		if(cube != null){
			cube.setFaceVisibility(dir, visible);
		}
	}
	
	/**
	 * @return true if no cube is attached, otherwise false.
	 */
	public boolean isAir(){
		return cube == null;
	}
	
	public void setCube(Block cube) {
		this.cube = cube;
	}
	
	public Block getCube() {
		return cube;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
}
