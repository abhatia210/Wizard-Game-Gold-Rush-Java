/*# THIS SECTION MUST BE COMPLETED IN ORDER TO RECEIVE CREDIT
 * Name: Aditi Bhatia
 * Per: 1
 * Summary: Level completed:  14      coins collected: 600
 * Teacher: Mr. Pinta
 * Date: 3/27/31
 * Full description of your strategy/algorithm:
 * (An average coder should be able to reproduce your code from your description here)        |
 * (bullet points or a numbered list is better than paragraphs)                               |
 *                    (Each line of your commented description should not be longer than this-|
 * The code first sorts the squares into three categories: lockpicks and chests in two array 
 * lists, and the goal. Then two priority queues are made for the locks and chests and the 
 * priority queues are offered the squares from the array lists. The lockpicks are prioritized 
 * based on closest distance from algor in the compareTo method. Chests are prioritized based 
 * on the number of coins (greatest to least in the compareMoney method. A new array list is 
 * made to store all the paths the wizard will take in the level. If Algor does not have a 
 * lockpick, Algor will go to the nearest lockpick with the getPath method and the lockpick 
 * will be removed from the lockpick priority queue. Algor will only go if the remainingActions 
 * are greater than or equal to the actions in the path (variable estTime). The estTime is 
 * calculated by the size of the path to the object and the size of the path to the goal 
 * combined. Once algor gets the lockpick and picks it up, algor finds the path to the chest 
 * with the most money with the getPath method. Algor opens the chest and moves to the exit 
 * and gets the path to the goal from the getPath method, returning goal once Algor is on the 
 * goal. If there is no path, Algor will move down as a default.
 */
//#-------------------------------------------------------------------------------------------|

import java.util.*;

public class Wizard1Update
{
    public static String update(
                                    Square[][] map,
                                    Wizard algor, 
                                    ArrayList<Square> specialSquares,
                                    int remainingActions, 
                                    int level_
                                )
                                {
        ArrayList<Square> chests = new ArrayList<Square>();
        ArrayList<Square> locks = new ArrayList<Square>();
        Square goal = null;
        for (int i = 0; i < specialSquares.size(); i++) {
            if(specialSquares.get(i).getChest() != null) {
                chests.add(specialSquares.get(i));
            }
            if(specialSquares.get(i).getItem() != null &&
            specialSquares.get(i).getItem().getType().equals("lockpick") ) {
                locks.add(specialSquares.get(i));
            }
            if (specialSquares.get(i).getType().equals("goal")) {
                goal = specialSquares.get(i);
            }
        }
        PriorityQueue<Square> pqLocks = new PriorityQueue<Square>((a,b) -> compareTo(a,b, map, map[algor.getRow()][algor.getColumn()]));
        for (Square l: locks) {
            pqLocks.offer(l);
        }
        PriorityQueue<Square> pqChests = new PriorityQueue<Square>((a,b) -> compareMoney(a,b, map));
        for (Square c: chests) {
            pqChests.offer(c);
        }
        
        ArrayList<String> path = new ArrayList<String>();
        //if algor does not have a lockpick, find path to nearest lockpick
        if (algor.inInventory("lockpick") == 0 && !pqLocks.isEmpty()) {
            Square sl = pqLocks.poll();
            ArrayList<String> pathToGoal = getPath(map, map[algor.getRow()][algor.getColumn()], goal);
            ArrayList<String> nearestLockPath = getPath(map, map[algor.getRow()][algor.getColumn()], sl);
            int estTime = nearestLockPath.size() + + pathToGoal.size();
            if(nearestLockPath != null && !nearestLockPath.isEmpty()) {
                if(remainingActions >= estTime) {
                    path.addAll(nearestLockPath);
                }
            }
        }
        //if algor has lockpick, pick it up
        if (map[algor.getRow()][algor.getColumn()].getItem() != null && map[algor.getRow()][algor.getColumn()] != null) {
            ArrayList<String> nearestLockPath = getPath(map, map[algor.getRow()][algor.getColumn()], locks.get(0));
            ArrayList<String> pathToGoal = getPath(map, map[algor.getRow()][algor.getColumn()], goal);
            if (algor.inInventory("lockpick") == 0 &&
            map[algor.getRow()][algor.getColumn()].getItem().getType().equals("lockpick") && map[algor.getRow()][algor.getColumn()].getItem().getType() != null) {
                if (remainingActions > nearestLockPath.size() + pathToGoal.size()) {
                    path.add("pickup lockpick");
                }
            }
        }
        //if algor has a lockpick, find path to chest 
        if (algor.inInventory("lockpick") > 0 && !pqChests.isEmpty()) {
            Square sl = pqChests.poll();
            ArrayList<String> chestPath = getPath(map, map[algor.getRow()][algor.getColumn()], sl);
            ArrayList<String> pathToGoal = getPath(map, map[algor.getRow()][algor.getColumn()], goal);
            int estTime = chestPath.size() + pathToGoal.size();
            if(chestPath != null && !chestPath.isEmpty()) {
                if(remainingActions >= estTime) {
                    path.addAll(chestPath);
                }
            }
        }
        //if we are at a chest, open it
        if (algor.inInventory("lockpick") > 0 && map[algor.getRow()][algor.getColumn()].getChest() != null && 
        map[algor.getRow()][algor.getColumn()].getChest().isClosed()) {
            ArrayList<String> chestPath = getPath(map, map[algor.getRow()][algor.getColumn()], chests.get(0));
            ArrayList<String> pathToGoal = getPath(map, map[algor.getRow()][algor.getColumn()], goal);
            if (remainingActions > chestPath.size() + pathToGoal.size()) {
                    path.add("open chest");
            }
        }
        //move to goal
        if(goal != null) {
            ArrayList<String> pathToGoal = getPath(map, map[algor.getRow()][algor.getColumn()], goal);
            int estTime = pathToGoal.size();
            if (pathToGoal != null && !pathToGoal.isEmpty()) {
                    System.out.println("path to goal is found");
                    System.out.println("goal is: " + goal.getRow() + " " + goal.getColumn());
                    System.out.println("goal remaining actions is:" + remainingActions + "estTime is:" + estTime);
                    path.addAll(pathToGoal);
                    return path.get(0);
            }
        }
        if(map[algor.getRow()][algor.getColumn()].getType().equals("goal")) {
            return "goal";
        }
        return "move down";
    }
    public static int compareTo(Square a, Square b, Square[][] squares, Square start) {
        return getPath(squares, start, a).size() - getPath(squares, start, b).size();
    }
    public static int compareMoney(Square a, Square b, Square[][] squares) {
        return b.getChest().getCoins() - a.getChest().getCoins();
    }
    
