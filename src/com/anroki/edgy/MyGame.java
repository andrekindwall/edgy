package com.anroki.edgy;

import com.anroki.edgy.camera.RotationCamera;
import com.anroki.edgy.objects.Player;
import com.anroki.edgy.world.Chunk;
import com.anroki.edgy.world.Cube;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MyGame extends SimpleApplication implements ActionListener {

	private Spatial sceneModel;
	private BulletAppState bulletAppState;
	private RigidBodyControl landscape;
	private Player player;
	private Vector3f walkDirection = new Vector3f();
	private boolean left = false, right = false, up = false, down = false;

	// Temporary vectors used on each frame.
	// They here to avoid instanciating new vectors on each frame
	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	
	public MyGame() {
		super(new StatsAppState());
	}
	  
	/**
	 * Main method.
	 * @param args arguments.
	 */
	public static void main(String[] args) {
		MyGame app = new MyGame();
		app.start();
	}
	
	@Override
	public void simpleInitApp() {
		// Set up Physics
	    bulletAppState = new BulletAppState();
	    stateManager.attach(bulletAppState);
	    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	 
	    // We re-use the flyby camera for rotation, while positioning is handled by physics
	    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
	    setUpKeys();
	    setUpLight();
	 
	    // We load the scene from the zip file and adjust its size.
	    assetManager.registerLocator("town.zip", ZipLocator.class);
	    sceneModel = assetManager.loadModel("main.scene");
	    sceneModel.setLocalScale(2f);
	    
	    //We create our custom camera to prevent looking upside down
	    RotationCamera newFlyCam = new RotationCamera(cam);
	    newFlyCam.registerWithInput(inputManager);
	    newFlyCam.setMoveSpeed(500);
	 
	    // We set up collision detection for the scene by creating a
	    // compound collision shape and a static RigidBodyControl with mass zero.
	    CollisionShape sceneShape =
	            CollisionShapeFactory.createMeshShape((Node) sceneModel);
	    landscape = new RigidBodyControl(sceneShape, 0);
	    sceneModel.addControl(landscape);
	 
	    // We create the player
	    // We also put the player in its starting position.
	    //TODO: Remove this material. Temporary when developing custom onGround method.
	    Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Green); 
		material.setBoolean("VertexColor",true); 
	    player = new Player(rootNode, material);
	    player.createObject(Chunk.CHUNK_WIDTH*Cube.SIZE/2, 64*Cube.SIZE, Chunk.CHUNK_DEPTH*Cube.SIZE/2);
	    
	    // We attach the scene and the player to the rootnode and the physics space,
	    // to make them appear in the game world.
	    rootNode.attachChild(sceneModel);
	    bulletAppState.getPhysicsSpace().add(landscape);
	    bulletAppState.getPhysicsSpace().add(player.getCharacterControl());
	    
//	    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	    
	    createChunks();
	}
	
	private void createChunks() {
		Chunk chunk = new Chunk(assetManager, bulletAppState.getPhysicsSpace(), 0, 0);
		rootNode.attachChild(chunk);
	}

	private void setUpLight() {
		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1.3f));
		rootNode.addLight(al);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
		rootNode.addLight(dl);
	}

	/**
	 * We over-write some navigational key mappings here, so we can add
	 * physics-controlled walking and jumping:
	 */
	private void setUpKeys() {
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
	}

	/**
	 * These are our custom actions triggered by key presses. We do not walk
	 * yet, we just keep track of the direction the user pressed.
	 */
	public void onAction(String binding, boolean isPressed, float tpf) {
		if (binding.equals("Left")) {
			left = isPressed;
		} else if (binding.equals("Right")) {
			right = isPressed;
		} else if (binding.equals("Up")) {
			up = isPressed;
		} else if (binding.equals("Down")) {
			down = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) {
				player.jump();
			}
		}
	}
	
	
	//TODO: Tempor�r kod
	int i = 0;
	int x = 0;
	int y = 0;
	int z = 0;
	
	/**
	 * This is the main event loop--walking happens here. We check in which
	 * direction the player is walking by interpreting the camera direction
	 * forward (camDir) and to the side (camLeft). The setWalkDirection()
	 * command is what lets a physics-controlled player walk. We also make sure
	 * here that the camera moves with player.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		moveCharacter();
//		System.out.println("using: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1048576f) + " mb");
		
		//TODO: Tempor�r kod
		//Ta bort block lite hela tiden och sen optimera chunken
//		if(i % 100 == 0){
//			if (chunk != null) {
//				System.out.println("using: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576f) + " mb");
//				chunk.removeBlock(x, y, z);
//				chunk.detachAllChildren();
//				chunk.showVisibleFaces();
//				GeometryBatchFactory.optimize(chunk);
//				x++;
//				if(x==Chunk.CHUNK_WIDTH){
//					x = 0;
//					y++;
//					if(y==Chunk.CHUNK_HEIGHT){
//						y = 0;
//						z++;
//						if(z==Chunk.CHUNK_DEPTH){
//							z = 0;
//						}
//					}
//				}
//			}
//		}
//		
//		if(i % 400 == 0){
//			
//		}
//		i++;
	}
	
	private void moveCharacter(){
		walkDirection.set(0, 0, 0);
		if(left || right || up || down){
			camLeft.set(cam.getLeft()).multLocal(player.getSpeed());
			if(up || down){
				camDir.set(-camLeft.z, 0, camLeft.x);
			}
			if (left) {
				walkDirection.addLocal(camLeft);
			}
			if (right) {
				walkDirection.addLocal(camLeft.negate());
			}
			if (up) {
				walkDirection.addLocal(camDir);
			}
			if (down) {
				walkDirection.addLocal(camDir.negate());
			}
		}
		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
	}
	
}
