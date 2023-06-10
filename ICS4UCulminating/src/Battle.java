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
	private boolean trainerSkipTurn = false; // Boolean to determine if the trainers turn is skipped 
	private boolean otherSkipTurn = false; // Same as last one, except its for opponent 
	private boolean hit = false; // To check if the attack hit the other pokemon
	
	private int trainerMonHp;
	private int trainerMonAttack;
	private int trainerMonDef;
	private int trainerMonSpAtk;
	private int trainerMonSpDef;
	private int trainerMonSpeed;
	
	private int otherMonHp;
	private int otherMonAttack;
	private int otherMonDef;
	private int otherMonSpAtk;
	private int otherMonSpDef;
	private int otherMonSpeed;
	
	
	private int trainerMonStatus = 0; 
	private int otherMonStatus = 0; 
	// Poison - 1
	// Burn - 2
	// Paralyzed - 3
	// Sleep - 4
	// Toxic Poison - 5
	
	// These trainerMon / otherMon stat counters are used to coordinate the number of stat raising/lowering.
	// The maximum number of times a stat can be raised is 6 stages, and there are moves that can raise your stats
	// 1 to 2 stages. These are used to ensure that the number of stat stage raising/lowering does not exceed 6 or -6. 
	private int trainerMonAtkCount = 0; 
	private int trainerMonDefCount = 0; 
	private int trainerMonSpAtkCount = 0;
	private int trainerMonSpDefCount = 0; 
	private int trainerMonSpeedCount = 0;
	
	private int otherMonAtkCount = 0; 
	private int otherMonDefCount = 0; 
	private int otherMonSpAtkCount = 0;
	private int otherMonSpDefCount = 0; 
	private int otherMonSpeedCount = 0;
	
	
	// Constructor
	public Battle (Pokemon trainerMon, Pokemon otherMon) {
		this.trainerMon = trainerMon;
		this.otherMon = otherMon;
		trainerMonHp = trainerMon.getHp() - trainerMon.getDeltaHp();
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		updateStats();
		battleStart();
	}
	
	// BATTLE START
	public void battleStart() {
		// Determining who goes first 
		while (battleContinue) {
			trainerChooseAttack();
			opponentChooseAttack();
			// blah blah people go
			applyStatus();
		}
	}
	// The trainerChooseAttack method is used to determine what attack the user chooses
	// It takes in no parameter
	// It returns nothing
	public void trainerChooseAttack() {
		// WHAT THE USER CHOOSES BASED ON THE GRAPHICS THINGY 
	}
	
	// The opponentChooseAttack method randomly chooses a move for the opponent to use
	// It takes in no parameters
	// It returns nothing 
	public Move opponentChooseAttack() {
		int random = (int) (Math.random()*4) + 1;
		while (otherMon.getMoves()[random-1].equals(null)) {
			random--;
		}
		return otherMon.getMoves()[random-1];
	}
	
	// The coordinateBattle method is used to coordinate the battle. It will check for speed, priority moves, etc. 
	// This is why it is important to know what both Pokemon will do first, and then finalizing the order 
	// This method takes in no parameters
	// It also returns nothing 
	public void coordinateBattle() {
		// This is going to be such a fucking pain in the ass holy SHIT 
		
		// things to check for:
		// Quick attacks (if both use, then higher speed goes first) 
		// If lets say I go first and apply a status, that will DIRECTLY AFFECT the opponent that is going RIGHT AFTER 
		// (fix is by applying status right after each move???)
		// such as applyTrainer()
		// applyStatus()
		// applyOther()
		
		trainerSkipTurn = false;
		otherSkipTurn = false;
	}

		
	// The attack method is used to determine the attacks of both the player and opponent 
	// WAS PREVIOUSLY trainerAttack method (so if this does not work, go back)
	// attack = trainerMon.getMoves()[index]
	 public void attack (Move attack, Pokemon attackMon, Pokemon defendMon) {
		 
		// PP Counter!!
		 
		// TEMPORARY 
		double stab = 1; // STAB stands for 'Same Type Attack Bonus'. If the Pokemon attacks with a move that has the same type as itself, it gets this bonus
		int random; // Used for the random status effects 
		hit = false;
		// Cycling through arraylist of moves to see if there is STAB 
		for (int i = 0; i<attackMon.getTypeList().size(); i++) {
			if (attackMon.getTypeList().get(i).equals(attack.getType())) {
				stab = 1.5; 
				break;
			}
		}
		if (attack.getCategory().equals("Physical")) {
			// HARD CODING EACH OF THE 'SPECIAL' CASES
			if (attack.getName().equals("Take Down") || attack.getName().equals("Double-Edge")) {
				// Take Down and Double-Edge does 25% of the damage done to the opponent as recoil to itself. 
				// To keep track of this, I am creating a tracker to see how much damage is done, then multiplying that by 0.25
				int beforeAttack = defendMon.getDeltaHp();
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						trainerMon.setDeltaHp(trainerMon.getDeltaHp() + (int)Math.round((afterAttack-beforeAttack)*0.25));
					}
					else {
						int afterAttack = trainerMon.getDeltaHp();
						otherMon.setDeltaHp(otherMon.getDeltaHp() + (int)Math.round((afterAttack-beforeAttack)*0.25));
					}
					
				}
				hit = false;
			}
			else if (attack.getName().equals("Skull Bash")) {
				
			}
			// These next moves are only possible by the opposing Pokemon, so there is no need to check if it is from the player 
			else if (attack.getName().equals("Fire Punch")) {
				applyOtherAttack (attack, stab); 
				if (hit && trainerMonStatus==0 && !trainerMon.getType1().equals(new PokeType ("Fire")) && !trainerMon.getType2().equals(new PokeType ("Fire"))) {
					random = (int) (Math.random()*10) + 1;
					if (random==1) {
						trainerMonStatus = 2; 
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Thunder Punch")) {
				applyOtherAttack (attack, stab); 
				if (hit && trainerMonStatus==0 && !trainerMon.getType1().equals(new PokeType ("Electric")) && !trainerMon.getType2().equals(new PokeType ("Electric"))) {
					random = (int) (Math.random()*10) + 1;
					if (random==1) {
						trainerMonStatus = 3; 
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Poison Sting")) {
				applyOtherAttack (attack, stab);
				if (hit && trainerMonStatus==0 && !trainerMon.getType1().equals(new PokeType ("Poison")) && !trainerMon.getType2().equals(new PokeType ("Poison"))) {
					random = (int) (Math.random()*10) + 1;
					// 30% chance to poison the target
					if (random<=3) {
						trainerMonStatus = 1; 
					}
				}
				hit = false;
			}
			// Will immediately make the opponent faint
			else if (attack.getName().equals("Self Destruct")) {
				applyOtherAttack(attack, stab);
				if (hit) {
					otherMon.setDeltaHp(otherMon.getHp());
				}
				hit = false;
			}
			else if (attack.getName().equals("Poison Fang")) {
				applyOtherAttack (attack, stab);
				if (hit && trainerMonStatus==0 && !trainerMon.getType1().equals(new PokeType ("Poison")) && !trainerMon.getType2().equals(new PokeType ("Poison"))) {
					random = (int) (Math.random()*10) + 1;
					if (random<=3) {
						trainerMonStatus = 5; 
					}
				}
				hit = false;
			}
			else {
				applyAttackChecker (attackMon, attack, stab);
				hit = false;
			}
			updateStats();
		}
		else if (attack.getCategory().equals("Special")) {
			if (attack.getName().equals("Acid")) {
				applyAttackChecker (attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonDefCount>-6) {
							random = (int) (Math.random()*(3)) + 1;
							// 33% chance of lowering defense by 1 stage
							if (random==1) {
								otherMonDefCount--;
								otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) Math.floor(otherMon.getDeltaDef()/6));
							}
						}
					}
					else if (attackMon.equals(otherMon)) {
						if (trainerMonDefCount>-6) {
							random = (int) (Math.random()*(3)) + 1;
							// 33% chance of lowering defense by 1 stage
							if (random==1) {
								trainerMonDefCount--;
								trainerMon.setDeltaDef(trainerMon.getDeltaDef() + (int) Math.floor(trainerMon.getDeltaDef()/6));
							}
						}
					}
					
					hit = false;
				}
			}
			else if (attack.getName().equals("Ember") || attack.getName().equals("Flamethrower") || attack.getName().equals("Fire Blast") || attack.getName().equals("Heat Wave")) {
				applyAttackChecker (attackMon, attack, stab);
				if (attackMon.equals(trainerMon)) {
					if (otherMonStatus==0) {
						if (hit) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random()* (10)) + 1;
							if (random==1 && !otherMon.getType1().equals(new PokeType ("Fire")) && !otherMon.getType1().equals(new PokeType("Fire"))) {
								otherMonStatus = 2; 
							}
						}
					}
				}
				else if (attackMon.equals(otherMon)) {
					if (trainerMonStatus==0) {
						if (hit) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random()* (10)) + 1;
							if (random==1 && !trainerMon.getType1().equals(new PokeType ("Fire")) && !trainerMon.getType1().equals(new PokeType("Fire"))) {
								trainerMonStatus = 2; 
							}
						}
					}
				}
				hit = false;
				
			}
			else if (attack.getName().equals("Bubble Beam") || attack.getName().equals("Bubble")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						// 33% chance to drop speed
						random = (int) (Math.random() * (3)) + 1;
						if (otherMonSpeedCount>-6 && random==1) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getDeltaSpeed()/6));
						}
					}
					else {
						random = (int) (Math.random() * (3)) + 1;
						if (trainerMonSpeedCount>-6 && random==1) {
							trainerMonSpeedCount--;
							trainerMon.setDeltaSpeed(trainerMon.getDeltaSpeed() + (int) Math.floor(trainerMon.getDeltaSpeed()/6));
						}
					}
					
				}
				hit = false;
			}
			
			else if (attack.getName().equals("Absorb") || attack.getName().equals("Mega Drain") || attack.getName().equals("Giga Drain")) {
				applyAttackChecker(attackMon, attack, stab);
				// In addition to hitting the opponent, it will also absorb HP based on half of the damage dealt. 
				int beforeAttack = defendMon.getDeltaHp();
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						trainerMon.setDeltaHp(trainerMon.getDeltaHp() - (int) (0.5*(afterAttack-beforeAttack)));
					}
					else {
						int afterAttack = trainerMon.getDeltaHp();
						otherMon.setDeltaHp(otherMon.getDeltaHp() - (int) (0.5*(afterAttack-beforeAttack)));
					}
				}
				hit = false;
			}

			else if (attack.getName().equals("Mud Shot")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonSpeedCount>-6) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getDeltaSpeed()/6));
						}
					}
					else {
						if (trainerMonSpeedCount>-6) {
							trainerMonSpeedCount--;
							trainerMon.setDeltaSpeed(trainerMon.getDeltaSpeed() + (int) Math.floor(trainerMon.getDeltaSpeed()/6));
						}
					}
				}
				hit = false;
			}
			
			// Only moves that the opposing Pokemon can have
			
			// HYPER BEAM
			
			else if (attack.getName().equals("Thunder Shock") || attack.getName().equals("Thunderbolt")) {
				applyOtherAttack(attack, stab); 
				if (hit) {
					// 10% chance of paralysis
					random = (int) (Math.random()*(10)) + 1;
					if (otherMonStatus==0 && random==1 && !otherMon.getType1().equals(new PokeType ("Electric")) && !otherMon.getType2().equals(new PokeType ("Electric"))) {
						otherMonStatus = 3;
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Sludge")) {
				applyOtherAttack(attack, stab);
				// 
				if (hit && trainerMonStatus==0 && !trainerMon.getType1().equals(new PokeType ("Poison")) && !trainerMon.getType2().equals(new PokeType ("Poison"))) {
					random = (int) (Math.random()*10) + 1;
					if (random<=3) {
						trainerMonStatus = 1;
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Mud-Slap")) {
				applyOtherAttack(attack, stab);
				if (hit) {
					// LOWER ACCURACY SOMEHOW 
				}
			}
			else {
				applyAttackChecker (attackMon, attack, stab);
				hit = false;
			}
			updateStats();
		}
		// If the user chooses status moves 
		else if (attack.getCategory().equals("Status")) {
			// Swords dance will raise the attack stat by 2 stages. If the user is already at +5 stage, it will only add 1 extra one. 
			if (attack.getName().equals("Swords Dance")) {
				if (attackMon.equals(trainerMon)) {
					if (trainerMonAtkCount==5) {
						trainerMonAtkCount++; 
						trainerMon.setDeltaAttack(trainerMon.getDeltaAttack() + (int) Math.floor(trainerMon.getAttack()/6));
					}
					else {
						trainerMonAtkCount+=2; 
						trainerMon.setDeltaAttack(trainerMon.getDeltaAttack() + (int) (2*Math.floor(trainerMon.getAttack()/6)));
					}
				}
				else {
					if (otherMonAtkCount==5) {
						otherMonAtkCount++; 
						otherMon.setDeltaAttack(otherMon.getDeltaAttack() + (int) Math.floor(otherMon.getAttack()/6));
					}
					else {
						otherMonAtkCount+=2; 
						otherMon.setDeltaAttack(otherMon.getDeltaAttack() + (int) (2*Math.floor(otherMon.getAttack()/6)));
					}
				}
				
			}
			else if (attack.getName().equals("Tail Whip") || attack.getName().equals("Leer")) {
				applyAttackChecker (attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonDefCount>-6) {
							otherMonDefCount--;
							otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) Math.floor(otherMon.getDeltaDef()/6));
						}
					}
					else {
						if (trainerMonDefCount>-6) {
							trainerMonDefCount--;
							trainerMon.setDeltaDef(trainerMon.getDeltaDef() + (int) Math.floor(trainerMon.getDeltaDef()/6));
						}
					}
					
				}
				hit = false;
			}
			else if (attack.getName().equals("Growl")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonAtkCount>-6) {
							otherMonAtkCount--;
							otherMon.setDeltaAttack(otherMon.getDeltaAttack() + (int) Math.floor(otherMon.getDeltaAttack()/6));
						}
					}
					else {
						if (trainerMonAtkCount>-6) {
							trainerMonAtkCount--;
							trainerMon.setDeltaAttack(trainerMon.getDeltaAttack() + (int) Math.floor(trainerMon.getDeltaAttack()/6));
						}
					}
					
				}
				hit = false;
			}
			else if (attack.getName().equals("Growth")) {
				if (attackMon.equals(trainerMon)) {
					if (trainerMonSpAtkCount<6) {
						trainerMonSpAtkCount++; 
						trainerMon.setDeltaSpAtk(trainerMon.getDeltaSpAtk() + (int) Math.floor(trainerMon.getSpAtk()/6));
					}
				}
				else {
					if (otherMonSpAtkCount<6) {
						otherMonSpAtkCount++; 
						otherMon.setDeltaSpAtk(otherMon.getDeltaSpAtk() + (int) Math.floor(otherMon.getSpAtk()/6));
					}
				}
			}
			else if (attack.getName().equals("Poison Powder")) {
				applyAttackChecker(attackMon, attack, stab);
				// If a Pokemon already has a status applied, other statuses will not work! 
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!otherMon.getType1().equals(new PokeType ("Poison")) && !otherMon.getType2().equals(new PokeType("Poison"))) {
								otherMonStatus = 1;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (trainerMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!trainerMon.getType1().equals(new PokeType ("Poison")) && !trainerMon.getType2().equals(new PokeType("Poison"))) {
								trainerMonStatus = 1;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
				}
				hit = false;
				
			}
			else if (attack.getName().equals("Stun Spore")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!otherMon.getType1().equals(new PokeType ("Electric")) && !otherMon.getType2().equals(new PokeType ("Electric"))) {
								otherMonStatus = 3;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (trainerMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!trainerMon.getType1().equals(new PokeType ("Electric")) && !trainerMon.getType2().equals(new PokeType ("Electric"))) {
								trainerMonStatus = 3;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
				}
				hit = false;
				
			}
			else if (attack.getName().equals("Sleep Powder")) {
				applyAttackChecker(attackMon, attack, stab);
				if (attackMon.equals(trainerMon)) {
					if (otherMonStatus!=0) {
						System.out.println("It had no effect!");
					}
					else {
						if (hit) {
							otherMonStatus = 4; 
						}
					}
				}
				else {
					if (trainerMonStatus!=0) {
						System.out.println("It had no effect!");
					}
					else {
						if (hit) {
							trainerMonStatus = 4; 
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Thunder Wave")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!otherMon.getType1().equals(new PokeType ("Electric")) && !otherMon.getType2().equals(new PokeType ("Electric"))) {
								otherMonStatus = 3;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (trainerMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!trainerMon.getType1().equals(new PokeType ("Electric")) && !trainerMon.getType2().equals(new PokeType ("Electric"))) {
								trainerMonStatus = 3;
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Toxic")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(trainerMon)) {
						if (otherMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!otherMon.getType1().equals(new PokeType("Poison")) && !otherMon.getType2().equals(new PokeType("Poison"))) {
								otherMonStatus = 5; 
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (trainerMonStatus!=0) {
							System.out.println("It had no effect!");
						}
						else {
							if (!trainerMon.getType1().equals(new PokeType("Poison")) && !trainerMon.getType2().equals(new PokeType("Poison"))) {
								trainerMonStatus = 5; 
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Withdraw")) {
				if (attackMon.equals(trainerMon)) {
					if (trainerMonDefCount<6) {
						trainerMonDefCount++; 
						trainerMon.setDeltaDef(trainerMon.getDeltaDef() + (int) (trainerMon.getDef()/6));
					}
				}
				else {
					if (otherMonDefCount<6) {
						otherMonDefCount++; 
						otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) (otherMon.getDef()/6));
					}
				}
			}
			updateStats();
		}
	}
	
	// The updateStats method is used to update the stats of the Pokemon
	// It takes in no parameters
	// It returns nothing 
	public void updateStats() {
		trainerMonHp = trainerMon.getHp() - trainerMon.getDeltaHp();
		trainerMonAttack = trainerMon.getAttack() - trainerMon.getDeltaAttack();
		trainerMonDef = trainerMon.getDef() - trainerMon.getDeltaDef();
		trainerMonSpAtk = trainerMon.getSpAtk() - trainerMon.getSpAtk();
		trainerMonSpDef = trainerMon.getSpDef() - trainerMon.getDeltaSpDef();
		trainerMonSpeed = trainerMon.getSpeed() - trainerMon.getDeltaSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		otherMonAttack = otherMon.getAttack() - otherMon.getDeltaAttack();
		otherMonDef = otherMon.getDef() - otherMon.getDeltaDef();
		otherMonSpAtk = otherMon.getSpAtk() - otherMon.getSpAtk();
		otherMonSpDef = otherMon.getSpDef() - otherMon.getDeltaSpDef();
		otherMonSpeed = otherMon.getSpeed() - otherMon.getDeltaSpeed();
		
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
	
	
	// The applyAttackChecker method is used to see which Pokemon is attacking - either the player or the opponent
	// It takes in the parameters of the attacking Pokemon, the move, and the STAB value. 
	public void applyAttackChecker (Pokemon attackMon, Move attack, double stab) {
		if (attackMon.equals(trainerMon)) applyTrainerAttack(attack, stab);
		else if (attackMon.equals(otherMon)) applyOtherAttack(attack, stab);
	}
	
	
	// Applies the users attack
	public void applyTrainerAttack(Move attack, double stab) {
		int newHp = 0;
		double accuracy = Math.random();
		if (attack.getAccuracy()==0) {} // 100% accurate moves have an accuracy STAT of 0.0
		// Applying accuracy %. If the attack misses, the damage is not applied. 
		else if (accuracy>attack.getAccuracy()) {
			System.out.println("Missed!");
			return;
		}
		hit = true;
		// If the other pokemon has 2 types
		if (otherMon.getTypeList().size()==2) {
			int type1 = otherMon.getType1().getTypeNum();
			int type2 = otherMon.getType2().getTypeNum();
			try {
				newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
			}
			catch (IOException e) {
				
			}
			otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
		}
		else {
			int type1 = otherMon.getType1().getTypeNum();
			try {
				newHp = (int) Math.round((2*(trainerMon.getLevel()+2)*attack.getAtkPower()*(trainerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
			}
			catch (IOException e) {
				
			}
			otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
		}
	}
	
	// Applies the opponents attack 
	public void applyOtherAttack(Move attack, double stab) {
		int newHp = 0;
		double accuracy = Math.random();
		// Applying accuracy %. If the attack misses, the damage is not applied. 
		if (attack.getAccuracy()==0) {}
		else if (accuracy>attack.getAccuracy()) {
			System.out.println("Missed!");
			return;
		}
		hit = true;
		// If the other pokemon has 2 types
		if (trainerMon.getTypeList().size()==2) {
			int type1 = trainerMon.getType1().getTypeNum();
			int type2 = trainerMon.getType2().getTypeNum();
			try {
				newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getAttack()/trainerMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
			}
			catch (IOException e) {
				
			}
			trainerMon.setDeltaHp(trainerMon.getDeltaHp() + newHp);
		}
		else {
			int type1 = trainerMon.getType1().getTypeNum();
			try {
				newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getAttack()/trainerMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
			}
			catch (IOException e) {
				
			}
			trainerMon.setDeltaHp(trainerMon.getDeltaHp() + newHp);
		}
	}
	
	public void applyStatus() {
		
		// Trainer Pokemon Status
		
		if (trainerMonStatus==0) {
			return; // No status 
		}
		// Poison - Takes away 1/8th of the Pokemon's HP
		else if (trainerMonStatus==1) {
			trainerMon.setDeltaHp(trainerMon.getDeltaHp() + trainerMon.getHp()/8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (trainerMonStatus==2) {
			trainerMon.setDeltaHp(trainerMon.getDeltaHp() + (trainerMon.getHp()/16));
			trainerMon.setDeltaAttack(trainerMonAttack/2);
		}
		// Paralyze
		else if (trainerMonStatus==3) {
			
		}
		// Sleep
		else if (trainerMonStatus==4) {
			
		}
		// Badly Poisoned (through the move Toxic)
		else {
			
		}
		
		// Opponent Pokemon Status
		if (otherMonStatus==0) {
			return; // No status 
		}
		// Poison 
		else if (otherMonStatus==1) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + otherMon.getHp()/8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (otherMonStatus==2) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + (otherMon.getHp()/16));
			otherMon.setDeltaAttack(otherMonAttack/2);
		}
		// Paralyze
		else if (otherMonStatus==3) {
			
		}
		// Sleep
		else if (otherMonStatus==4) {
			
		}
		// Badly Poisoned (through the move Toxic)
		else {
			
		}
		
	}
	
	public void cureStatus(Pokemon mon, int oldStatus) {
		if (mon.equals(trainerMon)) {
			if (oldStatus==1) {
				
			}
			else if (oldStatus==2) {
				
			}
			else if (oldStatus==3) {
				
			}
			else if (oldStatus==4) {
				
			}
			else {
				
			}
		}
		
		else {
			if (oldStatus==1) {
				
			}
			else if (oldStatus==2) {
				
			}
			else if (oldStatus==3) {
				
			}
			else if (oldStatus==4) {
				
			}
			else {
				
			}
		}
	}
}
