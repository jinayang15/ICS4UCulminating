// Class Pokemon
// All Pokemon will inherit this class
public class Pokemon {
	private String name; 
	private int hp;
	private int attack;
	private int def;
	private int spAtk;
	private int spDef;
	private int speed;
	private Move[] moveList = new Move[4];
	private int special;
	private PokeType type1;
	private PokeType type2;
	private int status;
	// Poison - 1
	// Burn - 2
	// Paralyzed - 3
	// Sleep - 4
	
	// Overloaded constructosr, which initializes the base stats and adds the moves to an array of moves. 
	
	public Pokemon (String name, PokeType type1, int hp, int attack, int def, int spAtk, int spDef, int speed, Move m1, Move m2, Move m3, Move m4) {
		this.name = name; 
		this.type1 = type1;
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
		moveList[0] = m1; 
		moveList[1] = m2;
		moveList[2] = m3;
		moveList[3] = m4;  
	}
	
	
	public Pokemon (String name, PokeType type1, PokeType type2, int hp, int attack, int def, int spAtk, int spDef, int speed, Move m1, Move m2, Move m3, Move m4) {
		this.name = name; 
		this.type1 = type1;
		this.type2 = type2;
		this.hp = hp;
		this.attack = attack; 
		this.def = def; 
		this.spAtk = spAtk;
		this.spDef = spDef; 
		this.speed = speed; 
		moveList[0] = m1; 
		moveList[1] = m2;
		moveList[2] = m3;
		moveList[3] = m4;  
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
