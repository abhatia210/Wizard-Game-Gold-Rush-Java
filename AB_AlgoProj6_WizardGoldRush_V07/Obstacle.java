import java.awt.*;
import java.util.*;
public class Obstacle
{
    // instance variables - replace the example below with your own
    protected String type;
    
    Image image;
    
    protected ArrayList<Item> neededItems = new ArrayList<>();
    protected String[][] typeAndNeededItemsList = {{"door", "key"}, {"spikes", "board"}, {"fire", "waterBucket", "gloves"}};
    
    protected boolean active = true;
    
    public Obstacle(String type_)
    {
        type = type_;
        if(type.equals("door"))
        {
            image = DataFiles.getImage("door_closed");
        }
        else if(type.equals("spikes"))
        {
            image = DataFiles.getImage("spikes_active");
        }
        else if(type.equals("fire"))
        {
            image = DataFiles.getImage("fire_active");
        }
        else
        {
            System.out.println("Bad obstacle name: " + type);
            image = DataFiles.getImage("error");
        }
        
        //create list of needed items
        for(int i = 0; i < typeAndNeededItemsList.length; i++)
        {
            if(type.equals(typeAndNeededItemsList[i][0]))
            {
                for(int j = 1; j < typeAndNeededItemsList[i].length; j++)
                    neededItems.add(new Item(typeAndNeededItemsList[i][j]));
            }
        }
    }
    

    public String getType()                         {return type;}
    public ArrayList<Item> getNeededItems()         {return neededItems;}
    public boolean isActive()                       {return active;}
    
    public void deactivate()        
    {
        active = false;
        if(type.equals("door"))
            image = DataFiles.getImage("door_open");
        else if(type.equals("spikes"))
            image = DataFiles.getImage("spikes_inactive");
        else if(type.equals("fire"))
            image = DataFiles.getImage("fire_inactive");
        
    }
    
    //This will get used in Fire(and maybe others) to do something.  For now, Fire will spread
    public void update(Wizard wizard, Square[][] map, int row, int column)
    {

    }
    
    public void draw(Graphics2D g2d, int x, int y, int size)
    {
        if(image != null)
        {
            g2d.drawImage(image, x,y, size,size, null);
        }
        
        
    }
    
    public boolean equals(Object obj) 
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Obstacle item = (Obstacle) obj;
        return Objects.equals(type, item.type); 
    }

    public int hashCode() 
    {
        return Objects.hash(type); // Hash based on `type`
    }
    
    public String toString()    
    {
        return "(Obstacle(" + type + ")";
    }
}
