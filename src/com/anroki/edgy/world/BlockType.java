package com.anroki.edgy.world;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.texture.Texture;

public enum BlockType{

	STONE("/Textures/stone.jpg", false),
	
	GLASS("/Textures/glass.png", true);

	private boolean isTransparent;
	private String texturePath;
	
	private static HashMap<BlockType, Material> materials = new HashMap<>();
	private static AssetManager assetManager;
	
	private BlockType(String texturePath, boolean isTransparent){
		this.texturePath = texturePath;
		this.isTransparent = isTransparent;
	}
	
	public static void initAssetManager(AssetManager assetManager){
		BlockType.assetManager = assetManager;
	}
	
	public boolean isTransparent() {
		return isTransparent;
	}
	
	public Material getMaterial(){
		if(!materials.containsKey(this)){
			Texture texture = assetManager.loadTexture(texturePath);
			Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			material.setTexture("ColorMap", texture);
			if(isTransparent){
				material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
			}
			materials.put(this, material);
		}
		return materials.get(this);
	}
}