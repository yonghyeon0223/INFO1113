package lawnlayer.GameObjects.Ground;


public class NoTile extends Tile{
    public NoTile(int x, int y){
        super(x, y, "transparent.png");
    }
    public void draw(){
        return;
    }
}
