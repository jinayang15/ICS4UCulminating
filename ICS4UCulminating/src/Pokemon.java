import java.util.*;

// Class Pokemon
// All Pokemon will inherit this class
public class Pokemon {
	// ArrayList of Pokemon (could be temporary) 
	public static ArrayList<Pokemon> pokeList = new ArrayList<>();
	private String name; 
	
	// The BASE stats 
	private int level = 1;
	private int hp;
	private int attack;
	private int def;
	private int spAtk;
	private int spDef;
	private int speed;

	// Changes applied to the base stats for battle.
	private int deltaHp = 0;
	private int deltaAttack = 0;
	private int deltaDef = 0;
	private int deltaSpAtk = 0;
	private int deltaSpDef = 0;
	private int deltaSpeed = 0; 
	
	private Move[] moveList = new Move[4];
	// moveSet is all moves that may be learned 
	private ArrayList<Move> moveSet;
	private PokeType type1;
	private PokeType type2;
	private ArrayList <PokeType> typeList = new ArrayList<>();
	private int status;
	private boolean faint = false;
	// Poison - 1
	// Burn - 2
	// Paralyzed - 3
	// Sleep - 4
	
	// Overloaded constructors, which initializes the base stats and adds the moves to an array of moves. 
	public Pokemon (String name, PokeType type1, int hp, int attack, int def, int spAtk, int spDef, int speed, ArrayList<Move> moveSet) {
		this.name = name; 
		this.type1 = type1;
		typeList.add(type1);
		this.type2 = null;
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
		typeList.add(type1);
		typeList.add(type2);
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
	
	public Pokemon (Pokemon mon) {
		this.level = mon.level; // Placeholder 
		this.name = mon.name;
		this.hp = mon.hp;
		this.attack = mon.attack;
		this.def = mon.def;
		this.spAtk = mon.spAtk;
		this.spDef = mon.spDef;
		this.speed = mon.speed;
		this.type1 = mon.type1;
		if (typeList.size()==2) {
			this.type2 = type2;
		}
		for (int i = 0; i<mon.moveList.length; i++) {
			this.moveList[i] = mon.moveList[i];
		}
	}
	
	public String toString() {
		return this.name;
	}
	
	// Getters
	
	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return name;
	}


	public int getHp() {
		return hp;
	}


	public int getAttack() {
		return attack;
	}


	public int getDef() {
		return def;
	}


	public int getSpAtk() {
		return spAtk;
	}


	public int getSpDef() {
		return spDef;
	}


	public int getSpeed() {
		return speed;
	}


	public int getDeltaHp() {
		return deltaHp;
	}


	public int getDeltaAttack() {
		return deltaAttack;
	}


	public int getDeltaDef() {
		return deltaDef;
	}


	public int getDeltaSpAtk() {
		return deltaSpAtk;
	}


	public int getDeltaSpDef() {
		return deltaSpDef;
	}


	public int getDeltaSpeed() {
		return deltaSpeed;
	}

	public PokeType getType1() {
		return type1;
	}


	public PokeType getType2() {
		return type2;
	}


	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status=status;
	}
	
	public Move[] getMoves(){
		return moveList;
	}
	
	public ArrayList<PokeType> getTypeList() {
		return typeList;
	}
	
	public boolean getFaint() {
		return faint; 
	}
	
	public void setFaint(boolean faint) {
		this.faint = faint;
	}
	
	public void setDeltaHp(int deltaHp) {
		this.deltaHp = deltaHp;
	}


	public void setDeltaAttack(int deltaAttack) {
		this.deltaAttack = deltaAttack;
	}


	public void setDeltaDef(int deltaDef) {
		this.deltaDef = deltaDef;
	}


	public void setDeltaSpAtk(int deltaSpAtk) {
		this.deltaSpAtk = deltaSpAtk;
	}


	public void setDeltaSpDef(int deltaSpDef) {
		this.deltaSpDef = deltaSpDef;
	}


	public void setDeltaSpeed(int deltaSpeed) {
		this.deltaSpeed = deltaSpeed;
	}
	
	public void setLevel (int level) {
		this.level = level;
		hp += Math.round((level/50.0)*(this.hp));
		attack += Math.round((level/50.0)*(this.attack));
		def += Math.round((level/50.0)*(def));
		spAtk += Math.round((level/50.0)*(spAtk));
		spDef += Math.round((level/50.0)*(spDef));
		speed += Math.round((level/50.0)*(speed));
		
	}
	
	public void levelUp() {
		
	}


	
}
