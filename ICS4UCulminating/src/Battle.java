

// This class is used to implement and manage battles. It will determine who goes first, what attack is used,
// how much damage is done, etc. It will also coordinate part of the graphical portion of battles. 

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

	private boolean attack = false;
	private boolean switchPokemon = false; // Boolean that sees if the user is switching Pokemon
	private boolean battleContinue = true; // Boolean to see if the battle is going to continue
	private boolean quickAttack = false;
	private boolean hit = false; // To check if the attack hit the other pokemon
	private boolean roundEnd = true; // Checks if the attack round has ended

	private boolean playerAttacking = false;
	private Move playerCurrentMove = null;
	private boolean playerSkipTurn = false;// Boolean to determine if the trainers turn is skipped
	private boolean playerAttacked = false;
	private boolean playerMiss = false;
	private String playerAttackEffect = null;

	private boolean otherAttacking = false;
	private Move otherCurrentMove = null;
	private boolean otherSkipTurn = false; // Same as last one, except its for opponent
	private boolean otherAttacked = false;
	private boolean otherMiss = false;
	private String otherAttackEffect = null;

	private String currentEffect; // current effect text
	private boolean displayingEffect = false; // currently displaying effect
	private boolean displayedBeforePlayerEffect = false; // previously displayed effect
	private boolean displayedBeforeOtherEffect = false; // previously displayed effect
	private boolean displayedAfterPlayerEffect = false; // next displayed effect
	private boolean displayedAfterOtherEffect = false; // next displayed effect
	// Faint - -1
	// Poison - 1
	// Paralyzed - 2
	// Sleep - 3
	// Burn - 4
	// Toxic Poison - 5

	// These playerMon / otherMon stat counters are used to the number of stat
	// raising/lowering.
	// The maximum number of times a stat can be raised is 6 stages, and there are
	// moves that can raise your stats
	// 1 to 2 stages. These are used to ensure that the number of stat stage
	// raising/lowering does not exceed 6 or -6.
	private int playerMonAtkCount = 0;
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
	private int playerSleepCounter = 0;
	private int playerToxicCounter = 1;
	private int otherSleepCounter = 0;
	private int otherToxicCounter = 1;

	// Constructor
	public Battle(Player player, Trainer other) {
		this.player = player;
		this.other = other;

		playerMon = player.getPokemonList()[0];
		otherMon = other.getPokemonList()[0];

		updateStats();
	}

	// Overloaded constructor for switching in Pokemon
	// It will get the index of the Pokemon that wants to be switched in
	public Battle(Trainer trainer, Player player, Pokemon otherMon, int index, boolean switchPokemon) {
		this.switchPokemon = switchPokemon;
		this.player = player;
		this.other = trainer;
		this.otherMon = otherMon;

		playerMon = player.getPokemonList()[index];

		System.out.println("NEW POKEMON HP: " + playerMon.getCurrentHp());
	}

	// Overloaded constructor for switching the enemy Pokemon
	public Battle(Player player, Trainer trainer, Pokemon playerMon, int index) {
		this.player = player;
		this.other = trainer;
		this.playerMon = playerMon; // Shouldn't affect anything...
		otherMon = other.getPokemonList()[index];
//		battleStart();
	}

//	// BATTLE START
//	public void battleStart() {
//		while (battleContinue) {
//			coordinateBattle();
//		}
//	}

	// The trainerChooseAttack method is used to determine what attack the user
	// chooses
	// It takes in no parameter
	// It returns nothing
	public Move trainerChooseAttack() {
		int index = 0;
		int tempCount = 0;
		Scanner s = new Scanner(System.in);
		System.out.println("Choose an attack: ");
		for (int i = 0; i < playerMon.getMoves().length; i++) {
			try {
				System.out.println((i + 1) + ") " + playerMon.getMoves()[i].getName());
				tempCount = i + 1;
			} catch (NullPointerException e) {
				break;
			}
		}
		System.out.println((tempCount + 1) + ") Change Pokemon");
		while (index == 0) {
			index = Integer.parseInt(s.nextLine());
			if (playerMon.getMoves()[index - 1].getCurrentPP() == 0) {
				index = 0;
			}
		}
		if (index==(tempCount+1)) {
			chooseNewPokemon();
			battleContinue = false;
			return null;
		} else {
			playerMon.getMoves()[index - 1].setPP(playerMon.getMoves()[index - 1].getCurrentPP() - 1);
			return playerMon.getMoves()[index - 1];
		}
	}

	// The opponentChooseAttack method randomly chooses a move for the opponent to
	// use
	// It takes in no parameters
	// It returns nothing
	public Move opponentChooseAttack() {
		Move tempMove = otherMon.getMoves()[0];
		boolean valid = false;
		int random = (int) (Math.random() * 4) + 1;
		while (!valid) {
			try {
				tempMove = otherMon.getMoves()[random - 1];
				String temp = tempMove.getName(); // Prevents other type to be null (I'm pretty sure as it throws
													// exception...)
				valid = true;
			}
			catch (NullPointerException e) {
				random--; 
			}
		}
		return tempMove;
	}

	// The Battle method is used to the battle. It will check for speed, priority
	// moves, etc.
	// This is why it is important to know what both Pokemon will do first, and then
	// finalizing the order
	// This method takes in no parameters
	// It also returns nothing
	public void coordinateBattle() {

		if (!battleContinue) return;
		
		// Ensures that switching Pokemon in means a free turn for the enemy!
		if (switchPokemon) {
			switchPokemon = false;
			Move otherMove = opponentChooseAttack();
			double stab = calculateStab (otherMon, otherMove);
			attack(otherMove, otherMon, playerMon);
			updateStats();
		}
		
		else {
			System.out.println("\nYOU\t" +  playerMon.getName() + " HP: " + playerMon.getCurrentHp() + "\t Level: " + playerMon.getLevel());
			System.out.println("--------------------");
			System.out.println("THEM\t" +  otherMon.getName() + " HP: " + otherMon.getCurrentHp() + "\t Level: " + otherMon.getLevel());
			System.out.println("\n");
			applyStatus(); // Apply status first to determine if moves are going to be skipped 
			
			Move trainerMove = trainerChooseAttack();
			Move otherMove = opponentChooseAttack();

			// If NullPointerException occurs, it means that Pokemon switching happened (on
			// purpose).
			// It should not crash, so an exception will be thrown
			try {
				String temp = trainerMove.getName();
			} catch (NullPointerException e) {
				battleContinue = false;
				return;
			}
//			turnCheck2(trainerMove, otherMove);
		}
	}

	// The turnCheck method is used to see whos turn it is
	// It takes in no parameters
	// Returns true for player turn
	// Returns false for other turn
	public boolean turnCheck() {
		// If both Pokemon choose to use quick attack, which is a priority move, then
		// the one with the higher speed stat will go first
		if (playerCurrentMove.getName().equals("Quick Attack") && otherCurrentMove.getName().equals("Quick Attack")) {
			if (playerMon.getCurrentSpeed() >= otherMon.getCurrentSpeed()) {
				return true;
			}
			return false;
		}
		// If only the user chooses quick attack, they are guaranteed to go first
		else if (playerCurrentMove.getName().equals("Quick Attack")) {
			return true;
		}
		// If the only the opponent uses quick attack, they are guaranteed to go first
		else if (otherCurrentMove.getName().equals("Quick Attack")) {
			return false;

			// Speed Check
		} else {
			if (playerMon.getCurrentSpeed() >= otherMon.getCurrentSpeed()) {
				return true;
			}
			return false;
		}
	}
	

