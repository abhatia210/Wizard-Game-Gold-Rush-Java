import java.awt.*;
import java.util.*;
public class Chest
{
    String trapType;
    boolean closed = true;
    int coins;
    Item item;
    Image image;
    public Chest(int coins_, String trapType_, Item item_)
    {
        trapType = trapType_;
        coins = coins_;
        if(item_ != null)
            item = item_;
        
        image = DataFiles.getImage("chest_closed");
        
    }
    public boolean isClosed()   { return closed;}
    public int open()
    {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        int temp = coins;
        image = DataFiles.getImage("chest_open");
        coins = 0;
        closed = false;
        return temp;
    }
    public int getCoins()       {return coins;}
    public boolean hasContents()
    {
        return coins != 0 || item != null;
    }
    public void draw(Graphics2D g2d, int x, int y, int size)
    {
        g2d.drawImage(image, x,y, size,size, null);
        g2d.setFont(new Font("Helvetica", Font.BOLD, 10));
        g2d.setColor(Color.yellow);
        g2d.drawString("" + coins, x + 2, y + size - 2);
        
    }
    public String toString()
    {
        return "CHEST: " + coins; 
    }
}
