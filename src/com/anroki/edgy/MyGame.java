package com.anroki.edgy;

import com.anroki.edgy.camera.RotationCamera;
import com.anroki.edgy.objects.Player;
import com.anroki.edgy.world.Block;
import com.anroki.edgy.world.BlockType;
import com.anroki.edgy.world.Chunk;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;

public class MyGame extends SimpleApplication implements ActionListener {

	private BulletAppState bulletAppState;
	private Player player;
	private Vector3f walkDirection = new Vector3f();
	private long currentTime = 0, lastTime = 0;
	private double difference;
	private boolean left = false, right = false, up = false, down = false, space = false, flydown = false;

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
		BlockType.initAssetManager(assetManager);
		initCrossHairs();
		
		// Set up Physics
	    bulletAppState = new BulletAppState();
	    stateManager.attach(bulletAppState);
	    //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	 
	    // We re-use the flyby camera for rotation, while positioning is handled by physics
	    viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
	    setUpKeys();
	    setUpLight();
	    
	    //We create our custom camera to prevent looking upside down
	    RotationCamera newFlyCam = new RotationCamera(cam);
	    newFlyCam.registerWithInput(inputManager);
	    newFlyCam.setMoveSpeed(500);
	 
	    // We create the player
	    // We also put the player in its starting position.
	    player = new Player(rootNode);
	    player.createObject(Chunk.CHUNK_WIDTH*Block.SIZE/2, 64*Block.SIZE, Chunk.CHUNK_DEPTH*Block.SIZE/2);
	    
	    // We attach the scene and the player to the rootnode and the physics space,
	    // to make them appear in the game world.
	    bulletAppState.getPhysicsSpace().add(player.getCharacterControl());
	    
//	    bulletAppState.getPhysicsSpace().enableDebug(assetManager);
	    
	    createChunks();
	}
	
	protected void initCrossHairs() {
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setLocalTranslation(
				// center
				settings.getWidth() / 2 - ch.getLineWidth() / 2,
				settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
		guiNode.attachChild(ch);
	}
	
	Chunk chunk;
	private void createChunks() {
		chunk = new Chunk(bulletAppState.getPhysicsSpace(), 0, 0);
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
		inputManager.addMapping("Flydown", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("MouseLeft", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
		inputManager.addListener(this, "Flydown");
		inputManager.addListener(this, "MouseLeft");
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
		} else if (binding.equals("Flydown")) {
			flydown = isPressed;
		} else if (binding.equals("Jump")) {
			if (isPressed) {
				lastTime = currentTime;
				currentTime = System.currentTimeMillis();
				difference = (currentTime - lastTime);
				player.flyMode(difference);
				player.jump();
			}
			space = isPressed;
		} else if (binding.equals("MouseLeft")) {
			if (isPressed) {
				shoot();
			}
		}
	}
	
	private void shoot(){
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        rootNode.collideWith(ray, results);
        
        if (results.size() > 0) {
          CollisionResult closest = results.getClosestCollision();
          int x = closest.getGeometry().getUserData("x");
          int y = closest.getGeometry().getUserData("y");
          int z = closest.getGeometry().getUserData("z");
          chunk.removeBlock(x, y, z);
        }
	}
	
	//TODO: Temporär kod
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
		// System.out.println("using: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/1048576f) + " mb");
		
		//TODO: Temporär kod
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
		if (space && player.getCharacterControl().getFallSpeed() == 0){
			player.getCharacterControl().setPhysicsLocation(new Vector3f(player.getPhysicsLocation().x, player.getPhysicsLocation().y + player.getFlyspeed(), player.getPhysicsLocation().z));
		}
		else if(flydown && player.getCharacterControl().getFallSpeed() == 0){
			player.getCharacterControl().setPhysicsLocation(new Vector3f(player.getPhysicsLocation().x, player.getPhysicsLocation().y - player.getFlyspeed(), player.getPhysicsLocation().z));
		}

		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
	}
	
}
