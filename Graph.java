import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Graph {
	private int edgeCount; //number of edges
	private int vertexCount; //number of vertices
	private List<List<String>> tree;
	
	//constructor
	public Graph() 
	{
		edgeCount = 0;
		vertexCount = 0;
		tree = new ArrayList<List<String>>();
	}
	
	//Add edge between vertex1 and vertex2
	public void addEdge(String vertex1, String vertex2){
		boolean find = false;
		if(tree.isEmpty()) //if the tree is empty, create a new array, set vertex1 as the first element for identification, and add the edge
		{
			tree.add(new ArrayList<String>());
			tree.get(0).add(vertex1);
			tree.get(0).add(vertex2);
			vertexCount++;
		}
		else
		{
			for(int i = 0; i < tree.size(); i++) //search through arrays for vertex1 identification, if it is found and the array does not contain vertex2 edge - add the edge
			{
				if(tree.get(i).get(0) == vertex1)
				{
					if(tree.get(i).contains(vertex2)) //does edge already exist?
						return;
					else
					{
						tree.get(i).add(vertex2);
						find = true;
					}
				}
			}
			if(!find) //if vertex array is not found, create it, set vertex1 as the first element for identification, and add the edge
			{
				tree.add(new ArrayList<String>());
				tree.get(tree.size()-1).add(vertex1);
				tree.get(tree.size()-1).add(vertex2);
				vertexCount++;
			}
				
		}
		edgeCount++;	
		vertexCount++;
	}
	//Retrieves number of vertices
	public int getSize(){
		return edgeCount + vertexCount;
	}
	//Print all edges from vertex1 to vertex2 where vertex1 matches pattern1, 
	//and vertex2 matches pattern2.
	public void printMatch(String pattern1, String pattern2){ 
		Pattern p1 = Pattern.compile(".*"+pattern1+".*");
		Pattern p2 = Pattern.compile(".*"+pattern2+".*");
		Matcher m1, m2;
		boolean b1, b2, find = false;
		for(int i = 0; i < tree.size(); i++)
		{
			m1 = p1.matcher(tree.get(i).get(0));
			b1 = m1.matches();
			if(b1)
			{
				for(int j = 0; j < tree.get(i).size(); j++)
				{
					m2 = p2.matcher(tree.get(i).get(j));
					b2 = m2.matches();
					if(b2)
					{
						System.out.println(tree.get(i).get(0) + " => " + tree.get(i).get(j));
						find = true;
					}
						
				}
			}
		}
		if(!find)
			System.out.println("No matches found");
	}
	//Find one path of passed-in length between vertex1 
	//that matches pattern1
	//and vertex2 that matches pattern2. 
	//Either print none found, or print the first matching path.
	public void printMatchingPath(String pattern1, String pattern2, int length){
		Pattern p1 = Pattern.compile(".*"+pattern1+".*");
		Matcher m1;
		boolean b1;
		String path = "", initialPath = null;
		for(int i = 0; i < tree.size(); i++) //go through 1st dimension of array and look for matching identifier
		{
			m1 = p1.matcher(tree.get(i).get(0));
			b1 = m1.matches();
			if(b1)
			{
				initialPath = tree.get(i).get(0); //starting vertex that matches pattern1
				for(int j = 1; j < tree.get(i).size(); j++) //go through 2nd dimension of the array that matched the identifier
				{
					path = checkVertex(initialPath, tree.get(i).get(j), pattern2, length-1); //search through edges in the 2nd dimension
					if(!path.equals("")) //if a path was returned, then this is the only path we need - stop the method
					{
						System.out.println(path);
						return;
					}
				}
			}
		}
		if(path.equals("")) //if blank is returned - nothing was found
			System.out.println("Path was not found");
	}
	
	public String checkVertex(String path, String vertex, String pattern, int length)
	{
		Pattern p = Pattern.compile(".*"+pattern+".*");
		Matcher m;
		boolean b;
		String result;
		//go through 1 dimension of array and look for matching identifier
		for(int i = 0; i < tree.size(); i++) 
		{
			//find the array with identifier that matches the vertex passed in
			if(tree.get(i).get(0) == vertex) 
			{
				//look through the 2nd dimension of found array to either descend further into another array 
				//or to look for the edged vertex that matches the pattern
				path+= " => " + vertex;
				for(int j = 1; j < tree.get(i).size(); j++)  
				{
					//if length is above zero, keep the recursion going
					if(length > 1)
					{
						result = checkVertex(path, tree.get(i).get(j), pattern, length-1);
						if(!result.isEmpty()) //if a path was found, stop the recursion
							return result;
					}
					//if the length is 0, the path is expected to end here. look for the vertex name in the current array
					else
					{
						m = p.matcher(tree.get(i).get(j));
						b = m.matches();
						if(b)
						{
							path+= " => " + tree.get(i).get(j);
							return path;
						}
					}
						
				}
			}
		}
		//if path match was not found in the expected depth, do not add anything to the initial path
		return "";
	}
	
	public static void main(String [] args)
	{
		Graph g = new Graph();
		//test addEdge from empty and getSize
		g.addEdge("cat", "dog");
		System.out.println("New Edge: cat => dog");
		int size = g.getSize();
		System.out.println("Graph size is: " + size + "\n");
		
		//test duplication validation
		g.addEdge("cat", "dog");
		System.out.println("New Edge: cat => dog");
		size = g.getSize();
		System.out.println("Graph size is: " + size + "\n");
		
		//test addition to size by 2
		g.addEdge("cat", "mouse");
		System.out.println("New Edge: cat => mouse");
		size = g.getSize();
		System.out.println("Graph size is: " + size + "\n");
		
		//test successful printMatch
		System.out.println("Looking for: ca and do");
		g.printMatch("ca", "do");
		System.out.println();
		
		//test unsuccessful printMatch
		System.out.println("Looking for: co and do");
		g.printMatch("co", "do");
		System.out.println();
		
		//test multiple matches and addition to size by 3
		g.addEdge("calf", "donkey");
		System.out.println("New Edge: calf => donkey");
		size = g.getSize();
		System.out.println("Graph size is: " + size);
		System.out.println("Looking for: ca and do");
		g.printMatch("ca", "do");
		System.out.println();
		
		//test path-finding (simple)
		g.addEdge("john", "alex");
	    g.addEdge("john", "each");
	    g.addEdge("each", "knee");
	    g.addEdge("each", "sun");
	    System.out.println(g.tree.toString());
	    System.out.println("Looking for: john and sun in 2 steps");
	    System.out.println("Correct answer: john => each => sun");
	    System.out.print("Your answer: ");
	    g.printMatchingPath("john", "sun", 2);
	    System.out.println();
		
		//test path-finding (advanced)
		g.addEdge("cat", "bob");
	    g.addEdge("john", "fred");
	    g.addEdge("john", "each");
	    g.addEdge("each", "knee");
	    g.addEdge("each", "sun");
	    g.addEdge("dog", "john");
	    g.addEdge("dog", "alex");
	    System.out.println(g.tree.toString());
	    System.out.println("Looking for: cat and sun in 4 steps");
	    System.out.println("Correct answer: cat => dog => john => each => sun");
	    System.out.print("Your answer: ");
	    g.printMatchingPath("cat", "sun", 4);
	    System.out.println();
	    
	    //test unsuccessful path-finding (length)
	    System.out.println("Looking for: cat and sun in 3 steps");
	    System.out.println("Correct answer: Path was not found");
	    System.out.print("Your answer: ");
	    g.printMatchingPath("cat", "sun", 3);
	    System.out.println();
	    
	    //test unsuccessful path-finding (pattern)
	    System.out.println("Looking for: cot and sun in 4 steps");
	    System.out.println("Correct answer: Path was not found");
	    System.out.print("Your answer: ");
	    g.printMatchingPath("cot", "sun", 4);
	    System.out.println();
	}
}
