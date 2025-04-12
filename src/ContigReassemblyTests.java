import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;


/**
 * Milestone 1 speed tests and helper functions
 * 
 * 
 * @author Nate Chenette. Improved lazy iterator speed tests by Henry Nunns. Refactor by Randyn Tarnoff.
 * 
 */
public class ContigReassemblyTests {

	private static int m1points = 0;
	public static int m1weight = 2;
	private static final int MAX_POINTS = 12;

	public static boolean speedTestsOn = true; // turn to false for faster testing without speed tests

	// MILESTONE 1
	// Implement the following methods first:
	// - the constructor taking a Set of data
	// - the addEdge method

	public static Set<String> getThreeVerticesToAdd() {
		HashSet<String> toInsert = new HashSet<String>();
		toInsert.add("a");
		toInsert.add("b");
		toInsert.add("c");
		return toInsert;
	}
	
	public static void helperTestAddEdge(AdjacencyMatrixGraph<String> g, Points m1points) {
		g.addEdge("a", "b");
		assertEquals(true,g.addEdge("b", "c"));
		m1points.add(2*m1weight);
		assertEquals(false,g.addEdge("b", "c"));
		try {
			g.addEdge("b", "d");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(m1weight);
	}
	
	public static Set<String> getExampleVertexData() {
		String[] toInsert = {"a","b","c","d","e","f"};
		HashSet<String> items = new HashSet<String>();
		for (String str : toInsert) {
			items.add(str);
		}
		return items;
	}
	
	public static Set<Integer> getExample2VertexData() {
		Integer[] toInsert = {0,1,2,3,4,5,6};
		HashSet<Integer> items = new HashSet<Integer>();
		for (Integer i : toInsert) {
			items.add(i);
		}
		return items;
	}
	
	public static void addExampleEdges(AdjacencyMatrixGraph<String> g) {
		g.addEdge("a", "b");
		g.addEdge("a", "c");
		g.addEdge("b", "d");
		g.addEdge("c", "d");
		g.addEdge("d", "c");
		g.addEdge("d", "e");
		g.addEdge("d", "f");
		g.addEdge("f", "c");
	}

	public static void addExample2Edges(AdjacencyMatrixGraph<Integer> g) {
		g.addEdge(0, 1);
		g.addEdge(1, 0);
		g.addEdge(0, 2);
		g.addEdge(2, 3);
		g.addEdge(2, 4);
		g.addEdge(3, 4);
		g.addEdge(4, 5);
		g.addEdge(4, 6);
		g.addEdge(6, 2);
	}
		
	public static AdjacencyMatrixGraph<String> makeExampleAMGraph() {
		AdjacencyMatrixGraph<String> g = new AdjacencyMatrixGraph<String>(getExampleVertexData());
		addExampleEdges(g);
		return g;
	}

	public static AdjacencyMatrixGraph<Integer> makeExample2AMGraph() {
		AdjacencyMatrixGraph<Integer> g = new AdjacencyMatrixGraph<Integer>(getExample2VertexData());
		addExample2Edges(g);
		return g;
	}
	
	public static void helperTestHasEdge(AdjacencyMatrixGraph<String> g, AdjacencyMatrixGraph<Integer> g2, Points m1points) {
		assertTrue(g.hasEdge("a","b"));
		assertTrue(g.hasEdge("f","c"));
		assertFalse(g.hasEdge("b","c"));
		assertFalse(g.hasEdge("b","a"));
		assertTrue(g2.hasEdge(0,2));
		assertTrue(g2.hasEdge(4,6));
		assertFalse(g2.hasEdge(2,5));
		assertFalse(g2.hasEdge(2,6));
		m1points.add(m1weight);
		try {
			g.hasEdge("a", "g"); // g not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		try {
			g2.hasEdge(10, 5);   // 10 not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(m1weight);
	}

	public static void helperTestInDegree(AdjacencyMatrixGraph<String> g, Points m1points) {
		assertEquals(0, g.inDegree("a"));
		assertEquals(1, g.inDegree("b"));
		assertEquals(3, g.inDegree("c"));
		assertEquals(2, g.inDegree("d"));
		assertEquals(1, g.inDegree("e"));
		assertEquals(1, g.inDegree("f"));
		try {
			g.inDegree("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(1.5*m1weight);
	}

	public static void helperTestOutDegree(AdjacencyMatrixGraph<String> g, Points m1points) {
		assertEquals(2, g.outDegree("a"));
		assertEquals(1, g.outDegree("b"));
		assertEquals(1, g.outDegree("c"));
		assertEquals(3, g.outDegree("d"));
		assertEquals(0, g.outDegree("e"));
		assertEquals(1, g.outDegree("f"));
		try {
			g.outDegree("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(1.5*m1weight);
	}

	public static void helperTestHasVertex(AdjacencyMatrixGraph<String> g, AdjacencyMatrixGraph<Integer> g2, Points m1points) {
		assertTrue(g.hasVertex("a"));
		assertTrue(g.hasVertex("f"));
		assertFalse(g.hasVertex("g"));
		assertTrue(g2.hasVertex(0));
		assertTrue(g2.hasVertex(6));
		assertFalse(g2.hasVertex(7));
		m1points.add(m1weight);
	}

	public static void helperTestKeySet(AdjacencyMatrixGraph<String> g, AdjacencyMatrixGraph<Integer> g2, Points m1points) {
		assertEquals(getExampleVertexData(),g.keySet());
		assertEquals(getExample2VertexData(),g2.keySet());
		m1points.add(m1weight);
	}
	
	public static void helperTestRemoveEdge(AdjacencyMatrixGraph<String> g, Points m1points) {
		assertTrue(g.hasEdge("a", "b"));
		int initialNumEdges = g.numEdges();
		g.removeEdge("a","b");
		assertEquals(initialNumEdges-1, g.numEdges());
		assertFalse(g.hasEdge("a", "b"));
		assertTrue(g.hasEdge("f", "c"));
		assertTrue(g.removeEdge("f","c"));
		assertFalse(g.hasEdge("f", "c"));
		assertFalse(g.hasEdge("f", "c"));
		assertFalse(g.removeEdge("b","c"));
		assertFalse(g.hasEdge("f", "c"));
		assertEquals(initialNumEdges-2, g.numEdges());
		m1points.add(m1weight);
		try {
			g.removeEdge("a", "g"); // g not in graph
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(m1weight);
	}
	
	public static void helperTestSuccessorSet(AdjacencyMatrixGraph<String> g, Points m1points) {
		Set<String> bSucc = new HashSet<String>();
		bSucc.add("d");
		assertEquals(bSucc,g.successorSet("b"));
		
		Set<String> gSucc = new HashSet<String>();
		assertEquals(gSucc,g.successorSet("e"));
		
		Set<String> dSucc = new HashSet<String>();
		dSucc.add("c");
		dSucc.add("e");
		dSucc.add("f");
		assertEquals(dSucc,g.successorSet("d"));
		try {
			g.successorSet("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(1.0*m1weight);
	}
	
	public static void helperTestPredecessorSet(AdjacencyMatrixGraph<String> g, Points m1points) {
		Set<String> bSucc = new HashSet<String>();
		bSucc.add("a");
		assertEquals(bSucc,g.predecessorSet("b"));
		
		Set<String> gSucc = new HashSet<String>();
		assertEquals(gSucc,g.predecessorSet("a"));
		
		Set<String> dSucc = new HashSet<String>();
		dSucc.add("a");
		dSucc.add("d");
		dSucc.add("f");
		assertEquals(dSucc,g.predecessorSet("c"));
		try {
			g.predecessorSet("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(0.5*m1weight);
	}
	
	public static void helperTestSuccessorIterator(AdjacencyMatrixGraph<String> g, Points m1points) {
		Iterator<String> it = g.successorIterator("b");
		assertTrue(it.hasNext());
		assertEquals("d",it.next());
		assertFalse(it.hasNext());
		m1points.add(0.5*m1weight);
		
		it = g.successorIterator("d");
		Set<String> returned = new HashSet<String>();
		Set<String> expected = new HashSet<String>();
		expected.add("c");
		expected.add("e");
		expected.add("f");
		while (it.hasNext()) {
			returned.add(it.next());
		}
		assertEquals(expected, returned);
		try {
			g.successorIterator("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(0.5*m1weight);
	}
	
	public static void helperTestPredecessorIterator(AdjacencyMatrixGraph<String> g, Points m1points) {
		Iterator<String> it = g.predecessorIterator("b");
		assertTrue(it.hasNext());
		assertEquals("a",it.next());
		assertFalse(it.hasNext());
		m1points.add(0.5*m1weight);
		
		it = g.predecessorIterator("c");
		Set<String> returned = new HashSet<String>();
		Set<String> expected = new HashSet<String>();
		expected.add("a");
		expected.add("d");
		expected.add("f");
		while (it.hasNext()) {
			returned.add(it.next());
		}
		assertEquals(expected, returned);
		try {
			g.predecessorIterator("z");
			fail("Did not throw NoSuchElementException");
		} catch (Exception e) {
			if (!(e instanceof NoSuchElementException)) {
				fail("Did not throw NoSuchElementException");
			}
		}
		m1points.add(0.5*m1weight);
	}
	
	public static long helperTestRelativeSpeedforAddRemoveEdge(AdjacencyMatrixGraph<Integer> g, int numVertices) {
		// Add edges everywhere
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				assertFalse(g.hasEdge(i, j));
				g.addEdge(i, j);
				assertTrue(g.hasEdge(i, j));
			}
		}
		long startTime = System.currentTimeMillis();
		// Remove edges everywhere
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				assertTrue(g.hasEdge(i, j));
				g.removeEdge(i, j);
				assertFalse(g.hasEdge(i, j));
			}
		}
		long endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}
	
	@Test
	public void testRelativeSpeedforRemoveEdge() {
		assertTrue(speedTestsOn);
		int numVertices = 1000;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i);
		}
		AdjacencyMatrixGraph<Integer> gAM = new AdjacencyMatrixGraph<Integer>(keySet);
		long timeAM = helperTestRelativeSpeedforAddRemoveEdge(gAM,numVertices);
		m1points += 3*m1weight;
	}
	
	public static long helperTestRelativeSpeedforOutDegree(AdjacencyMatrixGraph<Integer> g, int numVertices) {
		long startTime = System.currentTimeMillis();
		// Start with the empty graph
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				if (j == i + 1) {
					g.addEdge(i, j); // add one edge for each vertex, at a specific point,
									 // to get a bit of variety in the out-degrees
				}
				// main test: out-degree calculation
				assertEquals(((j >= i + 1) ? 1 : 0), g.outDegree(i)); 
			}
		}
		long endTime = System.currentTimeMillis();
		return (endTime - startTime);
	}

	@Test
	public void testRelativeSpeedforOutDegree() {
		assertTrue(speedTestsOn);
		int numVertices = 800;
		Set<Integer> keySet = new HashSet<Integer>();
		for (int i = 0; i < numVertices; i++) {
			keySet.add(i);
		}
		AdjacencyMatrixGraph<Integer> gAM = new AdjacencyMatrixGraph<Integer>(keySet);
		helperTestRelativeSpeedforOutDegree(gAM,numVertices);
		m1points += 3*m1weight;
	}
	
	@Test
	public void testEdgeSet() {
		AdjacencyMatrixGraph.Edge<Integer> e = new AdjacencyMatrixGraph.Edge<>(1, 2);
		AdjacencyMatrixGraph.Edge<Integer> e2 = new AdjacencyMatrixGraph.Edge<>(1, 2);
		Set<AdjacencyMatrixGraph.Edge<Integer>> edges = new HashSet<>();
		edges.add(e);
		edges.add(e2);
		assertEquals(1, edges.size());
		
		// Test forcing
		Set<String> keySet = new HashSet<>();
		keySet.add("a");
		keySet.add("b");
		keySet.add("c");
		keySet.add("d");
		keySet.add("e");
		AdjacencyMatrixGraph<String> m = new AdjacencyMatrixGraph<>(keySet);
		
		// a
		m.addUniEdge("a", "c");
		m.addUniEdge("a", "d");
		m.addUniEdge("a", "b");
		m.addUniEdge("a", "e");
		
		// c
		m.addUniEdge("c", "d");
		m.addUniEdge("c", "b");
		
		// d
		m.addUniEdge("d", "b");
		
		// b
		m.addUniEdge("b", "e");
		
		AdjacencyMatrixGraph.EdgeSet<String> s = m.forces(new AdjacencyMatrixGraph.Edge<>("a", "c"));
		assertEquals(1, s.edges.size());
		for(AdjacencyMatrixGraph.Edge<String> es: s.edges) {
			assertEquals("ae", es.toString());
		}
		
		AdjacencyMatrixGraph.EdgeSet<String> ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("a", "b"));
		System.out.println(ics + " " + ics.flipped());
		ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("c", "d"));
		System.out.println(ics);
		ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("a", "c"));
		System.out.println(ics + " " + ics.flipped());
		ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("b", "c"));
		System.out.println(ics);
		ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("c", "b"));
		System.out.println(ics);
		ics = m.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("b", "a"));
		System.out.println(ics);
		
