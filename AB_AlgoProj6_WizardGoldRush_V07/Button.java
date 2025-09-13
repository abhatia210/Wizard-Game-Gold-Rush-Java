import java.awt.*;

public class Button
{
    
    private int x, y, width, height;
    private String text;

    /**
     * Constructor for objects of class Button
     */
    public Button(int x_, int y_, int width_, int height_, String text_)
    {
        x = x_;
        y = y_;
        width = width_;
        height = height_;
        text = text_;
    }

    public boolean mouseHoving()
    {
        if(Mouse.x > x && Mouse.x < x + width &&
           Mouse.y > y && Mouse.y < y + height)
           {
               return true;
           }
        return false;
    }
    public void draw(Graphics g2d)
    {
        g2d.setColor(Color.gray);
        g2d.fillRect(x, y, width, height);
        g2d.setFont(new Font("Helvetica", Font.PLAIN, 12));
        g2d.setColor(Color.white);
        g2d.drawString(text, x + 5, y + height - 2);
    }
    
}
