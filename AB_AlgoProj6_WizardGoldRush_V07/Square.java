import java.awt.*;
import java.util.*;
public class Square
{
    private int x, y, row, column, size; 
    private String type;
    private boolean walkable = false;    
    
    private Item item;
    private Obstacle obstacle;
    private Chest chest;
    private Square next;
    private Square prev;
    
    
    int[] extraInts = new int[5];
    String[] extraStrings = new String[5];
    
    
    Image image;
    
    public Square(int row_, int column_,int size_ ,
                  String type_, boolean walkable_, 
                  Obstacle obstacle_, Item item_, Chest chest_)
    {
        row = row_;
        column = column_;
        size = size_;
        
        x = column * size;
        y = row * size;
        type = type_;
        
        obstacle = obstacle_;
        chest = chest_;
        item = item_;
        
        walkable = walkable_;
        
        
        if(type.equals("floor"))
        {
            image = DataFiles.getImage("floor_brick");
        }
        else if(type.contains("goal"))
        {
           image = DataFiles.getImage("goal");
        }
        else if(type.contains("lava"))    //LAVA
        {
            image = DataFiles.getImage("floor_lava");
        }
        else 
        {
            System.out.println("Error In Square: " + type);
            image = DataFiles.getImage("error");
        }
        
    }
    
    
    
    public int getRow()                             {  return row; }
    public int getColumn()                          {  return column; }
    public int getX()                               {  return x; }
    public int getY()                               {  return y; }
    public int getSize()                            {  return size; }
    public Item getItem()                           {  return item;}
    public String getType()                         {  return type; }
    public boolean getWalkable()                    {  return walkable; }
    public Obstacle getObstacle()                   {  return obstacle;}
    public Chest getChest()                         {  return chest;}
    public Square getNext()                         {  return next;}
    public Square getPrev()                         {  return prev;}
    public int getExtraInts(int i)                  {  return extraInts[i];}
    public String getExtraString(int i)             {  return extraStrings[i];}
    
    
    public void setExtraInts(int i, int a)          {  extraInts[i] = a;}
    public void setExtraStrings(int i, String a)    {  extraStrings[i] = a;}
    public void setNext(Square sq)                  {  next = sq;}
    public void setPrev(Square sq)                  {  prev = sq;}
    
    
    
    public boolean containsSomething()
    {
        boolean result = false;
        result = result || (obstacle != null && obstacle.isActive());
        result = result || (chest != null    && chest.hasContents());
        result = result || (item != null);
        
        result = result || type.equals("goal");
        return result;
        
    }
    
    public void addObstacle(String type)
    {
        if(obstacle == null)
        {
            walkable = false;
            if(type.equals("fire"))
                obstacle = new Fire(type, false);
            else
                obstacle = new Obstacle(type);
        }
    }
    
    public boolean pickupItem(Item item_)
    {
        if(item != null && item.equals(item_))
        {
            item = null;
            return true;
        }
        
        return false;
    }
    
    
    
    public void deactivate()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        walkable = true;
        obstacle.deactivate();
    }
    
    public void draw(Graphics2D g2d)
    {
        if(image != null)
            g2d.drawImage(image, x,y, size,size, null);
        
        if(obstacle != null)
        {
            obstacle.draw(g2d, x, y, size);
        }
        if(item != null)
        {
            item.draw(g2d, x, y, size);
        }
        
        if(chest != null)
        {
            chest.draw(g2d, x, y, size);
        }
        
        //uncomment to see the row and column on each square
        // g2d.setFont(new Font("Helvetica", Font.BOLD, 10));
        // g2d.setColor(Color.white);
        // g2d.drawString("" + row + "," + column, x + 2, y + size - 2);
        
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Square) {
            Square other = (Square) obj;
            return this.x == other.x && this.y == other.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    public String toString()
    {
        return "("+row + ","+column +")";
        // return "("+row + ","+column +") Ob: " + ((obstacle == null) ? "null" : obstacle.getType()) +
                // "  items: "+items + "  WK: " + walkable + "\n" ; 
    }
}
