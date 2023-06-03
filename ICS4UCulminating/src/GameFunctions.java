// The GameFunctions class is used to perform all the game functions 
// These game functions include text file reading, etc. 

import java.io.*; 
import java.util.*; 

public class GameFunctions {
	
	// ArrayList of Pokemon (could be temporary) 
	public static ArrayList<Pokemon> pokeList = new ArrayList<>();
	
	
	
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
			while ((line = br.readLine())!=null) {
				st = new StringTokenizer (line, " ");
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
					pokeList.add(new Pokemon (name, type1, hp, attack, def, spAtk, spDef, speed));
					System.out.println(pokeList.get(pokeList.size()-1));
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
					pokeList.add(new Pokemon (name, type1, type2, hp, attack, def, spAtk, spDef, speed));
					System.out.println(pokeList.get(pokeList.size()-1));
				}
			}
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
	
}
