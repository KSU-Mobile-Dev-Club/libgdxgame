package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import java.util.Random;

public class Drop extends ApplicationAdapter {
	private Texture pipeImage;
	private Texture birdImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private Rectangle bird;
	private Vector3 touchPos;
	//possible problem
	private Array<Rectangle> pipes;
	//test
	private Array<Rectangle> collisionObject;
	private long lastDropTime;
	
	private long score;
	
	
	@Override
	public void create() {
		  // load the images for the pipe and the bird,
		  // the bird is 34x24 pixels
		  // the pipe is 52x320 pixels
	      pipeImage = new Texture(Gdx.files.internal("pipe-green.png"));
	      birdImage = new Texture(Gdx.files.internal("yellowbird-midflap.png"));
	      
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
	      font = new BitmapFont();
	      
	      // Create the Rectangle and specify its initial values
	      // Put the bird 20 pixels above the bottom edge of the screen
	      bird = new Rectangle();
	      bird.x = 20;
	      bird.y = 20;
	      bird.width = 34;
	      bird.height = 24;
	      
	      // set score to 0
	      score = 0;
	      
	      // Spawn our first pipe
	      pipes = new Array<Rectangle>();
	      collisionObject = new Array<Rectangle>();
	      spawnPipe();
	      
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
		batch.draw(birdImage, bird.x, bird.y);
		// Increases the size of font!
		font.getData().setScale(2.5f);
		// Draws the font itself
		font.draw(batch, Long.toString(score), 25, 450);
		for(Rectangle raindrop: pipes) {
	    	batch.draw(pipeImage, raindrop.x, raindrop.y);
	    }
		for (Rectangle collision: collisionObject) {
			batch.draw(pipeImage, collision.x, collision.y);	
		}
		batch.end();
		
		// Ask whether the screen is currently touched (or a mouse is pressed)
		if (Gdx.input.isTouched()) {
			// three dimensional vector
			// will contain the touch/mouse coordinates in the coordinate system our
			// bird lives in
			touchPos = new Vector3();
			// return the current touch/mouse position
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// transforms these coordinates to our camera's coordinate system
			camera.unproject(touchPos);
			// change the position of the bird to be centered around the touch/mouse coordinates
			bird.x = touchPos.x - 64 / 2;
		}
		
		// Need to take in keyboard input also!
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			bird.y -= 200 * Gdx.graphics.getDeltaTime();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			bird.y += 200 * Gdx.graphics.getDeltaTime();
		}
		
		// Makes sure bird stays within the screen limits!
		if (bird.y < 0) {
			bird.y = 0;
		}
		if (bird.y > 400) {
			bird.y = 400;
		}
		
	      
	      //Checks how much time has passed since we spawned a new pipe, and
	      // creates one if necessary
	      if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
	    	  spawnPipe();
	      }
	      
	      // In order to make the pipes move!
	      // Move at a constant speed of 200 pixels/units per second
	      // If the pipe is past the left edge of the screen, remove
	      // it from the array!
	      // If the pipe hits the bird, play drop sound and remove the
	      // pipe from the array!
	      for (Iterator<Rectangle> iter = pipes.iterator(); iter.hasNext(); ) {
	          Rectangle pipe = iter.next();
	          pipe.x -= 200 * Gdx.graphics.getDeltaTime();
	          if(pipe.x == 0) {
	        	  iter.remove();
	          }
	          if(pipe.overlaps(bird)) {
	        	  score = 0;
		    	  dropSound.play();
		    	  iter.remove();
		      }
	       }
	      for (Iterator<Rectangle> iter = collisionObject.iterator(); iter.hasNext(); ) {
	          Rectangle collision = iter.next();
	          collision.x -= 200 * Gdx.graphics.getDeltaTime();
	          if(collision.x == 0) {
	        	  iter.remove();
	          }
	          if(collision.overlaps(bird)) {
	        	  score += 1;
		    	  dropSound.play();
		    	  iter.remove();
		      }
	       }
	}
	
	// Spawns a pipe at the rightmost corner of the screen, at a random y position.
	private void spawnPipe() {
		Rectangle pipe = new Rectangle();
		Rectangle collision = new Rectangle();
		// random.nextInt(max - min) + min; 
		int randomY = new Random().nextInt(0 - (-300)) + (-300);
	    pipe.y = randomY;
	    pipe.x = 810;
	    pipe.width = 52;
	    pipe.height = 320;
	    pipes.add(pipe);
		// random.nextInt(max - min) + min; 
	    // this controls the object above the pipes that acts as collision detection
	    collision.y = pipe.y + 320;
	    collision.x = 810;
	    collision.width = 52;
	    collision.height = 320;
	    collisionObject.add(collision);
	    // Time used to decide whether to spawn a new pipe or not (yet)
	    lastDropTime = TimeUtils.nanoTime();
	}
	
	// Clean up after the application is closed!
	@Override
	public void dispose () {
		pipeImage.dispose();
	    birdImage.dispose();
	    dropSound.dispose();
	    rainMusic.dispose();
	    batch.dispose();
	}
}
