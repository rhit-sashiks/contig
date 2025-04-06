import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyMatrixGraph<T> {
	Map<T,Integer> keyToIndex;
	List<T> indexToKey;
	int[][] matrix;
	boolean isComplement;
	
	private int connectionExistsValue() {
		return this.isComplement ? 0 : 1;
	}
	
	private int connectionDoesNotExistsValue() {
		return this.isComplement ? 1 : 0;
	}
	
	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<T,Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		this.isComplement = false;
		// need to populate keyToIndex and indexToKey with info from keys
		int i = 0;
		for(T key: keys) {
			this.keyToIndex.put(key, i);
			this.indexToKey.add(key);
			i++;
		}
	}
	
	AdjacencyMatrixGraph(Set<T> keys, Set<Edge<T>> edgeSet) {
		this(keys);
		for(Edge<T> edge: edgeSet) {
			this.addEdge(edge.a, edge.b);
		}
	}
	
	AdjacencyMatrixGraph(Set<T> keys, int[][] matrix) {
		this(keys);
		this.matrix = matrix;
		
		if(keys.size() != matrix.length) {
			throw new RuntimeException("keys and matrix have differing length");
		}
	}
	
	public boolean isComplement() {
		return this.isComplement;
	}
	
	/* Re-interpret the graph as being its complement */
	public void setIsComplement(boolean isComplement) {
		this.isComplement = true;
	}
	
	public int size() {
		// TODO Auto-generated method stub
		return this.indexToKey.size();
	}

	public int numEdges() {
		// TODO Auto-generated method stub
		int numEdges = 0;
		for(int i = 0; i < this.size(); i++) {
			for(int j = 0; j < this.size(); j++) {
				if(this.matrix[i][j] == this.connectionExistsValue()) {
					numEdges++;
				}
			}
		}
		return numEdges;
	}

	public boolean addEdge(T from, T to) {
		// TODO Auto-generated method stub
		Integer fromInt = this.keyToIndex.get(from);
		if(fromInt == null) {
			throw new NoSuchElementException();
		}
		Integer toInt = this.keyToIndex.get(to);
		if(toInt == null) {
			throw new NoSuchElementException();
		}
		if(this.matrix[fromInt][toInt] == this.connectionExistsValue()) {
			return false;
		}
		this.matrix[fromInt][toInt] = this.connectionExistsValue();
		return true;
	}
	
	public boolean addUniEdge(T a, T b) {
		Integer fromInt = this.keyToIndex.get(a);
		if(fromInt == null) {
			throw new NoSuchElementException();
		}
		Integer toInt = this.keyToIndex.get(b);
		if(toInt == null) {
			throw new NoSuchElementException();
		}
		if(this.matrix[fromInt][toInt] == this.connectionExistsValue() && this.matrix[toInt][fromInt] == this.connectionExistsValue()) {
			return false;
		}
		this.matrix[fromInt][toInt] = this.connectionExistsValue();
		this.matrix[toInt][fromInt] = this.connectionExistsValue();
		return true;
	}

	public Set<T> vertices() {
		return this.keyToIndex.keySet();
	}
	
	public boolean hasVertex(T key) {
		// TODO Auto-generated method stub
		return this.keyToIndex.containsKey(key);
	}

	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		// TODO Auto-generated method stub
		Integer fromInt = this.keyToIndex.get(from);
		if(fromInt == null) {
			throw new NoSuchElementException();
		}
		Integer toInt = this.keyToIndex.get(to);
		if(toInt == null) {
			throw new NoSuchElementException();
		}
		return this.matrix[fromInt][toInt] == this.connectionExistsValue();
	}

	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		// TODO Auto-generated method stub
		Integer fromInt = this.keyToIndex.get(from);
		if(fromInt == null) {
			throw new NoSuchElementException();
		}
		Integer toInt = this.keyToIndex.get(to);
		if(toInt == null) {
			throw new NoSuchElementException();
		}
		if(this.matrix[fromInt][toInt] == this.connectionDoesNotExistsValue()) {
			return false;
		}
		this.matrix[fromInt][toInt] = this.connectionDoesNotExistsValue();
		return true;
	}

	public int outDegree(T key) {
		// TODO Auto-generated method stub
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}
		int outDegrees = 0;
		for(int i = 0; i < this.matrix.length; i++) {
			if(this.matrix[keyInt][i] == this.connectionExistsValue()) {
				outDegrees++;
			}
		}
		return outDegrees;
	}

	public int inDegree(T key) {
		// TODO Auto-generated method stub
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}
		int inDegrees = 0;
		for(int i = 0; i < this.matrix.length; i++) {
			if(this.matrix[i][keyInt] == this.connectionExistsValue()) {
				inDegrees++;
			}
		}
		return inDegrees;
	}

	public Set<T> keySet() {
		// TODO Auto-generated method stub
		return this.keyToIndex.keySet();
	}

	public Set<T> successorSet(T key) {
		// TODO Auto-generated method stub
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}
				
		Set<T> successors = new HashSet<T>();
		for(int i = 0; i < this.matrix.length; i++) {
			if(this.matrix[keyInt][i] == this.connectionExistsValue()) {
				successors.add(this.indexToKey.get(i));
			}
		}

		return successors;
	}

	public Set<T> predecessorSet(T key) {
		// TODO Auto-generated method stub
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}
				
		Set<T> predecessors = new HashSet<T>();
		for(int i = 0; i < this.matrix.length; i++) {
			if(this.matrix[i][keyInt] == this.connectionExistsValue()) {
				predecessors.add(this.indexToKey.get(i));
			}
		}

		return predecessors;
	}

	public Iterator<T> successorIterator(T key) {
		// TODO Auto-generated method stub
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}

		return new SuccessorIterator(this.matrix, this.indexToKey, keyInt);
	}
	
	class SuccessorIterator implements Iterator<T> {
		int[][] matrix;
		List<T> indexToKey;
		int i;
		int keyInt;
		
		SuccessorIterator(int[][] matrix, List<T> indexToKey, int keyInt) {
			this.matrix = matrix;
			this.indexToKey = indexToKey;
			this.i = 0;
			this.keyInt = keyInt;
		}	
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			while(this.i < this.matrix.length) {
				if(this.matrix[this.keyInt][this.i] == connectionExistsValue()) {
					return true;
				}
				this.i++;
			}
			return false;
		}

		@Override
		public T next() {
			// TODO Auto-generated method stub
			
			// Find next one
			while(this.i < this.matrix.length) {
				if(this.matrix[this.keyInt][this.i] == connectionExistsValue()) {
					int j = this.i;
					this.i++;
					return this.indexToKey.get(j);
				}
				this.i++;
			}
			
			throw new RuntimeException("no next element");
		}
	}

	public Iterator<T> predecessorIterator(T key) {
		Integer keyInt = this.keyToIndex.get(key);
		if(keyInt == null) {
			throw new NoSuchElementException();
		}

		return new PredecessorIterator(this.matrix, this.indexToKey, keyInt);
	}
	
	class PredecessorIterator implements Iterator<T> {
		int[][] matrix;
		List<T> indexToKey;
		int i;
		int keyInt;
		
		PredecessorIterator(int[][] matrix, List<T> indexToKey, int keyInt) {
			this.matrix = matrix;
			this.indexToKey = indexToKey;
			this.i = 0;
			this.keyInt = keyInt;
		}	
		
		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			while(this.i < this.matrix.length) {
				if(this.matrix[this.i][this.keyInt] == connectionExistsValue()) {
					return true;
				}
				this.i++;
			}
			return false;
		}

		@Override
		public T next() {
			// TODO Auto-generated method stub
			
			// Find next one
			while(this.i < this.matrix.length) {
				if(this.matrix[this.i][this.keyInt] == connectionExistsValue()) {
					int j = this.i;
					this.i++;
					return this.indexToKey.get(j);
				}
				this.i++;
			}
			
			throw new RuntimeException("no next element");
		}
	}
	
	// Edge from a to b
	public static class Edge<T> {
		T a;
		T b;
		
		Edge(T a, T b) {
			this.a = a;
			this.b = b;
		}
		
		public Edge<T> reverse() {
			return new Edge<T>(this.b, this.a);
		}
		
		@Override
		public String toString() {
			return "" + this.a + this.b;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object other) {
			if(other instanceof Edge) {
				Edge<T> otherEdge = (Edge<T>)other;
				boolean isEqual = this.a.equals(otherEdge.a) && this.b.equals(otherEdge.b);
				return isEqual;
			}
			
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.a.hashCode()+this.b.hashCode();
		}
	}
		
	public static class EdgeSet<T> {
		public Set<Edge<T>> edges;
		
		public EdgeSet(Set<Edge<T>> edges) {
			this.edges = edges;
		}
		
		@Override
		public String toString() {
			return this.edges.toString();
		}
		
		// CITATION: https://stackoverflow.com/questions/51113134/union-or-intersection-of-java-sets
		// for how to do this non-destructively
		/**
		 Takes the union of two set of edges
		*/
		public EdgeSet<T> union(Set<Edge<T>> edges) {
			Set<Edge<T>> newSet = new HashSet<>(this.edges);
			newSet.addAll(edges);
			return new EdgeSet<T>(newSet);
		}
		
		public EdgeSet<T> union(EdgeSet<T> edges) {
			return this.union(edges.edges);
		}
		
		/**
		 Takes the intersection of two sets of edges
		*/
		public EdgeSet<T> intersection(Set<Edge<T>> edges) {
			Set<Edge<T>> newSet = new HashSet<>(this.edges);
			newSet.retainAll(edges);
			return new EdgeSet<T>(newSet);
		}
		
		public EdgeSet<T> intersection(EdgeSet<T> edges) {
			return this.intersection(edges.edges);
		}
		
		/**
		 * Takes the difference of two sets of edges
		 */
		public EdgeSet<T> difference(Set<Edge<T>> edges) {
			Set<Edge<T>> newSet = new HashSet<>(this.edges);
			newSet.removeAll(edges);
			return new EdgeSet<T>(newSet);
		}
		
		public EdgeSet<T> difference(EdgeSet<T> edges) {
			return this.difference(edges.edges);
		}
		
		/**
		 * 'Flip'/'Invert' (?) / A^-1 of a edge set
		 */
		public EdgeSet<T> flipped() {
			Set<Edge<T>> newSet = new HashSet<>(this.edges.size());
			for(Edge<T> edge: this.edges) {
				newSet.add(edge.reverse());
			}
			return new EdgeSet<T>(newSet);
		}
		
		/**
		 * Returns the symmetric closure of the edge set (A-hat) which
		 * is union(A, A^-1)
		 */
		public EdgeSet<T> symmetricClosure() {
			return this.union(this.flipped());
		}
		
		/**
		 * Returns an arbitrary edge in the EdgeSet
		 */
		public Edge<T> arbitraryEdge() {
			if(this.edges.size() == 0) {
				throw new NoSuchElementException("No elements in edgeset to pick arbitary edge from");
			}
			
			Edge<T> e = null;
			for(Edge<T> edge: this.edges) {
				e = edge;
				break;
			}
			
			return e;
		}
	}
	
	/**
	 * Returns an edgeSet containing every edge in the graph
	 */
	public EdgeSet<T> edgeSet() {
		// TODO Auto-generated method stub
		Set<Edge<T>> edges = new HashSet<>();
		for(int i = 0; i < this.size(); i++) {			
			T a = this.indexToKey.get(i);
			for(int j = 0; j < this.size(); j++) {				
				if(this.matrix[i][j] == this.connectionExistsValue()) {
					T b = this.indexToKey.get(j);
					
					if(a.equals(b)) {
						continue;
					}
					
					edges.add(new Edge<T>(a, b));
				}
			}
		}
		return new EdgeSet<T>(edges);
	}
	
	// Returns what edges ab forces
	// an edge ab forces a'b' iff a = a' and bb' not in E *or* b = b' and aa' not in E 
	public EdgeSet<T> forces(Edge<T> edge) {
		// a = a' case
		Set<Edge<T>> forcedEdges = new HashSet<Edge<T>>();
		for(T neighboringVertex: this.successorSet(edge.a)) {
			// Check all neighbors if it satisfies the forcing property
			if(neighboringVertex == edge.b) {
				continue; // Skip ourselves;
			}
			
			// Does a-d, a-e etc satisfy the forcing property?
			Edge<T> neighborEdge = new Edge<T>(edge.a, neighboringVertex);
			if(this.edgeForces(edge, neighborEdge)) {
				forcedEdges.add(neighborEdge);
			}
		}
		
		// b = b' case
		for(T neighboringVertex: this.successorSet(edge.b)) {
			// Check all neighbors if it satisfies the forcing property
			if(neighboringVertex == edge.a) {
				continue; // Skip ourselves;
			}
			
			// Does it satisfy the forcing property?
			Edge<T> neighborEdge = new Edge<T>(neighboringVertex, edge.b);
			if(this.edgeForces(edge, neighborEdge)) {
				forcedEdges.add(neighborEdge);
			}
		}
		
		return new EdgeSet<T>(forcedEdges);
	}
	
	private boolean edgeForces(Edge<T> e1, Edge<T> e2) {
		return (e1.a == e2.a && !this.hasEdge(e1.b, e2.b)) || (e1.b == e2.b && !this.hasEdge(e1.a, e2.a)); 
	}
	
	// Deduce the reflexive, transitive closure about an edge E
	public EdgeSet<T> deduceImplicationClass(Edge<T> edge) {
		Set<Edge<T>> edges = new HashSet<>();
		
		ArrayDeque<Edge<T>> edgeSearchSet = new ArrayDeque<>();
		edgeSearchSet.add(edge);
		
		while(!edgeSearchSet.isEmpty()) {
			Edge<T> nextEdge = edgeSearchSet.pop();
			if(edges.contains(nextEdge)) {
				continue;
			}
			
			edges.add(nextEdge);
			
			for(Edge<T> forcedEdge: this.forces(nextEdge).edges) {
				edgeSearchSet.add(forcedEdge); // Push to edgeSearchSet
			}
		}
		
		return new EdgeSet<T>(edges);
	}
		
	// Deduces a potential transitive orientation to the given graph
	public EdgeSet<T> transitiveOrientation() {
		// TODO: maybe optimize this slightly
		EdgeSet<T> currentEdges = this.edgeSet();
		EdgeSet<T> orientation = new EdgeSet<>(new HashSet<>()); 
		while(true) {
			// 1. Arbitrarily pick an edge
			System.out.println("Edges: " + currentEdges);
			Edge<T> edge = currentEdges.arbitraryEdge();
			EdgeSet<T> implicationClass = this.deduceImplicationClass(edge); // B
			EdgeSet<T> implicationClassFlipped = implicationClass.flipped(); // B^-1
			System.out.println("Implication Classes About This Edge: " + implicationClass + " " + implicationClassFlipped + " " +implicationClass.intersection(implicationClassFlipped));
						
			// If B intersection B^-1 == null set
			if(implicationClass.intersection(implicationClassFlipped).edges.size() == 0) {
				orientation = orientation.union(implicationClass); // Add Bi to F
			} else {
				System.out.println("ERROR: " + implicationClass.intersection(implicationClassFlipped));
				throw new Error("Graph is not a valid transitively orientable graph");
			}
			
			currentEdges = currentEdges.difference(implicationClass.symmetricClosure());
			
			if(currentEdges.edges.size() == 0) {
				return orientation; // Return the orientation now that we're done here
			}
		}
	}
	
	// Tries to find the max cliques of a graph
}