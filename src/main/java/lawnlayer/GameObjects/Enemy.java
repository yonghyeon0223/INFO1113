package lawnlayer.GameObjects;

import lawnlayer.App;
import lawnlayer.GameObjects.Ground.CementTile;
import lawnlayer.GameObjects.Ground.GrassTile;
import lawnlayer.GameObjects.Ground.RedPath;
import lawnlayer.GameObjects.Ground.Tile;
import lawnlayer.GameObjects.Ground.TileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum EnemyDirection{
    TOPLEFT,
    TOPRIGHT,
    BOTTOMLEFT,
    BOTTOMRIGHT,
    STAYSTILL;
}


public class Enemy extends TileMover{

    protected EnemyDirection enemDir;
    protected int speed;
    private String spawn;
    private int interruptIdx;
    public static int NOTINTERRUPT = -1;


    public Enemy(String spawn, String spritePath){
        super(0, 0, spritePath);
        this.speed = 2;
        this.spawn = spawn;
        this.setInitialPosition();
        this.setInitialDir();
        this.interruptIdx = NOTINTERRUPT;
    }


    public void setInitialPosition(){
        if (this.spawn.equals("random")){
            Random random = new Random();
            List<List<Tile>> emptyMap = TileManager.correctMisplaceGroups();
            List<Tile> totalEmptyMap = new ArrayList<>();
            for (List<Tile> ls : emptyMap)
                totalEmptyMap.addAll(ls);
            int randidx = random.nextInt(totalEmptyMap.size());
            Tile randomTile = totalEmptyMap.get(randidx);
            this.x = randomTile.x;
            this.y = randomTile.y;
        }
    }

    public void setInitialDir(){
        Random random = new Random();
        this.enemDir = EnemyDirection.values()[random.nextInt(4)];
    }

    public void moveDiagnollay(){
        if (this.enemDir == EnemyDirection.TOPLEFT)
            this.move(-this.speed, -this.speed);
        else if (this.enemDir == EnemyDirection.TOPRIGHT)
            this.move(this.speed, -this.speed);
        else if (this.enemDir == EnemyDirection.BOTTOMLEFT)
            this.move(-this.speed, this.speed);
        else if (this.enemDir == EnemyDirection.BOTTOMRIGHT)
            this.move(this.speed, this.speed);
        else if (this.enemDir == EnemyDirection.STAYSTILL)
            this.move(0, 0);
    }

    public void checkTile(){
        if (this.enemDir == EnemyDirection.TOPLEFT)
            this.onWhichTile(this.left, this.top);
        else if (this.enemDir == EnemyDirection.TOPRIGHT)
            this.onWhichTile(this.right, this.top);
        else if (this.enemDir == EnemyDirection.BOTTOMLEFT)
            this.onWhichTile(this.left, this.bottom);
        else if (this.enemDir == EnemyDirection.BOTTOMRIGHT)
            this.onWhichTile(this.right, this.bottom);
    }

    public void bounceOff(){
        if (this.isSafeTile(this.onTile) == false){
            return;
        }
        if (this.enemDir == EnemyDirection.TOPLEFT){
            this.onWhichTile(this.right, this.top);
            if (isSafeTile(this.onTile))
                this.enemDir = EnemyDirection.BOTTOMLEFT;
            else
                this.enemDir = EnemyDirection.TOPRIGHT;
        } else if (this.enemDir == EnemyDirection.TOPRIGHT){
            this.onWhichTile(this.left, this.top);
            if (isSafeTile(this.onTile))
                this.enemDir = EnemyDirection.BOTTOMRIGHT;
            else
                this.enemDir = EnemyDirection.TOPLEFT;
        }
        else if (this.enemDir == EnemyDirection.BOTTOMLEFT){
            this.onWhichTile(this.right, this.bottom);
            if (isSafeTile(this.onTile))
                this.enemDir = EnemyDirection.TOPLEFT;
            else
                this.enemDir = EnemyDirection.BOTTOMRIGHT;
        }
        else if (this.enemDir == EnemyDirection.BOTTOMRIGHT){
            this.onWhichTile(this.left, this.bottom);
            if (isSafeTile(this.onTile))
                this.enemDir = EnemyDirection.TOPRIGHT;
            else
                this.enemDir = EnemyDirection.BOTTOMLEFT;
        } else{
            this.enemDir = EnemyDirection.STAYSTILL;
        }
    }

    public void interruptPath(List<Integer[]> playerPath, Player p){
        if (p.isSafeTile(p.onTile)){
            this.interruptIdx = NOTINTERRUPT;
        }
        if (this.interruptIdx > NOTINTERRUPT)
            return;
        int enemX = TileManager.CoordXToTile(this.onTile);
        int enemY = TileManager.CoordYToTile(this.onTile);

        for (int i=0; i<playerPath.size(); i++){
            int x = playerPath.get(i)[1];
            int y = playerPath.get(i)[0];
            if (x == enemX && y == enemY){
                this.interruptIdx = i;
                System.out.println("interrupted!");
                return;
            }
        }
    }

    public boolean destroyPath(List<Integer[]> pathIdx, List<Tile> path, App app, Player p){
        if (this.interruptIdx <= NOTINTERRUPT)
            return false;
        if (app.frameCount % 3 != 0)
            return false;
        try{
            if (this.interruptIdx >= pathIdx.size() -1){
                p.died(app);
                this.interruptIdx = NOTINTERRUPT;
                return true;
            }
            if (path.get(this.interruptIdx) instanceof CementTile){
                throw new IndexOutOfBoundsException();
            } else if (path.get(this.interruptIdx) instanceof GrassTile)
                throw new IndexOutOfBoundsException();

            Integer[] destoryPathIdx = pathIdx.get(this.interruptIdx);

            int x = destoryPathIdx[1];
            int y = destoryPathIdx[0];
            
            Tile t =  new RedPath(TileManager.TileToCoordX(x), TileManager.TileToCoordY(y));
            t.setSprite(app);
            TileManager.LvlMap[y][x] = t;
            this.interruptIdx += 1;
        } catch(IndexOutOfBoundsException e){
            System.out.println("player already reached safe tile! dang it :D");
            this.interruptIdx = NOTINTERRUPT;
            e.printStackTrace();
            p.resetPlayerPath();
        } 
        return false;
        
    }


    public Tile getOnTile(){
        return this.onTile;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }

    public void EnemyMove(){
        this.moveDiagnollay();
        this.checkTile();
        this.bounceOff();
    }

    public void checkEnemyHitPlayerPath(Player p, App app){
        this.interruptPath(p.getPlayerPathIdx(), p);
        this.destroyPath(p.getPlayerPathIdx(), p.getPlayerPath(), app, p);
    }

}
