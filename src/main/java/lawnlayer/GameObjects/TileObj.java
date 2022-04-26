package lawnlayer.GameObjects;

import processing.core.PImage;
import lawnlayer.App;
import processing.core.PApplet;

class Location{
    protected int x;
    protected int y;
    protected int top;
    protected int bottom;
    protected int left;
    protected int right;

    protected void syncLoc(){
        this.top = this.y;
        this.bottom = this.y + App.SPRITESIZE;
        this.left = this.x;
        this.right = this.x + App.SPRITESIZE;
    }

    public Location(int x, int y){
        this.x = x;
        this.y = y;
        this.syncLoc();
    }
}

public class TileObj extends Location{

    protected PImage sprite;
    protected String spritePath;

    public TileObj(int x, int y, String spritePath){
        super(x, y);
        this.spritePath = spritePath;
    }

    public void setSprite(App app){
        this.sprite = app.getImageFromPath(this.spritePath);
    }

    public void move(int horiz, int vert){
        this.x  += horiz;
        this.y += vert;
        this.syncLoc();
    }

    public void draw(PApplet app){
        app.image(this.sprite, this.x, this.y);
    }
}
