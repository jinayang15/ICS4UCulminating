import java.io.*;
import java.util.*;

// Class Move 
// All Pokemon Moves will inherit this class
// It is used to get the name, accuracy, attack power, etc. 
public class Move {
	public static ArrayList<Move> allMoves = new ArrayList<>();
	public static ArrayList<Move>[] moveSets = new ArrayList[59];
	private String name;
	private PokeType type;
	private String category; // physical, status, or special 
	private int pp;
	private int deltaPP = 0;
	private int atkPower;
	private double accuracy;
	private String effect;

	// Constructor 
	public Move(String name, PokeType type, String category, int pp, int atkPower, double accuracy, String effect) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.pp = pp;
		this.atkPower = atkPower;
		this.accuracy = accuracy;
		this.effect = effect;
	}
	public String getName() {
		return name;
	}
	
	public int getMaxPP() {
		return pp;
	}
	
	public int getCurrentPP() {
		return pp + deltaPP;
	}
	
	public void setPP(int x) {
		pp = x;
	}
	public PokeType getType() {
		return type;
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
	
	public String getCategory() {
		return category;
	}
	
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	
	public String toString() {
		return name + "-" + type + "-" + category +  "-" + pp + "-" + atkPower + "-" + accuracy + "-" + effect;
	}

}
