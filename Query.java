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
	
	private static TreeMap<String,ArrayList<String>> dep_cap = new TreeMap<String,ArrayList<String>>();
	
	private static TreeMap<String,String> dep_srpm = new TreeMap<String,String>();
	private static TreeMap<String,String> dep_rpm = new TreeMap<String,String>();
	
	private static Graph graph = new Graph();
	
	public static void main(String[] args) {
		
		// Call reader methods to load CSV files. 
		ArrayReadCSV("/Users/pedro/Desktop/lb_goal.csv", goal);
		
		TreeReadCSV("/Users/pedro/Desktop/lb_dep_rpm.csv", dep_rpm);
		TreeReadCSV("/Users/pedro/Desktop/lb_dep_srpm.csv", dep_srpm);
		
		TreeMultiReadCSV("/Users/pedro/Desktop/lb_dep_cap.csv", dep_cap);
		
		
		// Goal sources will be used to get the ArrayList of dependencies,
		// and iterate through it, getting the binaries and adding edges
		// to the target sources.
		for(int i = 0; i < goal.size(); i++){
			// Get source string object.
			String source1 = goal.get(i);
			ArrayList<String> dependencies = dep_cap.get(source1);
			try{
				if(dependencies != null){
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
		System.out.println(searchGoal("perl"));
		System.out.println(graph.getEdgeSize() + "   ");
		System.out.println(graph.getVertexSize());
		graph.printMatch("perl", "perl");
		
		
	}
	// ArrayReadCSV - Method to read CSV files into 
	//	target ArrayList with given filename.

	public static void ArrayReadCSV(String filename, ArrayList<String> target){
		Scanner scanner = null;
		String readString;
		
		try {
			scanner = new Scanner(new File(filename));
			scanner.useDelimiter(",|\n|\r");
			
			while(scanner.hasNext()){
				readString = scanner.next();
				if(!readString.isEmpty() 
						&& !readString.equals("\"\"") 
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
	public static void TreeMultiReadCSV(String filename, TreeMap<String,ArrayList<String>> target){
		
		
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
