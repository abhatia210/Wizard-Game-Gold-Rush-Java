import java.awt.*;
import java.util.*;
public class Game
{
    // instance variables - replace the example below with your own
    
    //Drawing variables
    private DataFiles file = new DataFiles();

    //Map Variables
    private int squareSize;
    private Square[][] map;
    private Square goal;
    private String[][][] grid = file.getGrid();

    //Wizard Variables
    private Wizard wizard1;
    private Wizard wizard2;
    private boolean gamePaused = true;
    
    private int gameTicks = 0; //this replaced "moves"
    
    
    //control variables
    private int level;
    //Graphics2D g2d;
    
    public Game(Graphics2D g2d_, int startLevel)
    {
        
        level = startLevel;
        squareSize = 800 / grid[level].length;
        int[] wizard1Start = startLevel();
        wizard1 = new Wizard(1, 
                             wizard1Start[0], 
                             wizard1Start[1],
                             squareSize
                             );
        
    }
    
    public int[] startLevel()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        gameTicks = 0;
        if(level >= grid.length)
            return new int[2];
        
        map = new Square[grid[level].length][grid[level][0].length];
        
        
        int[] wizard1Start = {0,0};
        for(int i = 0; i < grid[level].length; i++)
        {
            for(int j = 0; j < grid[level][i].length; j++)
            {
                String type = "";
                boolean walkable = false;
                Obstacle obstacle = null;
                Chest chest = null;
                Item item = null;
                
                if(grid[level][i][j].equals("l1"))
                {
                    type = "lava";
                }
                else if(grid[level][i][j].equals(".."))
                {
                    type = "floor";
                    walkable = true;
                }
                else if(grid[level][i][j].equals("A1"))
                {
                    wizard1Start[0] = i;
                    wizard1Start[1] = j;
                    type = "floor";
                    walkable = true;
                }
                else if(grid[level][i][j].equals("go"))
                {
                    type = "goal";
                    walkable = true;
                }
                //ITEMS
                else if(grid[level][i][j].equals("lp"))
                {
                    type = "floor";
                    item = new Item("lockpick");
                    walkable = true;
                }else if(grid[level][i][j].equals("k1"))
                {
                    type = "floor";
                    item = new Item("key");
                    walkable = true;
                }
                else if(grid[level][i][j].equals("b1"))
                {
                    type = "floor";
                    item = new Item("board");
                    walkable = true;
                }
                else if(grid[level][i][j].equals("gl"))
                {
                    type = "floor";
                    item = new Item("gloves");
                    walkable = true;
                }
                else if(grid[level][i][j].equals("wa"))
                {
                    type = "floor";
                    item = new Item("waterBucket");
                    walkable = true;
                }
                
                //DOOR
                else if(grid[level][i][j].equals("dc"))
                {
                    type = "floor";
                    walkable = false;
                    obstacle = new Obstacle("door");
                }
                //World Traps
                else if(grid[level][i][j].equals("sa"))
                {
                    type = "floor";
                    obstacle = new Obstacle("spikes");
                    walkable = false;
                    
                }
                else if(grid[level][i][j].equals("fi"))
                {
                    type = "floor";
                    obstacle = new Fire("fire", true);
                    walkable = false;
                    
                }
                
                else if(grid[level][i][j].substring(0,1).equals("c")) //Chest
                {
                    type = "floor";
                    int coins = 10 * Integer.parseInt(grid[level][i][j].substring(1,2));
                    chest = new Chest(coins, "no traps",item);
                    walkable = true;
                    
                }
                else
                {
                    System.out.println("Error in Game class with grid["+i+"]["+j+"] = " + grid[level][i][j]);
                    type = "error";
                    walkable = false;
                    
                }
                
                // new Squere(i, j, x, y, width, height, type)
                squareSize = 800 / grid[level].length;
                map[i][j] = new Square(i, j, 
                                       squareSize,
                                       type,
                                       walkable,
                                       obstacle,
                                       item,
                                       chest
                                       );
                
            }
        }
        return wizard1Start;
        
    }
    
    public Wizard getWizard1()   {return wizard1;}
    public int getLevel()       {return level;}
    public int getGameTickCount()   {return gameTicks;}
    public String[][][] getGrid()     {return grid;}
    public int getSquareSize()      {return squareSize;}
    
    public void incrementLevel()    {level++;}
    
    public void update()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        if(level < grid.length)
        {
            gameTicks++;
            if(gameTicks > DataFiles.getTicksPerLevel()[level])
                wizard1.unalive();
            //get all the squares that have something
            ArrayList<Square> containsSomething = new ArrayList<>();
            for(int i = 0; i < map.length; i++)
            {
                for(int j = 0; j < map[i].length; j++)
                {
                    if(map[i][j].containsSomething())
                    {
                        containsSomething.add(map[i][j]);
                    }
                }
            }
            
            
            wizard1.update(map, containsSomething, this);
        }
        
    }
    
    
    
    
    
    
    
    //-----------------------------------DRAWING STUFF--------------------------------
    public void draw(Graphics2D g2d)
    {
        drawMap(g2d);
        drawTopBar(g2d);
        wizard1.draw(g2d);
    }
    
    public void drawMap(Graphics2D g2d)
    {
        for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map[i].length; j++)
            {
                map[i][j].draw(g2d);
            }
        }
    }
    public void drawTopBar(Graphics2D g2d)
    {
        int startY = 50;
        int dy = 30;
        g2d.setFont(new Font("Courier",Font.BOLD, 25));
        g2d.setColor(Color.white);
        g2d.drawString("LEVEL = " + (level+1),850,startY);
        if(level < grid.length)
            g2d.drawString("Move Count = " + gameTicks +"/"+DataFiles.getTicksPerLevel()[level],850,startY + dy);
        else
            g2d.drawString("Move Count = " + gameTicks,850,startY + dy);
        g2d.drawString("Coins = " + wizard1.getCoins(), 850,startY + dy*2);
        g2d.drawString("Health = " + wizard1.getHealth(), 850,startY + dy*3);
        g2d.drawString("Phase = " + WizardWorld1.phase, 850,startY + dy*4);
    }
    
}
