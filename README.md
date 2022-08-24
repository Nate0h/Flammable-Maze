# Flammable-Maze
AI Agents that try to navigate a path through a randomly generated 51x51 maze, with the addition of a fire that spreads at random from the center of the maze.
All Agents utilize A* Search Algorithm for Planning of their paths. 

Agent 1 - Plans the Shortest Path to the Goal cell ignoring the spreading fire

Agent 2 - Plans at every step that the fire spreads

Agent 3 - Plans path based on projected fire spread and moves to avoid that fire

Agent 4 - Predicts maximum flammability in the maze, and can see further into the future than Agent 3

Q-Values represent the flammability of the maze,by the equation 1−(1−q)^k where q = 0 is the lowest flammability and q = 1 is the highest. k represents the neighbors of cell that is on fire

Results-----------------------------------------------------------------------------------------------------------------------------------------------
  ![page2image63696880](https://user-images.githubusercontent.com/57077448/186299268-9cc4f4e5-9693-4159-97fb-4e18ec772eb0.png)

![Q-Value](https://user-images.githubusercontent.com/57077448/186299625-c31a4cb2-86c2-401e-9a3e-3783ce72d1a1.png)

