package maze;
import java.util.ArrayList;

public class Node {

	Node parent; // references the previous Node object
	ArrayList<Node> neighbors = new ArrayList<Node>(); //Contains the neighbors of each cell object
	int x; // Represents the value of the cell in the maze 0: unblocked, 1: blocked, 2: path of the agent, 3: fire, 4: predicted fire
	int f = 0; //Final value of A* Search
	int g = 0; //G value of A* Search 
	int h = 0; //Heuristic value of A* Search
	int row; //Represents the row of the Node object in the 2D array
	int col; // Represents the column of the Node object in the 2D array
	boolean visited = false; //Signifies whether Node object has been visited or not
	
	//Node object constructor
	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	
}