//	public void turnCheck2(Move trainerMove, Move otherMove) {
//		// If both Pokemon choose to use quick attack, which is a priority move, then
//		// the one with the higher speed stat will go first
//		if (trainerMove.getName().equals("Quick Attack") && otherMove.getName().equals("Quick Attack")) {
//			if (playerMon.getCurrentSpeed() >= otherMon.getCurrentSpeed()) {
//				if (!playerSkipTurn) {
//					// Checking stats to ensure that fainted Pokemon cannot go
//					attack(trainerMove, playerMon, otherMon);
//					if (!battleContinue)
//						return;
//					else if (otherMon.getCurrentHp() <= 0) {
//						checkBattle();
//						return;
//					}
//				} else {
//					if (playerMon.getStatus() == 2) {
//						System.out.println(playerMon.getName() + " was fully paralyzed!");
//					} else if (playerMon.getStatus() == 3) {
//						System.out.println(playerMon.getName() + " is asleep!");
//					}
//				}
//				if (!otherSkipTurn) {
//					attack(otherMove, otherMon, playerMon);
//					if (!battleContinue)
//						return;
//					else if (playerMon.getCurrentHp() <= 0) {
//						chooseNewPokemon();
//					}
//				} else {
//					if (otherMon.getStatus() == 2) {
//						System.out.println(otherMon.getName() + " was fully paralyzed!");
//					} else if (otherMon.getStatus() == 3) {
//						System.out.println(otherMon.getName() + " is asleep!");
//					} else if (otherMon.getStatus() == 0) {
//						if (!battleContinue) {
//							return;
//						}
//					}
//				}
//			} else {
//				if (!otherSkipTurn) {
//					attack(otherMove, otherMon, playerMon);
//					if (!battleContinue)
//						return;
//					else if (playerMon.getCurrentHp() <= 0) {
//						chooseNewPokemon();
//					}
//				} else {
//					if (otherMon.getStatus() == 2) {
//						System.out.println(otherMon.getName() + " was fully paralyzed!");
//					} else if (otherMon.getStatus() == 3) {
//						System.out.println(otherMon.getName() + " is asleep!");
//					}
//				}
//				if (!playerSkipTurn) {
//					attack(trainerMove, playerMon, otherMon);
//					if (!battleContinue)
//						return;
//					else if (otherMon.getCurrentHp() <= 0) {
//						checkBattle();
//						return;
//					}
//				} else {
//					if (playerMon.getStatus() == 2) {
//						System.out.println(playerMon.getName() + " was fully paralyzed!");
//					} else if (playerMon.getStatus() == 3) {
//						System.out.println(playerMon.getName() + " is asleep!");
//					}
//				}
//			}
//		}
//		// If only the user chooses quick attack, they are guaranteed to go first
//		else if (trainerMove.getName().equals("Quick Attack")) {
//			if (!playerSkipTurn) {
//				attack(trainerMove, playerMon, otherMon);
//				if (!battleContinue)
//					return;
//				else if (otherMon.getCurrentHp() <= 0) {
//					checkBattle();
//					return;
//				}
//			} else {
//				if (playerMon.getStatus() == 2) {
//					System.out.println(playerMon.getName() + " was fully paralyzed!");
//				} else if (playerMon.getStatus() == 3) {
//					System.out.println(playerMon.getName() + " is asleep!");
//				}
//			}
//			if (!otherSkipTurn) {
//				attack(otherMove, otherMon, playerMon);
//				if (!battleContinue)
//					return;
//				else if (playerMon.getCurrentHp() <= 0) {
//					chooseNewPokemon();
//				}
//			} else {
//				if (otherMon.getStatus() == 2) {
//					System.out.println(otherMon.getName() + " was fully paralyzed!");
//				} else if (otherMon.getStatus() == 3) {
//					System.out.println(otherMon.getName() + " is asleep!");
//				}
//			}
//		}
//		// If the only the opponent uses quick attack, they are guaranteed to go first
//		else if (otherMove.getName().equals("Quick Attack")) {
//			if (!otherSkipTurn) {
//				attack(otherMove, otherMon, playerMon);
//				if (!battleContinue)
//					return;
//				else if (playerMon.getCurrentHp() <= 0) {
//					chooseNewPokemon();
//				}
//			} else {
//				if (otherMon.getStatus() == 2) {
//					System.out.println(otherMon.getName() + " was fully paralyzed!");
//				} else if (otherMon.getStatus() == 3) {
//					System.out.println(otherMon.getName() + " is asleep!");
//				}
//			}
//			if (!playerSkipTurn) {
//				attack(trainerMove, playerMon, otherMon);
//				if (!battleContinue)
//					return;
//				else if (otherMon.getCurrentHp() <= 0) {
//					checkBattle();
//					return;
//				}
//			} else {
//				if (playerMon.getStatus() == 2) {
//					System.out.println(playerMon.getName() + " was fully paralyzed!");
//				} else if (playerMon.getStatus() == 3) {
//					System.out.println(playerMon.getName() + " is asleep!");
//				}
//			}
//		} else {
//			if (playerMon.getCurrentSpeed() >= otherMon.getCurrentSpeed()) {
//				if (!playerSkipTurn) {
//					attack(trainerMove, playerMon, otherMon);
//					if (!battleContinue)
//						return;
//					else if (otherMon.getCurrentHp() <= 0) {
//						checkBattle();
//						return;
//					}
//				} else {
//					if (playerMon.getStatus() == 2) {
//						System.out.println(playerMon.getName() + " was fully paralyzed!");
//					} else if (playerMon.getStatus() == 3) {
//						System.out.println(playerMon.getName() + " is asleep!");
//					}
//				}
//				if (!otherSkipTurn) {
//					attack(otherMove, otherMon, playerMon);
//					if (!battleContinue)
//						return;
//					else if (playerMon.getCurrentHp() <= 0) {
//						chooseNewPokemon();
//					}
//				} else {
//					if (otherMon.getStatus() == 2) {
//						System.out.println(otherMon.getName() + " was fully paralyzed!");
//					} else if (otherMon.getStatus() == 3) {
//						System.out.println(otherMon.getName() + " is asleep!");
//					}
//				}
//			} else {
//				if (!otherSkipTurn) {
//					attack(otherMove, otherMon, playerMon);
//					if (!battleContinue)
//						return;
//					else if (playerMon.getCurrentHp() <= 0) {
//						chooseNewPokemon();
//					}
//				} else {
//					if (otherMon.getStatus() == 2) {
//						System.out.println(otherMon.getName() + " was fully paralyzed!");
//					} else if (otherMon.getStatus() == 3) {
//						System.out.println(otherMon.getName() + " is asleep!");
//					}
//				}
//				if (!playerSkipTurn) {
//					attack(trainerMove, playerMon, otherMon);
//					if (!battleContinue)
//						return;
//					else if (otherMon.getCurrentHp() <= 0) {
//						checkBattle();
//						return;
//					}
//				} else {
//					if (playerMon.getStatus() == 2) {
//						System.out.println(playerMon.getName() + " was fully paralyzed!");
//					} else if (playerMon.getStatus() == 3) {
//						System.out.println(playerMon.getName() + " is asleep!");
//					}
//				}
//			}
//		}
//	}

		
	// The attack method is used to determine the attacks of both the player and opponent 
	// It takes in the parameter of the attacking move, the attacking Pokemon, and the defending Pokemon
	// It returns nothing. 
	 public void attack (Move attack, Pokemon attackMon, Pokemon defendMon) {
		boolean keepGoing = true;
		double stab = 1; // STAB stands for 'Same Type Attack Bonus'. If the Pokemon attacks with a move that has the same type as itself, it gets this bonus
		int random; // Used for the random status effects 
		hit = false;
		// Cycling through arraylist of moves to see if there is STAB
		for (int i = 0; i < attackMon.getTypeList().size(); i++) {
			if (attackMon.getTypeList().get(i).equals(attack.getType())) {
				stab = 1.5;
				break;
			}
		}
		if (attack.getCategory().equals("Physical")) {
			// HARD CODING EACH OF THE 'SPECIAL' CASES
			if (attack.getName().equals("Take Down") || attack.getName().equals("Double-Edge")) {
				// Take Down and Double-Edge does 25% of the damage done to the opponent as
				// recoil to itself.
				// To keep track of this, I am creating a tracker to see how much damage is
				// done, then multiplying that by 0.25
				int beforeAttack = defendMon.getDeltaHp();
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						playerMon.setDeltaHp(
								playerMon.getDeltaHp() + (int) Math.round((afterAttack - beforeAttack) * 0.25));
						playerAttackEffect = playerMon.getName() + " is damaged by recoil!";
						System.out.println("\n" + playerMon.getName() + " is damaged by recoil!");
					} else {
						int afterAttack = playerMon.getDeltaHp();
						otherMon.setDeltaHp(
								otherMon.getDeltaHp() + (int) Math.round((afterAttack - beforeAttack) * 0.25));
						otherAttackEffect = otherMon.getName() + " is damaged by recoil!";
						System.out.println("\n" + otherMon.getName() + " is damaged by recoil!");
					}

				}
				hit = false;
			}
			// These next moves are only possible by the opposing Pokemon, so there is no need to check if it is from the player 
			else if (attack.getName().equals("Fire Punch")) {
				applyOtherAttack(attack, stab);
				if (hit && playerMon.getStatus() == 0) {
					for (int i = 0; i < playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType("Fire")))
							keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random() * 10) + 1;
						if (random == 1) {
							playerMon.setStatus(4);
							otherAttackEffect = playerMon.getName() + " was burned!";
							System.out.println(playerMon.getName() + " was burned!");
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Thunder Punch")) {
				applyOtherAttack(attack, stab); // ELECTRIC
				if (hit && playerMon.getStatus() == 0) {
					for (int i = 0; i < playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType("Electric")))
							keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random() * 10) + 1;
						if (random == 1) {
							playerMon.setStatus(2);
							System.out.println(playerMon.getName() + " was paralyzed!");
							otherAttackEffect = playerMon.getName() + " was paralyzed!";
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Poison Sting")) {
				applyOtherAttack(attack, stab);
				if (hit && playerMon.getStatus() == 0) {
					for (int i = 0; i < playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType("Poison")))
							keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random() * 10) + 1;
						// 30% chance to poison the target
						if (random <= 3) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
							otherAttackEffect = playerMon.getName() + " was poisoned!";
						}
					}
				}
				hit = false;
			}
			// Will immediately make the opponent faint
			else if (attack.getName().equals("Self-Destruct")) {
				applyOtherAttack(attack, stab);
				if (hit) {
					otherMon.setDeltaHp(otherMon.getCurrentHp());
				}
				hit = false;
			} else if (attack.getName().equals("Poison Fang")) {
				applyOtherAttack(attack, stab);
				for (int i = 0; i < playerMon.getTypeList().size(); i++) {
					if (playerMon.getTypeList().get(i).equals(new PokeType("Poison")))
						keepGoing = false;
				}
				if (keepGoing) {
					random = (int) (Math.random() * 10) + 1;
					// 30% chance to poison the target
					if (random <= 3) {
						playerMon.setStatus(1);
						System.out.println(playerMon.getName() + " was poisoned!");
						otherAttackEffect = playerMon.getName() + " was poisoned!";
					}
				}
			} else {
				applyAttackChecker(attackMon, attack, stab);
				hit = false;
			}
		} else if (attack.getCategory().equals("Special")) {
			if (attack.getName().equals("Acid")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMonDefCount > -6) {
							random = (int) (Math.random() * (3)) + 1;
							// 33% chance of lowering defense by 1 stage
							if (random == 1) {
								otherMonDefCount--;
								otherMon.setDeltaDef(
										otherMon.getDeltaDef() + (int) Math.floor(otherMon.getDeltaDef() / 6));
							}
						}
					} else if (attackMon.equals(otherMon)) {
						if (playerMonDefCount > -6) {
							random = (int) (Math.random() * (3)) + 1;
							// 33% chance of lowering defense by 1 stage
							if (random == 1) {
								playerMonDefCount--;
								playerMon.setDeltaDef(
										playerMon.getDeltaDef() + (int) Math.floor(playerMon.getDeltaDef() / 6));
							}
						}
					}
					hit = false;
				}
			} else if (attack.getName().equals("Ember") || attack.getName().equals("Flamethrower")
					|| attack.getName().equals("Fire Blast") || attack.getName().equals("Heat Wave")) {
				applyAttackChecker(attackMon, attack, stab);
				if (attackMon.equals(playerMon) && hit) {
					if (otherMon.getStatus() == 0) {
						for (int i = 0; i < otherMon.getTypeList().size(); i++) {
							if (otherMon.getTypeList().get(i).equals(new PokeType("Fire")))
								keepGoing = false;
						}
						if (keepGoing) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random() * (10)) + 1;
							if (random == 1) {
								otherMon.setStatus(4);
								System.out.println(otherMon.getName() + " was burned!");
								playerAttackEffect = otherMon.getName() + " was burned!";
							}
						}
					}
				} else if (attackMon.equals(otherMon)) {
					if (playerMon.getStatus() == 0) {
						for (int i = 0; i < playerMon.getTypeList().size(); i++) {
							if (playerMon.getTypeList().get(i).equals(new PokeType("Fire")))
								keepGoing = false;
						}
						if (keepGoing) {
							// 10% chance to burn the enemy, and fire Pokemon cannot get burned
							random = (int) (Math.random() * (10)) + 1;
							if (random == 1) {
								playerMon.setStatus(4);
								System.out.println(playerMon.getName() + " was burned!");
								otherAttackEffect = playerMon.getName() + " was burned!";
							}
						}
					}
				}
				hit = false;

			} else if (attack.getName().equals("Bubble Beam") || attack.getName().equals("Bubble")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						// 33% chance to drop speed
						random = (int) (Math.random() * (3)) + 1;
						if (otherMonSpeedCount > -6 && random == 1) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(
									otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getBaseSpeed() / 6));
							playerAttackEffect = otherMon.getName() + "'s speed fell!";
							System.out.println("\n" + otherMon.getName() + "'s speed fell!");
						}
					} else {
						random = (int) (Math.random() * (3)) + 1;
						if (playerMonSpeedCount > -6 && random == 1) {
							playerMonSpeedCount--;
							playerMon.setDeltaSpeed(
									playerMon.getDeltaSpeed() + (int) Math.floor(playerMon.getBaseSpeed() / 6));
							otherAttackEffect = playerMon.getName() + "'s speed fell!";
							System.out.println("\n" + playerMon.getName() + "'s speed fell!");
						}
					}

				}
				hit = false;
			}

			else if (attack.getName().equals("Absorb") || attack.getName().equals("Mega Drain")
					|| attack.getName().equals("Giga Drain")) {
				applyAttackChecker(attackMon, attack, stab);
				// In addition to hitting the opponent, it will also absorb HP based on half of
				// the damage dealt.
				int beforeAttack = defendMon.getDeltaHp();
				if (hit) {
					if (attackMon.equals(playerMon)) {
						int afterAttack = otherMon.getDeltaHp();
						playerMon.setDeltaHp(playerMon.getDeltaHp() - (int) (0.5 * (afterAttack - beforeAttack)));
						System.out.println("\n" + playerMon.getName() + " recovered some HP!");
						playerAttackEffect = playerMon.getName() + " recovered some HP!";
					} else {
						int afterAttack = playerMon.getDeltaHp();
						otherMon.setDeltaHp(otherMon.getDeltaHp() - (int) (0.5 * (afterAttack - beforeAttack)));
						System.out.println("\n" + otherMon.getName() + " recovered some HP!");
						otherAttackEffect = otherMon.getName() + " recovered some HP!";
					}
				}
				hit = false;
			}

			else if (attack.getName().equals("Mud Shot")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMonSpeedCount > -6) {
							otherMonSpeedCount--;
							otherMon.setDeltaSpeed(
									otherMon.getDeltaSpeed() + (int) Math.floor(otherMon.getBaseSpeed() / 6));
							System.out.println("\n" + otherMon.getName() + "'s speed fell!");
							playerAttackEffect = otherMon.getName() + "'s speed fell!";
						}
					} else {
						if (playerMonSpeedCount > -6) {
							playerMonSpeedCount--;
							playerMon.setDeltaSpeed(
									playerMon.getDeltaSpeed() + (int) Math.floor(playerMon.getBaseSpeed() / 6));
							System.out.println("\n" + playerMon.getName() + "'s speed fell!");
							otherAttackEffect = playerMon.getName() + "'s speed fell!";
						}
					}
				}
				hit = false;
			}

			// Only moves that the opposing Pokemon can have
			
			else if (attack.getName().equals("Thunder Shock") || attack.getName().equals("Thunderbolt")) {
				applyOtherAttack(attack, stab);
				if (hit) {
					for (int i = 0; i < playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType("Electric")))
							keepGoing = false;
					}
					if (keepGoing) {
						// 10% chance of paralysis
						random = (int) (Math.random() * (10)) + 1;
						if (playerMon.getStatus() == 0 && random == 1) {
							playerMon.setStatus(2);
							System.out.println(playerMon.getName() + " was paralyzed!");
							otherAttackEffect = playerMon.getName() + " was paralyzed!";
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Sludge")) {
				applyOtherAttack(attack, stab);
				// 30% chance to poison
				if (hit && playerMon.getStatus() == 0) {
					for (int i = 0; i < playerMon.getTypeList().size(); i++) {
						if (playerMon.getTypeList().get(i).equals(new PokeType("Poison")))
							keepGoing = false;
					}
					if (keepGoing) {
						random = (int) (Math.random() * 10) + 1;
						if (random <= 3) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
							otherAttackEffect = playerMon.getName() + " was poisoned!";
						}
					}
				}
				hit = false;
			}
			else {
				applyAttackChecker(attackMon, attack, stab);
				hit = false;
			}
			updateStats();
		}
		// If the user chooses status moves 
		// More hard coding, everything is pretty self explanatory if you read. 
		else if (attack.getCategory().equals("Status")) {
			// Swords dance will raise the attack stat by 2 stages. If the user is already
			// at +5 stage, it will only add 1 extra one.
			if (attack.getName().equals("Swords Dance")) {
				System.out.println(attackMon.getName() + " used Swords Dance!");
				System.out.println(attackMon.getName() + "'s attack rose sharply!");
				if (attackMon.equals(playerMon)) {
					if (playerMonAtkCount == 5) {
						playerMonAtkCount++;
						playerMon.setDeltaAttack(
								playerMon.getDeltaAttack() + (int) Math.floor(playerMon.getBaseAttack() / 6));
					} else {
						playerMonAtkCount += 2;
						playerMon.setDeltaAttack(
								playerMon.getDeltaAttack() + (int) (2 * Math.floor(playerMon.getBaseAttack() / 6)));
					}
					playerAttackEffect = attackMon.getName() + "'s attack rose sharply!";
				} else {
					if (otherMonAtkCount == 5) {
						otherMonAtkCount++;
						otherMon.setDeltaAttack(
								otherMon.getDeltaAttack() + (int) Math.floor(otherMon.getBaseAttack() / 6));
					} else {
						otherMonAtkCount += 2;
						otherMon.setDeltaAttack(
								otherMon.getDeltaAttack() + (int) (2 * Math.floor(otherMon.getBaseAttack() / 6)));
					}
					otherAttackEffect = attackMon.getName() + "'s attack rose sharply!";
				}

			} else if (attack.getName().equals("Tail Whip") || attack.getName().equals("Leer")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					System.out.println(defendMon.getName() + "'s defense fell!");
					if (attackMon.equals(playerMon)) {
						if (otherMonDefCount > -6) {
							otherMonDefCount--;
							otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) Math.floor(otherMon.getBaseDef() / 6));
						}
						playerAttackEffect = defendMon.getName() + "'s defense fell!";
					} else {
						if (playerMonDefCount > -6) {
							playerMonDefCount--;
							playerMon.setDeltaDef(
									playerMon.getDeltaDef() + (int) Math.floor(playerMon.getBaseDef() / 6));
						}
						otherAttackEffect = defendMon.getName() + "'s defense fell!";
					}

				}
				hit = false;
			} else if (attack.getName().equals("Growl")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					System.out.println(defendMon.getName() + "'s attack fell!");
					if (attackMon.equals(playerMon)) {
						if (otherMonAtkCount > -6) {
							otherMonAtkCount--;
							otherMon.setDeltaAttack(
									otherMon.getDeltaAttack() + (int) Math.floor(otherMon.getBaseAttack() / 6));
						}
						playerAttackEffect = defendMon.getName() + "'s attack fell!";
					} else {
						if (playerMonAtkCount > -6) {
							playerMonAtkCount--;
							playerMon.setDeltaAttack(
									playerMon.getDeltaAttack() + (int) Math.floor(playerMon.getBaseAttack() / 6));
						}
						otherAttackEffect = defendMon.getName() + "'s attack fell!";
					}

				}
				hit = false;
			} else if (attack.getName().equals("Growth")) {
				otherAttackEffect = attackMon.getName() + "'s special attack rose!";
				System.out.println(attackMon.getName() + "'s special attack rose!");
				if (attackMon.equals(playerMon)) {
					if (playerMonSpAtkCount < 6) {
						playerMonSpAtkCount++;
						playerMon.setDeltaSpAtk(
								playerMon.getDeltaSpAtk() + (int) Math.floor(playerMon.getBaseSpAtk() / 6));
					}
				} else {
					if (otherMonSpAtkCount < 6) {
						otherMonSpAtkCount++;
						otherMon.setDeltaSpAtk(
								otherMon.getDeltaSpAtk() + (int) Math.floor(otherMon.getBaseSpAtk() / 6));
					}
				}
			} else if (attack.getName().equals("Poison Powder")) {
				applyAttackChecker(attackMon, attack, stab);
				// If a Pokemon already has a status applied, other statuses will not work!
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus() != 0) {
							playerAttackEffect = "It had no effect!";
							System.out.println("It had no effect!");
						} else {
							for (int i = 0; i < otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType("Poison")))
									keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(1);
								playerAttackEffect = "The opposing " + otherMon.getName() + " was poisoned!";
								System.out.println("The opposing " + otherMon.getName() + " was poisoned!");
							} else {
								playerAttackEffect = "It had no effect!";
								System.out.println("It had no effect!");
							}
						}
					} else {
						if (playerMon.getStatus() != 0) {
							otherAttackEffect = "It had no effect!";
							System.out.println("It had no effect!");
						}
						for (int i = 0; i < playerMon.getTypeList().size(); i++) {
							if (playerMon.getTypeList().get(i).equals(new PokeType("Poison")))
								keepGoing = false;
						}
						if (keepGoing) {
							playerMon.setStatus(1);
							System.out.println(playerMon.getName() + " was poisoned!");
							otherAttackEffect = playerMon.getName() + " was poisoned!";
						} else {
							System.out.println("It had no effect!");
							otherAttackEffect = "It had no effect!";
						}
					}
				}
				hit = false;

			} else if (attack.getName().equals("Stun Spore")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							playerAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType("Electric")))
									keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(2);
								System.out.println("The opposing " + otherMon.getName()
										+ " was paralyzed! It may be unable to move!");
								playerAttackEffect = otherMon.getName() + " was paralyzed!~It may be unable to move!";
							} else {
								System.out.println("It had no effect!");
								playerAttackEffect = "It had no effect!";
							}
						}
					} else {
						if (playerMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							otherAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType("Electric")))
									keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(2);
								System.out.println(playerMon.getName() + " was paralyzed! It may be unable to move!");
								otherAttackEffect = playerMon.getName() + " was paralyzed!~It may be unable to move!";
							} else {
								System.out.println("It had no effect!");
								otherAttackEffect = "It had no effect!";
							}
						}
					}
				}
				hit = false;

			} else if (attack.getName().equals("Sleep Powder")) {
				applyAttackChecker(attackMon, attack, stab);
				if (attackMon.equals(playerMon)) {
					if (otherMon.getStatus() != 0) {
						System.out.println("It had no effect!");
						playerAttackEffect = "It had no effect!";
					} else {
						if (hit) {
							otherMon.setStatus(3);
							System.out.println(otherMon.getName() + " went to sleep!");
							playerAttackEffect = otherMon.getName() + " went to sleep!";
						}
					}
				} else {
					if (playerMon.getStatus() != 0) {
						System.out.println("It had no effect!");
						otherAttackEffect = "It had no effect!";
					} else {
						if (hit) {
							playerMon.setStatus(3);
							System.out.println(playerMon.getName() + " went to sleep!");
							otherAttackEffect = playerMon.getName() + " went to sleep!";
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Thunder Wave")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							playerAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType("Electric")))
									keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(2);
								System.out.println("The opposing " + otherMon.getName()
										+ " was paralyzed! It may be unable to move!");
								playerAttackEffect = otherMon.getName() + " was paralyzed!~It may be unable to move!";
							} else {
								System.out.println("It had no effect!");
								playerAttackEffect = "It had no effect!";
							}
						}
					} else {
						if (playerMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							otherAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType("Electric")))
									keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(2);
								System.out.println(playerMon.getName() + " was paralyzed! It may be unable to move!");
								otherAttackEffect = playerMon.getName() + " was paralyzed!~It may be unable to move!";
							} else {
								System.out.println("It had no effect!");
								otherAttackEffect = "It had no effect!";
							}
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Toxic")) {
				applyAttackChecker(attackMon, attack, stab);
				if (hit) {
					if (attackMon.equals(playerMon)) {
						if (otherMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							playerAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < otherMon.getTypeList().size(); i++) {
								if (otherMon.getTypeList().get(i).equals(new PokeType("Poison")))
									keepGoing = false;
							}
							if (keepGoing) {
								otherMon.setStatus(5);
								System.out.println("The opposing " + otherMon.getName() + " was badly poisoned!");
								playerAttackEffect = otherMon.getName() + " was badly poisoned!";
							} else {
								System.out.println("It had no effect!");
								playerAttackEffect = "It had no effect!";
							}
						}
					} else {
						if (playerMon.getStatus() != 0) {
							System.out.println("It had no effect!");
							otherAttackEffect = "It had no effect!";
						} else {
							for (int i = 0; i < playerMon.getTypeList().size(); i++) {
								if (playerMon.getTypeList().get(i).equals(new PokeType("Poison")))
									keepGoing = false;
							}
							if (keepGoing) {
								playerMon.setStatus(5);
								System.out.println(playerMon.getName() + " was badly poisoned!");
								otherAttackEffect = playerMon.getName() + " was badly poisoned!";
							} else {
								System.out.println("It had no effect!");
								otherAttackEffect = "It had no effect!";
							}
						}
					}
				}
				hit = false;
			} else if (attack.getName().equals("Withdraw")) {
				
				System.out.println(attackMon.getName() + " used Withdraw!");
				System.out.println(attackMon.getName() + "'s defense rose!");
				if (attackMon.equals(playerMon)) {
					if (playerMonDefCount < 6) {
						playerMonDefCount++;
						playerMon.setDeltaDef(playerMon.getDeltaDef() + (int) (playerMon.getBaseDef() / 6));
						playerAttackEffect = attackMon.getName() + "'s defense rose!";
					}
				} else {
					System.out.println(otherMon.getName() + " used Withdraw!");
					if (otherMonDefCount < 6) {
						otherMonDefCount++;
						otherMon.setDeltaDef(otherMon.getDeltaDef() + (int) (otherMon.getBaseDef() / 6));
						otherAttackEffect = attackMon.getName() + "'s defense rose!";
					}
				}
			}
