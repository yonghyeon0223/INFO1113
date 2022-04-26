package lawnlayer.GameObjects.Ground;

import lawnlayer.App;
import processing.core.PApplet;
import lawnlayer.GameObjects.Enemy;
import lawnlayer.GameObjects.Player;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TileManager {

    public static Tile[][] LvlMap;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 32;
    public static final String CEMENT = "X";
    public static final String GRASS = " ";
    
    public static String[][] readMap(String path){
        // List<String> MapScan = new ArrayList<>(WIDTH*HEIGHT);
        String[][] MapScan = new String[HEIGHT][WIDTH];
        try{
            File f = new File(path);
            Scanner sc = new Scanner(f);
            sc.useDelimiter("");
            
            int i = 0;
            while (sc.hasNext()){
                String nextstr = sc.next();
                if (nextstr.equals(" ") || nextstr.equals("X")){
                    MapScan[i/WIDTH][i%WIDTH] = nextstr;
                    i += 1;
                }
            }
            
            sc.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(MapScan.length * MapScan[31].length);
        return MapScan;
     }

     public static int TileToCoordX(int x){
        return x * App.SPRITESIZE;
     }
     public static int TileToCoordY(int y){
         return y * App.SPRITESIZE + App.TOPBAR;
     }
     public static int CoordXToTile(Tile tile){
         return tile.getX() / App.SPRITESIZE;
     }
     public static int CoordYToTile(Tile tile){
         return (tile.getY() - App.TOPBAR) / App.SPRITESIZE;
     }

    public static void InitialiseMap(String mapFilePath, App app){
        String[][] currentMap = readMap(mapFilePath);
        LvlMap = new Tile[HEIGHT][WIDTH];

        for (int i=0; i<HEIGHT; i++){
            for (int j=0; j<WIDTH; j++){
                String currentTile = currentMap[i][j];
                Tile t;
                if (currentTile.equals(CEMENT))
                    t = new CementTile(TileToCoordX(j), TileToCoordY(i));
                else
                    t = new NoTile(TileToCoordX(j), TileToCoordY(i));
                t.setSprite(app);
                LvlMap[i][j] = t;
            }
        }
    }

    public static void printMap(PApplet app){
        for (Tile[] tileROw : LvlMap){
            for (Tile tiles : tileROw){
                tiles.draw(app);
            }
        }
    }

    public static void setPath(int[] tileIdx, App app){
        int y = tileIdx[0];
        int x = tileIdx[1];
        Tile playerTile = LvlMap[y][x];
        if (playerTile instanceof NoTile){
            Tile t = new greenPath(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
    }

    public static void PathFill(List<Integer[]> pathidx, App app){
        for (int i=1; i<pathidx.size()-1; i++){
            int x = pathidx.get(i)[1];
            int y = pathidx.get(i)[0];

            Tile t = new GrassTile(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
    }
    public static void PathRemove(List<Integer[]> pathidx, App app){
        for (int i=1; i<pathidx.size(); i++){
            int x = pathidx.get(i)[1];
            int y = pathidx.get(i)[0];

            Tile t = new NoTile(TileToCoordX(x), TileToCoordY(y));
            t.setSprite(app);
            LvlMap[y][x] = t;
        }
    }

    public static List<Tile> makeNewGroup(Tile t){
        List<Tile> newGroup =  new ArrayList<>();
        newGroup.add(t);
        return newGroup;
    }

    public static List<List<Tile>> groupTiles(){
        List<List<Tile>> Groups = new ArrayList<>();
        for (int i=0; i<HEIGHT; i++){
            for (int j=0; j<WIDTH; j++){
                Tile t = LvlMap[i][j];
                if (t instanceof NoTile == false)
                    continue;
                if (Groups.size() == 0)
                    Groups.add(makeNewGroup(t));
                else{
                    boolean createNew = true;
                    for (List<Tile> group : Groups){
                        if (belongToGroup(group, t)){
                            group.add(t);
                            createNew = false;
                            break;
                        }
                    }
                    if (createNew)
                        Groups.add(makeNewGroup(t));
                }
            }
        }
        return Groups;
    }

    public static boolean belongToGroup(List<Tile> lsTile, Tile tile){
        for (Tile t: lsTile){
            if (adjacentTiles(t, tile))
                return true;
        }
        return false;
    }


    public static boolean adjacentTiles(Tile control, Tile subject){
        int xDiff = Math.abs(control.getX() - subject.getX());
        int yDiff = Math.abs(control.getY() - subject.getY());
        if (xDiff + yDiff == 20){
            return true;
        }
        return false;
    }

    public static boolean needToMerge(List<Tile> a, List<Tile> b){
        for (Tile tb : b){
            if (belongToGroup(a, tb))
                return true;
        }
        return false;
    }

    public static List<List<Tile>> correctMisplaceGroups(){
        List<List<Tile>> Groups = groupTiles();

        int idx = 0;
        while (true){
            try{
            List<Integer> remove = new ArrayList<>();
            for (int i=idx+1; i<Groups.size(); i++){
                if (needToMerge(Groups.get(idx), Groups.get(i))){
                    Groups.get(idx).addAll(Groups.get(i));
                    remove.add(i);
                }
            }
            
            for (int i=0; i<remove.size(); i++){
                int r = remove.get(i) -i;
                Groups.remove(Groups.get(r));
            }

            idx += 1;
            if (idx+1 > Groups.size())
                break;
        } catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }
        return Groups;
    }

    public static boolean groupHasEnemy(List<Tile> group, List<Enemy> enemyList){
        for (Tile tile : group){
            for (Enemy enemy : enemyList){
                int xDiff = Math.abs(tile.getX() - enemy.getX());
                int yDiff = Math.abs(tile.getY() - enemy.getY());
                if (xDiff + yDiff <= App.SPRITESIZE)
                    return true;
            }
        }
        return false;
    }


    public static void colorMap(List<Tile> group, App app){
        for (Tile tile : group){
            Tile t = new GrassTile(tile.getX(), tile.getY());
            t.setSprite(app);
            LvlMap[CoordYToTile(tile)][CoordXToTile(tile)] = t;
        }
    }

    public static void updateMap(Player p, App app, List<Enemy> enemyList){
        setPath(p.getTileIdx(), app);
        if (p.getCheckPath()){
            List<List<Tile>> Groups = correctMisplaceGroups();
            for (List<Tile> group : Groups){
                if (groupHasEnemy(group, enemyList) == false){
                    colorMap(group, app);
                }
            }
        }
    }

}