		// Now try deducing a transitive orientation for 'm'
		ics = m.transitiveOrientation();
		System.out.println("Transitive Orientation: " + ics);
		System.out.println("Max Cliques: " + m.maxCliques());
		// Graph 2
		Set<String> keySet2 = new HashSet<>();
		keySet2.add("a");
		keySet2.add("b");
		keySet2.add("c");
		keySet2.add("d");
		keySet2.add("e");
		keySet2.add("f");
		
		AdjacencyMatrixGraph<String> m2 = new AdjacencyMatrixGraph<>(keySet2);
		
		// a
		m2.addUniEdge("a", "b");
		
		// b
		m2.addUniEdge("b", "c");
		m2.addUniEdge("b", "f");
		
		// c
		m2.addUniEdge("c", "d");
		m2.addUniEdge("c", "f");
		
		// f
		m2.addUniEdge("f", "e");
		
		System.out.println("Graph 2");
		ics = m2.deduceImplicationClass(new AdjacencyMatrixGraph.Edge<>("a", "b"));
		System.out.println(ics);

		System.out.println("Max Cliques: " + m2.maxCliques());
		
		Set<String> keySet3 = new HashSet<>();
		keySet3.add("a");
		keySet3.add("c");
		keySet3.add("e");
		keySet3.add("g");
		
