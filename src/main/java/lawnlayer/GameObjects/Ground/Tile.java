package lawnlayer.GameObjects.Ground;

import lawnlayer.GameObjects.TileObj;

public class Tile extends TileObj{

    public Tile(int x, int y, String spritePath){
        super(x, y, spritePath);
    }
    
     public int getX(){
         return this.x;
     }
     public int getY(){
         return this.y;
     }

}
