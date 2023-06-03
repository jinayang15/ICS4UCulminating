// Class Move 
// All Pokemon Moves will inherit this class
public class Move {
	private String name;
	private PokeType type;
	private String category;
	private int pp;
	private int atkPower;
	private int accuracy;

	public Move(String name, PokeType type, String category, int pp, int atkPower, int accuracy) {
		this.name = name;
		this.type = type;
		this.category = category;
		this.pp = pp;
		this.atkPower = atkPower;
		this.accuracy = accuracy;
	}

	public int getPP() {
		return pp;
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

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	
}
