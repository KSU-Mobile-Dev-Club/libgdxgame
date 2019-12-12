package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class Drop extends ApplicationAdapter {
	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Rectangle bucket;
	private Vector3 touchPos;
	//possible problem
	private Array<Rectangle> raindrops;
	private long lastDropTime;
	
	
	@Override
	public void create() {
		// load the images for the droplet and the bucket, 64x64 pixels each
	      dropImage = new Texture(Gdx.files.internal("droplet.png"));
	      bucketImage = new Texture(Gdx.files.internal("bucket.png"));
	      
	      // load the drop sound effect and the rain background "music"
	      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
	      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
	      
	      // start the playback of the background music immediately
	      rainMusic.setLooping(true);
	      rainMusic.play();
	      
	      // make sure the camera always hows us an area of our game that is 
	      // 800x480 units wide
	      camera = new OrthographicCamera();
	      camera.setToOrtho(false, 800, 480);
	      
	      batch = new SpriteBatch();
	      
	      // Create the Rectangle and specify its initial values
	      // Put the bucket 20 pixels above the bottom edge of the screen
	      bucket = new Rectangle();
	      bucket.x = 20;
	      bucket.y = 20;
	      bucket.width = 64;
	      bucket.height = 64;
	      
	      // Spawn our first raindrop
	      raindrops = new Array<Rectangle>();
	      spawnRaindrop();
	      
	}

	@Override
	public void render () {
		// Set the clear color to the color blue
		Gdx.gl.glClearColor(0.4f, 0.8f,1f, 0);
		// Actually clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Update the camera once per frame
		camera.update();
		
		// Tells the SpriteBatch to use the coordinate system specified by the camera
		// camera.combined field is a matrix
		// From here on, SpriteBatch will render everything in the coordinate system
		batch.setProjectionMatrix(camera.combined);
		// Record all drawing commands between begin() and end()
		// Speeds up rendering a ton
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
	    	batch.draw(dropImage, raindrop.x, raindrop.y);
	    }
		batch.end();
		
		// Ask whether the screen is currently touched (or a mouse is pressed)
		if (Gdx.input.isTouched()) {
			// three dimensional vector
			// will contain the touch/mouse coordinates in the coordinate system our
			// bucket lives in
			touchPos = new Vector3();
			// return the current touch/mouse position
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// transforms these coordinates to our camera's coordinate system
			camera.unproject(touchPos);
			// change the position of the bucket to be centered around the touch/mouse coordinates
			bucket.x = touchPos.x - 64 / 2;
		}
		
		// Need to take in keyboard input also!
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			bucket.y -= 200 * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			bucket.y += 200 * Gdx.graphics.getDeltaTime();
		}
		
		// Makes sure bucket stays within the screen limits!
		if (bucket.y < 0) {
			bucket.y = 0;
		}
		if (bucket.y > 400) {
			bucket.y = 400;
		}
		
	      
	      //Checks how much time has passed since we spawned a new raindrop, and
	      // creates one if necessary
	      if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
	    	  spawnRaindrop();
	      }
	      
	      // In order to make the raindrops move!
	      // Move at a constant speed of 200 pixels/units per second
	      // If the raindrop is beneath the bottom edge of the screen, remove
	      // if from the array!
	      // If the raindrop hits the bucket, play drop sound and remove the
	      // raindrop from the array!
	      for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext(); ) {
	          Rectangle raindrop = iter.next();
	          raindrop.x -= 200 * Gdx.graphics.getDeltaTime();
	          if(raindrop.x == 0) {
	        	  iter.remove();
	          }
	          if(raindrop.overlaps(bucket)) {
		    	  dropSound.play();
		    	  iter.remove();
		      }
	       }
	}
	
	// Spawns a raindrop at a random x position
	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
	    raindrop.y = MathUtils.random(0, 800-64);
	    raindrop.x = 480;
	    raindrop.width = 64;
	    raindrop.height = 64;
	    raindrops.add(raindrop);
	    // Time used to decide whether to spawn a new drop or not (yet)
	    lastDropTime = TimeUtils.nanoTime();
	}
	
	// Clean up after the application is closed!
	@Override
	public void dispose () {
		dropImage.dispose();
	    bucketImage.dispose();
	    dropSound.dispose();
	    rainMusic.dispose();
	    batch.dispose();
	}
}