    public static ArrayList<String> getPath(Square[][] squares, Square start, Square end)
    {
        ArrayList<String>[][] paths = new ArrayList[squares.length][squares[0].length];
        for(int i = 0; i < paths.length; i++)
        {
            for(int j = 0; j < paths[i].length; j++)
            {
                paths[i][j] = new ArrayList<>();
            }
        }
        ArrayList<String> path = new ArrayList<>();
        ArrayList<Square> visited = new ArrayList<>();
        Queue<Square> open = new LinkedList<>();
        //add start to open and mark as visited
        open.add(start);
        visited.add(start);
        while(!open.isEmpty())
        {
            //get the oldest element in open and find its row and column
            Square current = open.remove();
            int row = current.getRow();
            int column = current.getColumn();
            
            if(row == end.getRow() && column == end.getColumn())
                return paths[row][column];
            
            //Is up possible, open, and not already visited
            if(row > 0  && !visited.contains(squares[row-1][column]) && 
                (squares[row-1][column].equals(end) || squares[row-1][column].getWalkable()))
            {
                //add to visited and to the open list
                visited.add(squares[row-1][column]);
                open.add(squares[row-1][column]);
                //add how you get to this square to its path.
                paths[row-1][column].addAll(paths[row][column]);
                paths[row-1][column].add("move up");
            }
            //Is down possible, open, and not already visited
            if(row < squares.length-1 && !visited.contains(squares[row+1][column]) && 
                (squares[row+1][column].equals(end) || 
                 squares[row+1][column].getWalkable()))
            {
                //add to visited and to the open list
                visited.add(squares[row+1][column]);
                open.add(squares[row+1][column]);
                //add how you get to this square to its path.
                paths[row+1][column].addAll(paths[row][column]);
                paths[row+1][column].add("move down");
                
            }
            //Is left possible, open, and not already visited
            if(column > 0 && !visited.contains(squares[row][column-1]) && 
                (squares[row][column-1].equals(end) || 
                squares[row][column-1].getWalkable()))
            {
                //add to visited and to the open list
                visited.add(squares[row][column-1]);
                open.add(squares[row][column-1]);
                //add how you get to this square to its path.
                paths[row][column-1].addAll(paths[row][column]);
                paths[row][column-1].add("move left");
                
            }
            //Is right possible, open, and not already visited
            if(column < squares[row].length-1 && !visited.contains(squares[row][column+1]) && 
                (squares[row][column+1].equals(end) || 
                squares[row][column+1].getWalkable()))
            {
                //add to visited and to the open list
                visited.add(squares[row][column+1]);
                open.add(squares[row][column+1]);
                //add how you get to this square to its path.
                paths[row][column+1].addAll(paths[row][column]);
                paths[row][column+1].add("move right");
                
            }
        }
        
        return null;
    }

}