package maze;
import java.util.*;
public class Agents {
	static double q = 0.0;
	public static void main(String[] args) {
		long start, end;
		Node [][] grid1 = create_Maze();
		Node[][] grid2 = create_Maze();
		Node[][] grid3 = create_Maze();
		Node[][] grid4 = create_Maze();

		//Agent 1 trial at q = 0.0
		start = System.currentTimeMillis();    
		System.out.println(Agent1(grid1) + "--------------------------------------------------------------------------------");
		end = System.currentTimeMillis(); 
		Maze.printMaze(grid1);
		System.out.println("Elapsed Time in milli seconds: "+ (end-start));

		//Agent 2 Trial at q = 0.0
		start = System.currentTimeMillis();    
		System.out.println(Agent2(grid2) + "--------------------------------------------------------------------------------");
		end = System.currentTimeMillis(); 
		Maze.printMaze(grid2);
		System.out.println("Elapsed Time in milli seconds: "+ (end-start));

		//Agent 3 Trial at q = 0.0
		start = System.currentTimeMillis();    
		System.out.println(Agent3(grid3) + "--------------------------------------------------------------------------------");
		end = System.currentTimeMillis(); 
		Maze.printMaze(grid3);
		System.out.println("Elapsed Time in milli seconds: "+ (end-start));

		//Agent 4 Trial at q = 0.0
		start = System.currentTimeMillis();    
		System.out.println(Agent4(grid4) + "--------------------------------------------------------------------------------");
		end = System.currentTimeMillis(); 
		Maze.printMaze(grid4);
		System.out.println("Elapsed Time in milli seconds: "+ (end-start));

		/*20 trial run on each q values from 0 - 1.0 at +0.1 increments for a total 220 trials for each agent and 880 total trials
		System.out.println("--------------Agent 1--------------");
		testAgent1();
		System.out.println("--------------Agent 2--------------");
		testAgent2();
		System.out.println("--------------Agent 3--------------");
		testAgent3();
		System.out.println("--------------Agent 4--------------");
		testAgent4();*/



	}
	//Agent 1 plans shortest path to the goal using the A* algorithm and essentially ignores the spread of the fire,
	//this algorithm uses a stack to hold the steps of the agent from start to finish. The algorithm terminates if the next step is where fire is
	//located or the goal is reached by the agent.
	public static String Agent1(Node[][] grid) {
		Stack<Node> path;
		Node node = aStar(grid, grid[0][0]);
		//Finds and returns shortest path to goal, if no path available returns "No path available"
		if(node != null) {
			path = configurePath(node, grid[0][0]);
		}
		else {

			return new String("No path avialable");
		}
		//Start agent at top left corner of the grid
		grid[0][0].x = 2;
		//while the path is not empty move agent 1, one step ahead if step meets a fire cell "3" return "Agent 1 burned"
		while(!path.isEmpty()) {
			Node step = path.pop();
			if(grid[step.row][step.col].x == 3) {
				return new String("Agent 1 Burned");

			}
			grid[step.row][step.col].x = 2;
			spreadFire(grid);
		}
		//return success when the path is empty 
		return new String("Success");
	}
	//Agent 2 re-plans after every step as the fire spreads and makes new paths to the goal, Agent 2 also uses a stack to guide steps. And has
	// the same terminating pattern as Agent 1.
	public static String Agent2(Node[][] grid) {
		Stack<Node> path;
		Node node = aStar(grid, grid[0][0]);

		if(node != null) {
			path = configurePath(node, grid[0][0]);
		}
		else {
			return new String("No path avialable");
		}
		grid[0][0].x = 2;
		while(!path.isEmpty()) {

			Node step = path.pop();
			if(grid[step.row][step.col].x == 3) {		
				return new String("Agent 2 Burned");
			}
			grid[step.row][step.col].x = 2;
			//If step that is taken reached the goal cell we return "success"
			if(step.row == 50 && step.col == 50) {
				return new String("Success");
			}
			spreadFire(grid);
			node = aStar(grid, grid[step.row][step.col]);
			path = configurePath(node, grid[step.row][step.col]);
			//plan a new path based on the spread of the fire, and if there is no viable path to the goal cell
			//then we return "Agent 2 Burned" meaning that the Agent will eventually get burned no matter what.
			if(path == null) {
				return new String("Agent 2 Burned");
			}
		}

		return new String("Success");
	}
	//Agent 3 re-plans at every timestep but also has the ability to look 3 steps ahead and use the predicted spread
	// of fire to guide their path to the goal. Agent 3 makes use of two stacks one which makes a path based on the predicted fire (oath1) and another
	//which creates a path based on the actual fire (path2). If there is no possible path to the target from path1, the algorithm continues based on path2.
	//The algorithm terminates when the agent gets to the goal or there is not path 2.
	public static String Agent3(Node[][] grid) {
		//path1 keeps track of predicted fire path
		Stack<Node> path1;
		//path2 keeps track of the actual fire path
		Stack<Node> path2;
		Node node = modified_aStar(grid, grid[0][0]);
		Node node2 = aStar(grid, grid[0][0]);
		
		//we check to see if there is a path initially
		if(node != null && node2 != null) {
			path1 = configurePath(node, grid[0][0]);
			path2 = configurePath(node, grid[0][0]);
		}
		else {
			return new String("No path avialable");
		}
		grid[0][0].x = 2;
		Node step = grid[0][0];
		//We check to see if path 2 is empty because that is our terminating condition
		while(!path2.isEmpty()) {
			//creates a path based on the predicted fire 3 moves ahead
			spreadPredictedFire(grid);
			node = modified_aStar(grid, grid[step.row][step.col]);
			path1 = configurePath(node, grid[step.row][step.col]);
			//If there is no path through predicted fire, we see if there is a path through the actual fire and move based on path2
			if(path1 == null) {
				node2 =  aStar(grid, grid[step.row][step.col]);
				path2 = configurePath(node2, grid[step.row][step.col]);
				//if path 2 is null we know that there is no possible path to the goal so we return "Agent 3 Burned"
				if(path2 == null) {
					return new String("Agent 3 Burned");
				}
				else {
					step = path2.pop();
					if(grid[step.row][step.col].x == 3) {
						return new String("Agent 3 Burned");
					}
					grid[step.row][step.col].x = 2;
					if(step.row == 50 && step.col == 50) {
						return new String("Success");
					}
				}
			//if path 1 is not null then we move based on that pathway 
			}else {
				step = path1.pop();
				if(grid[step.row][step.col].x == 3) {
					return new String("Agent 3 Burned");
				}
				grid[step.row][step.col].x = 2;
				if(step.row == 50 && step.col == 50) {
					return new String("Success");
				}
			}
			//we the clear the grid of the predicted fire "4" and spread the real fire once more
			clearPredictedFire(grid);
			spreadFire(grid);
		}
		return new String("Success");
	}
	//Agent 4 looks 5 steps ahead and overpredicts the current rate of the fire spread and has the same terminating patterns and functionality of Agent 3
	public static String Agent4(Node[][] grid) {
		Stack<Node> path1;
		Stack<Node> path2;
		Node node = modified_aStar(grid, grid[0][0]);
		Node node2 = aStar(grid, grid[0][0]);

		if(node != null && node2 != null) {
			path1 = configurePath(node, grid[0][0]);
			path2 = configurePath(node, grid[0][0]);
		}
		else {
			return new String("No path avialable");
		}
		grid[0][0].x = 2;
		Node step = grid[0][0];
		while(!path2.isEmpty()) {
			spreadOverPredictedFire(grid);
			node = modified_aStar(grid, grid[step.row][step.col]);
			path1 = configurePath(node, grid[step.row][step.col]);
			if(path1 == null) {
				node2 =  aStar(grid, grid[step.row][step.col]);
				path2 = configurePath(node2, grid[step.row][step.col]);
				if(path2 == null) {
					return new String("Agent 4 Burned");
				}
				else {
					step = path2.pop();
					if(grid[step.row][step.col].x == 3) {
						return new String("Agent 4 Burned");
					}
					grid[step.row][step.col].x = 2;
					if(step.row == 50 && step.col == 50) {
						return new String("Success");
					}
				}
			}else {
				step = path1.pop();
				if(grid[step.row][step.col].x == 3) {
					return new String("Agent 4 Burned");

				}
				grid[step.row][step.col].x = 2;
				if(step.row == 50 && step.col == 50) {
					return new String("Success");
				}
			}
			clearPredictedFire(grid);
			spreadFire(grid);
		}
		return new String("Success");
	}



//A* Star algorithm with open set and closed set implementation, we essentially expand on the Node with the best f value and when we reach the goal node
//we can be sure that it's the shortest path because of the "greedy" algorithm
	public static Node aStar(Node[][] grid, Node start) {
		ArrayList<Node> openset = new ArrayList<Node>();
		ArrayList<Node> closedset = new ArrayList<Node>();
		openset.add(start);
		while(!openset.isEmpty()) {

			int best = 0;
			for(int i = 0; i < openset.size(); i++) {
				if(openset.get(i).f < openset.get(best).f ) {
					best = i;
				}
			}
			Node crnt = openset.get(best);
			if(openset.get(best) == grid[50][50] ) {
				return crnt;
			}
			openset.remove(crnt);
			closedset.add(crnt);

			ArrayList<Node> neighbors = crnt.neighbors;

			for(int i = 0; i < neighbors.size(); i++) {
				Node neighbor = neighbors.get(i);
				//if neighbor is blocked or on fire ignore and dont add to openset
				if(!closedset.contains(neighbor) && neighbor.x != 1 && neighbor.x != 3) {
					int tempg = crnt.g + 1;

					if(openset.contains(neighbor)) {
						if(tempg < neighbor.g) {
							neighbor.g = tempg;
						}
					}
					else {
						neighbor.g = tempg;
						openset.add(neighbor);
					}

					neighbor.h = Maze.getHeuristic(neighbor.row, neighbor.col);
					neighbor.f = neighbor.g + neighbor.h;
					neighbor.parent = crnt;
				}
			}
		}

		return null;
	}
//Same A* Star algorithm but with the change on the constraints of adding to the openset. Instead of checking neighboring nodes to see if they are on fire
// "3" we are also creating a restriction for the predicted fire "4"
	public static Node modified_aStar(Node[][] grid, Node start) {
		ArrayList<Node> openset = new ArrayList<Node>();
		ArrayList<Node> closedset = new ArrayList<Node>();
		openset.add(start);
		while(!openset.isEmpty()) {

			int best = 0;
			for(int i = 0; i < openset.size(); i++) {
				if(openset.get(i).f < openset.get(best).f ) {
					best = i;
				}
			}
			Node crnt = openset.get(best);
			if(openset.get(best) == grid[50][50] ) {
				return crnt;
			}
			openset.remove(crnt);
			closedset.add(crnt);

			ArrayList<Node> neighbors = crnt.neighbors;

			for(int i = 0; i < neighbors.size(); i++) {
				Node neighbor = neighbors.get(i);

				if(!closedset.contains(neighbor) && neighbor.x != 1 && neighbor.x != 4 && neighbor.x != 3) {
					int tempg = crnt.g + 1;

					if(openset.contains(neighbor)) {
						if(tempg < neighbor.g) {
							neighbor.g = tempg;
						}
					}
					else {
						neighbor.g = tempg;
						openset.add(neighbor);
					}

					neighbor.h = Maze.getHeuristic(neighbor.row, neighbor.col);
					neighbor.f = neighbor.g + neighbor.h;
					neighbor.parent = crnt;
				}
			}
		}

		return null;
	}

