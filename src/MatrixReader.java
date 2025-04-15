import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MatrixReader {
	public static void main(String[] args) {
		String filename = null;
		try {
			System.out.print("Enter file to read: ");
			Scanner stdinReaderBuf = new Scanner(System.in);
			filename = stdinReaderBuf.nextLine();
			
			stdinReaderBuf.close();
		} catch (Exception err) {
			err.printStackTrace();
			return;
		}
		
		FileReader f;
		BufferedReader bufr;
	
		AdjacencyMatrixGraph<String> G = null;
		
		boolean initialComplement = false;
		try {
			// Thanks to https://www.baeldung.com/java-current-directory
			String userDir = new File("").getAbsolutePath();
			System.out.println("In user dir: " + userDir);
			
			f = new FileReader(filename);
			bufr = new BufferedReader(f);
			
			// Look at first line
			String firstLine = null;
			while(true) {
				firstLine = bufr.readLine();
				System.out.println("FirstLine " + firstLine);
				
				if(firstLine.startsWith("#")) {
					continue;
				}
				
				if(firstLine.startsWith("@asComplement")) {
					initialComplement = true;
					continue;
				}
				
				break;
			}
			
			Set<String> keys = new TreeSet<>();
			
			int matrixSize = 0;
			for(int i = 0; i < firstLine.length(); i++) {
				char ch = firstLine.charAt(i);
				if (ch == ',' || ch == ' ') {
					continue;
				}
				
				matrixSize++;
			}
			
			System.out.println("Matrix Size: " + matrixSize + "\nFirst Line: " + firstLine);
			
			int[][] matrix = new int[matrixSize][matrixSize];
			
			char startCh = 'A';
			int atIdx = 0;
			for(int i = 0; i < firstLine.length(); i++) {
				char ch = firstLine.charAt(i);
				if (ch == ',' || ch == ' ') {
					continue;
				}
				
				// Add to keyset
				keys.add(startCh + "");
				System.out.println("Keyset: " + keys + "\ni: " + i);
				startCh += 1;
				
				if(ch == '1') {
					matrix[0][atIdx] = 1;
				}
				atIdx++;
			}
			
			// Now go to the rest of the lines
			int i = 1;
			while(true) {
				String s = bufr.readLine();
				if(s == null) {
					break;
				}
				
				atIdx = 0;
				for(int j = 0; j < s.length(); j++) {
					char ch = s.charAt(j);
					if (ch == ',' || ch == ' ') {
						continue;
					}

					if(ch == '1') {
						matrix[i][atIdx] = 1;
					}
					
					atIdx++;
				}
				
				i++;
			}
			
			G = new AdjacencyMatrixGraph<>(keys, matrix);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} 
		
		// Close file
		if(f != null) {
			try {
				f.close();
				if(bufr != null) {
					bufr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		if(initialComplement) {
			G.setIsComplement(true);
		}
		
		System.out.println("Adjacency Matrix Of G:\n" + G.adjacencyMatrix());

		// Actual contig assembly here
		System.out.println("Edges: " + G.edgeSet());
		ArrayList<Set<String>> maxCliques = G.maxCliques();
		System.out.println("Max Cliques: " + maxCliques);
		
		// Extract out unique vertices across the cliques
		HashMap<String, Set<String>> uniqueVerticesOfClique = new HashMap<>(maxCliques.size());
		HashSet<String> added = new HashSet<String>();
		for(Set<String> clique: maxCliques) {
			for(String vertex: clique) {
				if(!added.contains(vertex)) {
					uniqueVerticesOfClique.put(vertex, clique);
					added.add(vertex);
				} else {
					uniqueVerticesOfClique.remove(vertex);
				}
			}
		}
				
		System.out.println("Unique Vertices Of Max Cliques: " + uniqueVerticesOfClique);
		
		G.setIsComplement(!G.isComplement());
		
		System.out.println("Adjacency Matrix Of G-complement:\n" + G.adjacencyMatrix());
		
		AdjacencyMatrixGraph.EdgeSet<String> transitiveOrientation = G.transitiveOrientation();
		System.out.println("Transitive Orientation of complement: " + transitiveOrientation);
		System.out.println("TO Graph Adjacency Matrix:\n" + new AdjacencyMatrixGraph<String>(transitiveOrientation).adjacencyMatrix());
		// Extract out the subgraph containing the max cliques from the EdgeSet<T>
		AdjacencyMatrixGraph<String> cliqueSubgraph = transitiveOrientation.extractSubgraph(uniqueVerticesOfClique.keySet());
		System.out.println("Clique Subgraph: " + cliqueSubgraph);
		List<String> hamiltonianPath = cliqueSubgraph.hamiltonianPathAcyclic();
		System.out.println("Hamiltonian Path of clique subgraph: " + hamiltonianPath);
		
		// Final interval graph construction
		HashMap<String, ArrayList<Integer>> intervalGraph = new HashMap<String, ArrayList<Integer>>();
		int i = 0;
		for(String directKey: hamiltonianPath) {
			Set<String> elements = uniqueVerticesOfClique.get(directKey);
			System.out.println("HamPathKey=" + directKey + ", elements="+elements);
			for(String element: elements) {
				ArrayList<Integer> interval = intervalGraph.get(element);
				if(interval == null) {
					interval = new ArrayList<>();
					intervalGraph.put(element, interval);
				}
				
				if(!interval.contains(i)) {
					interval.add(i);
				}
				if(!interval.contains(i+1)) {
					interval.add(i+1);
				}
			}
			
			i++;
		}
		
		System.out.println("Interval Graph: " + intervalGraph);
		
		// Then convert the interval graph to Interval
		HashMap<String, ArrayList<IntervalPath.Interval>> intervalGraphPath = new HashMap<>(
				intervalGraph.size()
		);
		
		for(String element: intervalGraph.keySet()) {
			intervalGraphPath.put(element, IntervalPath.Interval.getInterval(intervalGraph.get(element)));
		}
		
		System.out.println("Interval Graph: " + intervalGraphPath);
		ArrayList<IntervalPath> completedPaths = new ArrayList<>();
		ArrayDeque<IntervalPath> paths = new ArrayDeque<>();
		paths.add(new IntervalPath());
		while(!paths.isEmpty()) {
			IntervalPath path = paths.pop();
			if(path.interval.size() == uniqueVerticesOfClique.size()) {
				completedPaths.add(path);
				continue;
			}
			
			for(String el: intervalGraphPath.keySet()) {
				for(IntervalPath.Interval iv: intervalGraphPath.get(el)) {
					IntervalPath extPath = path.add(el, iv);
					if(extPath != null) {
						paths.add(extPath);
					}
				}
			}
		}
		
		System.out.println("Paths: " + completedPaths);
	}
}
