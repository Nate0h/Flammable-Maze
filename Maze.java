package maze;
import java.util.Random;
import java.util.*;
import java.util.Stack;

public class Maze {

//this function generates a 2D object maze of class node with 51 rows and 51 columns
//populate maze fills the maze with 0's (which represents open cells) and 1's
//(which represents blocked cells)
	public static Node[][] generate_Maze() {
		Node[][] maze = new Node[51][51];
		populate_Maze(maze);
		return maze;
	}
//this functions fills the maze with 0's and 1's based on the random function "generate_Zero_Or_One"
	public static void populate_Maze(Node[][] maze) {
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				maze[i][j] = new Node(i,j);
				maze[i][j].row = i;
				maze[i][j].col = j;
				maze[i][j].x = generate_Zero_Or_One();
			}
		}
	}

//This functions adds all the neighboring cells (up, down, left, right) of the current cell into the object variable 
//neighbors which is an ArrayList
	public static void getNeighbors(Node[][] maze) {
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				if(i-1 >= 0) {
					maze[i][j].neighbors.add(maze[i-1][j]);
				}
				if(i+1 <= 50) {
					maze[i][j].neighbors.add(maze[i+1][j]);
				}
				if(j-1 >= 0) {
					maze[i][j].neighbors.add(maze[i][j-1]);
				}
				if(j+1 <= 50) {
					maze[i][j].neighbors.add(maze[i][j+1]);
				}
			}
		}
	}
//I used the Random class which I used to generate number between 0 and 100
//If the number generated falls below 30 I returned 1 else I returned 0
//Thus giving each cell in the maze a probability of 30% of being blocked
	public static int generate_Zero_Or_One() {
		int min = 0;
		int max = 100;

		Random random = new Random();
		int value = random.nextInt(max + min) + min;
		if(value <= 30) 
			return 1;
		else 
			return 0;
	}
//I checked to see if there was a path from the center of the maze to each of the four corners of the maze using DFS search
//I also checked to see if the starting point, the center, and the goal cells of the maze were blocked, if they were then I unblocked them
	public static boolean validate_Maze(Node[][] maze) {
		if(maze[0][0].x == 1) {
			maze[0][0].x = 0;
		}
		if(maze[26][26].x == 1) {
			maze[26][26].x = 0;
		}
		if(maze[50][50].x == 1) {
			maze[50][50].x = 0;	
		}
		boolean isValid = DFS(maze);
		return isValid;
	}
//Completed a DFS search with a stack implementation from the center to each of the four corners of the maze, 
//I set four variables which represent the locations each of the four corners in the maze (ul,ur,ll,lr) 
// And the DFS search checks if all these locations have been visited if all of them have not then DFS returns false
// If all the locations are reachable from the center then DFS returns true
	public static boolean DFS(Node[][] grid) {
		boolean ul = false;
		boolean ur = false;
		boolean ll = false;
		boolean lr = false;
		int h = grid.length;
		int l = grid[0].length;
		boolean[][] visited = new boolean[h][l];
		Stack<Node> stack = new Stack<>();
		stack.push(grid[25][25]);

		while (stack.empty() == false) {
			Node x = stack.pop();
			int row = x.row;
			int col = x.col;

			if(row<0 || col<0 || row>=h || col>=l || visited[row][col] || grid[row][col].x == 1)
				continue;
			visited[row][col]=true;

			if(row == 0 && col == 0) {
				ul = true;
			}
			if(row == 0 && col == 50){
				ur = true;
			}
			if(row == 50 && col == 0) {
				ll = true;
			}
			if(row == 50 && col == 50) {
				lr = true;
			}
			if (col - 1 >= 0) {
			stack.push(grid[row][col-1]); //go left
			}
			if( col + 1 <= 50) {
			stack.push(grid[row][col+1]); //go right
			}
			if(row -1 >= 0) {
			stack.push(grid[row-1][col]); //go up
			}
			if(row + 1 <= 50) {
			stack.push(grid[row+1][col]); //go down
			}

		}
		if(ul && ur && ll && lr) {
			return true;
		}
		return false;
	}
	//Implementation of the Manhattan heuristic which returns the number of cells to get to the goal cell
	public static int getHeuristic(int x , int y) {

		return Math.abs(x - 50) + Math.abs(y - 50);
	}
	
//path Exists uses a recursive DFS approach to see if there is a path from the starting cell 0,0 to the goal cell 50,50
	public static boolean pathExists(Node[][] maze, int row, int col) {
		// base cases         
		if (row < 0 || col < 0 || row >= maze.length || col >= maze[0].length) 
			return false; // out-of-bounds
		if (maze[row][col].x != 0)
			return false; // avoid visiting walls or re-visiting
		if (row == maze.length-1 && col == maze[0].length-1)
			return true;  // success!

		// mark & prepare for recursion
		maze[row][col].x = 2; 
		return pathExists(maze, row+1, col) ||
				pathExists(maze, row-1, col) ||
				pathExists(maze, row, col+1) ||
				pathExists(maze, row, col-1);
	}
//Displays the maze
	public static void printMaze(Node[][] maze) {
		for(int i = 0; i < maze.length; i++) {
			for(int j = 0; j < maze[i].length; j++) {
				System.out.print(maze[i][j].x + " ");
			}
			System.out.println();
		}
	}

}
