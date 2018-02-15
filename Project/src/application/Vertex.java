package application;

import java.util.ArrayList;

/**
 * public class Vertex
 * 
 * This is the class needed to create Vertex objects,
 * which will be connected through different Edges.
 * 
 * @author Michael Albrecht
 * @author Sarah Carr
 * @author Shelby Medlock
 * 
 * Filename: Vertex.java
 * Course: CSCI 2082
 * College: Century
 *
 */

public class Vertex {
	private final String airportName;
	private final String iataCode;
	private final String country;
    private ArrayList<Edge> adjacencies;
    private double minDistance = Double.POSITIVE_INFINITY;
    private Vertex previous;
    
    public Vertex(String argName, String code, String country) {
    	this.airportName = argName;
    	this.iataCode = code;
    	this.country = country;
    }

	public ArrayList<Edge> getAdjacencies() {
		return adjacencies;
	}

	public void setAdjacencies(Edge adjacencies) {
		if (this.adjacencies == null) {
			this.adjacencies = new ArrayList<Edge>(50);
		}
		this.adjacencies.add(adjacencies);
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	public Vertex getPrevious() {
		return previous;
	}

	public void setPrevious(Vertex previous) {
		this.previous = previous;
	}

	public String getAirportName() {
		return airportName;
	}

	public String getIataCode() {
		return iataCode;
	}

	public String getCountry() {
		return country;
	}

	@Override
	public String toString() {
		return iataCode;
	}

	public String printAdjacencies() {
		String temp = "";
		for (int i = 0; i < adjacencies.size(); i++) {
			temp += adjacencies.get(i).toString() + " ";
		}
		return temp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adjacencies == null) ? 0 : adjacencies.hashCode());
		result = prime * result + ((airportName == null) ? 0 : airportName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((iataCode == null) ? 0 : iataCode.hashCode());
		long temp;
		temp = Double.doubleToLongBits(minDistance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((previous == null) ? 0 : previous.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vertex other = (Vertex) obj;
		if (adjacencies == null) {
			if (other.adjacencies != null)
				return false;
		} else if (!adjacencies.equals(other.adjacencies))
			return false;
		if (airportName == null) {
			if (other.airportName != null)
				return false;
		} else if (!airportName.equals(other.airportName))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (iataCode == null) {
			if (other.iataCode != null)
				return false;
		} else if (!iataCode.equals(other.iataCode))
			return false;
		if (Double.doubleToLongBits(minDistance) != Double.doubleToLongBits(other.minDistance))
			return false;
		if (previous == null) {
			if (other.previous != null)
				return false;
		} else if (!previous.equals(other.previous))
			return false;
		return true;
	}
}