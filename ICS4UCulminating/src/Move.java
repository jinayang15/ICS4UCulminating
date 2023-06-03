import java.io.*;
import java.util.*;

// Class Move 
// All Pokemon Moves will inherit this class
public class Move {
	private static ArrayList<Move> allMoves;
	private String name;
	private PokeType type;
	private String category;
	private int pp;
	private int atkPower;
	private double accuracy;
	private String effect;

	public Move(String name, PokeType type, String category, int pp, int atkPower, double accuracy, String effect) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.pp = pp;
		this.atkPower = atkPower;
		this.accuracy = accuracy;
		this.effect = effect;
	}

	public int getPP() {
		return pp;
	}

	public void setPP(int pp) {
		this.pp = pp;
	}

	public int getAtkPower() {
		return atkPower;
	}

	public void setAtkPower(int atkPower) {
		this.atkPower = atkPower;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	public static void importMoves() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("allMoves.txt"));
		allMoves.add(null);
		String line = "";
		while ((line = in.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "^");
			st.nextToken();
			String name = st.nextToken();
			PokeType type = new PokeType(st.nextToken());
			String category = st.nextToken();
			int pp = Integer.parseInt(st.nextToken());
			int atkPower = Integer.parseInt(st.nextToken());
			double accuracy = Double.parseDouble(st.nextToken());
			String effect = st.nextToken();
			Move m = new Move(name, type, category, pp, atkPower, accuracy, effect);
			allMoves.add(m);
		}
	}
}
