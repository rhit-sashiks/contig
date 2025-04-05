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
	
	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<T,Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		int i = 0;
		for(T key: keys) {
			this.keyToIndex.put(key, i);
			this.indexToKey.add(key);
			i++;
		}
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
				if(this.matrix[i][j] == 1) {
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
		if(this.matrix[fromInt][toInt] == 1) {
			return false;
		}
		this.matrix[fromInt][toInt] = 1;
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
		if(this.matrix[fromInt][toInt] == 1 && this.matrix[toInt][fromInt] == 1) {
			return false;
		}
		this.matrix[fromInt][toInt] = 1;
		this.matrix[toInt][fromInt] = 1;
		return true;
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
		return this.matrix[fromInt][toInt] == 1;
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
		if(this.matrix[fromInt][toInt] == 0) {
			return false;
		}
		this.matrix[fromInt][toInt] = 0;
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
			if(this.matrix[keyInt][i] == 1) {
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
			if(this.matrix[i][keyInt] == 1) {
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
			if(this.matrix[keyInt][i] == 1) {
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
			if(this.matrix[i][keyInt] == 1) {
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
				if(this.matrix[this.keyInt][this.i] == 1) {
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
				if(this.matrix[this.keyInt][this.i] == 1) {
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
				if(this.matrix[this.i][this.keyInt] == 1) {
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
				if(this.matrix[this.i][this.keyInt] == 1) {
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
		
		@Override
		public String toString() {
			return "" + this.a + this.b;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public boolean equals(Object other) {
			if(other instanceof Edge) {
				Edge<T> otherEdge = (Edge<T>)other;
				return this.a == otherEdge.a && this.b == otherEdge.b;
			}
						
			return false;
		}
		
		@Override
		public int hashCode() {
			return this.a.hashCode()+this.b.hashCode();
		}
	}
		
	// Returns what edges ab forces
	// an edge ab forces a'b' iff a = a' and bb' not in E *or* b = b' and aa' not in E 
	public Set<Edge<T>> forces(Edge<T> edge) {
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
		
		return forcedEdges;
	}
	
	private boolean edgeForces(Edge<T> e1, Edge<T> e2) {
		return (e1.a == e2.a && !this.hasEdge(e1.b, e2.b)) || (e1.b == e2.b && !this.hasEdge(e1.a, e2.a)); 
	}
	
	// Deduce the reflexive, transitive closure about an edge E
	public Set<Edge<T>> deduceImplicationClass(Edge<T> edge) {
		Set<Edge<T>> edges = new HashSet<>();
		
		ArrayDeque<Edge<T>> edgeSearchSet = new ArrayDeque<>();
		edgeSearchSet.add(edge);
		
		while(!edgeSearchSet.isEmpty()) {
			Edge<T> nextEdge = edgeSearchSet.pop();
			if(edges.contains(nextEdge)) {
				continue;
			}
			
			edges.add(nextEdge);
			
			for(Edge<T> forcedEdge: this.forces(nextEdge)) {
				edgeSearchSet.add(forcedEdge); // Push to edgeSearchSet
			}
		}
		
		return edges;
	}
}