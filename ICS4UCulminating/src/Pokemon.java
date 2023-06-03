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
	private Type type1;
	private Type type2;
	private Move[] moveList = new Move[4];
	
	// Constructor, which initializes the base stats and adds the moves to an array of moves. 
	public Pokemon (String name, int hp, int attack, int def, int spAtk, int spDef, int speed, Move m1, Move m2, Move m3, Move m4) {
		this.name = name; 
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
}