//			updateStats();
		}
	}

	// The updateStats method is used to update the stats of the Pokemon
	// It takes in no parameters
	// It returns nothing
	public void updateStats() {
		// checkBattle();
	}

	public void checkFaint(Pokemon mon) {
		if (mon.getCurrentHp() <= 0) {
			System.out.println("\n" + mon.getName() + " fainted!");
			mon.setDeltaHp(mon.getBaseHp());
			mon.setStatus(-1);
			mon.setFaint(true);
		}
	}
	
	// The checkBattle method is used to see if the CURRENT battle will continue (i.e. same Pokemon)
	// The battle will not continue if a Pokemon has fainted, or the user has switched out
	// It will return TRUE if the battle is good, FALSE if the battle must end. 
	public boolean checkBattle() {
		if (otherMon.getFaint()) {
			otherSkipTurn = true;
			for (int i = 0; i < other.getPokemonList().length; i++) {
				if (other.getPokemonList()[i].getFaint() == false) {
					return true;
				} 
				else if (i == other.getPokemonList().length - 1) {
					System.out.println("You won!");
					battleContinue = false;
					endBattle();
					return false;
					
				}
			}
		}
		if (playerMon.getFaint()) {
			playerSkipTurn = true;
			for (int i = 0; i < player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint() == false) {
					return true;
				} else if (i == player.getPokemonList().length - 1) {
					System.out.println("You are out of usable Pokemon! You give out a badge...");
					endBattle();
					battleContinue = false;
					return false;
				}
			}
		}
		return true;
	}

	public void otherChooseNewPokemon() {
		for (int i = 0; i < other.getPokemonList().length; i++) {
			if (other.getPokemonList()[i].getFaint() == false) {
				this.setOtherMon(other.getPokemonList()[i]);
				break;
			}
		}
		this.setOtherAttacking(false);
		this.setOtherCurrentMove(null);
		this.setOtherSkipTurn(false);
	}

	// The endBattle method is used when the battle is over
	// It is used to reset the delta variables and other variables back to their initial value
	// It takes in no parameters
	// It returns nothing 
	public void endBattle() {
		
		playerMon.setDeltaAttack(0);
		playerMon.setDeltaDef(0);
		playerMon.setDeltaSpAtk(0);
		playerMon.setDeltaSpDef(0);
		playerMon.setDeltaSpeed(0);
		// Resetting the PP of all moves. 
//		for (int i = 0; i<playerMon.getMoves().length; i++) {
//			try {
//				playerMon.getMoves()[i].resetTempPP();
//			}
//			catch (NullPointerException e) {
//				
//			}
//		}
		otherMon.setDeltaHp(0);
		otherMon.setDeltaAttack(0);
		otherMon.setDeltaDef(0);
		otherMon.setDeltaSpAtk(0);
		otherMon.setDeltaSpDef(0);
		otherMon.setDeltaSpeed(0);
		
		attack = false;
		switchPokemon = false; // Boolean that sees if the user is switching Pokemon
		battleContinue = true; // Boolean to see if the battle is going to continue
		quickAttack = false;
		hit = false; // To check if the attack hit the other pokemon
		roundEnd = true; // Checks if the attack round has ended

		playerAttacking = false;
		playerCurrentMove = null;
		playerSkipTurn = false;// Boolean to determine if the trainers turn is skipped
		playerAttacked = false;
		playerMiss = false;
		playerAttackEffect = null;

		otherAttacking = false;
		otherCurrentMove = null;
		otherSkipTurn = false; // Same as last one, except its for opponent
		otherAttacked = false;
		otherMiss = false;
		otherAttackEffect = null;

		displayingEffect = false; // currently displaying effect
		displayedBeforePlayerEffect = false; // previously displayed effect
		displayedBeforeOtherEffect = false; // previously displayed effect
		displayedAfterPlayerEffect = false; // next displayed effect
		displayedAfterOtherEffect = false; // next displayed effect
		
		if (Player.getLosses()==8) {
			Main.bgX = 0;
			Main.bgY = 0;
			Main.gameState = 14;
		}
		
		Main.bgX = Main.saveBGX;
		Main.bgY = Main.saveBGY;
		Main.gameState = 2;
	}
	
	// The applyAttackChecker method is used to see which Pokemon is attacking - either the player or the opponent
	// It takes in the parameters of the attacking Pokemon, the move, and the STAB value. 
	// It returns nothing - instead, it will call applyTrainerAttack or applyOtherAttack depending on who the attacker is 
	public void applyAttackChecker (Pokemon attackMon, Move attack, double stab) {
		if (attackMon.equals(playerMon)) applyTrainerAttack(attack, stab);
		else if (attackMon.equals(otherMon)) applyOtherAttack(attack, stab);
	}
	
	
	// The applyTrainerAttack method applies the attack of the player's Pokemon
	// It takes in the paramters of the attack and the value of STAB
	// It returns nothing, simply updating the stats of the opponent Pokemon
	public void applyTrainerAttack(Move attack, double stab) {
		System.out.println(playerMon.getName() + " used " + attack.getName() + "!");
		int newHp = 0;
		double accuracy = Math.random();
		// If the trainer's turn is skipped, nothing will happen
		if (playerSkipTurn) {
			playerSkipTurn = false;
			return;
		}
		if (attack.getAccuracy() == 0) {
		} // 100% accurate moves have an accuracy STAT of 0.0
			// Applying accuracy %. If the attack misses, the damage is not applied.
		else if (accuracy > attack.getAccuracy()) {
			playerMiss = true;
			System.out.println("Missed!");
			return;
		}
		hit = true;
		// If the move does not damage, we can skip the rest
		
		// Checking the attack power, because some moves are STATUS that do no damage - however, they still need to hit to apply the status!
		if (attack.getAtkPower()==0) { 
			return;
		}
		
		// Physical attacks are for physical stats (aka attack and defence)
		if (attack.getCategory().equals("Physical")) {
			// If the Pokemon has 2 types
			if (otherMon.getTypeList().size()==2) {
				int type1 = otherMon.getType1().getTypeNum();
				int type2 = otherMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2 * (playerMon.getLevel() + 2) * attack.getAtkPower()
							* (playerMon.getBaseAttack() / otherMon.getBaseDef()) / 50 + 2) * stab
							* PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				} catch (IOException e) {

				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			} else {
				int type1 = otherMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2 * (playerMon.getLevel() + 2) * attack.getAtkPower()
							* (playerMon.getBaseAttack() / otherMon.getBaseDef()) / 50 + 2) * stab
							* PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				} catch (IOException e) {
					System.out.println("EXCEPTION");
				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			}
		}

		// Special attacks are for special stats (special attack and special defence)
		else if (attack.getCategory().equals("Special")) {
			if (otherMon.getTypeList().size() == 2) {
				int type1 = otherMon.getType1().getTypeNum();
				int type2 = otherMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2 * (playerMon.getLevel() + 2) * attack.getAtkPower()
							* (playerMon.getBaseSpAtk() / otherMon.getBaseSpDef()) / 50 + 2) * stab
							* PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				} catch (IOException e) {

				}
				otherMon.setDeltaHp(otherMon.getDeltaHp() + newHp);
			} else {
				int type1 = otherMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2 * (playerMon.getLevel() + 2) * attack.getAtkPower()
							* (playerMon.getBaseSpAtk() / otherMon.getBaseSpDef()) / 50 + 2) * stab
							* PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				} catch (IOException e) {
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
		if (attack.getAccuracy() == 0) {
		} else if (accuracy > attack.getAccuracy()) {
			otherMiss = true;
			System.out.println("Missed!");
			return;
		}
		hit = true;
		if (attack.getAtkPower() == 0) {
			return;
		}
		if (attack.getCategory().equals("Physical")) {
			// If the Pokemon has 2 types
			if (playerMon.getTypeList().size()==2) {
				int type1 = playerMon.getType1().getTypeNum();
				int type2 = playerMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getBaseAttack()/playerMon.getBaseDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				}
				catch (IOException e) {

				}
				playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
			}
			else {
				int type1 = playerMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getBaseAttack()/playerMon.getBaseDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				}
				catch (IOException e) {
					System.out.println("EXCEPTION");
				}
				playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
			}
		}
		
		// Special attacks are for special stats (special attack and special defence) 
		else if (attack.getCategory().equals("Special")) {
			if (playerMon.getTypeList().size()==2) {
				int type1 = playerMon.getType1().getTypeNum();
				int type2 = playerMon.getType2().getTypeNum();
				try {
					newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getBaseSpAtk()/playerMon.getBaseDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1, type2));
				}
				catch (IOException e) {

				}
				playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
			}
			else {
				int type1 = playerMon.getType1().getTypeNum();
				try {
					newHp = (int) Math.round((2*(otherMon.getLevel()+2)*attack.getAtkPower()*(otherMon.getBaseSpAtk()/playerMon.getBaseDef())/50+2) * stab * PokeType.getTypeEffectiveness(attack.getType().getTypeNum(), type1));
				}
				catch (IOException e) {
					System.out.println("EXCEPTION");
				}
				playerMon.setDeltaHp(playerMon.getDeltaHp() + newHp);
			}
		}
	}

	// The chooseNewPokemon() method is used to select a new Pokemon, either if the
	// current one dies or if you choose to do so.
	// It takes in no parameters
	// It returns nothing - instead, a new battle is started
	public void chooseNewPokemon() {
		int index = -1;
		boolean valid = false;
		Scanner s = new Scanner(System.in);
		System.out.println("Choose your Pokemon: ");
		if (playerMon.getCurrentHp() == 0) {
			for (int i = 0; i < player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint() == false) {
					System.out.println(i + ") " + player.getPokemonList()[i].getName());
					valid = true;
				} else if (i == 2 && !valid) {
					System.out.println("You are out of usable Pokemon! You give out a badge...");
					battleContinue = false;
					return;
				}
			}
			while (index == -1) {
				index = Integer.parseInt(s.nextLine());
			}
			new Battle(other, player, otherMon, index, playerSkipTurn);
		}
		// If you decide to switch Pokemon even though your current one is still good
		else {
			index = -1;
			for (int i = 0; i < player.getPokemonList().length; i++) {
				if (player.getPokemonList()[i].getFaint() == false) {
					System.out.println(i + ") " + player.getPokemonList()[i].getName());
					valid = true;
				}
				else if (i==2 && !valid) {
					System.out.println("This is your only Pokemon left!"); 
					new Battle(other, player, otherMon, index, playerSkipTurn);
				}
			}
			System.out.println("Choose a Pokemon");
			valid = false;
			while (!valid) {
				while (index == -1) {
					index = Integer.parseInt(s.nextLine());
				}
				if (player.getPokemonList()[index].equals(playerMon)) {
					System.out.println("That Pokemon is already in battle!");
					new Battle(other, player, otherMon, index, playerSkipTurn);
				} else {
					valid = true;
				}
			}
			// Call the constructor again, except the switchPokemon boolean will be true.
			// This ensures that switching Pokemon counts as a turn, and the new Pokemon
			// will get hit!
			new Battle(other, player, otherMon, index, true);
		}
	}

	// Statuses are applied at the end of every turn
	public void applyStatus() {
		// Trainer Pokemon Status
		if (playerMon.getStatus() == 0) {
			return; // No status
		}
		// Poison - Takes away 1/8th of the Pokemon's HP
		else if (playerMon.getStatus() == 1) {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + playerMon.getCurrentHp() / 8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (playerMon.getStatus() == 4) {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + (playerMon.getCurrentHp() / 16));
			playerMon.setDeltaAttack(playerMon.getCurrentAttack() / 2);
		}
		// Paralyze
		// This will cut the Pokemon's speed to 25%
		// There is also a 25% chance for the Pokemon to be fully paralyzed, rendering
		// it unable to move for a turn
		else if (playerMon.getStatus() == 2) {
			playerMon.setDeltaSpeed(playerMon.getDeltaSpeed() / 4);
			int random = (int) (Math.random() * 4) + 1;
			if (random == 1) {
				playerSkipTurn = true;
			} else {
				playerSkipTurn = false;
			}
		}
		// Sleep
		else if (playerMon.getStatus() == 3) {
			// Guaranteed turn of sleep
			if (playerSleepCounter == 0) {
				playerSkipTurn = true;
				playerSleepCounter++;
			}
			// Forced to wake up to prevent infinite sleep
			else if (playerSleepCounter == 3) {
				playerSleepCounter = 0;
				playerMon.setStatus(0);
				playerSkipTurn = false;
			} else { // 50% chance to wake up
				int random = (int) (Math.random() * 2) + 1;
				if (random == 1) {
					playerSleepCounter = 0;
					playerMon.setStatus(0);
					playerSkipTurn = false;
				} else {
					playerSkipTurn = true;
					playerSleepCounter++;
				}
			}
		}
		// Badly Poisoned (through the move Toxic)
		// It will do damage N*1/16 of max HP, where N is a counter that increases per
		// turn.
		else {
			playerMon.setDeltaHp(playerMon.getDeltaHp() + playerToxicCounter * playerMon.getCurrentHp() / 16);
			playerToxicCounter++;
		}

		// Opponent Pokemon Status
		if (otherMon.getStatus() == 0) {
			return; // No status
		}
		// Poison
		else if (otherMon.getStatus() == 1) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + otherMon.getCurrentHp() / 8);
		}
		// Burn - Takes away 1/16th of HP every turn and HALVES the current attack stat.
		else if (otherMon.getStatus() == 4) {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + (otherMon.getCurrentHp() / 16));
			otherMon.setDeltaAttack(otherMon.getCurrentAttack() / 2);
		}
		// Paralyze
		else if (otherMon.getStatus() == 2) {
			otherMon.setDeltaSpeed(otherMon.getDeltaSpeed() / 4);
			int random = (int) (Math.random() * 4) + 1;
			if (random == 1) {
				otherSkipTurn = true;
			} else {
				otherSkipTurn = false;
			}
		}
		// Sleep
		else if (otherMon.getStatus() == 3) {
			// Guaranteed turn of sleep
			if (otherSleepCounter == 0) {
				otherSkipTurn = true;
				otherSleepCounter++;
			}
			// Forced to wake up to prevent infinite sleep
			else if (otherSleepCounter == 3) {
				otherSleepCounter = 0;
				otherMon.setStatus(0);
				otherSkipTurn = false;
			} else {
				int random = (int) (Math.random() * 2) + 1;
				if (random == 1) {
					otherSleepCounter = 0;
					otherMon.setStatus(0);
					otherSkipTurn = false;
				} else {
					otherSkipTurn = true;
					otherSleepCounter++;
				}
			}
		}
		// Badly Poisoned (through the move Toxic)
		else {
			otherMon.setDeltaHp(otherMon.getDeltaHp() + otherToxicCounter * otherMon.getCurrentHp() / 16);
			otherToxicCounter++;
		}
	}

	public void resetTurn() {
		this.setPlayerMiss(false);
		this.setOtherMiss(false);
		this.setPlayerCurrentMove(null);
		this.setOtherCurrentMove(null);
		this.setPlayerAttacking(false);
		this.setOtherAttacking(false);
		this.setDisplayedBeforePlayerEffect(false);
		this.setDisplayedBeforeOtherEffect(false);
		this.setDisplayedAfterOtherEffect(false);
		this.setDisplayedAfterPlayerEffect(false);
		this.setPlayerAttacked(false);
		this.setOtherAttacked(false);
		this.setOtherAttackEffect(null);
		this.setPlayerAttackEffect(null);
		this.setCurrentEffect(null);
		this.setDisplayingEffect(false);
	}

	// The calculateStab method is used to calculate STAB (same type attack bonus)
	// Although this is already found in the attack() method, I created this other
	// one for the weird case
	// that the user switches in a Pokemon.
	// It takes in the parameters of the attacking Pokemon and the Move
	// It returns a double
	public double calculateStab(Pokemon mon, Move attack) {
		double stab = 1;
		for (int i = 0; i < mon.getTypeList().size(); i++) {
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

	public Move getPlayerCurrentMove() {
		return playerCurrentMove;
	}

	public void setPlayerCurrentMove(Move playerCurrentMove) {
		this.playerCurrentMove = playerCurrentMove;
	}

	public Move getOtherCurrentMove() {
		return otherCurrentMove;
	}

	public void setOtherCurrentMove(Move otherCurrentMove) {
		this.otherCurrentMove = otherCurrentMove;
	}

	public boolean getRoundEnd() {
		return roundEnd;
	}

	public void setRoundEnd(boolean roundEnd) {
		this.roundEnd = roundEnd;
	}

	public boolean getPlayerSkipTurn() {
		return playerSkipTurn;
	}

	public void setPlayerSkipTurn(boolean playerSkipTurn) {
		this.playerSkipTurn = playerSkipTurn;
	}

	public boolean getOtherSkipTurn() {
		return otherSkipTurn;
	}

	public void setOtherSkipTurn(boolean otherSkipTurn) {
		this.otherSkipTurn = otherSkipTurn;
	}

	public boolean isPlayerAttacking() {
		return playerAttacking;
	}

	public void setPlayerAttacking(boolean playerAttacking) {
		this.playerAttacking = playerAttacking;
	}

	public boolean isOtherAttacking() {
		return otherAttacking;
	}

	public void setOtherAttacking(boolean otherAttacking) {
		this.otherAttacking = otherAttacking;
	}

	public boolean isSwitchPokemon() {
		return switchPokemon;
	}

	public void setSwitchPokemon(boolean switchPokemon) {
		this.switchPokemon = switchPokemon;
	}

	public boolean isDisplayingEffect() {
		return displayingEffect;
	}

	public void setDisplayingEffect(boolean displayingEffect) {
		this.displayingEffect = displayingEffect;
	}

	public boolean isDisplayedBeforePlayerEffect() {
		return displayedBeforePlayerEffect;
	}

	public void setDisplayedBeforePlayerEffect(boolean displayedEffect) {
		this.displayedBeforePlayerEffect = displayedEffect;
	}

	public String getCurrentEffect() {
		return currentEffect;
	}

	public void setCurrentEffect(String currentEffect) {
		this.currentEffect = currentEffect;
	}

	public boolean isDisplayedBeforeOtherEffect() {
		return displayedBeforeOtherEffect;
	}

	public void setDisplayedBeforeOtherEffect(boolean displayedBeforeOtherEffect) {
		this.displayedBeforeOtherEffect = displayedBeforeOtherEffect;
	}

	public boolean isDisplayedAfterPlayerEffect() {
		return displayedAfterPlayerEffect;
	}

	public void setDisplayedAfterPlayerEffect(boolean displayedAfterPlayerEffect) {
		this.displayedAfterPlayerEffect = displayedAfterPlayerEffect;
	}

	public boolean isDisplayedAfterOtherEffect() {
		return displayedAfterOtherEffect;
	}

	public void setDisplayedAfterOtherEffect(boolean displayedAfterOtherEffect) {
		this.displayedAfterOtherEffect = displayedAfterOtherEffect;
	}

	public boolean isPlayerMiss() {
		return playerMiss;
	}

	public void setPlayerMiss(boolean playerMiss) {
		this.playerMiss = playerMiss;
	}

	public boolean isOtherMiss() {
		return otherMiss;
	}

	public void setOtherMiss(boolean otherMiss) {
		this.otherMiss = otherMiss;
	}

	public String getPlayerAttackEffect() {
		return playerAttackEffect;
	}

	public void setPlayerAttackEffect(String playerAttackEffect) {
		this.playerAttackEffect = playerAttackEffect;
	}

	public String getOtherAttackEffect() {
		return otherAttackEffect;
	}

	public void setOtherAttackEffect(String otherAttackEffect) {
		this.otherAttackEffect = otherAttackEffect;
	}

	public boolean isPlayerAttacked() {
		return playerAttacked;
	}

	public void setPlayerAttacked(boolean playerAttacked) {
		this.playerAttacked = playerAttacked;
	}

	public boolean isOtherAttacked() {
		return otherAttacked;
	}

	public void setOtherAttacked(boolean otherAttacked) {
		this.otherAttacked = otherAttacked;
	}

}
