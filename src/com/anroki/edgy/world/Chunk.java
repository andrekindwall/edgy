package com.anroki.edgy.world;

import java.util.Random;

import com.anroki.edgy.world.Cube.Direction;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

public class Chunk extends Node{

	public static final int CHUNK_WIDTH = 16;
	public static final int CHUNK_DEPTH = 16;
	public static final int CHUNK_HEIGHT = 64;
	
	private BlockSpace[][][] blockSpaces = new BlockSpace[CHUNK_WIDTH][CHUNK_HEIGHT][CHUNK_DEPTH];
	
	public Chunk(AssetManager am) {
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Magenta);
		Material material1 = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material1.setColor("Color", ColorRGBA.Blue);
		Quad quad = new Quad(Cube.SIZE, Cube.SIZE);
		Random rand = new Random();
		
		for(int x=0; x<CHUNK_WIDTH; x++){
			for(int y=0; y<CHUNK_HEIGHT; y++){
				for(int z=0; z<CHUNK_DEPTH; z++){
					BlockSpace blockSpace = new BlockSpace(x, y, z);
					blockSpace.setCube(new Cube(rand.nextBoolean() ? material : material1, quad, this, x*Cube.SIZE, y*Cube.SIZE, z*Cube.SIZE));
					blockSpaces[x][y][z] = blockSpace;
				}
			}
		}
		
		showVisibleFaces();
		
	}
	
	/**
	 * Loops through all blocks in the chunk to see which faces is next to air.<br>
	 * If a face is next to air then it will be visible, otherwise we hide it.
	 */
	private void showVisibleFaces(){
		for(int x=0; x<CHUNK_WIDTH; x++){
			for(int y=0; y<CHUNK_HEIGHT; y++){
				for(int z=0; z<CHUNK_DEPTH; z++){
					
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
		}
	}
	
	
}
