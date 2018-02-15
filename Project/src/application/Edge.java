package application;


/**
 * public class Edge
 * 
 * This is the class needed to create Edge objects,
 * which connect the Vertex objects by different weights.
 * 
 * @author Michael Albrecht
 * @author Sarah Carr
 * @author Shelby Medlock
 * 
 * Filename: Edge.java
 * Course: CSCI 2082
 * College: Century
 *
 */
public class Edge {
	private final Vertex target;
	private final int weight;

	public Edge(Vertex argTarget, int argWeight) {
		target = argTarget;
		weight = argWeight;
	}

	public Vertex getTarget() {
		return target;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "[IATA=" + target.getIataCode() + " weight=" + weight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		result = prime * result + weight;
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
		Edge other = (Edge) obj;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.getIataCode().equals(other.target.getIataCode()))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

}
