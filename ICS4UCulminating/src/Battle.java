// The battle class is used for BATTLES

public class Battle {

	Pokemon trainerMon;
	Pokemon otherMon;
	private boolean attack = false;
	private boolean switchPokemon = false;
	private boolean battleContinue = true;
	private int trainerMonHp;
	private int otherMonHp;
	
	// Constructor
	public Battle (Pokemon trainerMon, Pokemon otherMon) {
		this.trainerMon = trainerMon;
		this.otherMon = otherMon;
		trainerMonHp = trainerMon.getHp() - trainerMon.getDeltaHp();
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		battleStart();
	}
	
	// BATTLE START
	public void battleStart() {
		// Determining who goes first 
		while (battleContinue) {
			if (trainerMon.getSpeed()>=trainerMon.getSpeed()) {
				trainerAttack();
				updateStats();
				checkBattle();
				otherAttack();
				updateStats();
				checkBattle();
			}
			else {
				otherAttack();
				updateStats();
				checkBattle();
				trainerAttack();
				updateStats();
				checkBattle();
			}
		}
	}
	
	// The trainerAttack method is used to determine the attack of the player
	public void trainerAttack() {
		// TEMPORARY 
		int index = 0; 
		Move attack = trainerMon.getMoves()[index];
		if (attack.getCategory().equals("Physical")) {
			
		}
		
	}
	
	// The otherAttack is for the opponent 
	public void otherAttack() {
		
	}
	
	// The updateStats method is used to update the stats of the Pokemon
	// It takes in no parameters
	// It returns nothing 
	public void updateStats() {
		trainerMonHp = trainerMon.getHp() - trainerMon.getDeltaHp();
		trainerMonHp = trainerMon.getAttack() - trainerMon.getDeltaAttack();
		trainerMonHp = trainerMon.getDef() - trainerMon.getDeltaDef();
		trainerMonHp = trainerMon.getSpAtk() - trainerMon.getSpAtk();
		trainerMonHp = trainerMon.getSpDef() - trainerMon.getDeltaSpDef();
		trainerMonHp = trainerMon.getSpeed() - trainerMon.getDeltaSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		otherMonHp = otherMon.getAttack() - otherMon.getDeltaAttack();
		otherMonHp = otherMon.getDef() - otherMon.getDeltaDef();
		otherMonHp = otherMon.getSpAtk() - otherMon.getSpAtk();
		otherMonHp = otherMon.getSpDef() - otherMon.getDeltaSpDef();
		otherMonHp = otherMon.getSpeed() - otherMon.getDeltaSpeed();
	}
	
	// The checkBattle method is used to see if the CURRENT battle will continue (i.e. same Pokemon)
	// The battle will not continue if a Pokemon has fainted, or the user has switched out
	public void checkBattle() {
		if (trainerMonHp<=0 || otherMonHp<=0) { // OR SWITCH POKEMON 
			battleContinue = false;
			// MUST CHOOSE POKEMON, AND CONSTRUCTOR WILL BE USED AGAIN 
		}
		if (!battleContinue) {
			
		}
	}
	
	// The endBattle method is used when the battle is over
	// It is used to reset the delta variables to 0
	// 
	public void endBattle() {
		trainerMon.setDeltaAttack(0);
		trainerMon.setDeltaDef(0);
		trainerMon.setDeltaSpAtk(0);
		trainerMon.setDeltaSpDef(0);
		trainerMon.setDeltaSpeed(0);
		
		otherMon.setDeltaHp(0);
		otherMon.setDeltaAttack(0);
		otherMon.setDeltaDef(0);
		otherMon.setDeltaSpAtk(0);
		otherMon.setDeltaSpDef(0);
		otherMon.setDeltaSpeed(0);
	}
}
