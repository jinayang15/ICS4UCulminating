// The battle class is used for BATTLES

// LOGIC BEHIND BATTLES:
// We cannot simply choose who goes first based on speed, because there are some moves that have priority - more specifically, quick attack,
// which ALWAYS goes first. As a result, we will first get what the user wants to do, then get what the computer wants, and THEN apply it. 

import java.io.*; 
public class Battle {

	// Variables 
	Pokemon trainerMon;
	Pokemon otherMon;
	private boolean attack = false; 
	private boolean switchPokemon = false; // Boolean that sees if the user is switching Pokemon
	private boolean battleContinue = true; // Boolean to see if the battle is going to continue 
	private boolean quickAttack = false; 
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
				trainerAttack(); // will fix
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
				trainerAttack(); // will fix
				updateStats();
				checkBattle();
			}
		}
	}
	
	public void trainerChooseAttack() {
		// WHAT THE USER CHOOSES BASED ON THE GRAPHICS THINGY 
	}
	
	public void opponentChooseAttack() {
		
	}
	
	
	// The trainerAttack method is used to determine the attack of the player
	// attack = trainerMon.getMoves()[index]
	public void trainerAttack(Move attack) {
		// TEMPORARY 
		double stab = 1; // STAB stands for 'Same Type Attack Bonus'. If the Pokemon attacks with a move that has the same type as itself, it gets this bonus
		
		// Cycling through arraylist of moves to see if there is STAB 
		for (int i = 0; i<trainerMon.getTypeList().size(); i++) {
			if (trainerMon.getTypeList().get(i).equals(attack.getType())) {
				stab = 1.5; 
				break;
			}
		}
		if (attack.getCategory().equals("Physical")) {
			// HARD CODING EACH OF THE 'SPECIAL' CASES
			if (attack.getName().equals("Take Down") || attack.getName().equals("Double-Edge")) {
				// Take Down and Double-Edge does 25% of the damage done to the opponent as recoil to itself. 
				// To keep track of this, I am creating a tracker to see how much damage is done, then multiplying that by 0.25
				int beforeAttack = otherMon.getDeltaHp();
				applyTrainerAttack(attack, stab);
				int afterAttack = otherMon.getDeltaHp();
				trainerMon.setDeltaHp(afterAttack-beforeAttack);
				updateStats();
			}
			else if (attack.getName().equals("Dig")) {
				System.out.println(trainerMon.getName() + " has gone underground!");
				
			}
			else {
				applyTrainerAttack(attack,stab);
				updateStats();
			}
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
			// ----------------------------------------------------------------------------------------------------
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
	
	
	// DAMAGE FORMULA: -----------------------------------------------------------
	// For 2 defender types:
	// newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2))
	// For 1 defender type:
	// newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
		
	// Applies the users attack
	public void applyTrainerAttack(Move attack, double stab) {
		int newHp = 0;
		// If the other pokemon has 2 types
		if (otherMon.getTypeList().size()==2) {
			int type1 = otherMon.getType1().getTypeNum();
			int type2 = otherMon.getType2().getTypeNum();
			try {
				newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
			}
			catch (IOException e) {
				
			}
			otherMon.setDeltaHp(newHp);
		}
		else {
			int type1 = otherMon.getType1().getTypeNum();
			try {
				newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
			}
			catch (IOException e) {
				
			}
			otherMon.setDeltaHp(newHp);
		}
	}
	
	// Applies the opponents attack 
	public void applyOtherAttack(Move attack) {
		
	}
}
