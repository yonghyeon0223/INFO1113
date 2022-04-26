package lawnlayer.GameObjects;

import java.util.ArrayList;
import java.util.List;

import lawnlayer.App;
import lawnlayer.GameObjects.Ground.*;
import processing.core.PApplet;


public class Player extends TileMover{

    private int tempDirection;
    private int speed;
    private int keyPress;
    private int life;

    private List<Integer[]> pathIdx;
    private List<Tile> path;
    private boolean checkPath;

    public static final int L = 37;
    public static final int R = 39;
    public static final int U = 38;
    public static final int D = 40;
    public static final int NotMv = -1;
    public static PApplet paap = new PApplet();

    public Player(){
        super(0, App.TOPBAR, "ball.png");
        this.speed = 2;
        this.tileIdx = new int[2];
        this.pathIdx = new ArrayList<>();
        this.path = new ArrayList<>();
        this.checkPath = false;
        this.life = 3;
    }

    public void playerMove(){
        this.moveByPixel(); // moveing mechanism (by pixel)
        if (this.restrictedArea()) // stay inside the map
            this.keyPress = NotMv;
        this.onWhichTile(); // get current tile
        if (!keepMoving()) // prepraring for next move
            this.keyPress = NotMv; // field, dirt = keep moving! cement = don't!
    }

    public boolean restrictedArea(){
        if (this.left < 0){
            this.move(this.speed, 0);
            return true;
        } else if (this.right > App.WIDTH){
            this.move(-this.speed, 0);
            return true;
        } else if (this.top < App.TOPBAR){
            this.move(0, this.speed);
            return true;
        } else if (this.bottom > App.HEIGHT){
            this.move(0, -this.speed);
            return true;
        }
        return false;
    }


    public boolean onPixel(){
        if (this.x % App.SPRITESIZE + this.y % App.SPRITESIZE == 0){
            this.tempDirection = this.keyPress;
            return true;
        }
        return false;
    }
    
    public void moveByPixel(){
        if (this.onPixel()){
            this.movingDirection(this.keyPress);
        }
        else
            this.movingDirection(this.tempDirection);
    }

    public boolean keepMoving(){
        if (this.onTile == null)
            return false;
        if (this.onTile instanceof CementTile && this.onPixel())
            return false;
        else
            return true;
    }

    public void movingDirection(int direction){
        if (direction == L){ //left
            this.move(-this.speed, 0);
        } else if (direction == 38){
            this.move(0, -this.speed); 
        } else if (direction == 39){ //right
            this.move(this.speed, 0);
        } else if (direction == 40){
            this.move(0, this.speed);
        }
    }

    public boolean wentThroughSafePath(){
        if (this.pathIdx.size() >= 2){
            boolean start = this.isSafeTile(this.path.get(0));
            boolean end = this.isSafeTile(this.path.get(this.path.size()-1));
            if (start && end)
                return true;
        }
        return false;
    }

    public void setPath(App app){
        try{
            Tile curTile = this.onTile;
            Integer[] curIdx = {this.tileIdx[0], this.tileIdx[1]};
        if (this.pathIdx.size() == 0){
            this.checkPath = false;
            this.pathIdx.add(curIdx);
            this.path.add(curTile);
        } else {
            if (this.path.get(this.path.size()-1).equals(this.onTile) == false){
            this.pathIdx.add(curIdx); 
            this.path.add(curTile);
            }
        }
        
        if (this.wentThroughSafePath()){
            TileManager.PathFill(this.pathIdx, app);
            this.checkPath = true;
            this.resetPlayerPath();
        }
        
    } catch(NullPointerException e){
        e.printStackTrace();
    }
    }

    public void setInputPress(int keyBoardInput){

        if (!(this.onTile instanceof CementTile) && this.onTile.x != 0 && this.onTile.y != App.TOPBAR){
            if (Math.abs(this.keyPress - keyBoardInput) == 2)
                return;
        }

        this.keyPress = keyBoardInput;
    }

    public int getLeft(){
        return this.left;
    }
    public int getRight(){
        return this.right;
    }
    public int getTop(){
        return this.top;
    }
    public int getBottom(){
        return this.bottom;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public int[] getTileIdx(){
        return this.tileIdx;
    }
    public boolean getCheckPath(){
        return this.checkPath;
    }

    public List<Integer[]> getPlayerPathIdx(){
        return this.pathIdx;
    }
    public List<Tile> getPlayerPath(){
        return this.path;
    }
    public void resetPlayerPath(){
        this.path.clear();
        this.pathIdx.clear();
    }

    public void died(App app){
        this.x = 0;
        this.y = App.TOPBAR;
        this.life -= 1;
        this.syncLoc();
        this.onTile = TileManager.LvlMap[0][0];
        this.tileIdx[0] = 0;
        this.tileIdx[1] = 0;
        this.tempDirection = NotMv;
        this.keyPress = NotMv;
        TileManager.PathRemove(this.pathIdx, app);
        this.resetPlayerPath();
    }

    public boolean stepPathAgain(){
        if (this.pathIdx.size() <= 2)
            return false;
        Integer[] lastTileidx = this.pathIdx.get(this.pathIdx.size()-1);

        for (int i=0; i<this.pathIdx.size()-1; i++){
            Integer[] pIdx = this.pathIdx.get(i);
            
            if ( pIdx[1] == lastTileidx[1] && pIdx[0] == lastTileidx[0])
                return true;
        }
        return false;
    }


}
