// The battle class is used for BATTLES

// LOGIC BEHIND BATTLES:
// We cannot simply choose who goes first based on speed, because there are some moves that have priority - more specifically, quick attack,
// which ALWAYS goes first. As a result, we will first get what the user wants to do, then get what the computer wants, and THEN apply it. 

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*; 
import java.util.*;
public class Battle {

	// Variables 
	private Player player;
	private Trainer other;
	
	private Pokemon playerMon;
	private Pokemon otherMon;
	private BufferedImage playerMonSprite;
	private BufferedImage otherMonSprite;
	
	private int battleState = 0;
	private boolean attack = false; 
	private boolean switchPokemon = false; // Boolean that sees if the user is switching Pokemon
	private boolean battleContinue = true; // Boolean to see if the battle is going to continue 
	private boolean quickAttack = false; 
	private boolean trainerSkipTurn = false; // Boolean to determine if the trainers turn is skipped 
	private boolean otherSkipTurn = false; // Same as last one, except its for opponent 
	private boolean hit = false; // To check if the attack hit the other pokemon
	
	private int playerMonHp;
	private int playerMonAttack;
	private int playerMonDef;
	private int playerMonSpAtk;
	private int playerMonSpDef;
	private int playerMonSpeed;
	
	private int otherMonHp;
	private int otherMonAttack;
	private int otherMonDef;
	private int otherMonSpAtk;
	private int otherMonSpDef;
	private int otherMonSpeed;
	
	// Poison - 1
	// Paralyzed - 2
	// Sleep - 3
	// Burn - 4
	// Toxic Poison - 5
	
	// These playerMon / otherMon stat counters are used to  the number of stat raising/lowering.
	// The maximum number of times a stat can be raised is 6 stages, and there are moves that can raise your stats
	// 1 to 2 stages. These are used to ensure that the number of stat stage raising/lowering does not exceed 6 or -6. 
	private int trainerMonAtkCount = 0; 
	private int playerMonDefCount = 0; 
	private int playerMonSpAtkCount = 0;
	private int playerMonSpDefCount = 0; 
	private int playerMonSpeedCount = 0;
	
	private int otherMonAtkCount = 0; 
	private int otherMonDefCount = 0; 
	private int otherMonSpAtkCount = 0;
	private int otherMonSpDefCount = 0; 
	private int otherMonSpeedCount = 0;
	
	// Status counter variables - used to count status conditions
	private int trainerSleepCounter = 0;
	private int trainerToxicCounter = 1;
	private int otherSleepCounter = 0;
	private int otherToxicCounter = 1;
	
	//Arrow position
	private int[][] optionsArrowPositions = {{512, 496}, {512, 560}, {736, 496}, {736, 560}};
	private int optionsArrowX = 512;
	private int optionsArrowY = 496;
	private int optionsArrowIdx = 0;

	private int[][] attackArrowPositions = {{36, 490}, {332, 490}, {36, 554}, {332, 554}};
	private int attackArrowX = 36;
	private int attackArrowY = 490;
	private int attackArrowIdx = 0;
	
	// Constructor
	public Battle (Player player, Trainer other) {
		this.player = player;
		this.other = other;
		
		playerMon = player.getPokemonList()[0];
		playerMonSprite = Images.battleSprites[Images.battleSpritesIdx.get(playerMon.getName().toLowerCase())][1];
		otherMon = other.getPokemonList()[0];
		otherMonSprite = Images.battleSprites[Images.battleSpritesIdx.get(otherMon.getName().toLowerCase())][0];

		playerMonHp = playerMon.getHp() - playerMon.getDeltaHp();
		playerMonAttack = playerMon.getAttack();
		playerMonDef = playerMon.getDef();
		playerMonSpAtk = playerMon.getSpAtk();
		playerMonSpDef = playerMon.getSpDef();
		playerMonSpeed = playerMon.getSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		otherMonAttack = otherMon.getAttack();
		otherMonDef = otherMon.getDef();
		otherMonSpAtk = otherMon.getSpAtk();
		otherMonSpDef = otherMon.getSpDef();
		otherMonSpeed = otherMon.getSpeed();
	
		updateStats();
//		battleStart();
	}
	
	// Overloaded constructor for switching in Pokemon 
	// It will get the index of the Pokemon that wants to be switched in 
	public Battle (Trainer trainer, Player player, Pokemon otherMon, int index, boolean switchPokemon) {
		this.switchPokemon = switchPokemon;
		this.player = player;
		this.other = trainer;
		this.otherMon = otherMon;
		
		playerMon = player.getPokemonList()[index];
		playerMonHp = playerMon.getHp() - playerMon.getDeltaHp();
		playerMonAttack = playerMon.getAttack();
		playerMonDef = playerMon.getDef();
		playerMonSpAtk = playerMon.getSpAtk();
		playerMonSpDef = playerMon.getSpDef();
		playerMonSpeed = playerMon.getSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		otherMonAttack = otherMon.getAttack();
		otherMonDef = otherMon.getDef();
		otherMonSpAtk = otherMon.getSpAtk();
		otherMonSpDef = otherMon.getSpDef();
		otherMonSpeed = otherMon.getSpeed();
		
		System.out.println("NEW POKEMON HP: " + playerMon.getHp());
		updateStats();
//		battleStart(); 
	}
	
	// Overloaded constructor for switching the enemy Pokemon 
	public Battle (Player player, Trainer trainer, Pokemon playerMon, int index) {
		this.player = player;
		this.other = trainer;
		this.playerMon = playerMon; // Shouldn't affect anything...
		otherMon = other.getPokemonList()[index];

		playerMonHp = playerMon.getHp() - playerMon.getDeltaHp();
		playerMonAttack = playerMon.getAttack();
		playerMonDef = playerMon.getDef();
		playerMonSpAtk = playerMon.getSpAtk();
		playerMonSpDef = playerMon.getSpDef();
		playerMonSpeed = playerMon.getSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		otherMonAttack = otherMon.getAttack();
		otherMonDef = otherMon.getDef();
		otherMonSpAtk = otherMon.getSpAtk();
		otherMonSpDef = otherMon.getSpDef();
		otherMonSpeed = otherMon.getSpeed();
	
		updateStats();
//		battleStart();
	}
	
