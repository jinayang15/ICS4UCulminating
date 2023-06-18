import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial") // funky warning, just suppress it. It's not gonna do anything.
public class Main extends JPanel implements Runnable, KeyListener, MouseListener {
	// 0 - start menu
	// 1 -
	// 2 - Pewter City
	// 3 - Battle
	// 4 - Misc
	static int gameState = 0;
	// 0 - not in battle
	// 1 - options
	// 2 - moves
	// 3 - turn check
	// 4 - player turn
	// 5 - enemy turn
	// 6 - displaying attacks and effects
	// 8 - pokemon menu
	static int battleState = 0;
	static int nextBattleState = 0;
	static Battle battle = null;

	public static Player player;
	public static Trainer trainer;
	// self explanatory variables
	int FPS = 60;
	Thread thread;
	static int screenWidth = 960;
	static int screenHeight = 640;
	static int tileSize = 64;
	static int tileScreenWidth = screenWidth / tileSize;
	static int tileScreenHeight = screenHeight / tileSize;
	static int tileMapWidth = 48;
	static int tileMapHeight = 40;
	BufferedImage currentBG;
	// background top-left corner position, x and y value
	static int bgX = 0;
	static int bgY = 0;
	static int defaultShiftPixels = 4;
	static boolean bgAdjusting = false;
	// last bg position
	int saveBGX = -960;
	int saveBGY = -1920;

	long lastActionTime = 0;
	char lastKeyPressed = ' ';
	char lastKeyReleased = ' ';

	public static Wall[][] allWalls = new Wall[tileMapHeight][tileMapWidth];
	public static boolean collisionUp = false;
	public static boolean collisionDown = false;
	public static boolean collisionLeft = false;
	public static boolean collisionRight = false;

	BufferedImage[] spriteTest = new BufferedImage[2];
	int spriteIdx = 0;

	// Arrow position
	static int[][] optionsArrowPositions = { { 512, 496 }, { 736, 496 }, { 512, 560 }, { 736, 560 } };
	static int optionsArrowX = 512;
	static int optionsArrowY = 496;
	static int optionsArrowIdx = 0;

	static int[][] attackArrowPositions = { { 36, 490 }, { 332, 490 }, { 36, 554 }, { 332, 554 } };
	static int attackArrowX = 36;
	static int attackArrowY = 490;
	static int attackArrowIdx = 0;

	// Pokemon Menu Select Position
	private static int[] pkmnListIdx = new int[3]; // stores the pokeList idx of the pokemon on the pokemon menu
	private static int pkmnMenuSelectIdx = 0;
	private static int pkmnMenuIdx = 0;

