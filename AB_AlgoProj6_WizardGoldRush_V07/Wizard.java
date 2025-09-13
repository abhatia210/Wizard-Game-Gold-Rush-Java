import java.util.*;
import java.awt.*;

public class Wizard
{
    
    //positional variables
    private int x, y, row, column, size; 
    private int health = 50;
    private int coins = 0;
    private Square goal;
    private int playerNum;
    
    private boolean stillAlive = true;
    private boolean foundGoal = false;
    private String instruction = "stop";
    private String prevInstruction = "right"; //used for animation
    //file that contains the wizards drawing pattern in Datafiles
    
    private HashMap<Item,Integer> inventory = new HashMap<Item,Integer>();
    
    private Image image;
    
    public Wizard(int playerNum_, int row_, int column_, int size_)
    {
        row = row_;
        column = column_;
        size = size_;        
        playerNum = playerNum_;
        x = column * size;
        y = row * size;
        image = DataFiles.getImage("algor_standing");        
        
        
        
    }
    
    public int getRow()                                     {   return row; }
    public int getColumn()                                  {   return column; }
    public int getHealth()                                  {   return health; }
    public boolean getFoundGoal()                           {   return foundGoal;}
    public int getSize()                                    {   return size; }
    public int getCoins()                                   {   return coins;} 
    public boolean isAlive()                                {   return stillAlive;}
    public HashMap<Item, Integer> getInventory()            {   return inventory;}
    
    public int inInventory(String type_)
    {
        for(Item item: inventory.keySet())
        {
            if(type_.equals(item.getType()))
                return inventory.get(item);
        }
        return 0;
    }
    public int inInventory(Item i)
    {
        for(Item item: inventory.keySet())
        {
            if(i.equals(item))
                return inventory.get(item);
        }
        return 0;
    }
    
    
    public void setFoundGoal()       {      foundGoal = false;}
    public void unalive()            {      stillAlive = false;}
    