	//The algorithm spreads the fire in the maze based on the equation 1−(1−q)^k where q is the flammability and k represents neighboring nodes that are on fire
	public static void spreadFire(Node[][] grid) {


		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].x == 0) {
					int k = checkNeighborsOnFire(grid, i, j);
					double prob = 1 - Math.pow(1 - q, k);

					if(Math.random() <= prob) {
						grid[i][j].x = 3;
					}
				}	
			}	
		}		
	}

	//Same as spreadfire but instead of assigning "new" fire "3" we are assigning the predicted fire with "4", and 3 are also 3 iterations total of 
	//the predicted fire spread
	public static void spreadPredictedFire(Node[][] grid) {
		int steps = 0;
		while(steps  < 3) {
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++) {
					if (grid[i][j].x == 0) {
						int k = checkNeighborsOnFire(grid, i, j);
						double prob = 1 - Math.pow(1 - q, k);

						if(Math.random() <= prob) {
							grid[i][j].x = 4;
						}
					}	
				}	
			}
			steps++;
		}
	}
	
	//Same as predicted fire but with a maximum flammability constant and 5 iterations of predicted fire spread
	public static void spreadOverPredictedFire(Node[][] grid) {
		double qq = 1;
		int steps = 0;
		while(steps  < 5) {
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[i].length; j++) {
					if (grid[i][j].x == 0) {
						int k = checkNeighborsOnFire(grid, i, j);
						double prob = 1 - Math.pow(1 - qq, k);

						if(Math.random() <= prob) {
							grid[i][j].x = 4;
						}
					}	
				}	
			}
			steps++;
		}
	}
