import java.util.*;

// Class Pokemon
// All Pokemon will inherit this class
public class Pokemon {
	// ArrayList of Pokemon (could be temporary) 
	public static ArrayList<Pokemon> pokeList = new ArrayList<>();
	private String name; 
	private int hp;
	private int attack;
	private int def;
	private int spAtk;
	private int spDef;
	private int speed;
	private Move[] moveList = new Move[4];
	// moveSet is all moves that may be learned 
	private ArrayList<Move> moveSet;
	private int special;
	private PokeType type1;
	private PokeType type2;
	private int status;
	// Poison - 1
	// Burn - 2
	// Paralyzed - 3
	// Sleep - 4
	
	// Overloaded constructors, which initializes the base stats and adds the moves to an array of moves. 
	
	public Pokemon (String name, PokeType type1, int hp, int attack, int def, int spAtk, int spDef, int speed, ArrayList<Move> moveSet) {
		this.name = name; 
		this.type1 = type1;
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
		this.moveSet = moveSet;
		for (int i = 0; i < Math.min(moveSet.size(), 4); i++) {
			moveList[i] = moveSet.get(i);
		}
	}
	
	
	public Pokemon (String name, PokeType type1, PokeType type2, int hp, int attack, int def, int spAtk, int spDef, int speed, ArrayList<Move> moveSet) {
		this.name = name; 
		this.type1 = type1;
		this.type2 = type2;
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
		this.moveSet = moveSet;
		for (int i = 0; i < Math.min(moveSet.size(), 4); i++) {
			moveList[i] = moveSet.get(i);
		}
	}
	
	// Temporary (?) ------------------
	public Pokemon (String name, PokeType type1, int hp, int attack, int def, int spAtk, int spDef, int speed) {
		this.name = name; 
		this.type1 = type1;
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
	}
	
	public Pokemon (String name, PokeType type1, PokeType type2, int hp, int attack, int def, int spAtk, int spDef, int speed) {
		this.name = name; 
		this.type1 = type1;
		this.type2 = type2; 
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
	}
	// ---------------------------------
	
	public String toString() {
		return this.name;
	}
}
