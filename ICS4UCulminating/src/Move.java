import java.io.*;
import java.util.*;

// Class Move 
// All Pokemon Moves will inherit this class
public class Move {
	public static ArrayList<Move> allMoves = new ArrayList<>();
	public static ArrayList<Move>[] moveSets = new ArrayList[59];
	private String name;
	private PokeType type;
	private String category; // physical, status, or special 
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
	public String getName() {
		return name;
	}
	
	public int getPP() {
		return pp;
	}
	
	public PokeType getType() {
		return type;
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
