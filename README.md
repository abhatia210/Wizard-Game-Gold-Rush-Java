general strategy for pathfinding:
The wizard has the choice to move in 4 directions, and first moves based on where the position of the goal is respective to the wizard (ex. If the wizard is above the goal, it will move 
down as much as it can until it reaches the same row of the goal). The wizard only moves if the next open square is the square with the number of times least visited, which is calculated 
in the directionValue method, otherwise it will move on to the next direction to see if it is the least visited square. If the wizard is stuck meaning that the wizard is at an open 
square where the wizard is trapped by lava and is at the same row or column of the goal, then the wizard will find the next open square with the number of times least visited again but 
now with the nextDirection method. If the wizard is at the goal, the update method will return "goal" and the next level will be unlocked.
