// The GameFunctions class is used to perform all the game functions 
// These game functions include text file reading, etc. 

import java.io.*; 
import java.util.*; 

public class GameFunctions {
	
	
	
	public GameFunctions () {

	}
	
	// The importPokemon method is used to import the Pokemon from the textfile 
	public static void importPokemon() {
		// Order of text file:
		// name, type1, type2 (if there), hp, attack, def, spAtk, spDef, speed
		try {
			BufferedReader br = new BufferedReader (new FileReader ("allPokemon.txt"));
			StringTokenizer st;
			String line = "";
			int i = 0;
			while ((line = br.readLine())!=null) {
				st = new StringTokenizer (line, " ");
				Pokemon p = null;
				// If there are 8 elements, it means the Pokemon has 1 type.
				if (st.countTokens()==8) {
					String name = st.nextToken();
					PokeType type1 = new PokeType (st.nextToken());
					int hp = Integer.parseInt(st.nextToken());
					int attack = Integer.parseInt(st.nextToken());
					int def = Integer.parseInt(st.nextToken());
					int spAtk = Integer.parseInt(st.nextToken());
					int spDef = Integer.parseInt(st.nextToken());
					int speed = Integer.parseInt(st.nextToken());
					p = new Pokemon (name, type1, hp, attack, def, spAtk, spDef, speed, Move.moveSets[i]);
				}
				// If there are 9 elements, the Pokemon has 2 types
				else if (st.countTokens()==9) {
					String name = st.nextToken();
					PokeType type1 = new PokeType (st.nextToken());
					PokeType type2 = new PokeType (st.nextToken());
					int hp = Integer.parseInt(st.nextToken());
					int attack = Integer.parseInt(st.nextToken());
					int def = Integer.parseInt(st.nextToken());
					int spAtk = Integer.parseInt(st.nextToken());
					int spDef = Integer.parseInt(st.nextToken());
					int speed = Integer.parseInt(st.nextToken());
					p = new Pokemon (name, type1, type2, hp, attack, def, spAtk, spDef, speed, Move.moveSets[i]);
				}
				Pokemon.pokeList.add(p);
				try {
				System.out.println(p.getName() + " " + p.getMoves()[0].getName() + " " + p.getMoves()[1].getName() + " " + p.getMoves()[2].getName() + " " + p.getMoves()[3].getName());
				} catch (NullPointerException e) {}
				i++;
			}
			System.out.println(Pokemon.pokeList.size());
		}
		catch (IOException e) {
			System.out.println("BAD");
		}
	}
	
	// The importMoves method is used to import all the moves from the allMoves text file
	public static void importMoves() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("allMoves.txt"));
		Move.allMoves.add(null);
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
			Move.allMoves.add(m);
		}
	}
	
	public static void importMoveSets() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("moveSet.txt"));
		for (int i = 0; i < 59; i++) {
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			ArrayList<Move> m = new ArrayList<>();
			while(st.hasMoreTokens()) {
				String s = st.nextToken();
				if (s.equals("#")) break;
				else {
					m.add(Move.allMoves.get(Integer.parseInt(s)));
				}
			}
			Move.moveSets[i] = m;
		}
	}
	
}