		AdjacencyMatrixGraph<String> m3 = new AdjacencyMatrixGraph<>(keySet3);
		m3.addEdge("a", "g");
		m3.addEdge("a", "c");
		m3.addEdge("c", "g");
		m3.addEdge("e", "c");
		m3.addEdge("e", "g");
		m3.addEdge("e", "a");
		
		// SMTH like EACG
		System.out.println("Hamiltonian path: " + m3.hamiltonianPathAcyclic());

		Set<String> keySet4 = new HashSet<>();
		keySet4.add("1");
		keySet4.add("2");
		keySet4.add("3");
		
		AdjacencyMatrixGraph<String> m4 = new AdjacencyMatrixGraph<>(keySet4);
		m4.addEdge("1", "3");
		m4.addEdge("1", "2");
		m4.addEdge("3", "2");
		
		System.out.println("Hamiltonian path: " + m4.hamiltonianPathAcyclic());
	}
	

	@AfterAll
	public static void printSummary() {
		System.out.print("\n ===============     ");
		System.out.print("Total M1 Core points: ");
		System.out.print(m1points + "/" + MAX_POINTS);
		System.out.println("     ===============");
	}


	public static class Points {
	
		private double val;
		
		public Points(double val) {
			this.val = val;
		}

		public void set(double val) {
			this.val = val;
		}

		public double get() {
			return val;
		}

		public void add(double n) {
			val += n;
		}

		@Override
		public String toString() {
			return String.valueOf((int)val);
		}
	}
}