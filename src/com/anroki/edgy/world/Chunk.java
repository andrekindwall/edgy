package com.anroki.edgy.world;

import java.util.Random;

import com.anroki.edgy.world.Cube.Direction;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

public class Chunk extends Node{

	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;
	public static final int CHUNK_HEIGHT = 64;
	
	private BlockSpace[][][] blockSpaces = new BlockSpace[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_DEPTH];
	
	public Chunk(AssetManager am, PhysicsSpace physicsSpace, int lowerX, int lowerY) {
		Texture texture = am.loadTexture("/Textures/dirt.jpg");
		Texture texture1 = am.loadTexture("/Textures/stone.jpg");
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setTexture("ColorMap", texture);
		Material material1 = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material1.setTexture("ColorMap", texture1);
		Quad quad = new Quad(Cube.SIZE, Cube.SIZE);
		Random rand = new Random();
		
		for(int x=lowerX; x<lowerX+CHUNK_WIDTH; x++){
			for(int y=lowerY; y<lowerY+CHUNK_HEIGHT; y++){
				for(int z=0; z<CHUNK_DEPTH; z++){
					BlockSpace blockSpace = new BlockSpace(x, y, z);
					if(y < 63){
						blockSpace.setCube(new Cube(rand.nextBoolean() ? material : material1, quad, this, physicsSpace, x, y, z));
					}
					blockSpaces[x][y][z] = blockSpace;
				}
			}
		}
		
		showVisibleFaces();
		
	}
	
	//TODO This method should return an enum telling which block type was removed
		public void removeBlock(int x, int y, int z){
			Cube cube = blockSpaces[x][y][z].getCube();
			if(cube != null){
				cube.detach();
				blockSpaces[x][y][z].setCube(null);
				cube = null;
				processBlockSpace(x, y, z);
			}
		}
	
	/**
	 * Loops through all blocks in the chunk and process them with {@link #processBlockSpace(int, int, int)}
	 */
	public void showVisibleFaces(){
		for(int x=0; x<CHUNK_WIDTH; x++){
			for(int y=0; y<CHUNK_HEIGHT; y++){
				for(int z=0; z<CHUNK_DEPTH; z++){
					
					processBlockSpace(x, y, z);
					
				}
			}
		}
	}
	
	/**
	 * Checks if blockspace is transparent, if so then it reveals all adjacent blocks, otherwise it hides them.<br>
	 * Also revealing the block faces if [x|y|z] = 0 or [x|y|z] = [maxvalue].
	 * @param x
	 * @param y
	 * @param z
	 */
	private void processBlockSpace(int x, int y, int z){
		if(blockSpaces[x][y][z].isTransparent()){
			
			if(x>0){
				blockSpaces[x-1][y][z].setFaceVisibility(Direction.EAST, true);
			}
			if(x<CHUNK_WIDTH-1){
				blockSpaces[x+1][y][z].setFaceVisibility(Direction.WEST, true);
			}
			if(y>0){
				blockSpaces[x][y-1][z].setFaceVisibility(Direction.UP, true);
			}
			if(y<CHUNK_HEIGHT-1){
				blockSpaces[x][y+1][z].setFaceVisibility(Direction.DOWN, true);
			}
			if(z>0){
				blockSpaces[x][y][z-1].setFaceVisibility(Direction.SOUTH, true);
			}
			if(z<CHUNK_DEPTH-1){
				blockSpaces[x][y][z+1].setFaceVisibility(Direction.NORTH, true);
			}
			
		} else {
			
			if(x>0){
				blockSpaces[x-1][y][z].setFaceVisibility(Direction.EAST, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.WEST, true);
			}
			
			if(x<CHUNK_WIDTH-1){
				blockSpaces[x+1][y][z].setFaceVisibility(Direction.WEST, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.EAST, true);
			}
			
			if(y>0){
				blockSpaces[x][y-1][z].setFaceVisibility(Direction.UP, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.DOWN, true);
			}
			
			if(y<CHUNK_HEIGHT-1){
				blockSpaces[x][y+1][z].setFaceVisibility(Direction.DOWN, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.UP, true);
			}
			
			if(z>0){
				blockSpaces[x][y][z-1].setFaceVisibility(Direction.SOUTH, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.NORTH, true);
			}
			
			if(z<CHUNK_DEPTH-1){
				blockSpaces[x][y][z+1].setFaceVisibility(Direction.NORTH, false);
			} else {
				blockSpaces[x][y][z].setFaceVisibility(Direction.SOUTH, true);
			}
			
		}
	}
	
	
}