    public void resetForNewLevel(int[] pos, int size_)  
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        row = pos[0];
        column = pos[1];
        size =  size_;
        x = column * size;
        y = row * size;
        foundGoal = false;
    }
    
    
    public String update(Square[][] map, ArrayList<Square> squaresWithSomething, Game game)
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        int ticksRemaining = DataFiles.getTicksPerLevel()[game.getLevel()] - game.getGameTickCount();
        String current = Wizard1Update.update(map, this, squaresWithSomething,ticksRemaining, game.getLevel() );
          
        String[] instruction = current.split(" ");
        //System.out.println("        IN WIZARD "+playerNum+": RETURNED VALUE: "+Arrays.toString(instruction));
        
        //-----------------------------------------------------------------------MOVING
        if(instruction[0].equals("move"))
        {
            if(instruction[1].equals("right"))
            {
                x += size;
                column++;
            }
            else if(instruction[1].equals("left"))
            {
                x -= size;
                column--;
            }
            else if(instruction[1].equals("up"))
            {
                y -= size;
                row--;
            }
            else if(instruction[1].equals("down"))
            {
                y += size;
                row++;
            }
            else
            {
                System.out.println("Direction not understood: "+instruction[1]);
            }
        }
        //-----------------------------------------------------------------------picking up something
        else if(instruction[0].equals("pickup"))
        {
            String itemToPickUp = instruction[1];
            Item item = map[row][column].getItem();
            
            if(item != null && item.equals(new Item(itemToPickUp)))
            {
                
                if(itemToPickUp.equals("waterBucket"))
                    inventory.put(item, inventory.getOrDefault(item, 0) + 3);
                else
                    inventory.put(item, inventory.getOrDefault(item, 0) + 1);
                map[row][column].pickupItem(item);
            }
            else            
            {
                System.err.println(itemToPickUp + " is not HERE");
                stillAlive = false;
            }
            
        }
        
        //-----------------------------------------------------------------------deactivating
        else if(instruction[0].equals("deactivate")) 
        {
            Obstacle obstacle = null;
            String type = instruction[1];
            String direction = instruction[2];
            int dirR= 0;
            int dirC = 0;
            if(row > 0 && direction.equals("up") && 
               map[row-1][column].getObstacle() != null &&
               map[row-1][column].getObstacle().getType().equals(type))
            {
                obstacle =  map[row-1][column].getObstacle();
                if(hasNeededItems(obstacle))
                {
                    map[row-1][column].deactivate();
                    removeFromInventory(obstacle);
                }
                else
                {
                    stillAlive = false;
                    System.err.println("Dont' have required materials to deactivate.");
                }
            }
            else if(row < map.length && direction.equals("down") && 
               map[row+1][column].getObstacle() != null &&
               map[row+1][column].getObstacle().getType().equals(type))
            {
                obstacle =  map[row+1][column].getObstacle();
                if(hasNeededItems(obstacle))
                {
                    map[row+1][column].deactivate();
                    removeFromInventory(obstacle);
                }
                else
                {
                    stillAlive = false;
                    System.err.println("Dont' have required materials to deactivate.");
                }
            }
            else if(column < map[0].length && direction.equals("right") && 
               map[row][column+1].getObstacle() != null &&
               map[row][column+1].getObstacle().getType().equals(type))
            {
                obstacle =  map[row][column+1].getObstacle();
                if(hasNeededItems(obstacle))
                {
                    map[row][column+1].deactivate();
                    removeFromInventory(obstacle);
                }
                else
                {
                    stillAlive = false;
                    System.err.println("Dont' have required materials to deactivate.");
                }
            }
            else if(column > 0 && direction.equals("left") && 
               map[row][column-1].getObstacle() != null &&
               map[row][column-1].getObstacle().getType().equals(type))
            {
                obstacle =  map[row][column-1].getObstacle();
                if(hasNeededItems(obstacle))
                {
                    map[row][column-1].deactivate();
                    removeFromInventory(obstacle);
                }
                else
                {
                    stillAlive = false;
                    System.err.println("Dont' have required materials to deactivate.");
                }
            }
            else
            {
                System.err.println("Bad direction to move to");
                stillAlive = false;
            }
            
            
        }
        //-----------------------------------------------------------------------open Chest
        else if(instruction[0].equals("open")) 
        {
            Chest chest = map[row][column].getChest();
            if(chest != null && chest.isClosed() && 
                inventory.get(new Item("lockpick")) != null &&inventory.get(new Item("lockpick")) > 0)
            {
                inventory.put(new Item("lockpick"), inventory.get(new Item("lockpick"))-1);
                coins += chest.open();
            }
            else
            {
                stillAlive = false;
            }
        }
        else if (instruction[0].equals("goal") &&  map[row][column].getType().equals("goal"))
        {
            System.out.println("******FOUND GOAL********");
            foundGoal = true;
        }
        else
        {
            stillAlive = false;
            System.out.println("Bad return from update: "+Arrays.toString(instruction));
        }
        
        //If movement put you in a non-walkable square
        if(!map[row][column].getWalkable())
        {
            System.out.println("Walked on Non-walkable Square: " + map[row][column]);
            stillAlive = false;
        }  
        
    
    
    
        
        if(instruction[0].equals("move"))
            prevInstruction = instruction[1];
        return current;
    }
    
    public boolean hasNeededItems(Obstacle obstacle)
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        ArrayList<Item> needed = obstacle.getNeededItems();
        for(Item itemNeeded:needed)
        {
            if( ! inventory.containsKey(itemNeeded) || inventory.get(itemNeeded) <=0)
            {
                return false;
            }
        }
        return true;
    }
    
    public void removeFromInventory(Obstacle obstacle)
    {
        ArrayList<Item> needed = obstacle.getNeededItems();
        
        for(Item itemNeeded:needed)
        {
            System.out.println("removing: " + itemNeeded);
            if(itemNeeded.isConsumable())
                inventory.put(itemNeeded, inventory.get(itemNeeded)-1);
            
        }
        
    }
    
    public boolean containsItem(HashMap<Item, Integer> inventory, String targetType) 
    {
        for (Item item : inventory.keySet()) 
        {
            if (item.getType().equals(targetType)) {
                return true; // Found an item with the matching type
            }
        }
        return false; // No matching item found
    }
    
    public void drawDebug(Graphics2D g2d)
    {
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Helvetica", Font.PLAIN, 20));
        g2d.drawString("stillAlive = " + stillAlive, 20,80);
        g2d.drawString("foundGoal = " + foundGoal, 20,110);
    }
    public void draw(Graphics2D g2d)
    {
        if(!stillAlive)
        {
            g2d.setColor(Color.red);
            g2d.fillOval(x, y, size, size);
        }
        
        
        g2d.drawImage(image, x,y, size,size+2, null);
        //---DRAW Algor's inventory
        int y = 300; 
        int dy = 30;
        g2d.setColor(new Color(255, 255, 255));
        g2d.setFont(new Font("Helvetica",Font.ITALIC,20));
        g2d.drawString("Algor's Inventory", 810,y);
        for(Map.Entry<Item, Integer> item: inventory.entrySet())
        {
            y += dy;
            g2d.drawString(""+item.getKey(), 810,y);
            g2d.drawString(""+item.getValue(), 1000,y);
        }
        
    }
     
    
    
    
    
}
