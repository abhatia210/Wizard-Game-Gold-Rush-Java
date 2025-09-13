import java.awt.*;
import java.util.*;
public class Item
{
    private String type;    
    
    Image image;
    
    protected boolean consumable;
    public Item(String type_)
    {
        type = type_;
        if(type.equals("lockpick"))
        {
            consumable = true;
            image = DataFiles.getImage("lockpick");
        }
        else if(type.equals("key"))
        {
            consumable = true;
            image = DataFiles.getImage("key");
        }
        else if(type.equals("board"))
        {
            consumable = true;
            image = DataFiles.getImage("board");
        }
        else if(type.equals("gloves"))
        {
            consumable = true;
            image = DataFiles.getImage("gloves");
        }
        else if(type.equals("waterBucket"))
        {
            consumable = true;
            image = DataFiles.getImage("waterBucket");
        }
        
        else
        {
            System.out.println("Bad item name: " + type);
            image = DataFiles.getImage("error");
        }
        
    }
    
    public String getType()     {return type;}
    public boolean isConsumable()   {return consumable;}
    
    public void draw(Graphics2D g2d, int x, int y, int size)
    {
        g2d.drawImage(image, x,y, size,size, null);
    }

    
    public boolean equals(Object o)
   {
        Item v = (Item)o;
        return type.equals(v.getType());
   }
   public boolean equals(String s)
   {
        return type.equals(s);
   }
   

   public int hashCode()
   {
       return Objects.hash(type);
   }
    
    public String toString()
    {
        return type;
    }
}
