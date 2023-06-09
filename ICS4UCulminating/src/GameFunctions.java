// The GameFunctions class is used to perform all the game functions 
// These game functions include text file reading, etc. 

import java.awt.Rectangle;
import java.io.*;
import java.util.*;

public class GameFunctions {

	// The importPokemon method is used to import the Pokemon from the textfile
	public static void importPokemon() {
		// Order of text file:
		// name, type1, type2 (if there), hp, attack, def, spAtk, spDef, speed
		try {
			BufferedReader br = new BufferedReader(new FileReader("allPokemon.txt"));
			StringTokenizer st;
			String line = "";
			// Temporary Pokemon in index 0
			ArrayList<Move> tempMoveList = new ArrayList<>();
			tempMoveList.add(Move.allMoves.get(0));
			Pokemon.pokeList.add(new Pokemon("peepeepoopoo", new PokeType("Water"), 1, 1, 1, 1, 1, 1, tempMoveList));
			// ----------------------------
			int i = 0;
			while ((line = br.readLine()) != null) {
				st = new StringTokenizer(line, " ");
				Pokemon p = null;
				// If there are 8 elements, it means the Pokemon has 1 type.
				if (st.countTokens() == 8) {
					String name = st.nextToken();
					PokeType type1 = new PokeType(st.nextToken());
					int hp = Integer.parseInt(st.nextToken());
					int attack = Integer.parseInt(st.nextToken());
					int def = Integer.parseInt(st.nextToken());
					int spAtk = Integer.parseInt(st.nextToken());
					int spDef = Integer.parseInt(st.nextToken());
					int speed = Integer.parseInt(st.nextToken());
					p = new Pokemon(name, type1, hp, attack, def, spAtk, spDef, speed, Move.moveSets[i]);
				}
				// If there are 9 elements, the Pokemon has 2 types
				else if (st.countTokens() == 9) {
					String name = st.nextToken();
					PokeType type1 = new PokeType(st.nextToken());
					PokeType type2 = new PokeType(st.nextToken());
					int hp = Integer.parseInt(st.nextToken());
					int attack = Integer.parseInt(st.nextToken());
					int def = Integer.parseInt(st.nextToken());
					int spAtk = Integer.parseInt(st.nextToken());
					int spDef = Integer.parseInt(st.nextToken());
					int speed = Integer.parseInt(st.nextToken());
					p = new Pokemon(name, type1, type2, hp, attack, def, spAtk, spDef, speed, Move.moveSets[i]);
				}
				Pokemon.pokeList.add(p);

//				for (int k = 0; k < p.getMoves().length; k++) {
//					if (p.getMoves()[k] != null) {
//					System.out.print(p.getMoves()[k].getName() + " ");
//					}else {
//						System.out.print(null + " ");
//					}
//				}
//				System.out.println();
				i++;
			}
		} catch (IOException e) {
			System.out.println("BAD");
		}
	}

	// The importMoves method is used to import all the moves from the allMoves text
	// file
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

	// imports all move sets of each pokemon
	public static void importMoveSets() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("moveSet.txt"));
		for (int i = 0; i < 59; i++) {
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			ArrayList<Move> m = new ArrayList<>();
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				if (s.equals("#"))
					break;
				else {
					m.add(Move.allMoves.get(Integer.parseInt(s)));
				}
			}
			Move.moveSets[i] = m;
		}
	}

	// walls in Pewter City
	public static void importWalls() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("PewterCity.txt"));
		for (int i = 0; i < 40; i++) {
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			for (int j = 0; j < 48; j++) {
				if (Integer.parseInt(st.nextToken()) == 1) {
					Main.allWalls[i][j] = new Wall(i * Main.tileSize, j * Main.tileSize, Main.tileSize, Main.tileSize);
				}
			}
		}
		for (int i = 0; i < Main.tileMapHeight; i++) {
			for (int j = 0; j < Main.tileMapWidth; j++) {
				Wall w = Main.allWalls[i][j];
				if (w != null) {
					if (i - 1 >= 0) {
						if (Main.allWalls[i - 1][j] != null) {
							w.setBlockedUp(true);
						}
					}
					if (i + 1 < Main.tileMapHeight) {
						if (Main.allWalls[i + 1][j] != null) {
							w.setBlockedDown(true);
						}
					}
					if (j - 1 >= 0) {
						if (Main.allWalls[i][j - 1] != null) {
							w.setBlockedLeft(true);
						}
					}
					if (j + 1 < Main.tileMapHeight) {
						if (Main.allWalls[i][j + 1] != null) {
							w.setBlockedRight(true);
						}
					}
				}
			}
		}
	}

	public static void importEverything() throws IOException {
		importMoves();
		importMoveSets();
		importWalls();
		importPokemon();
	}

}