	public Main() {
		// sets up JPanel
		setPreferredSize(new Dimension(screenWidth, screenHeight));
		setVisible(true);

		// starting the thread
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		try {
			initialize();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while (true) {
			// main game loop
			update();
			this.repaint();
			try {
				// pause when displaying an effect
				if (battle.isDisplayingEffect()) {
					Thread.sleep(2000);
					battle.setDisplayingEffect(false);
				}
				Thread.sleep(1000 / FPS);
			} catch (NullPointerException e) {
			} catch (InterruptedException e) {
			}
		}
	}

	public static void initialize() throws IOException {
		// setups before the game starts running
		try {
			Images.importAllImages();
			GameFunctions.importEverything();
			PokeType.addToChart();
			player = new Player("Fire");
		} catch (FileNotFoundException e) {
		}

	}

	public void update() {
		// update stuff
		if (gameState == 0) {
			currentBG = Images.fireRedPressStart;
		} else if (gameState == 1) {

		} else if (gameState == 2) {
			currentBG = Images.pewterCity[0];
			adjustWalls();
			for (int i = 0; i < 40; i++) {
				for (int j = 0; j < 48; j++) {
					if (allWalls[i][j] != null) {
						checkCollision(allWalls[i][j]);
					}
				}
			}
			Animations.walk();
			Animations.resetWalk();
			bgShift();
		} else if (gameState == 3) {
			currentBG = Images.battleBackground;
		}
		if (battleState == 3) {
			// moves intended to happen
			battle.applyStatus();
			battle.setPlayerCurrentMove(battle.getPlayerMon().getMoves()[attackArrowIdx]);
			battle.setOtherCurrentMove(battle.opponentChooseAttack());
			battle.setRoundEnd(false);
			if (battle.turnCheck()) {
				battleState = 4;
			} else {
				battleState = 5;
			}
		}
		if (battleState == 4) {
			battle.setOtherAttacking(false);
			battle.checkFaint(battle.getPlayerMon());
			battle.checkFaint(battle.getOtherMon());
			if (battle.checkBattle()) {
				if (battle.getPlayerMon().getFaint()) {
					battleState = 8;
				} else if (battle.getOtherMon().getFaint()) {
					battle.otherChooseNewPokemon();
					battle.resetTurn();
					battleState = 1;
				} else if (!battle.getPlayerSkipTurn() && !battle.isPlayerAttacking()) {
					battle.attack(battle.getPlayerCurrentMove(), battle.getPlayerMon(), battle.getOtherMon());
					battle.setPlayerAttacking(true);
				}
			} else {
				if (battle.getOtherMon().getFaint()) {
					if (battle.getPlayerMon().getFaint()) {
						// tie
					}
					// win
				} else if (battle.getPlayerMon().getFaint()) {
					// lost
				}
			}

		}
		if (battleState == 5) {
			battle.setPlayerAttacking(false);
			battle.checkFaint(battle.getPlayerMon());
			battle.checkFaint(battle.getOtherMon());
			if (battle.checkBattle()) {
				if (battle.getPlayerMon().getFaint()) {
					battleState = 8;
				} else if (battle.getOtherMon().getFaint()) {
					battle.otherChooseNewPokemon();
					battle.resetTurn();
					battleState = 1;
				} else if (!battle.getOtherSkipTurn() && !battle.isOtherAttacking()) {
					battle.attack(battle.getOtherCurrentMove(), battle.getOtherMon(), battle.getPlayerMon());
					battle.setOtherAttacking(true);
				}
			} else {
				if (battle.getOtherMon().getFaint()) {
					if (battle.getPlayerMon().getFaint()) {
						// tie
					}
					// win
				} else if (battle.getPlayerMon().getFaint()) {
					// lost
				}
			}
		}
		if (battleState == 8) {
			currentBG = Images.pkmnMenuBG;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentBG, bgX, bgY, null);
		if (gameState == 2) {
			g.drawImage(Player.getCurrentPlayerImage(), Player.getPlayerX(), Player.getPlayerY(), null);
			g.drawRect(Player.hitbox.x, Player.hitbox.y, Player.hitbox.width, Player.hitbox.height);
			for (int i = 0; i < 40; i++) {
				for (int j = 0; j < 48; j++) {
					if (allWalls[i][j] != null) {
						g.setColor(Color.RED);
						g.fillRect(allWalls[i][j].x, allWalls[i][j].y, 64, 64);
					}
				}
			}
		}
		if (gameState == 3) {
			if (battleState < 8) {
				baseBattleGraphics(g);
				displayBattleSprites(g);
				displayPokemonStats(g);
				displayStatus(g);
				displayHealth(g);
			}
			if (battleState == 1) {
				// reset moves after attack round and pause to show attacks
				battle.resetTurn();
				displayOptionsMenuAndArrow(g);
				displayText(g, Images.whiteFontIdx, Images.whiteFont,
						"What will~" + battle.getPlayerMon().getName().toUpperCase() + " do?", 40,
						640 - Images.battleMenu[4].getHeight() + 40);
			} else if (battleState == 2) {
				displayAttackMenuAndArrow(g);
				displayAttacks(g);
			} else if (battleState == 4) {
				battle.setDisplayedBeforeOtherEffect(false);
				if (battle.getPlayerSkipTurn()) {
					if (!battle.isDisplayedBeforePlayerEffect()) {
						if (battle.isSwitchPokemon()) {
							battle.setCurrentEffect("Go " + battle.getPlayerMon().getName().toUpperCase() + "!");
							battle.setSwitchPokemon(false);
							battle.setPlayerSkipTurn(false);
						} else if (battle.getPlayerMon().getStatus() == 2) {
							battle.setCurrentEffect(battle.getPlayerMon().getName().toUpperCase()
									+ " is paralyzed.~It is unable to move!");
						} else if (battle.getPlayerMon().getStatus() == 3) {
							battle.setCurrentEffect(battle.getPlayerMon().getName().toUpperCase() + " is fast asleep.");
						}
						if (battle.getRoundEnd()) {
							nextBattleState = 1;
							battle.setRoundEnd(false);
						} else {
							nextBattleState = 5;
							battle.setRoundEnd(true);
						}
						battleState = 6;
						battle.setDisplayingEffect(true);
					} else {
						if (battle.getRoundEnd()) {
							battleState = 1;
							battle.setRoundEnd(false);
						} else {
							battleState = 5;
							battle.setRoundEnd(true);
						}
						battle.setPlayerSkipTurn(false);
					}
				} else if (!battle.getPlayerSkipTurn()) {
					if (battle.getPlayerMon().getStatus() >= 1) {
						if (battle.isDisplayedBeforePlayerEffect()) {
							if (battle.getRoundEnd()) {
								nextBattleState = 1;
								battle.setRoundEnd(false);
							} else {
								nextBattleState = 5;
								battle.setRoundEnd(true);
							}
							battle.setCurrentEffect(battle.getPlayerMon().getName().toUpperCase() + " used~"
									+ battle.getPlayerCurrentMove().getName().toUpperCase() + "!");
							battle.setDisplayedBeforePlayerEffect(false);
						} else {
							nextBattleState = battleState;
							if (battle.getPlayerMon().getStatus() == 1) {
								battle.setCurrentEffect(
										battle.getPlayerMon().getName().toUpperCase() + " is poisoned.");
							} else if (battle.getPlayerMon().getStatus() == 2) {
								battle.setCurrentEffect(
										battle.getPlayerMon().getName().toUpperCase() + " is paralyzed.");
							} else if (battle.getPlayerMon().getStatus() == 4) {
								battle.setCurrentEffect(battle.getPlayerMon().getName().toUpperCase() + " was burned.");
							} else if (battle.getPlayerMon().getStatus() == 5) {
								battle.setCurrentEffect(
										battle.getPlayerMon().getName().toUpperCase() + " was badly poisoned");
							}
						}
						battleState = 6;
						battle.setDisplayingEffect(true);
					} else if (!battle.getPlayerSkipTurn() && !battle.isPlayerAttacked()) {
						System.out.println("Player: " + battle.getPlayerAttackEffect());
						System.out.println("P: " + (battle.getPlayerAttackEffect() == null));
						if (battle.getRoundEnd()) {
							nextBattleState = 1;
							battle.setRoundEnd(false);
						} else if (battle.getPlayerAttackEffect() == null) {
							nextBattleState = 5;
							battle.setRoundEnd(true);
						} else {
							nextBattleState = 4;
						}
						battle.setCurrentEffect(battle.getPlayerMon().getName().toUpperCase() + " used~"
								+ battle.getPlayerCurrentMove().getName().toUpperCase() + "!");
						battleState = 6;
						battle.setDisplayingEffect(true);
						battle.setPlayerAttacked(true);

					}
				} else if (battle.isPlayerAttacked() && !battle.isDisplayedAfterPlayerEffect()) {
					System.out.println("Displaying player attack effect");
					nextBattleState = battleState;
					battle.setCurrentEffect(battle.getPlayerAttackEffect());
					battleState = 6;
					battle.setDisplayingEffect(true);
					battle.setDisplayedAfterPlayerEffect(true);
				} else if (battle.isDisplayedAfterPlayerEffect()) {
					System.out.println("Switching battleState1");
					if (battle.getRoundEnd()) {
						battleState = 1;
						battle.setRoundEnd(false);
					} else {
						battleState = 5;
						battle.setRoundEnd(true);
					}
					battle.setDisplayedAfterPlayerEffect(false);
					battle.setPlayerAttacked(false);
				}
			} else if (battleState == 5) {
				battle.setDisplayedBeforePlayerEffect(false);
				if (battle.getOtherSkipTurn()) {
					if (!battle.isDisplayedBeforeOtherEffect()) {
						if (battle.getOtherMon().getStatus() == 2) {
							battle.setCurrentEffect("Enemy " + battle.getOtherMon().getName().toUpperCase()
									+ " is paralyzed.~It is unable to move!");
						} else if (battle.getOtherMon().getStatus() == 3) {
							battle.setCurrentEffect(
									"Enemy " + battle.getOtherMon().getName().toUpperCase() + " is fast asleep.");
						}
						if (battle.getRoundEnd()) {
							nextBattleState = 1;
							battle.setRoundEnd(false);
						} else {
							nextBattleState = 4;
							battle.setRoundEnd(true);
						}
						battleState = 6;
						battle.setDisplayingEffect(true);
					} else {
						if (battle.getRoundEnd()) {
							battleState = 1;
							battle.setRoundEnd(false);
						} else {
							battleState = 4;
							battle.setRoundEnd(true);
						}
						battle.setOtherSkipTurn(false);
					}
				} else if (!battle.getOtherSkipTurn() && !battle.isOtherAttacked()) {
					if (battle.getOtherMon().getStatus() >= 1) {
						if (battle.isDisplayedBeforeOtherEffect()) {
							if (battle.getRoundEnd()) {
								nextBattleState = 1;
								battle.setRoundEnd(false);
							} else {
								nextBattleState = 4;
								battle.setRoundEnd(true);
							}
							battle.setCurrentEffect("Enemy " + battle.getOtherMon().getName().toUpperCase() + " used~"
									+ battle.getOtherCurrentMove().getName().toUpperCase() + "!");
							battle.setDisplayedBeforeOtherEffect(false);
						} else {
							nextBattleState = battleState;
							if (battle.getOtherMon().getStatus() == 1) {
								battle.setCurrentEffect(
										"Enemy " + battle.getOtherMon().getName().toUpperCase() + " is poisoned.");
							} else if (battle.getOtherMon().getStatus() == 2) {
								battle.setCurrentEffect(
										"Enemy " + battle.getOtherMon().getName().toUpperCase() + " is paralyzed.");
							} else if (battle.getOtherMon().getStatus() == 4) {
								battle.setCurrentEffect(
										"Enemy " + battle.getOtherMon().getName().toUpperCase() + " was burned.");
							}
						}
						battleState = 6;
						battle.setDisplayingEffect(true);
					} else {
						System.out.println("Other: " + battle.getOtherAttackEffect());
						System.out.println("O: " + (battle.getOtherAttackEffect() == null));
						if (battle.getRoundEnd()) {
							nextBattleState = 1;
							battle.setRoundEnd(false);
						} else if (battle.getOtherAttackEffect() == null) {
							nextBattleState = 4;
							battle.setRoundEnd(true);
						} else {
							nextBattleState = 5;
						}
						battle.setCurrentEffect("Enemy " + battle.getOtherMon().getName().toUpperCase() + " used~"
								+ battle.getOtherCurrentMove().getName().toUpperCase() + "!");
						battleState = 6;
						battle.setDisplayingEffect(true);
						battle.setOtherAttacked(true);
					}
				} else if (battle.isOtherAttacked() && !battle.isDisplayedAfterOtherEffect()) {
					System.out.println("Displaying other attack effect: " + battle.getOtherAttackEffect());
					battle.setCurrentEffect(battle.getOtherAttackEffect());
					nextBattleState = battleState;
					battleState = 6;
					battle.setDisplayingEffect(true);
					battle.setDisplayedAfterOtherEffect(true);
				} else if (battle.isDisplayedAfterOtherEffect()) {
					System.out.println("Switching battle states2");
					if (battle.getRoundEnd()) {
						battleState = 1;
						battle.setRoundEnd(false);
					} else {
						battleState = 4;
						battle.setRoundEnd(true);
					}
					battle.setOtherAttacked(false);
					battle.setDisplayedAfterOtherEffect(false);
				}
			} else if (battleState == 6) {
				System.out.println("Attacked: " + battle.isPlayerAttacked() + " " + "PlayerAfterEffect: "
						+ battle.isDisplayedAfterPlayerEffect());
				System.out.println("Attacked: " + battle.isOtherAttacked() + " " + "OtherAFterEffect: "
						+ battle.isDisplayedAfterOtherEffect());
				try {
//					System.out.println(battle.getCurrentEffect());
					displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getCurrentEffect(), 40, 488);
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				if (battle.getPlayerSkipTurn() && nextBattleState != 4) {
					battle.setDisplayedBeforePlayerEffect(true);
				} else if (battle.getOtherSkipTurn() && nextBattleState != 5) {
					battle.setDisplayedBeforeOtherEffect(true);
				}
				if (battle.isPlayerAttacking()) {
					if (nextBattleState == 4) {
						battle.setDisplayedBeforePlayerEffect(true);
					}
				} else if (battle.isOtherAttacking()) {
					if (nextBattleState == 5) {
						battle.setDisplayedBeforeOtherEffect(true);
					}
				}
				battleState = nextBattleState;
				// pokemon menu
			} else if (battleState == 8) {
				// first pokemon
				if (pkmnMenuSelectIdx == 0) {
					g.drawImage(Images.pkmnMenuIcons[1], 8, 72, null);
				} else {
					g.drawImage(Images.pkmnMenuIcons[0], 8, 72, null);
				}
				displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getPlayerMon().getName().toUpperCase(), 72,
						148); // first name
				displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getPlayerMon().getLevel() + "", 192, 184); // first
																														// lvl
				displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getPlayerMon().getCurrentHp() + "", 196,
						248); // first health
				displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getPlayerMon().getBaseHp() + "", 280, 248);

