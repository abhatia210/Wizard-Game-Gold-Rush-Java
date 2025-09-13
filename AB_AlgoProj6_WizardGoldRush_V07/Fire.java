import java.awt.*;
import java.util.*;
public class Fire extends Obstacle
{
    // instance variables - replace the example below with your own
    
    private int delayToSpread = 5;
    boolean primary;
    
    public Fire(String type_, boolean primary_)
    {
        super(type_);
        primary = primary_;
    }
    
    
    public void update(Wizard wizard, Square[][] map, int row, int column)
    {
        
    }
    
    private void spreadFire(Wizard wizard, Square[][] map, int row, int column)
    {
        int[][] DIRECTIONS = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Right, Down, Left, Up (Clockwise)
        int rows = map.length;
        int columns = map[0].length;
        
        System.out.println("SPREADING FIRE!!!");
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row,column});
        while(queue.size() > 0)
        {
            int[] current = queue.poll();
            int r = current[0], c = current[1];
            
            for(int[] dir:DIRECTIONS)
            {
                int newR = r+dir[0];
                int newC = c+dir[1];
                if(inBounds(newR, newC, rows, columns))
                {
                    if(map[newR][newC].getType().equals("floor") &&
                       map[newR][newC].getObstacle() == null)
                    {
                        map[newR][newC].addObstacle("fire");
                        return;
                    }
                    else
                    {
                        queue.add(new int[]{newR, newC});
                    }
                }
            }
            
        }
        
        map[2][2].addObstacle("fire");
    }
    
    private static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
    
    
    
    
}
