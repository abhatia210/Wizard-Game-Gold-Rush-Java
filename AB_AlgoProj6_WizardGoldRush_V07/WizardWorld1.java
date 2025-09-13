import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
public class WizardWorld1 extends Drawing {

    public static void main(String[] args) {
        Runner.project = new WizardWorld1();
        Runner.project.drawGrid = drawGrid;
        Runner.project.directions = directions;
        Runner.main();
    }
    //Change this to false to turn off the grid
    static boolean drawGrid = false;
    //Change the strings in the the array to display your directions for your project
    //directions can be any length up to about 10.
    static String[] directions = {"Press space to start moving",
                                  "Once you get to the goal, Press n to go to the next level",
                                  "Change 'delayReset' in the code in line 35 in ",
                                  "                  WizardWorld1 to change game speed",
                                  "Hold SPACE to Make Algor MOVE."
                                  };
    
    
                                  
    //Game control variables                                  
    static String phase = "title";
    private Game game;
    
    private int startingLevel = -1;   
    private int choice = 0;
    
    private boolean grading = false;
    
    private int delayReset = 10;
    private int delayCounter = delayReset;
    
    
    boolean endScreen = false;
    
    @Override
    public void drawPerFrame(Graphics2D g2d)  
    {  
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("Wizard1Update")) {
                throw new IllegalStateException("Wizard1Update is not allowed to call this method!");
            }
        }
        if(phase.equals("title"))
        {
            drawTitleAndGetLevel(g2d);
            if(startingLevel >= 0 && startingLevel < DataFiles.getGrid().length)
                phase = "startup";
            
        }
        else if(phase.equals("startup"))
        {
            game = new Game(g2d, startingLevel);
            phase = "main";
        }
        else if(phase.equals("main"))
        {
            game.draw(g2d);
            delayCounter--;
            if(delayCounter <=0)
            {
                if(!game.getWizard1().getFoundGoal())
                {
                    if(Keys.space)
                        game.update();
                    delayCounter = delayReset;
                }
                else if(Keys.n)
                {
                    game.incrementLevel();
                    System.out.println("Resetting to the next level" + game.getLevel());
                    if( game.getLevel() >= DataFiles.getGrid().length)
                        phase = "winner";
                    else
                    {
                        int[] startingPosition = game.startLevel();
                        game.getWizard1().resetForNewLevel(startingPosition, game.getSquareSize());
                        delayCounter = delayReset;
                    }
                }
                else
                {
                    drawGoToNextLevel(g2d);
                }
            }
            
                    
            if(! game.getWizard1().isAlive())
                phase = "dead";
            
        }
        else if(phase.equals("dead"))
        {
            game.draw(g2d); 
            drawDeadSummary(g2d);
        }
        else if(phase.equals("winner"))
        {
            
               
            drawWinnerSummary(g2d);
        }
        
    }

    
    public void drawDeadSummary(Graphics2D g2d)
    {
        
        g2d.setFont(new Font("Helvetica", Font.BOLD, 30));
        g2d.setColor(Color.white);
        if(choice == 0)
        {
            g2d.drawString("You died.  Try again", 120,200);
        }
        else
        {
            g2d.drawString("You started at level "+ (choice+1)+".", 120, 200);
        }
        g2d.drawString("Total number of move: "+game.getGameTickCount(), 180, 300);
        g2d.drawString("Total Coins Collected: "+ game.getWizard1().getCoins(), 180, 350);
    }
    public void drawWinnerSummary(Graphics2D g2d)
    {
        
        g2d.setFont(new Font("Helvetica", Font.BOLD, 30));
        g2d.setColor(Color.WHITE);
        if(choice == 0)
        {
            g2d.drawString("CONGRATS, YOU FINISHED ALL LEVELS!!", 320, 200);
        }
        else
        {
            g2d.drawString("You started at level "+ (choice+1)+".", 320, 200);
        }
        g2d.drawString("Coins Collected: "+game.getWizard1().getCoins(), 320, 300);
        
    }
    public void drawGoToNextLevel(Graphics2D g2d)
    {
        
        g2d.setFont(new Font("Helvetica", Font.BOLD, 30));
        g2d.setColor(Color.green);
        
        g2d.drawString("Press n to go to next level", 820, 600);
    }
    public void drawTitleAndGetLevel(Graphics g2d)
    {
        
        int top = 100;
        int dy = 50;
        int left = 100;
        int dx = 100;
        int row = 0;
        
        g2d.setColor(Color.white);
        g2d.setFont(new Font("Helvetica", Font.PLAIN,30));
        g2d.drawString("Pick the level you want to start at.", 100,80);
        g2d.fillRect(left,top,600,400);
    
        ArrayList<Button> buttons = new ArrayList<Button>();
        for(int i = 0; i < DataFiles.getGrid().length; i++)
        {
            if(i != 0 && i % 5 == 0)
                row++;
            buttons.add(new Button(20+ left + (i % 5)* dx, 20+ top + (row)* dy, 
                                    80,20,
                                    "level" + (i+1)));
            
        }
        for(int i = 0; i < buttons.size(); i++)
        {
            if(buttons.get(i).mouseHoving() && Mouse.button)
            {
                startingLevel = i;
                choice = i;
            }
            buttons.get(i).draw(g2d);
        }
        if(grading)
            startingLevel = 0;
        
    }
}