				// other pokemon
				for (int i = 0; i < 3; i++) {
					if (battle.getPlayer().getPokemonList()[i] != battle.getPlayerMon()) {
						if (pkmnMenuSelectIdx == pkmnMenuIdx + 1) {
							g.drawImage(Images.pkmnMenuIcons[3], 352, 36 + 96 * pkmnMenuIdx, null);
						} else {
							g.drawImage(Images.pkmnMenuIcons[2], 352, 36 + 96 * pkmnMenuIdx, null);
						}
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayer().getPokemonList()[i].getName().toUpperCase(), 432,
								52 + 96 * pkmnMenuIdx); // other name
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayer().getPokemonList()[i].getLevel() + "", 544, 88 + 96 * pkmnMenuIdx); // other
																														// level
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayer().getPokemonList()[i].getCurrentHp() + "", 800, 88 + 96 * pkmnMenuIdx); // other
																															// current
																															// hp
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayer().getPokemonList()[i].getBaseHp() + "", 884, 88 + 96 * pkmnMenuIdx); // other
																														// base
																														// hp
						pkmnMenuIdx++;
						pkmnListIdx[pkmnMenuIdx] = i;
					} else {
						pkmnListIdx[0] = i;
					}
				}
				// cancel button
				if (!battle.getPlayerMon().getFaint()) {
					if (pkmnMenuSelectIdx == 3) {
						g.drawImage(Images.pkmnMenuIcons[5], 736, 528, null);
					} else {
						g.drawImage(Images.pkmnMenuIcons[4], 736, 528, null);
					}
				}
				pkmnMenuIdx = 0;
				displayHealth(g); // all health
				displayText(g, Images.moveFontIdx, Images.moveFont, "Select a pokemon to switch to.", 40, 536);

			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	// changes direction depending on key pressed and sets moving to true !
	public void keyPressed(KeyEvent e) {
		char x = e.getKeyChar();
		if (gameState == 2) {
			if (!Player.getMoving() || x != lastKeyPressed) {
//			if (x != lastKeyPressed) {
//				movesQ.add(e.getKeyChar());
//			} 
//			if (checkTile()) {
//				x = (char) movesQ.remove();
				if (x == 'w') {
					Player.setDirection(1);
					Player.setMoving(true);
					if (bgX == -448 && bgY == -768) {
//						Battle b = new Battle(player, new Trainer());
					}
				} else if (x == 's') {
					Player.setDirection(2);
					Player.setMoving(true);
				} else if (x == 'a') {
					Player.setDirection(3);
					Player.setMoving(true);
				} else if (x == 'd') {
					Player.setDirection(4);
					Player.setMoving(true);
				}
				lastKeyPressed = e.getKeyChar();
//			}
//			System.out.println(checkTile());
			}
		} else if (gameState == 3 && battleState == 1) {
			if (x == 'w') {
				if (optionsArrowIdx == 2 || optionsArrowIdx == 3) {
					optionsArrowIdx -= 2;
				}
			} else if (x == 's') {
				if (optionsArrowIdx == 0 || optionsArrowIdx == 1) {
					optionsArrowIdx += 2;

				}
			} else if (x == 'a') {
				if (optionsArrowIdx == 1 || optionsArrowIdx == 3) {
					optionsArrowIdx--;
				}
			} else if (x == 'd') {
				if (optionsArrowIdx == 0 || optionsArrowIdx == 2) {
					optionsArrowIdx++;
				}

			} else if (x == 'e')
				if (optionsArrowIdx == 0) {
					battleState = 2;
				} else if (optionsArrowIdx == 1) {

				} else if (optionsArrowIdx == 2) {
					battleState = 8;
					pkmnMenuIdx = 0;
					pkmnMenuSelectIdx = 0;
				} else if (optionsArrowIdx == 3) {

				}
			optionsArrowX = optionsArrowPositions[optionsArrowIdx][0];
			optionsArrowY = optionsArrowPositions[optionsArrowIdx][1];
		} else if (gameState == 3 && battleState == 2) {
			if (x == 'w') {
				if (attackArrowIdx == 2 || attackArrowIdx == 3) {
					if (battle.getPlayerMon().getMoves()[attackArrowIdx - 2] != null) {
						attackArrowIdx -= 2;
					}
				}
			} else if (x == 's') {
				if (attackArrowIdx == 0 || attackArrowIdx == 1) {
					if (battle.getPlayerMon().getMoves()[attackArrowIdx + 2] != null) {
						attackArrowIdx += 2;
					}
				}
			} else if (x == 'a') {
				if (attackArrowIdx == 1 || attackArrowIdx == 3) {
					if (battle.getPlayerMon().getMoves()[attackArrowIdx - 1] != null) {
						attackArrowIdx--;
					}
				}
			} else if (x == 'd') {
				if (attackArrowIdx == 0 || attackArrowIdx == 2) {
					if (battle.getPlayerMon().getMoves()[attackArrowIdx + 1] != null) {
						attackArrowIdx++;
					}
				}

			} else if (x == 'e') {
				battleState = 3;
			} else if (x == 'q') {
				battleState = 1;
			}
			attackArrowX = attackArrowPositions[attackArrowIdx][0];
			attackArrowY = attackArrowPositions[attackArrowIdx][1];
		} else if (battleState == 8) {
			if (x == 'w') {
				if (pkmnMenuSelectIdx == 2 || pkmnMenuSelectIdx == 3) {
					pkmnMenuSelectIdx--;
				}
			} else if (x == 's') {
				if (pkmnMenuSelectIdx == 1 || pkmnMenuSelectIdx == 2) {
					pkmnMenuSelectIdx++;
				}
			} else if (x == 'a') {
				if (pkmnMenuSelectIdx >= 1 && pkmnMenuSelectIdx <= 2) {
					pkmnMenuSelectIdx = 0;
				}
			} else if (x == 'd') {
				if (pkmnMenuSelectIdx == 0) {
					pkmnMenuSelectIdx = 2;
				}

			} else if (x == 'e') {
				if (battle.getPlayerMon().getFaint()) {
					if (!battle.getPlayer().getPokemonList()[pkmnListIdx[pkmnMenuSelectIdx]].getFaint()) {
						if (pkmnMenuSelectIdx != 0) {
							battle.setPlayerMon(battle.getPlayer().getPokemonList()[pkmnListIdx[pkmnMenuSelectIdx]]);
							battle.resetTurn();
							battle.setSwitchPokemon(true);
							battle.setPlayerSkipTurn(true);
							battleState = 1;
						}
					}
				} else {
					if (pkmnMenuSelectIdx == 3) {
						battleState = 1;
					} else if (pkmnMenuSelectIdx != 0) {
						battle.setPlayerMon(battle.getPlayer().getPokemonList()[pkmnListIdx[pkmnMenuSelectIdx]]);
						battle.resetTurn();
						battle.setSwitchPokemon(true);
						battle.setPlayerSkipTurn(true);
						battleState = 1;
					}
				}
			} else if (x == 'q') {
				if (!battle.getPlayerMon().getFaint()) {
					battleState = 1;
				}
			}
		}

	}

	@Override
	// reset to still image depending on direction
	public void keyReleased(KeyEvent e) {
		if (e.getKeyChar() == lastKeyPressed) {
			if (e.getKeyChar() == 'w') {
				Player.setMoving(false);
			} else if (e.getKeyChar() == 's') {
				Player.setMoving(false);
			} else if (e.getKeyChar() == 'a') {
				Player.setMoving(false);
			} else if (e.getKeyChar() == 'd') {
				Player.setMoving(false);
			}
			lastKeyReleased = e.getKeyChar();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (gameState == 0) {
			gameState = 2;
			bgX = saveBGX;
			bgY = saveBGY;
		} else if (gameState == 2) {
			gameState++;
			saveBGX = bgX;
			saveBGY = bgY;
			bgX = 0;
			bgY = 0;
			battleState = 1;
			trainer = new Trainer();
			battle = new Battle(player, trainer);
		} else if (gameState == 3) {
//			gameState = 0;
//			bgX = 0;
//			bgY = 0;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws IOException {

		// The following lines creates your window

		// makes a brand new JFrame
		JFrame frame = new JFrame("PokemonFireRed");
		// makes a new copy of your "game" that is also a JPanel
		Main myPanel = new Main();
		// so your JPanel to the frame so you can actually see it

		frame.add(myPanel);
		// so you can actually get keyboard input
		frame.addKeyListener(myPanel);
		// so you can actually get mouse input
		frame.addMouseListener(myPanel);
		// self explanatory. You want to see your frame
		frame.setVisible(true);
		// some weird method that you must run
		frame.pack();
		// place your frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		// without this, your thread will keep running even when you windows is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// self explanatory. You don't want to resize your window because
		// it might mess up your graphics and collisions
		frame.setResizable(false);
	}

	// background shifts with the player
	public void bgShift() {
		if (Player.getMoving()) {
			if (Player.getDirection() == 1 && !collisionUp) {
				bgShiftUp(defaultShiftPixels);
			} else if (Player.getDirection() == 2 && !collisionDown) {
				bgShiftDown(defaultShiftPixels);
			} else if (Player.getDirection() == 3 && !collisionLeft) {
				bgShiftLeft(defaultShiftPixels);
			} else if (Player.getDirection() == 4 && !collisionRight) {
				bgShiftRight(defaultShiftPixels);
			}
		}
	}

	public void bgShiftUp(int bgShiftPixels) {
		if (bgY + bgShiftPixels > 0) {
			bgY = 0;
		} else {
			bgY += bgShiftPixels;
			collisionUp = false;
			collisionDown = false;
			collisionLeft = false;
			collisionRight = false;
		}
	}

	public void bgShiftUp(double bgShiftPixels) {
		if (bgY + bgShiftPixels > 0) {
			bgY = 0;
		} else {
			bgY += bgShiftPixels;
			collisionUp = false;
			collisionDown = false;
			collisionLeft = false;
			collisionRight = false;
		}
	}

	public void bgShiftDown(int bgShiftPixels) {
		if (bgY - bgShiftPixels < -tileMapHeight * tileSize + screenHeight)
			bgY = -tileMapHeight * tileSize + screenHeight;
		else {
			bgY -= bgShiftPixels;
			collisionUp = false;
			collisionDown = false;
			collisionLeft = false;
			collisionRight = false;
		}
	}

	public void bgShiftDown(double bgShiftPixels) {
		if (bgY - bgShiftPixels < -tileMapHeight * tileSize + screenHeight)
			bgY = -tileMapHeight * tileSize + screenHeight;
		else {
			bgY -= bgShiftPixels;
			collisionUp = false;
			collisionDown = false;
			collisionLeft = false;
			collisionRight = false;
		}
	}

	public void bgShiftLeft(int bgShiftPixels) {
		if (Player.getDirection() == 3 && !collisionLeft) {
			if (bgX + bgShiftPixels > 0)
				bgX = 0;
			else {
				bgX += bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			}
		}
	}

	public void bgShiftLeft(double bgShiftPixels) {
		if (Player.getDirection() == 3 && !collisionLeft) {
			if (bgX + bgShiftPixels > 0)
				bgX = 0;
			else {
				bgX += bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			}
		}
	}

	public void bgShiftRight(int bgShiftPixels) {
		if (Player.getDirection() == 4 && !collisionRight) {
			if (bgX - bgShiftPixels < -tileMapWidth * tileSize + screenWidth)
				bgX = -tileMapWidth * tileSize + screenWidth;
			else {
				bgX -= bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			}
		}
	}

	public void bgShiftRight(double bgShiftPixels) {
		if (Player.getDirection() == 4 && !collisionRight) {
			if (bgX - bgShiftPixels < -tileMapWidth * tileSize + screenWidth)
				bgX = -tileMapWidth * tileSize + screenWidth;
			else {
				bgX -= bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			}
		}
	}

	public void bgAdjust() {
		if (!Player.getMoving()) {
			if (Math.abs(bgX) % tileSize != 0) {
				System.out.println("Horizontal Shift " + Math.abs(bgX) % tileSize + " " + Player.getDirection());
				if (Player.getDirection() == 3) {
					System.out.println("Slide to the left");
					bgX += defaultShiftPixels;
				} else if (Player.getDirection() == 4) {
					System.out.println("Slide to the right");
					bgX -= defaultShiftPixels;
				}
			} else if (Math.abs(bgY) % tileSize != 0) {
				System.out.println("Vertical Shift " + Math.abs(bgY) % tileSize + " " + Player.getDirection());
				if (Player.getDirection() == 1) {
					System.out.println("Slide up");
					bgY += defaultShiftPixels;
				} else if (Player.getDirection() == 2) {
					System.out.println("Slide down");
					bgY -= defaultShiftPixels;
				}
			}
		}
	}

	public static void adjustWalls() {
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < 48; j++) {
				if (allWalls[i][j] != null) {
					allWalls[i][j].x = j * 64 + bgX;
					allWalls[i][j].y = i * 64 + bgY;
				}
			}
		}
	}

	void checkCollision(Wall wall) {
		// check if Player.hitbox touches wall
		if (Player.hitbox.intersects(wall)) {
			// stop the Player.hitbox from moving
			double left1 = Player.hitbox.getX();
			double right1 = Player.hitbox.getX() + Player.hitbox.getWidth();
			double top1 = Player.hitbox.getY();
			double bottom1 = Player.hitbox.getY() + Player.hitbox.getHeight();
			double left2 = wall.getX();
			double right2 = wall.getX() + wall.getWidth();
			double top2 = wall.getY();
			double bottom2 = wall.getY() + wall.getHeight();

			if (right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 && right1 - left2 < bottom2 - top1) {
				// Player.hitbox collides from left side of the wall
				collisionRight = true;
			} else if (left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2
					&& right2 - left1 < bottom2 - top1) {
				// Player.hitbox collides from right side of the wall
				collisionLeft = true;
			} else if (bottom1 > top2 && top1 < top2) {
				// Player.hitbox collides from top side of the wall
				collisionDown = true;
			} else if (top1 < bottom2 && bottom1 > bottom2) {
				// Player.hitbox collides from bottom side of the wall
				collisionUp = true;
			}
		}
	}

	public static boolean checkTile() {
		if (Math.abs(bgX) % tileSize != 0 && Math.abs(bgY) % tileSize != 0) {
			return true;
		}
		return false;

	}

	public static void displayText(Graphics g, HashMap<String, Integer> map, BufferedImage[] images, String text, int x,
			int y) {
		int xPos = x;
		int yPos = y;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == ' ') {
				xPos += 14;
			} else if (text.charAt(i) == '~') { // newline
				xPos = x;
				yPos += 64;

			} else {
				int idx;
				if (text.charAt(i) == '\'') {
					idx = 75;
				} else {
					idx = map.get("" + text.charAt(i));
				}
				g.drawImage(images[idx], xPos, yPos, null);
				xPos += images[idx].getWidth() - images[idx].getWidth() / 4 - 1;
			}

		}

	}

	public static void displaySymbol(Graphics g, HashMap<String, Integer> map, BufferedImage[] images, String sym,
			int x, int y) {
		int idx = map.get(sym);
		g.drawImage(images[idx], x, y, null);

	}

	public static void baseBattleGraphics(Graphics g) {
		g.drawImage(Images.battleMenu[5], 0, 640 - Images.battleMenu[5].getHeight(), null);
		g.drawImage(Images.battleMenu[0], 48, 64, null);
		g.drawImage(Images.battleMenu[1], 484, 284, null);
	}

	public static void displayPokemonStats(Graphics g) {
		g.drawImage(Images.battleMenu[0], 48, 64, null);
		g.drawImage(Images.battleMenu[1], 484, 284, null);
		displayText(g, Images.battleFontIdx, Images.battleFont, battle.getOtherMon().getName().toUpperCase(), 80, 88);
		displayText(g, Images.battleFontIdx, Images.battleFont, battle.getPlayerMon().getName().toUpperCase(), 556,
				313);
		displayText(g, Images.battleFontIdx, Images.battleFont, battle.getOtherMon().getLevel() + "", 379, 88);
		displayText(g, Images.battleFontIdx, Images.battleFont, battle.getPlayerMon().getLevel() + "", 850, 312);
	}

	public static void displayBattleSprites(Graphics g) {
		g.drawImage(battle.getOtherMon().getOtherSprite(), 576, 32, null);
		g.drawImage(battle.getPlayerMon().getPlayerSprite(), 128, 196, null);
	}

	public static void displayOptionsMenuAndArrow(Graphics g) {
		g.drawImage(Images.battleMenu[2], 960 - Images.battleMenu[2].getWidth(), 640 - Images.battleMenu[2].getHeight(),
				null);
		g.drawImage(Images.battleMenu[3], optionsArrowX, optionsArrowY, null);
	}

	public static void displayAttackMenuAndArrow(Graphics g) {
		g.drawImage(Images.battleMenu[4], 960 - Images.battleMenu[4].getWidth(), 640 - Images.battleMenu[4].getHeight(),
				null);
		g.drawImage(Images.battleMenu[3], attackArrowX, attackArrowY, null);
	}

	public static void displayAttacks(Graphics g) {
		if (battle.getPlayerMon().getMoves()[0] != null) {
			displayText(g, Images.moveFontIdx, Images.moveFont,
					battle.getPlayerMon().getMoves()[0].getName().toUpperCase(), 64, 490);
		}
		if (battle.getPlayerMon().getMoves()[1] != null) {
			displayText(g, Images.moveFontIdx, Images.moveFont,
					battle.getPlayerMon().getMoves()[1].getName().toUpperCase(), 360, 490);
		}
		if (battle.getPlayerMon().getMoves()[2] != null) {
			displayText(g, Images.moveFontIdx, Images.moveFont,
					battle.getPlayerMon().getMoves()[2].getName().toUpperCase(), 64, 554);
		}
		if (battle.getPlayerMon().getMoves()[3] != null) {
			displayText(g, Images.moveFontIdx, Images.moveFont,
					battle.getPlayerMon().getMoves()[3].getName().toUpperCase(), 360, 554);
		}
		displayText(g, Images.moveFontIdx, Images.moveFont,
				battle.getPlayerMon().getMoves()[attackArrowIdx].getCurrentPP() + "", 790, 496);
		displayText(g, Images.moveFontIdx, Images.moveFont,
				battle.getPlayerMon().getMoves()[attackArrowIdx].getMaxPP() + "", 880, 496);
		displayText(g, Images.moveFontIdx, Images.moveFont,
				battle.getPlayerMon().getMoves()[attackArrowIdx].getType() + "", 764, 560);
	}

	public static void displayStatus(Graphics g) {
		if (battle.getPlayerMon().getStatus() > 0) {
			if (battle.getPlayerMon().getStatus() == 5) {
				g.drawImage(Images.statusIcons[0], 544, 356, null);
			} else {
				g.drawImage(Images.statusIcons[battle.getPlayerMon().getStatus() - 1], 544, 356, null);
			}
		}
		if (battle.getOtherMon().getStatus() > 0) {
			g.drawImage(Images.statusIcons[battle.getOtherMon().getStatus() - 1], 72, 128, null);
		}

	}

	public static void displayHealth(Graphics g) {
		if (battleState < 8) {
			double percentHp = battle.getOtherMon().getCurrentHp() / (battle.getOtherMon().getBaseHp() * 1.0);
			int pixelsHealth = (int) Math.round(48 * percentHp);
			BufferedImage barColor;
			if (pixelsHealth >= 48 * 0.75) {
				barColor = Images.healthBars[0];
			} else if (pixelsHealth > 48 * 0.25) {
				barColor = Images.healthBars[1];
			} else {
				barColor = Images.healthBars[2];
			}
			for (int i = 0; i < pixelsHealth; i++) {
				g.drawImage(barColor, 216 + i * 4, 140, null);
			}

			percentHp = battle.getPlayerMon().getCurrentHp() / (battle.getPlayerMon().getBaseHp() * 1.0);
			pixelsHealth = (int) Math.round(48 * percentHp);
			if (pixelsHealth >= 48 * 0.75) {
				barColor = Images.healthBars[0];
			} else if (pixelsHealth > 48 * 0.25) {
				barColor = Images.healthBars[1];
			} else {
				barColor = Images.healthBars[2];
			}
			for (int i = 0; i < pixelsHealth; i++) {
				g.drawImage(barColor, 688 + i * 4, 364, null);
			}
		} else if (battleState == 8) {
			double percentHp = battle.getPlayerMon().getCurrentHp() / (battle.getPlayerMon().getBaseHp() * 1.0);
			int pixelsHealth = (int) Math.round(48 * percentHp);
			BufferedImage barColor;
			if (pixelsHealth >= 48 * 0.75) {
				barColor = Images.healthBars[0];
			} else if (pixelsHealth > 48 * 0.25) {
				barColor = Images.healthBars[1];
			} else {
				barColor = Images.healthBars[2];
			}
			for (int i = 0; i < pixelsHealth; i++) {
				g.drawImage(barColor, 128 + i * 4, 236, null);
			}
			for (int i = 1; i < 3; i++) {
				percentHp = battle.getPlayer().getPokemonList()[pkmnListIdx[i]].getCurrentHp()
						/ (battle.getPlayer().getPokemonList()[pkmnListIdx[i]].getBaseHp() * 1.0);
				pixelsHealth = (int) Math.round(48 * percentHp);
				if (pixelsHealth >= 48 * 0.75) {
					barColor = Images.healthBars[0];
				} else if (pixelsHealth > 48 * 0.25) {
					barColor = Images.healthBars[1];
				} else {
					barColor = Images.healthBars[2];
				}
				for (int j = 0; j < pixelsHealth; j++) {
					g.drawImage(barColor, 736 + j * 4, 72 + 96 * (i - 1), null);
				}
			}

		}

	}
}
