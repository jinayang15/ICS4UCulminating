import java.io.*;
import java.util.*;

// The Type class
// This class is used to import the type effectiveness chart, which determines how much damage each 
// attack will due to a Pokemon based on the type. This chart can be found in the typeChart.jpg file. 
// For example, water against fire will do 2.0 times the damage, while fire against water will do  
// For Pokemon with dual types, the effectiveness number will be multiplied together. For example, 
// if a Pokemon with a Fire-Electric type (hypothetical) gets hit with a Ground move, which is super effective (2.0x)
// on BOTH types, the move will do 2x2 or 4 times the damage. Similarily, if a Pokemon with a Flying-Poison type
// gets hit with a Grass attack, which both Flying and Poison resists (0.5), the damage done will be 0.5x0.5 or 0.25.  

// The index of each type (same for attacker and defender) 
// Fire = 0
// Water = 1
// Grass = 2
// Electric = 3
// Ground = 4
// Normal = 5
// Flying = 6 
// Poison = 7

public class PokeType {
	private String type;
	private int typeNum;
	private static double[][] typeChart = new double[8][8];

	public PokeType(String type) {
		this.type = type;
		if (type.toLowerCase().equals("fire"))
			typeNum = 0;
		else if (type.toLowerCase().equals("water"))
			typeNum = 1;
		else if (type.toLowerCase().equals("grass"))
			typeNum = 2;
		else if (type.toLowerCase().equals("electric"))
			typeNum = 3;
		else if (type.toLowerCase().equals("ground"))
			typeNum = 4;
		else if (type.toLowerCase().equals("normal"))
			typeNum = 5;
		else if (type.toLowerCase().equals("flying"))
			typeNum = 6;
		else if (type.toLowerCase().equals("poison"))
			typeNum = 7;
	}

	// Importing to the 2D array from the typeChartFile text file
	public static void addToChart() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("typeChartFile.txt"));
		StringTokenizer st;
		for (int i = 0; i < 8; i++) {
			String line = br.readLine();
			st = new StringTokenizer(line, " ");
			for (int j = 0; j < 8; j++) {
				typeChart[i][j] = Double.parseDouble(st.nextToken());
			}
		}
	}

	// Getter that will return the value of the effectiveness (will be multiplied to
	// the attack damage)
	public static double getTypeEffectiveness(int attackType, int defendType) throws IOException {
		return typeChart[attackType][defendType];
	}

	public static double getTypeEffectiveness(int attackType, int defendType1, int defendType2) throws IOException {
		return typeChart[attackType][defendType1] * typeChart[attackType][defendType2];
	}

	public String toString() {
		return type;
	}

	public boolean equals(Object o) {
		PokeType t = (PokeType) o;
		return t.type.equals(this.type);
	}
	
	public int getTypeNum() {
		return typeNum;
	}
}
