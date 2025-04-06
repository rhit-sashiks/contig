import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

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
		
		try {
			// Thanks to https://www.baeldung.com/java-current-directory
			String userDir = new File("").getAbsolutePath();
			System.out.println("In user dir: " + userDir);
			
			f = new FileReader(filename);
			bufr = new BufferedReader(f);
			
			// Look at first line
			String firstLine = bufr.readLine();
			
			Set<String> keys = new HashSet<>();
			
			int matrixSize = 0;
			for(int i = 0; i < firstLine.length(); i++) {
				char ch = firstLine.charAt(i);
				if (ch == ',' || ch == ' ') {
					continue;
				}
				
				matrixSize++;
			}
			
			int[][] matrix = new int[matrixSize][matrixSize];
			
			char startCh = 'A';
			for(int i = 0; i < firstLine.length(); i++) {
				char ch = firstLine.charAt(i);
				if (ch == ',' || ch == ' ') {
					continue;
				}
				
				// Add to keyset
				System.out.println(keys);
				keys.add(startCh + "");
				startCh += 1;
				
				if(ch == '1') {
					matrix[0][i] = 1;
				}
			}
			
			// Now go to the rest of the lines
			int i = 1;
			while(true) {
				String s = bufr.readLine();
				if(s == null) {
					break;
				}
				
				for(int j = 0; j < keys.size(); j++) {
					char ch = s.charAt(j);
					if (ch == ',' || ch == ' ') {
						continue;
					}

					if(ch == '1') {
						matrix[i][j] = 1;
					}
				}
				
				i++;
			}
			
			G = new AdjacencyMatrixGraph<>(keys, matrix);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} 

		System.out.println("Max Cliques: " + G.maxCliques());
		G.setIsComplement(true);
		System.out.println("Transitive Orientation: " + G.transitiveOrientation());
		
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
	}
}
