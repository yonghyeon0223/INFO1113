package lawnlayer.GameObjects;

import lawnlayer.App;
import lawnlayer.GameObjects.Ground.*;

public class TileMover extends TileObj{

    protected Tile onTile;
    protected int[] tileIdx;

    public TileMover(int x, int y, String spritePath){
        super(x, y, spritePath);
    }

    public boolean onPixel(){
        if (this.x % App.SPRITESIZE + this.y % App.SPRITESIZE == 0)
            return true;
        return false;
    }

    public void onWhichTile(){
        int left = this.left / App.SPRITESIZE;
        int top = (this.top - App.TOPBAR) / App.SPRITESIZE;

        if (this.onPixel()){
            this.onTile = TileManager.LvlMap[top][left];
            this.tileIdx[0] = top;
            this.tileIdx[1] = left;
        }
    }
    public void onWhichTile(int horiz, int vert){
        horiz = horiz / App.SPRITESIZE;
        vert = (vert - App.TOPBAR) / App.SPRITESIZE;

        this.onTile = TileManager.LvlMap[vert][horiz];
    }

    public boolean isSafeTile(Tile t){
        if (t instanceof CementTile || t instanceof GrassTile)
            return true;
        return false;
    }
}
