package assignment3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Query {
	
	// Private Member Variables
	private static ArrayList<String> goal = new ArrayList<String>();
	
	private static TreeMap<String,ArrayList<String>> dep_cap 
		= new TreeMap<String,ArrayList<String>>();
	
	private static TreeMap<String,String> dep_srpm = new TreeMap<String,String>();
	private static TreeMap<String,String> dep_rpm = new TreeMap<String,String>();
	
	private static Graph graph = new Graph();
	
	public static void main(String[] args) {
		
		System.out.println("*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*");
		System.out.println("|            Assignment 3              |");
		System.out.println("|         Query Dependencies           |");
		System.out.println("*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*");
		
		System.out.println("Loading CSV files...");
		
		// Call reader methods to load CSV files. 
		ArrayReadCSV("/Users/pedro/Desktop/lb_goal.csv", goal);
		
		TreeReadCSV("/Users/pedro/Desktop/lb_dep_rpm.csv", dep_rpm);
		TreeReadCSV("/Users/pedro/Desktop/lb_dep_srpm.csv", dep_srpm);
		
		TreeMultiReadCSV("/Users/pedro/Desktop/lb_dep_cap.csv", dep_cap);
		
		System.out.println("[Done]");
		
		// Goal sources will be used to get the ArrayList of dependencies,
		// and iterate through it, getting the binaries and adding edges
		// to the target sources.
		
		System.out.println("Adding Edges...");
		
		for(int i = 0; i < goal.size(); i++){
			// Get source string object.
			String source1 = goal.get(i);
			// Use source string to find list of dependencies.
			ArrayList<String> dependencies = dep_cap.get(source1);
			try{
				if(dependencies != null){
					// Find binaries and their sources and add edges.
					for(int j = 0; j < dependencies.size(); j++){
						String binary = dep_rpm.get(dependencies.get(j));
						String source2 = dep_srpm.get(binary);
						graph.addEdge(source1, source2);						
					}
				}
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			
		}
		System.out.println("[Done]");

		Scanner scanner = new Scanner(System.in);
		boolean quit = false;
		System.out.println("\n==== (Type 'help' for commands):");
		System.out.print(">> ");
		do{
	
			String option = scanner.next();
			switch(option) {
				case "size": {
					System.out.println("\n|=======================================|\n");
					System.out.println("Total # of Vertices: " + graph.getVertexSize());
					System.out.println("Total # of Edges   : " + graph.getEdgeSize());
					System.out.println("\n|=======================================|\n");
					break;
				}
				case "goal":{
					String source = scanner.next();
					ArrayList<String> goals = searchGoal(source);
					System.out.println("~~~Goal Matches:");
					for(String goal : goals){
						System.out.println(goal);
					}
					break;
				}
				case "dep":{
					System.out.println("~~~Dependencies Matches:");
					String source1 = scanner.next();
					String source2 = scanner.next();
					graph.printMatch(source1, source2);
					break;
				}
				case "path":{
					System.out.println("~~~Path:");
					String source1 = scanner.next();
					String source2 = scanner.next();
					int size = scanner.nextInt(); 
					graph.printMatchingPath(source1, source2, size);
					break;
				}
				case "quit":{
					quit = true;
					break;
				}
				case "help":{
					System.out.println("size                         - Print size of graph.");
					System.out.println("goal <pattern>               - Print matching goals.");
					System.out.println("dep <string> <string>        - Print size of graph.");
					System.out.println("path <string> <string> <int> - Print path of given length.");
					System.out.println("quit                         - Exit program.");
				}
				default:{
					System.out.println("Try again.");
					break;
				}
			}
			System.out.println("\n==== (Type 'help' for commands):");
			System.out.print(">> ");
		}while(!quit);
		
		scanner.close();
		
		
		
		
	}
	// ArrayReadCSV - Method to read CSV files into 
	//	target ArrayList with given filename.

	public static void ArrayReadCSV(String filename, ArrayList<String> target){
		Scanner scanner = null;
		String readString; // Hold strings that were retrieved from file. x
		
		try {
			scanner = new Scanner(new File(filename));
			scanner.useDelimiter(",|\n|\r");
			
			while(scanner.hasNext()){
				readString = scanner.next();
				if(!readString.isEmpty() 
						&& !readString.equals("\"\"") // Ignore theses entries.
						&& !readString.equals("0")){
					target.add(readString);
				}
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException np){
			np.printStackTrace();
		}	
	}
	
	// TreeReadCSV - Method to read CSV files into TreeMap<String,String> 
	// with given filename.
	// Assumption is that CSV file have two columns. 
	public static void TreeReadCSV(String filename, TreeMap<String,String> target){
		BufferedReader buffer = null;
		String line = "";
		try {
			buffer = new BufferedReader(new FileReader(filename));
			while ((line = buffer.readLine()) != null) {
			    // Split line into array of strings. 
				// strings[0] = First column.
				// strings[1] = Second column.
				String[] strings = line.split(",");
	 
				target.put(strings[0], strings[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	// TreeMultiReadCSV - Method to read CSV files in target with give filename,
	// Each key has multiple values stored in an ArrayList<String>.
	public static void TreeMultiReadCSV(String filename, 
			TreeMap<String,ArrayList<String>> target){
		BufferedReader buffer = null;
		String line = "";
		try {
	 
			buffer = new BufferedReader(new FileReader(filename));
			// Go through lines from file
			while((line = buffer.readLine())!= null){
				
				String[] strings = line.split(",");
				String key = strings[0];
				ArrayList<String> values = new ArrayList<String>();
				// If the key does not exist, add read string to ArrayList.
				if(target.get(key) == null){
					values.add(strings[1]);
				}
				// If key exists, get values and add read string to existing
				// ArrayList.
				else{
					values = target.get(key);
					values.add(strings[1]);
				}
				target.put(key, values);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	
	// searchGoal - Method to iterate through goal Array and gather all 
	// matches for given pattern. 
	public static ArrayList<String> searchGoal(String pattern){
		ArrayList<String> matches = new ArrayList<String>();
		for(String match : goal){
			if(match.matches("(?i)("+ pattern + ").*")){
				matches.add(match);
			}
		}
		return matches;
	}
	
}