	// BATTLE START
	public void battleStart() { 
		while (battleContinue) {
			coordinateBattle(); 
		}
	}
	
	// The trainerChooseAttack method is used to determine what attack the user chooses
	// It takes in no parameter
	// It returns nothing
	public Move trainerChooseAttack() {
		// WHAT THE USER CHOOSES BASED ON THE GRAPHICS THINGY 
		int index = 0;
		int tempCount = 0;
		Scanner s = new Scanner (System.in);
		System.out.println("Choose an attack: ");
		for (int i = 0; i < playerMon.getMoves().length; i++) {
			try {
				System.out.println((i+1) + ") " + playerMon.getMoves()[i].getName());
				tempCount = i+1;
			}
			catch (NullPointerException e) {
				break;
			}
		}
		System.out.println((tempCount+1) + ") Change Pokemon");
		while (index==0) {
			index = Integer.parseInt(s.nextLine());
			if (playerMon.getMoves()[index-1].getCurrentPP()==0) {
				index = 0;
			}
		}
		if (index==(tempCount+1)) {
			chooseNewPokemon();
			battleContinue = false;
			return null;
		}
		else {
			playerMon.getMoves()[index-1].setPP(playerMon.getMoves()[index-1].getCurrentPP()-1);
			return playerMon.getMoves()[index-1];
		}
	}
	
	// The opponentChooseAttack method randomly chooses a move for the opponent to use
	// It takes in no parameters
	// It returns nothing 
	public Move opponentChooseAttack() {
		Move tempMove = otherMon.getMoves()[0];
		boolean valid = false;
		int random = (int) (Math.random()*4) + 1;
		while (!valid) {
			try {
				tempMove = otherMon.getMoves()[random-1];
				String temp = tempMove.getName(); // Prevents other type to be null (I'm pretty sure as it throws exception...)
				valid = true;
			}
			catch (NullPointerException e) {
				// System.out.println("other move failed");
//				System.out.println("\nREROLL MOVES\n");
				random--; 
			}
		}
		return tempMove;
	}
	
