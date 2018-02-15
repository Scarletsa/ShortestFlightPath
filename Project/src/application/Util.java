package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

import com.jaunt.UserAgent;

/**
 * public class Util
 * 
 * This class will do all of the heavy
 * lifting needed for the program to run.
 * 
 * @author Michael Albrecht
 * @author Sarah Carr
 * @author Shelby Medlock
 * 
 * Filename: Util.java
 * Course: CSCI 2082
 * College: Century
 *
 */

public class Util {
	public static Map<String, Vertex> map = new HashMap<String, Vertex>();
	
	public static void computePaths(Vertex source) {
        resetPrevious();
		PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
		source.setMinDistance(0.);
		vertexQueue.add(source);

		while (!vertexQueue.isEmpty()) {
			Vertex u = vertexQueue.poll();

			if (u.getAdjacencies() != null) {
				// Visit each edge exiting u
				for (Edge e : u.getAdjacencies()) {
					Vertex v = e.getTarget();
					int weight = e.getWeight();
					int distanceThroughU = (int) (u.getMinDistance() + weight);

					if (distanceThroughU < v.getMinDistance()) {
						vertexQueue.remove(v);

						v.setMinDistance(distanceThroughU);
						v.setPrevious(u);
						vertexQueue.add(v);
					}
				}
			}
		}
	}

	public static ArrayList<Vertex> getShortestPathTo(Vertex target) {
		ArrayList<Vertex> path = new ArrayList<Vertex>();
		for (Vertex vertex = target; vertex != null; vertex = vertex.getPrevious()) {
			path.add(vertex);
			System.out.println(vertex.toString() + "added.");
		}

		Collections.reverse(path);
		System.out.println(path);
		return path;
	}
	
	public static BufferedReader readFile(String filename) {
		try {
			FileReader fileReader = new FileReader(filename);
			return new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void readAirports(String filename) {
		String iataUS = "";
		int iataCount = 0;
		String line = null;	
		
		BufferedReader bufferedReader = readFile(filename);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				String[] airports = line.split(",");
				if (airports[3].contains("United States")) {
					
					String name = airports[1];
					String country = airports[3];
					String iataCode = airports[4];
					
					if (!airports[4].equals("\\N")) {
						iataUS += airports[4] + " ";
					}
					if (iataCount%20 == 0) {
						iataUS += "\n";
					}
					
					Vertex terminal = new Vertex(name, iataCode, country);
					if (!map.containsKey(iataCode)) {
						map.put(iataCode, terminal);
					}
				}
			}
			if (new File("flighttimesUS.txt").length() < 1) {
				writeFlightTimes("Flights.txt", iataUS);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("\nSomething went wrong in readFile...");
		}
	}
	
	public static void writeFlightTimes(String filename, String US_IATAs) {
		try {
			UserAgent user = new UserAgent();
			BufferedWriter writer = new BufferedWriter(new FileWriter("flighttimesUS.txt"));
			String line = null;
			
			BufferedReader bufferedReader = readFile(filename);
			while ((line = bufferedReader.readLine()) != null) {
				String[] flights = line.split(",");
				if (US_IATAs.contains(flights[2]) && US_IATAs.contains(flights[4]) && !flights[2].contains(flights[4])) {
					String source = flights[2];
					String destination = flights[4];
					user.visit("https://www.travelmath.com/flying-time/from/MSP/to/LAX");
					user.doc.fillout("To:", destination);
					user.doc.fillout("From: ", source);
					user.doc.submit("CALCULATE");
					line =  source + "," + destination + "," + user.doc.findFirst("<h3 class=\"space\" id=\"flyingtime\">").getText();
					System.out.println(line);
					writer.write(line);
					writer.newLine();
					writer.flush();
				}
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nSomething went wrong in writeFlightTimes...");
		} finally {
			
		}
	}
	
	public static void readFlightTimes(String filename) {
		try {
			String line = null;
			int minutes = 0;

			BufferedReader bufferedReader = readFile(filename);
			while ((line = bufferedReader.readLine()) != null) {
				String[] path = line.split(",");
				
				String src = path[0];
				String dest = path[1];
				
				if (path.length == 3 && path[2].contains("minutes")) {
					String[] time = path[2].split(" ");
					minutes = Integer.parseInt(time[0]);
				} else if (path.length == 3 && path[2].contains("hour")) {
					String[] time = path[2].split(" ");
					minutes = Integer.parseInt(time[0])*60;
				} else if (path.length == 4) {
					String[] hour = path[2].split(" ");
					String[] minute = path[3].split(" ");
					minutes = Integer.parseInt(minute[1]);
					minutes += Integer.parseInt(hour[0])*60;
				}
				
				Edge temp = new Edge(map.get('"' + dest + '"'), minutes);
				addAdjacency(src, temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nSomething went wrong in readFlightTimes...");
		} finally {
			
		}
	}
	
	public static void addAdjacency(String source, Edge edge) {
		if (map.get('"' + source + '"').getAdjacencies() == null) {
			map.get('"' + source + '"').setAdjacencies(edge);
		} else if (!map.get('"' + source + '"').getAdjacencies().contains(edge)) {
			map.get('"' + source + '"').setAdjacencies(edge);
		}
	}
	
	public static String calculateFlightTime(String src, String dest) {
		int time;
		int minutes;
		int hours;
		String ret = "";
        computePaths(getVertex(src));
        ArrayList<Vertex> path = getShortestPathTo(map.get(dest));
        ret += path.toString();
        time = (int) map.get(dest).getMinDistance();
        minutes = time%60;
        hours = (int) Math.floor(time/60);
        if (((path.get(0)).getMinDistance() == Double.POSITIVE_INFINITY)) { (path.get(0)).setMinDistance(0); }
        	if (hours == 0) {
        		ret += "Time from " + src + " to " + dest + " is roughly " + minutes + " minutes.";
        	} else {
        		ret += "Time from " + src + " to " + dest + " is roughly " + hours + " hours " + minutes + " minutes.";
        	}
		return ret;
	}
	
	public static Vertex getVertex(String iata) {
		return map.get(iata);
	}
	
	public static void resetPrevious() {
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (((Vertex) pair.getValue()).getPrevious() != null) {
				((Vertex) pair.getValue()).setPrevious(null);
			} 
		}
		System.out.println("Paths have been reset.");
	}
	
	public static void main(String args[]) {
		readAirports("airports.txt");
		readFlightTimes("flighttimesUS.txt");

		GuiManager driver = new GuiManager(map);
		driver.setVisible(true);
	}
}
