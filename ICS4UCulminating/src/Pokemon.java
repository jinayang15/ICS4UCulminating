import java.awt.image.BufferedImage;
import java.util.*;

// Class Pokemon
// All Pokemon will inherit this class
// Contains their stats and their moves. 

public class Pokemon {
	// ArrayList of Pokemon (could be temporary)
	public static ArrayList<Pokemon> pokeList = new ArrayList<>();
	private String name;
	private BufferedImage[] sprites;

	// The BASE stats
	private int level = 1;
	private int baseHp;
	private int baseAttack;
	private int baseDef;
	private int baseSpAtk;
	private int baseSpDef;
	private int baseSpeed;

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
	private ArrayList<PokeType> typeList = new ArrayList<>();
	private int status;
	private boolean faint = false;
	// Poison - 1
	// Paralyzed - 2
	// Sleep - 3
	// Burn - 4

	// Constructor
	public Pokemon(String name, PokeType type1, int hp, int attack, int def, int spAtk, int spDef, int speed,
			ArrayList<Move> moveSet) {
		if (!name.equals("peepeepoopoo")) {
			this.name = name;
			this.type1 = type1;
			typeList.add(type1);
			this.type2 = null;
			this.baseHp = hp;
			this.baseAttack = attack;
			this.baseDef = def;
			this.baseSpAtk = spAtk;
			this.baseSpDef = spDef;
			this.baseSpeed = speed;
			this.moveSet = moveSet;
			for (int i = 0; i < Math.min(moveSet.size(), 4); i++) {
				moveList[i] = moveSet.get(i);
			}
			this.sprites = Images.battleSprites[Images.battleSpritesIdx.get(name.toLowerCase())];
		}
	}
	// Overloaded constructors, which initializes the base stats and adds the moves
	// to an array of moves.
	public Pokemon(String name, PokeType type1, PokeType type2, int hp, int attack, int def, int spAtk, int spDef,
			int speed, ArrayList<Move> moveSet) {
		this.name = name;
		this.type1 = type1;
		this.type2 = type2;
		typeList.add(type1);
		typeList.add(type2);
		this.baseHp = hp;
		this.baseAttack = attack;
		this.baseDef = def;
		this.baseSpAtk = spAtk;
		this.baseSpDef = spDef;
		this.baseSpeed = speed;
		this.moveSet = moveSet;
		for (int i = 0; i < Math.min(moveSet.size(), 4); i++) {
			moveList[i] = moveSet.get(i);
		}
		this.sprites = Images.battleSprites[Images.battleSpritesIdx.get(name.toLowerCase())];

	}

	public Pokemon(Pokemon mon) {
		this.level = mon.level; // Placeholder
		this.name = mon.name;
		this.baseHp = mon.baseHp;
		this.baseAttack = mon.baseAttack;
		this.baseDef = mon.baseDef;
		this.baseSpAtk = mon.baseSpAtk;
		this.baseSpDef = mon.baseSpDef;
		this.baseSpeed = mon.baseSpeed;
		this.type1 = mon.type1;
		if (typeList.size() == 2) {
			this.type2 = type2;
		}
		for (int i = 0; i < mon.moveList.length; i++) {
			this.moveList[i] = mon.moveList[i];
		}
		this.sprites = Images.battleSprites[Images.battleSpritesIdx.get(name.toLowerCase())];
	}

	public String toString() {
		return this.name;
	}

	// Getters and Setters

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public int getCurrentHp() {
		return baseHp - deltaHp;
	}

	public int getCurrentAttack() {
		return baseAttack - deltaAttack;
	}

	public int getCurrentDef() {
		return baseDef - deltaDef;
	}

	public int getCurrentSpAtk() {
		return baseSpAtk - deltaSpAtk;
	}

	public int getCurrentSpDef() {
		return baseSpDef - deltaSpDef;
	}

	public int getCurrentSpeed() {
		return baseSpeed - deltaSpeed;
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
		this.status = status;
	}

	public Move[] getMoves() {
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

	public int getBaseHp() {
		return baseHp;
	}

	public void setBaseHp(int baseHp) {
		this.baseHp = baseHp;
	}

	public int getBaseAttack() {
		return baseAttack;
	}

	public void setBaseAttack(int baseAttack) {
		this.baseAttack = baseAttack;
	}

	public int getBaseDef() {
		return baseDef;
	}

	public void setBaseDef(int baseDef) {
		this.baseDef = baseDef;
	}

	public int getBaseSpAtk() {
		return baseSpAtk;
	}

	public void setBaseSpAtk(int baseSpAtk) {
		this.baseSpAtk = baseSpAtk;
	}

	public int getBaseSpDef() {
		return baseSpDef;
	}

	public void setBaseSpDef(int baseSpDef) {
		this.baseSpDef = baseSpDef;
	}

	public int getBaseSpeed() {
		return baseSpeed;
	}

	public void setBaseSpeed(int baseSpeed) {
		this.baseSpeed = baseSpeed;
	}

	public void setLevel(int level) {
		this.level = level;
		baseHp += Math.round((level / 50.0) * (baseHp));
		baseAttack += Math.round((level / 50.0) * (baseAttack));
		baseDef += Math.round((level / 50.0) * (baseDef));
		baseSpAtk += Math.round((level / 50.0) * (baseSpAtk));
		baseSpDef += Math.round((level / 50.0) * (baseSpDef));
		baseSpeed += Math.round((level / 50.0) * (baseSpeed));

	}

	public BufferedImage getPlayerSprite() {
		return sprites[1];
	}

	public BufferedImage getOtherSprite() {
		return sprites[0];
	}

	public void levelUp() {

	}

}
