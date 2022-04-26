package lawnlayer;

import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.units.qual.A;
import lawnlayer.GameObjects.*;
import lawnlayer.GameObjects.Ground.TileManager;
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
    
    private Player player;
    private BackGround background;
    private List<Enemy> enemies;

    public App() {
        this.configPath = "config.json";
    }

    public PImage getImageFromPath(String path){
        return loadImage(this.getClass().getResource(path).getPath());
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        frameRate(FPS);
        this.player = new Player();
        this.player.setSprite(this);

        this.background = new BackGround();
        this.background.setSprite(this);

        TileManager.InitialiseMap("level2.txt", this);

        this.enemies = new ArrayList<>();
        this.enemies.add(new Worm("random"));
        this.enemies.add(new Worm("random"));
        for (Enemy enem : this.enemies)
            enem.setSprite(this);

        /*this.player.run(this);
        for (Enemy e : this.enemies)
            e.run(this, this.player);*/
    }
 
    public void draw() {
        this.background.draw(this);

        this.player.playerMove();
        this.player.setPath(this);
        if (this.player.stepPathAgain())
            this.player.died(this);

        for (Enemy en : this.enemies){
            en.EnemyMove();
            en.checkEnemyHitPlayerPath(this.player, this);
        }

        TileManager.updateMap(this.player, this, this.enemies);

        TileManager.printMap(this);
        this.player.draw(this);
        for (Enemy en : this.enemies)
            en.draw(this);
    }

    public void keyPressed() {
        player.setInputPress(this.keyCode);
    }

    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
    
}
