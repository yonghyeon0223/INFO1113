package lawnlayer;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
//import processing.data.JSONObject;
//import processing.data.JSONArray;
//import processing.core.PFont;

public class App extends PApplet {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    public String configPath;
	
	public PImage grass;
    public PImage concrete;
    public PImage worm;
    public PImage beetle;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
        this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        this.worm = loadImage(this.getClass().getResource("worm.png").getPath());
        this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
        

    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {

    }


    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