	// The Battle method is used to  the battle. It will check for speed, priority moves, etc. 
	// This is why it is important to know what both Pokemon will do first, and then finalizing the order 
	// This method takes in no parameters
	// It also returns nothing 
	public void coordinateBattle() {
		
		// Ensures that switching Pokemon in means a free turn for the enemy!
		if (switchPokemon) {
			switchPokemon = false;
			Move otherMove = opponentChooseAttack();
			double stab = calculateStab (otherMon, otherMove);
			attack(otherMove, otherMon, playerMon);
			updateStats();
			System.out.println("\nYOU\t" +  playerMon.getName() + " HP: " + playerMonHp + "\t Level: " + playerMon.getLevel());
			System.out.println("--------------------");
			System.out.println("THEM\t" +  otherMon.getName() + " HP: " + otherMonHp + "\t Level: " + otherMon.getLevel());
			System.out.println("\n");
		}
		
<<<<<<< HEAD
		Move trainerMove = trainerChooseAttack();
		Move otherMove = opponentChooseAttack();
		
		// If both Pokemon choose to use quick attack, which is a priority move, then the one with the higher speed stat will go first
		if (trainerMove.getName().equals("Quick Attack") && otherMove.getName().equals("Quick Attack")) {
			System.out.println("went into 1");
			if (playerMonSpeed>=otherMonSpeed) {
				if (!trainerSkipTurn) {
					// Checking stats to ensure that fainted Pokemon cannot go
					attack(trainerMove, playerMon, otherMon);
					if (!battleContinue) return;
					else if (otherMonHp<=0) {
						checkBattle();
						return;
					}
				}
				else {
					if (playerMon.getStatus()==2) {
						System.out.println(playerMon.getName() + " was fully paralyzed!");
					}
					else if (playerMon.getStatus()==3) {
						System.out.println(playerMon.getName() + " is asleep!");
					}
				}
				if (!otherSkipTurn) {
					attack(otherMove, otherMon, playerMon);
					if (!battleContinue) return;
					else if (playerMonHp<=0) {
						chooseNewPokemon();
					}
				}
				else {
					if (otherMon.getStatus()==2) {
						System.out.println(otherMon.getName() + " was fully paralyzed!");
					}
					else if (otherMon.getStatus()==3) {
						System.out.println(otherMon.getName() + " is asleep!");
					}
					else if (otherMon.getStatus()==0) {
						if (!battleContinue) {
=======
		else {
			System.out.println("\nYOU\t" +  playerMon.getName() + " HP: " + playerMonHp + "\t Level: " + playerMon.getLevel());
			System.out.println("--------------------");
			System.out.println("THEM\t" +  otherMon.getName() + " HP: " + otherMonHp + "\t Level: " + otherMon.getLevel());
			System.out.println("\n");
			applyStatus(); // Apply status first to determine if moves are going to be skipped 
			
			Move trainerMove = trainerChooseAttack();
			Move otherMove = opponentChooseAttack();
			
			// If NullPointerException occurs, it means that Pokemon switching happened (on purpose).
			// It should not crash, so an exception will be thrown
			try {
				String temp = trainerMove.getName();
			}
			catch (NullPointerException e) {
				battleContinue = false;
				return;
			}
			// If both Pokemon choose to use quick attack, which is a priority move, then the one with the higher speed stat will go first
			if (trainerMove.getName().equals("Quick Attack") && otherMove.getName().equals("Quick Attack")) {
				System.out.println("went into 1");
				if (playerMonSpeed>=otherMonSpeed) {
					if (!trainerSkipTurn) {
						// Checking stats to ensure that fainted Pokemon cannot go
						attack(trainerMove, playerMon, otherMon);
						if (!battleContinue) return;
						else if (otherMonHp<=0) {
							checkBattle();
>>>>>>> refs/heads/master
							return;
						}
					}
					else {
						if (playerMon.getStatus()==3) {
							System.out.println(playerMon.getName() + " was fully paralyzed!");
						}
						else if (playerMon.getStatus()==4) {
							System.out.println(playerMon.getName() + " is asleep!");
						}
					}
					if (!otherSkipTurn) {
						attack(otherMove, otherMon, playerMon);
						if (!battleContinue) return;
						else if (playerMonHp<=0) {
							chooseNewPokemon();
						}
					}
					else {
						if (otherMon.getStatus()==3) {
							System.out.println(otherMon.getName() + " was fully paralyzed!");
						}
						else if (otherMon.getStatus()==4) {
							System.out.println(otherMon.getName() + " is asleep!");
						}
						else if (otherMon.getStatus()==0) {
							if (!battleContinue) {
								return;
							}
						}
					}
				}
				else {
					if (!otherSkipTurn) {
						attack(otherMove, otherMon, playerMon);
						if (!battleContinue) return;
						else if (playerMonHp<=0) {
							chooseNewPokemon();
						}
					}
					else {
						if (otherMon.getStatus()==3) {
							System.out.println(otherMon.getName() + " was fully paralyzed!");
						}
						else if (otherMon.getStatus()==4) {
							System.out.println(otherMon.getName() + " is asleep!");
						}
					}
					if (!trainerSkipTurn) {
						attack(trainerMove, playerMon, otherMon);
						if (!battleContinue) return;
						else if (otherMonHp<=0) {
							checkBattle();
							return;
						}
					}
					else {
						if (playerMon.getStatus()==3) {
							System.out.println(playerMon.getName() + " was fully paralyzed!");
						}
						else if (playerMon.getStatus()==4) {
							System.out.println(playerMon.getName() + " is asleep!");
						}
					}
				}
			}
<<<<<<< HEAD
			else {
				if (!otherSkipTurn) {
					attack(otherMove, otherMon, playerMon);
					if (!battleContinue) return;
					else if (playerMonHp<=0) {
						chooseNewPokemon();
					}
				}
				else {
					if (otherMon.getStatus()==2) {
						System.out.println(otherMon.getName() + " was fully paralyzed!");
					}
					else if (otherMon.getStatus()==3) {
						System.out.println(otherMon.getName() + " is asleep!");
					}
				}
				if (!trainerSkipTurn) {
					attack(trainerMove, playerMon, otherMon);
					if (!battleContinue) return;
					else if (otherMonHp<=0) {
						checkBattle();
						return;
					}
				}
				else {
					if (playerMon.getStatus()==2) {
						System.out.println(playerMon.getName() + " was fully paralyzed!");
					}
					else if (playerMon.getStatus()==3) {
						System.out.println(playerMon.getName() + " is asleep!");
					}
				}
			}
		}
		// If only the user chooses quick attack, they are guaranteed to go first 
		else if (trainerMove.getName().equals("Quick Attack")) {
			if (!trainerSkipTurn) {
				attack(trainerMove, playerMon, otherMon);
				if (!battleContinue) return;
				else if (otherMonHp<=0) {
					checkBattle();
					return;
				}
			}
			else {
				if (playerMon.getStatus()==2) {
					System.out.println(playerMon.getName() + " was fully paralyzed!");
				}
				else if (playerMon.getStatus()==3) {
					System.out.println(playerMon.getName() + " is asleep!");
				}
			}
			if (!otherSkipTurn) {
				attack(otherMove, otherMon, playerMon);
				if (!battleContinue) return;
				else if (playerMonHp<=0) {
					chooseNewPokemon();
				}
			}
			else {
				if (otherMon.getStatus()==2) {
					System.out.println(otherMon.getName() + " was fully paralyzed!");
				}
				else if (otherMon.getStatus()==3) {
					System.out.println(otherMon.getName() + " is asleep!");
				}
			}
		}
		// If the only the opponent uses quick attack, they are guaranteed to go first
		else if (otherMove.getName().equals("Quick Attack")) {
			if (!otherSkipTurn) {
				attack(otherMove, otherMon, playerMon);
				if (!battleContinue) return;
				else if (playerMonHp<=0) {
					chooseNewPokemon();
				}
			}
			else {
				if (otherMon.getStatus()==2) {
					System.out.println(otherMon.getName() + " was fully paralyzed!");
				}
				else if (otherMon.getStatus()==3) {
					System.out.println(otherMon.getName() + " is asleep!");
				}
			}
			if (!trainerSkipTurn) {
				attack(trainerMove, playerMon, otherMon);
				if (!battleContinue) return;
				else if (otherMonHp<=0) {
					checkBattle();
					return;
				}
			}
			else {
				if (playerMon.getStatus()==2) {
					System.out.println(playerMon.getName() + " was fully paralyzed!");
				}
				else if (playerMon.getStatus()==3) {
					System.out.println(playerMon.getName() + " is asleep!");
				}
			}
		}
		else {
			if (playerMonSpeed>=otherMonSpeed) {
=======
			// If only the user chooses quick attack, they are guaranteed to go first 
			else if (trainerMove.getName().equals("Quick Attack")) {
>>>>>>> refs/heads/master
				if (!trainerSkipTurn) {
					attack(trainerMove, playerMon, otherMon);
					if (!battleContinue) return;
					else if (otherMonHp<=0) {
						checkBattle();
						return;
					}
				}
				else {
					if (playerMon.getStatus()==2) {
						System.out.println(playerMon.getName() + " was fully paralyzed!");
					}
					else if (playerMon.getStatus()==3) {
						System.out.println(playerMon.getName() + " is asleep!");
					}
				}
				if (!otherSkipTurn) {
					attack(otherMove, otherMon, playerMon);
					if (!battleContinue) return;
					else if (playerMonHp<=0) {
						chooseNewPokemon();
					}
				}
				else {
					if (otherMon.getStatus()==2) {
						System.out.println(otherMon.getName() + " was fully paralyzed!");
					}
					else if (otherMon.getStatus()==3) {
						System.out.println(otherMon.getName() + " is asleep!");
					}
				}
			}
			// If the only the opponent uses quick attack, they are guaranteed to go first
			else if (otherMove.getName().equals("Quick Attack")) {
				if (!otherSkipTurn) {
					attack(otherMove, otherMon, playerMon);
					if (!battleContinue) return;
					else if (playerMonHp<=0) {
						chooseNewPokemon();
					}
				}
				else {
					if (otherMon.getStatus()==2) {
						System.out.println(otherMon.getName() + " was fully paralyzed!");
					}
					else if (otherMon.getStatus()==3) {
						System.out.println(otherMon.getName() + " is asleep!");
					}
				}
				if (!trainerSkipTurn) {
					attack(trainerMove, playerMon, otherMon);
					if (!battleContinue) return;
					else if (otherMonHp<=0) {
						checkBattle();
						return;
					}
				}
				else {
					if (playerMon.getStatus()==2) {
						System.out.println(playerMon.getName() + " was fully paralyzed!");
					}
					else if (playerMon.getStatus()==3) {
						System.out.println(playerMon.getName() + " is asleep!");
					}
				}
			}
			else {
				if (playerMonSpeed>=otherMonSpeed) {
					if (!trainerSkipTurn) {
						attack(trainerMove, playerMon, otherMon);
						if (!battleContinue) return;
						else if (otherMonHp<=0) {
							checkBattle();
							return;
						}
					}
					else {
						if (playerMon.getStatus()==3) {
							System.out.println(playerMon.getName() + " was fully paralyzed!");
						}
						else if (playerMon.getStatus()==4) {
							System.out.println(playerMon.getName() + " is asleep!");
						}
					}
					if (!otherSkipTurn) {
						attack(otherMove, otherMon, playerMon);
						if (!battleContinue) return;
						else if (playerMonHp<=0) {
							chooseNewPokemon();
						}
					}
					else {
						if (otherMon.getStatus()==3) {
							System.out.println(otherMon.getName() + " was fully paralyzed!");
						}
						else if (otherMon.getStatus()==4) {
							System.out.println(otherMon.getName() + " is asleep!");
						}
					}
				}
				else {
					if (!otherSkipTurn) {
						attack(otherMove, otherMon, playerMon);
						if (!battleContinue) return;
						else if (playerMonHp<=0) {
							chooseNewPokemon();
						}
					}
					else {
						if (otherMon.getStatus()==3) {
							System.out.println(otherMon.getName() + " was fully paralyzed!");
						}
						else if (otherMon.getStatus()==4) {
							System.out.println(otherMon.getName() + " is asleep!");
						}
					}
					if (!trainerSkipTurn) {
						attack(trainerMove, playerMon, otherMon);
						if (!battleContinue) return;
						else if (otherMonHp<=0) {
							checkBattle();
							return;
						}
					}
					else {
						if (playerMon.getStatus()==3) {
							System.out.println(playerMon.getName() + " was fully paralyzed!");
						}
						else if (playerMon.getStatus()==4) {
							System.out.println(playerMon.getName() + " is asleep!");
						}
					}
				}
			}
		}
	}

		
	// The attack method is used to determine the attacks of both the player and opponent 
	// WAS PREVIOUSLY trainerAttack method (so if this does not work, go back)
	// attack = playerMon.getMoves()[index]
	 public void attack (Move attack, Pokemon attackMon, Pokemon defendMon) {
		boolean keepGoing = true;
		// PP Counter!!
		
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
					if (attackMon.equals(playerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						playerMon.setDeltaHp(playerMon.getDeltaHp() + (int)Math.round((afterAttack-beforeAttack)*0.25));
						System.out.println("\n" + playerMon.getName() + " is damaged by recoil!");
					}
					else {
						int afterAttack = playerMon.getDeltaHp();
						otherMon.setDeltaHp(otherMon.getDeltaHp() + (int)Math.round((afterAttack-beforeAttack)*0.25));
						System.out.println("\n" + otherMon.getName() + " is damaged by recoil!");
					}
					
				}
				hit = false;
			}
//			else if (attack.getName().equals("Skull Bash")) {
//				
//			}
			// These next moves are only possible by the opposing Pokemon, so there is no need to check if it is from the player 
			else if (attack.getName().equals("Fire Punch")) {
				applyOtherAttack (attack, stab);  
				if (hit && playerMon.getStatus()==0) {
					for (int i = 0; i<playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType ("Fire"))) keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random()*10) + 1;
						if (random==1) {
							playerMon.setStatus(4);
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Thunder Punch")) {
				applyOtherAttack (attack, stab); // ELECTRIC 
				if (hit && playerMon.getStatus()==0) {
					for (int i = 0; i<playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random()*10) + 1;
						if (random==1) {
							playerMon.setStatus(2);
							System.out.println(playerMon.getName() + " was paralyzed!");
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Poison Sting")) {
				applyOtherAttack (attack, stab);
				if (hit && playerMon.getStatus()==0) {
					for (int i = 0; i<playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random()*10) + 1;
						// 30% chance to poison the target
						if (random<=3) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
						}
					}
				}
				hit = false;
			}
			// Will immediately make the opponent faint
			else if (attack.getName().equals("Self-Destruct")) {
				applyOtherAttack(attack, stab);
				if (hit) {
					otherMon.setDeltaHp(otherMon.getHp());
				}
				hit = false;
			}
			else if (attack.getName().equals("Poison Fang")) {
				applyOtherAttack (attack, stab);
				for (int i = 0; i<playerMon.getTypeList().size(); i++) {
					if (playerMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
				}
				if (keepGoing) {
					random = (int) (Math.random()*10) + 1;
					// 30% chance to poison the target
					if (random<=3) {
						playerMon.setStatus(1);
					}
				}
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
					if (attackMon.equals(playerMon)) {
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
						if (playerMonDefCount>-6) {
							random = (int) (Math.random()*(3)) + 1;
							// 33% chance of lowering defense by 1 stage
							if (random==1) {
								playerMonDefCount--;
								playerMon.setDeltaDef(playerMon.getDeltaDef() + (int) Math.floor(playerMon.getDeltaDef()/6));
							}
						}
					}
					hit = false;
				}
			}
			else if (attack.getName().equals("Ember") || attack.getName().equals("Flamethrower") || attack.getName().equals("Fire Blast") || attack.getName().equals("Heat Wave")) {
				applyAttackChecker (attackMon, attack, stab);
				if (attackMon.equals(playerMon) && hit) {
					if (otherMon.getStatus()==0) {
						for (int i = 0; i<otherMon.getTypeList().size(); i++) {
							if (otherMon.getTypeList().get(i).equals(new PokeType ("Fire"))) keepGoing = false;
						}
						if (keepGoing) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random()* (10)) + 1;
							if (random==1) {
								otherMon.setStatus(4);
								System.out.println("The opposing " + otherMon.getName() + " was burned!");
							}
						}
					}
				}
				else if (attackMon.equals(otherMon)) {
					if (playerMon.getStatus()==0) {
						for (int i = 0; i<playerMon.getTypeList().size(); i++) {
							if (playerMon.getTypeList().get(i).equals(new PokeType ("Fire"))) keepGoing = false;
						}
						if (keepGoing) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random()* (10)) + 1;
							if (random==1) {
								playerMon.setStatus(4);
								System.out.println(playerMon.getName() + " was burned!");
							}
						}
					}
				}
				hit = false;
				
			}
			else if (attack.getName().equals("Bubble Beam") || attack.getName().equals("Bubble")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						// 33% chance to drop speed
						random = (int) (Math.random() * (3)) + 1;
						if (otherMonSpeedCount>-6 && random==1) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getDeltaSpeed()/6));
							System.out.println("\n" + otherMon.getName() + "'s speed fell!");
						}
					}
					else {
						random = (int) (Math.random() * (3)) + 1;
						if (playerMonSpeedCount>-6 && random==1) {
							playerMonSpeedCount--;
							playerMon.setDeltaSpeed(playerMon.getDeltaSpeed() + (int) Math.floor(playerMon.getDeltaSpeed()/6));
							System.out.println("\n" + playerMon.getName() + "'s speed fell!");
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
					if (attackMon.equals(playerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						playerMon.setDeltaHp(playerMon.getDeltaHp() - (int) (0.5*(afterAttack-beforeAttack)));
						System.out.println("\n" + playerMon.getName() + " recovered some HP!");
					}
					else {
						int afterAttack = playerMon.getDeltaHp();
						otherMon.setDeltaHp(otherMon.getDeltaHp() - (int) (0.5*(afterAttack-beforeAttack)));
						System.out.println("\n" + otherMon.getName() + " recovered some HP!");
					}
				}
				hit = false;
			}

			else if (attack.getName().equals("Mud Shot")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMonSpeedCount>-6) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getDeltaSpeed()/6));
							System.out.println("\n" + otherMon.getName() + "'s speed fell!");
						}
					}
					else {
						if (playerMonSpeedCount>-6) {
							playerMonSpeedCount--;
							playerMon.setDeltaSpeed(playerMon.getDeltaSpeed() + (int) Math.floor(playerMon.getDeltaSpeed()/6));
							System.out.println("\n" + playerMon.getName() + "'s speed fell!");
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
					for (int i = 0; i<playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
					}
					if (keepGoing) {
						// 10% chance of paralysis
						random = (int) (Math.random()*(10)) + 1;
						if (playerMon.getStatus()==0 && random==1) {
							playerMon.setStatus(2);
							System.out.println(playerMon.getName() + " was paralyzed!");
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Sludge")) {
				applyOtherAttack(attack, stab);
				// 30% chance to poison
				if (hit && playerMon.getStatus()==0) {
					for (int i = 0; i<playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random()*10) + 1;
						if (random<=3) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
						}
					}
				}
				hit = false;
			}
//			else if (attack.getName().equals("Mud-Slap")) {
//				applyOtherAttack(attack, stab);
//				if (hit) {
//					// LOWER ACCURACY SOMEHOW 
//				}
//			}
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
				System.out.println(attackMon.getName() + " used Swords Dance!");
				System.out.println(attackMon.getName() + "'s attack rose sharply!");
				if (attackMon.equals(playerMon)) {
					if (trainerMonAtkCount==5) {
						trainerMonAtkCount++; 
						playerMon.setDeltaAttack(playerMon.getDeltaAttack() + (int) Math.floor(playerMon.getAttack()/6));
					}
					else {
						trainerMonAtkCount+=2; 
						playerMon.setDeltaAttack(playerMon.getDeltaAttack() + (int) (2*Math.floor(playerMon.getAttack()/6)));
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
					System.out.println(defendMon.getName() + "'s defense fell!");
					if (attackMon.equals(playerMon)) {
						if (otherMonDefCount>-6) {
							otherMonDefCount--;
							otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) Math.floor(otherMon.getDeltaDef()/6));
						}
					}
					else {
						if (playerMonDefCount>-6) {
							playerMonDefCount--;
							playerMon.setDeltaDef(playerMon.getDeltaDef() + (int) Math.floor(playerMon.getDeltaDef()/6));
						}
					}
					
				}
				hit = false;
			}
			else if (attack.getName().equals("Growl")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					System.out.println(defendMon.getName() + "'s attack fell!");
					if (attackMon.equals(playerMon)) {
						if (otherMonAtkCount>-6) {
							otherMonAtkCount--;
							otherMon.setDeltaAttack(otherMon.getDeltaAttack() + (int) Math.floor(otherMon.getDeltaAttack()/6));
						}
					}
					else {
						if (trainerMonAtkCount>-6) {
							trainerMonAtkCount--;
							playerMon.setDeltaAttack(playerMon.getDeltaAttack() + (int) Math.floor(playerMon.getDeltaAttack()/6));
						}
					}
					
				}
				hit = false;
			}
			else if (attack.getName().equals("Growth")) {
				System.out.println(attackMon.getName() + "'s special attack rose!");
				if (attackMon.equals(playerMon)) {
					if (playerMonSpAtkCount<6) {
						playerMonSpAtkCount++; 
						playerMon.setDeltaSpAtk(playerMon.getDeltaSpAtk() + (int) Math.floor(playerMon.getSpAtk()/6));
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
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(1);
								System.out.println("The opposing " + otherMon.getName() + " was poisoned!");
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (playerMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						for (int i = 0; i<playerMon.getTypeList().size(); i++) {
							if (playerMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
						}
						if (keepGoing) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
						}
						else {
							System.out.println("It had no effect!");
						}
					}
				}
				hit = false;
				
			}
			else if (attack.getName().equals("Stun Spore")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(2);
								System.out.println("The opposing " + otherMon.getName() + " was paralyzed! It may be unable to move!");
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (playerMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(2);
								System.out.println(playerMon.getName() + " was paralyzed! It may be unable to move!");
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
				if (attackMon.equals(playerMon)) {
					if (otherMon.getStatus()!=0) {
						System.out.println("It had no effect!");
					}
					else {
						if (hit) {
							otherMon.setStatus(3); 
							System.out.println(otherMon.getName() + " went to sleep!");
						}
					}
				}
				else {
					if (playerMon.getStatus()!=0) {
						System.out.println("It had no effect!");
					}
					else {
						if (hit) {
							playerMon.setStatus(3); 
							System.out.println(playerMon.getName() + " went to sleep!");
						}
					}
				}
				hit = false;
			}
			else if (attack.getName().equals("Thunder Wave")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(2);
								System.out.println("The opposing " + otherMon.getName() + " was paralyzed! It may be unable to move!");
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (playerMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType ("Electric"))) keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(2);
								System.out.println(playerMon.getName() + " was paralyzed! It may be unable to move!");
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
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(5);
								System.out.println("The opposing " + otherMon.getName() + " was badly poisoned!");
							}
							else {
								System.out.println("It had no effect!");
							}
						}
					}
					else {
						if (playerMon.getStatus()!=0) {
							System.out.println("It had no effect!");
						}
						else {
							for (int i = 0; i<playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType ("Poison"))) keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(5);
								System.out.println(playerMon.getName() + " was badly poisoned!");
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
				System.out.println(attackMon.getName()+ " used Withdraw!");
				System.out.println(attackMon.getName() +"'s defense rose!");
				if (attackMon.equals(playerMon)) {
					if (playerMonDefCount<6) {
						playerMonDefCount++; 
						playerMon.setDeltaDef(playerMon.getDeltaDef() + (int) (playerMon.getDef()/6));
					}
				}
				else {
					System.out.println(otherMon.getName()+ " used Withdraw!");
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
		playerMonHp = playerMon.getHp() - playerMon.getDeltaHp();
		if (playerMonHp<=0) {
			System.out.println("\n" + playerMon.getName() + " fainted!");
			playerMonHp = 0;
			playerMon.setStatus(0);
			trainerSkipTurn = true; 
		}
		playerMonAttack = playerMon.getAttack() - playerMon.getDeltaAttack();
		playerMonDef = playerMon.getDef() - playerMon.getDeltaDef();
		playerMonSpAtk = playerMon.getSpAtk() - playerMon.getSpAtk();
		playerMonSpDef = playerMon.getSpDef() - playerMon.getDeltaSpDef();
		playerMonSpeed = playerMon.getSpeed() - playerMon.getDeltaSpeed();
		
		otherMonHp = otherMon.getHp() - otherMon.getDeltaHp();
		if (otherMonHp<=0) {
			System.out.println("\n" + otherMon.getName() + " fainted!");
			otherMonHp = 0; 
			otherMon.setStatus(0);
			otherSkipTurn = true; 
		}
		otherMonAttack = otherMon.getAttack() - otherMon.getDeltaAttack();
		otherMonDef = otherMon.getDef() - otherMon.getDeltaDef();
		otherMonSpAtk = otherMon.getSpAtk() - otherMon.getSpAtk();
		otherMonSpDef = otherMon.getSpDef() - otherMon.getDeltaSpDef();
		otherMonSpeed = otherMon.getSpeed() - otherMon.getDeltaSpeed();
		checkBattle(); 
	}
	
	// The checkBattle method is used to see if the CURRENT battle will continue (i.e. same Pokemon)
	// The battle will not continue if a Pokemon has fainted, or the user has switched out
	// It will return TRUE if the battle is good, FALSE if the battle must end. 
	public void checkBattle() {
		if (otherMonHp<=0) {
			otherMon.setFaint(true);
			for (int i = 0; i<other.getPokemonList().length; i++) {
				if (other.getPokemonList()[i].getFaint()==false) {
					System.out.println("NEXT POKEMON: " + other.getPokemonList()[i].getName());
					new Battle(player, other, playerMon, i);
					battleContinue = false;
					return;
				}
				else if (i==other.getPokemonList().length-1) {
					System.out.println("You won!");
					battleContinue = false;
					endBattle();
					return;
				}
			}
		}
		
		if (playerMonHp<=0) {
			playerMon.setFaint(true);
			for (int i = 0; i<player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint()==false) {
					battleContinue = true; 
					chooseNewPokemon();
					battleContinue = false;
					return;
				}
				else if (i==player.getPokemonList().length-1) {
					System.out.println("You are out of usable Pokemon! You give out a badge...");
					endBattle();
					battleContinue = false;
					return;
				}
			}
		}
	}
	// The endBattle method is used when the battle is over
	// It is used to reset the delta variables to 0
	// 
	public void endBattle() {
		playerMon.setDeltaAttack(0);
		playerMon.setDeltaDef(0);
		playerMon.setDeltaSpAtk(0);
		playerMon.setDeltaSpDef(0);
		playerMon.setDeltaSpeed(0);
		
		otherMon.setDeltaHp(0);
		otherMon.setDeltaAttack(0);
		otherMon.setDeltaDef(0);
		otherMon.setDeltaSpAtk(0);
		otherMon.setDeltaSpDef(0);
		otherMon.setDeltaSpeed(0);
	}
	
	
	// DAMAGE FORMULA: -----------------------------------------------------------
	// For 2 defender types:
	// newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2))
	// For 1 defender type:
	// newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
	
	
	// The applyAttackChecker method is used to see which Pokemon is attacking - either the player or the opponent
	// It takes in the parameters of the attacking Pokemon, the move, and the STAB value. 
	public void applyAttackChecker (Pokemon attackMon, Move attack, double stab) {
		if (attackMon.equals(playerMon)) applyTrainerAttack(attack, stab);
		else if (attackMon.equals(otherMon)) applyOtherAttack(attack, stab);
	}
	
	
	// Applies the users attack
	public void applyTrainerAttack(Move attack, double stab) {
		System.out.println(playerMon.getName() + " used " + attack.getName() + "!");
		int newHp = 0;
		double accuracy = Math.random();
		// If the trainer's turn is skipped, nothing will happen 
		if (trainerSkipTurn) {
			trainerSkipTurn=false;
			return;
		}
		if (attack.getAccuracy()==0) {} // 100% accurate moves have an accuracy STAT of 0.0
		// Applying accuracy %. If the attack misses, the damage is not applied. 
		else if (accuracy>attack.getAccuracy()) {
			System.out.println("Missed!");
			return;
		}
		hit = true;
		// If the move does not damage, we can skip the rest
		// However, this is checked after seeing if the move hit
		if (attack.getAtkPower()==0) { 
			return;
		}
		// If the other pokemon has 2 types
		
		// Physical attacks are for physical stats (aka attack and defence)
		if (attack.getCategory().equals("Physical")) {
			if (otherMon.getTypeList().size()==2) {
				int type1 = otherMon.getType1().getTypeNum();
				int type2 = otherMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				}
				catch (IOException e) {

				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			}
			else {
				int type1 = otherMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getAttack()/otherMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				}
				catch (IOException e) {
					System.out.println("EXCEPTION");
				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			}
		}
		
		// Special attacks are for special stats (special attack and special defence) 
		else if (attack.getCategory().equals("Special")) {
			if (otherMon.getTypeList().size()==2) {
				int type1 = otherMon.getType1().getTypeNum();
				int type2 = otherMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getSpAtk()/otherMon.getSpDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				}
				catch (IOException e) {

				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			}
			else {
				int type1 = otherMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2*(playerMon.getLevel()+2)*attack.getAtkPower()*(playerMon.getSpAtk()/otherMon.getSpDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				}
				catch (IOException e) {
					System.out.println("EXCEPTION");
				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			}
		}
		
		
		
		
//		updateStats();
	}
	
	// Applies the opponents attack 
	public void applyOtherAttack(Move attack, double stab) {
		System.out.println(otherMon.getName() + " used " + attack.getName() + "!");
		int newHp = 0;
		double accuracy = Math.random();
		if (otherSkipTurn) {
			otherSkipTurn = false;
			return;
		}
		// Applying accuracy %. If the attack misses, the damage is not applied. 
		if (attack.getAccuracy()==0) {}
		else if (accuracy>attack.getAccuracy()) {
			System.out.println("Missed!");
			return;
		}
		hit = true;
		if (attack.getAtkPower()==0) {
			return;
		}
		// If the other pokemon has 2 types
		if (playerMon.getTypeList().size()==2) {
			int type1 = playerMon.getType1().getTypeNum();
			int type2 = playerMon.getType2().getTypeNum();
			try {
				newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getAttack()/playerMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
			}
			catch (IOException e) {
				
			}
			playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
		}
		else {
			int type1 = playerMon.getType1().getTypeNum();
			try {
				newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getAttack()/playerMon.getDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
			}
			catch (IOException e) {
				
			}
			playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
		}
	}
	
	// The chooseNewPokemon() method is used to select a new Pokemon, either if the current one dies or if you choose to do so.
	// It takes in no parameters
	// It returns nothing - instead, a new battle is started 
	public void chooseNewPokemon() {
		int index = -1; 
		boolean valid = false;
		Scanner s = new Scanner (System.in);
		System.out.println("Choose your Pokemon: ");
		if (playerMonHp==0) {
			for (int i = 0; i<player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint()==false) {
					System.out.println(i + ") " + player.getPokemonList()[i].getName());
					valid = true;
				}
				else if (i==2 && !valid) {
					System.out.println("You are out of usable Pokemon! You give out a badge...");
					battleContinue = false;
					return;
				}
			}
			while (index==-1) {
				index = Integer.parseInt(s.nextLine());
			}
			new Battle(other, player, otherMon, index, trainerSkipTurn);
		}
		// If you decide to switch Pokemon even though your current one is still good 
		else {
			index = -1;
			for (int i = 0; i<player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint()==false) {
					System.out.println(i + ") " + player.getPokemonList()[i].getName());
					valid = true;
				}
				// hmmm... 
				else if (i==2 && !valid) {
					System.out.println("This is your only Pokemon left!"); 
					new Battle(other, player, otherMon, index, trainerSkipTurn);
				}
			}
			System.out.println("Choose a Pokemon");
			valid = false;
			while (!valid) {
				while (index==-1) {
					index = Integer.parseInt(s.nextLine());
				}
				if (player.getPokemonList()[index].equals(playerMon)) {
					System.out.println("That Pokemon is already in battle!");
					new Battle(other, player, otherMon, index, trainerSkipTurn);
				}
				else {
					valid = true;
				}
			}
			// Call the constructor again, except the switchPokemon boolean will be true.
			// This ensures that switching Pokemon counts as a turn, and the new Pokemon will get hit!
			new Battle (other, player, otherMon, index, true);
		}
	}
	
	
	
	// Statuses are applied at the end of every turn 
	public void applyStatus() {
		// Trainer Pokemon Status
		if (playerMon.getStatus()==0) {
			return; // No status 
		}
		// Poison - Takes away 1/8th of the Pokemon's HP
		else if (playerMon.getStatus()==1) {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + playerMon.getHp()/8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (playerMon.getStatus()==4) {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + (playerMon.getHp()/16));
			playerMon.setDeltaAttack(playerMonAttack/2);
		}
		// Paralyze
		// This will cut the Pokemon's speed to 25% 
		// There is also a 25% chance for the Pokemon to be fully paralyzed, rendering it unable to move for a turn
		else if (playerMon.getStatus()==2) {
			playerMon.setDeltaSpeed(playerMon.getDeltaSpeed()/4);
			int random = (int) (Math.random()*4) + 1;
			if (random==1) {
				trainerSkipTurn = true;
			}
			else {
				trainerSkipTurn = false;
			}
		}
		// Sleep
		else if (playerMon.getStatus()==3) {
			// Guaranteed turn of sleep 
			if (trainerSleepCounter==0) {
				trainerSkipTurn = true;
				trainerSleepCounter++;
			}
			// Forced to wake up to prevent infinite sleep 
			else if (trainerSleepCounter==3){
				trainerSleepCounter=0;
				playerMon.setStatus(0);
				trainerSkipTurn = false;
			}
			else { // 50% chance to wake up 
				int random = (int) (Math.random()*2) + 1;
				if (random==1) {
					trainerSleepCounter=0;
					playerMon.setStatus(0);
					trainerSkipTurn = false;
				}
				else {
					trainerSkipTurn = true;
					trainerSleepCounter++;
				}
			}
		}
		// Badly Poisoned (through the move Toxic)
		// It will do damage N*1/16 of max HP, where N is a counter that increases per turn. 
		else {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + trainerToxicCounter*playerMon.getHp()/16);
			trainerToxicCounter++; 
		}
		
		// Opponent Pokemon Status
		if (otherMon.getStatus()==0) {
			return; // No status 
		}
		// Poison 
		else if (otherMon.getStatus()==1) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + otherMon.getHp()/8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (otherMon.getStatus()==4) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + (otherMon.getHp()/16));
			otherMon.setDeltaAttack(otherMonAttack/2);
		}
		// Paralyze
		else if (otherMon.getStatus()==2) {
			otherMon.setDeltaSpeed(otherMon.getDeltaSpeed()/4);
			int random = (int) (Math.random()*4) + 1;
			if (random==1) {
				otherSkipTurn = true;
			}
			else {
				otherSkipTurn = false;
			}
		}
		// Sleep
		else if (otherMon.getStatus()==3) {
			// Guaranteed turn of sleep 
			if (otherSleepCounter==0) {
				otherSkipTurn = true;
				otherSleepCounter++;
			}
			// Forced to wake up to prevent infinite sleep 
			else if (otherSleepCounter==3){
				otherSleepCounter=0;
				otherMon.setStatus(0);
				otherSkipTurn = false;
			}
			else {
				int random = (int) (Math.random()*2) + 1;
				if (random==1) {
					otherSleepCounter=0;
					otherMon.setStatus(0);
					otherSkipTurn = false;
				}
				else {
					otherSkipTurn = true;
					otherSleepCounter++; 
				}
			}
		}
		// Badly Poisoned (through the move Toxic)
		else {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + otherToxicCounter*otherMon.getHp()/16);
			otherToxicCounter++; 
		}
	}
	
	// The calculateStab method is used to calculate STAB (same type attack bonus)
	// Although this is already found in the attack() method, I created this other one for the weird case
	// that the user switches in a Pokemon.
	// It takes in the parameters of the attacking Pokemon and the Move
	// It returns a double 
	public double calculateStab(Pokemon mon, Move attack) {
		double stab = 1;
		for (int i = 0; i<mon.getTypeList().size(); i++) {
			if (mon.getTypeList().get(i).equals(attack.getType())) {
				stab = 1.5; 
				break;
			}
		}
		return stab;
	}
	public boolean getBattleContinue() {
		return battleContinue;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Trainer getOther() {
		return other;
	}

	public void setOther(Trainer other) {
		this.other = other;
	}

	public Pokemon getPlayerMon() {
		return playerMon;
	}

	public void setPlayerMon(Pokemon playerMon) {
		this.playerMon = playerMon;
	}

	public Pokemon getOtherMon() {
		return otherMon;
	}

	public void setOtherMon(Pokemon otherMon) {
		this.otherMon = otherMon;
	}
	public BufferedImage getPlayerMonSprite() {
		return playerMonSprite;
	}

	public BufferedImage getOtherMonSprite() {
		return otherMonSprite;
	}
	
	public int[][] getOptionsArrowPositions() {
		return optionsArrowPositions;
	}

	public void setOptionsArrowPositions(int[][] optionsArrowPositions) {
		this.optionsArrowPositions = optionsArrowPositions;
	}

	public int getOptionsArrowX() {
		return optionsArrowX;
	}

	public void setOptionsArrowX(int optionsArrowX) {
		this.optionsArrowX = optionsArrowX;
	}

	public int getOptionsArrowY() {
		return optionsArrowY;
	}

	public void setOptionsArrowY(int optionsArrowY) {
		this.optionsArrowY = optionsArrowY;
	}

	public int[][] getAttackArrowPositions() {
		return attackArrowPositions;
	}

	public void setAttackArrowPositions(int[][] attackArrowPositions) {
		this.attackArrowPositions = attackArrowPositions;
	}

	public int getAttackArrowX() {
		return attackArrowX;
	}

	public void setAttackArrowX(int attackArrowX) {
		this.attackArrowX = attackArrowX;
	}

	public int getAttackArrowY() {
		return attackArrowY;
	}

	public void setAttackArrowY(int attackArrowY) {
		this.attackArrowY = attackArrowY;
	}
	public int getOptionsArrowIdx() {
		return optionsArrowIdx;
	}

	public void setOptionsArrowIdx(int optionsArrowIdx) {
		this.optionsArrowIdx = optionsArrowIdx;
	}

	public int getAttackArrowIdx() {
		return attackArrowIdx;
	}

	public void setAttackArrowIdx(int attackArrowIdx) {
		this.attackArrowIdx = attackArrowIdx;
	}
	
}