//Clears the predicted fire for the next iteration of predicted fire in main loops Agent 3 and Agent 4
	public static void clearPredictedFire(Node[][] grid) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if (grid[i][j].x == 4) {
					grid[i][j].x = 0;
				}	
			}
		}

	}
//Check to see if neighboring Nodes are on fire and returns the number of neighbors that are on fire
	public static int checkNeighborsOnFire(Node[][] grid, int i, int j) {
		int n = 0;
		if (i > 0 && grid[i - 1][j].x == 3)
			n++;
		if (j > 0 && grid[i][j - 1].x == 3)
			n++;
		if (i < 50 && grid[i + 1][j].x == 3)
			n++;
		if (j < 50 && grid[i][j + 1].x == 3)
			n++;

		return  n;
	}

	//Create a pathway working from the goal node backwards using curr.parent and a Stack to get a path from start to finish
	public static Stack<Node> configurePath(Node curr, Node start){
		Stack<Node> stack = new Stack<Node>();
		if(curr == null ) {
			return null;
		}
		while (curr != start) {
			stack.add(curr);
			curr = curr.parent;	
		}
		if(stack.isEmpty()) {
			return null;
		}
		return stack;
	}

	//runs trials 20 trials with different mazes on Agent 1 for each q-value at +0.1 increments 
	public static void testAgent1() {
		int trials = 0;
		int successes = 0;
		int i = 0;

		while(q <= 1.0) {
			while(i < 20) {
				trials++;
				Node [][] grid = create_Maze();
				String res = Agent1(grid);
				if(res.equals("Success")) {
					successes++;
				}
				i++;

			}
			System.out.println("At q =" + q + "Success rate is " + successes + "/" + trials );
			trials = 0;
			successes = 0;
			i = 0;
			q += 0.1;
		}

	}
	//runs trials 20 trials with different mazes on Agent 2 for each q-value at +0.1 increments 
	public static void testAgent2() {
		int trials = 0;
		int successes = 0;
		int i = 0;

		while(q <= 1.0) {
			while(i < 20) {
				trials++;
				Node [][] grid = create_Maze();
				String res = Agent2(grid);
				if(res.equals("Success")) {
					successes++;
				}
				i++;

			}
			System.out.println("At q =" + q + "Success rate is " + successes + "/" + trials );
			trials = 0;
			successes = 0;
			i = 0;
			q += 0.1;
		}

	}
	//runs trials 20 trials with different mazes on Agent 3 for each q-value at +0.1 increments 
	public static void testAgent3() {
		int trials = 0;
		int successes = 0;
		int i = 0;

		while(q <= 1.0) {
			while(i < 20) {
				trials++;
				Node [][] grid =  create_Maze();
				String res = Agent3(grid);
				if(res.equals("Success")) {
					successes++;
				}
				i++;

			}
			System.out.println("At q = " + q + " Success rate is " + successes + "/" + trials );
			trials = 0;
			successes = 0;
			i = 0;
			q += 0.1;
		}

	}
	//runs trials 20 trials with different mazes on Agent 4 for each q-value at +0.1 increments 
	public static void testAgent4() {
		int trials = 0;
		int successes = 0;
		int i = 0;

		while(q <= 1.0) {
			while(i < 20) {
				trials++;
				Node [][] grid = create_Maze();

				grid[26][26].x = 3;
				String res = Agent4(grid);
				if(res.equals("Success")) {
					successes++;
				}
				i++;

			}
			System.out.println("At q = " + q + " Success rate is " + successes + "/" + trials );
			trials = 0;
			successes = 0;
			i = 0;
			q += 0.1;
		}

	}
//Makes sure that maze generated is valid and adds the neighbors of each of the unit cells in the maze and also begins the fire in the center of the maze
	public static Node[][] create_Maze(){
		Node [][] grid = Maze.generate_Maze();
		while(!Maze.validate_Maze(grid) && !Maze.pathExists(grid,51,51)) {
			grid = Maze.generate_Maze();
		}
		Maze.getNeighbors(grid);
		grid[26][26].x = 3;
		return grid;
	}


}
