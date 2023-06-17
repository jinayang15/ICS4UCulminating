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
	/*
	 * 0 - initial menu 1 - Options Menu 2 - Pewter City 3 - Battle 4 - PokeCenter 5
	 * - PokeMart
	 */
	static int gameState = 0;
	// 0 - not in battle
	// 1 - options
	// 2 - moves
	// 3 - turn check
	// 4 - player turn
	// 5 - enemy turn
	// 6 - player effect
	// 7 - enemy effect
	static int battleState = 0;
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
	private static int[][] optionsArrowPositions = { { 512, 496 }, { 736, 496 }, { 512, 560 }, { 736, 560 } };
	private static int optionsArrowX = 512;
	private static int optionsArrowY = 496;
	private static int optionsArrowIdx = 0;

	private static int[][] attackArrowPositions = { { 36, 490 }, { 332, 490 }, { 36, 554 }, { 332, 554 } };
	private static int attackArrowX = 36;
	private static int attackArrowY = 490;
	private static int attackArrowIdx = 0;

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
				Thread.sleep(1000 / FPS);
			} catch (Exception e) {
				e.printStackTrace();
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

			// bgAdjust();
			bgShift();
			// System.out.println(collisionUp + " " + collisionDown + " " + collisionLeft +
			// " " + collisionRight);
		} else if (gameState == 3) {
			currentBG = Images.battleBackground;
		}
		if (battleState == 3) {
			System.out.println("hi");
			battle.setPlayerCurrentMove(battle.getPlayerMon().getMoves()[attackArrowIdx]);
			battle.setOtherCurrentMove(battle.opponentChooseAttack());
			battle.setRoundEnd(false);
			if (battle.turnCheck()) {
				battleState = 4;
			} else {
				battleState = 5;
			}
		} else if (battleState == 4) {
			battle.checkFaint(battle.getPlayerMon());
			battle.checkFaint(battle.getOtherMon());
			if (battle.checkBattle()) {
				if (!battle.getPlayerSkipTurn() && !battle.isPlayerAttacking()) {
					battle.attack(battle.getPlayerCurrentMove(), battle.getPlayerMon(), battle.getOtherMon());
					battle.setPlayerAttacking(true);
				} else if (battle.getPlayerMon().getFaint()) {

				}
			}

		} else if (battleState == 5) {
			battle.checkFaint(battle.getPlayerMon());
			battle.checkFaint(battle.getOtherMon());
			if (battle.checkBattle()) {
				if (!battle.getPlayerSkipTurn() && !battle.isOtherAttacking()) {
					battle.attack(battle.getOtherCurrentMove(), battle.getOtherMon(), battle.getPlayerMon());
					battle.setOtherAttacking(true);
				} else if (battle.getOtherMon().getFaint()) {
					battle.setOtherMon(battle.otherChooseNewPokemon());
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentBG, bgX, bgY, null);
		if (gameState == 2) {
			g.drawImage(Player.getCurrentPlayerImage(), Player.getPlayerX(), Player.getPlayerY(), null);
			g.drawRect(Player.hitbox.x, Player.hitbox.y, Player.hitbox.width, Player.hitbox.height);
//			System.out.println(Player.getPlayerX() + " " + Player.getPlayerY());
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
			baseBattleGraphics(g);
			displayBattleSprites(g);
			displayPokemonStats(g);
			displayStatus(g);
			displayHealth(g);
			if (battleState == 1) {
				// reset moves after attack round and pause to show attacks
				if (battle.isPlayerAttacking()) {
					battle.setPlayerCurrentMove(null);
					battle.setOtherCurrentMove(null);
					battle.setPlayerAttacking(false);
					battle.setOtherAttacking(false);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				displayOptionsMenuAndArrow(g);
				displayText(g, Images.whiteFontIdx, Images.whiteFont,
						"What will~" + battle.getPlayerMon().getName().toUpperCase() + " do?", 40,
						640 - Images.battleMenu[4].getHeight() + 40);
			} else if (battleState == 2) {
				displayAttackMenuAndArrow(g);
				displayAttacks(g);
			} else if (battleState == 4) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (battle.getPlayerSkipTurn()) {
					if (battle.getPlayerMon().getStatus() == 2) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayerMon().getName().toUpperCase() + " is paralyzed.~It is unable to move!",
								40, 488);
					} else if (battle.getPlayerMon().getStatus() == 3) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayerMon().getName().toUpperCase() + " is fast asleep.", 40, 488);
					}
				} else {
					if (battle.getPlayerMon().getStatus() == 2) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getPlayerMon().getName().toUpperCase() + " is paralyzed.", 40, 488);
					}
					System.out.println("Our attack");
					displayText(g, Images.whiteFontIdx, Images.whiteFont, battle.getPlayerMon().getName().toUpperCase()
							+ " used~" + battle.getPlayerCurrentMove().getName().toUpperCase() + "!", 40, 488);
				}
				if (!battle.getRoundEnd()) {
					battleState = 5;
					battle.setRoundEnd(true);
				} else {
					battleState = 1;
				}
			} else if (battleState == 5) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (battle.getOtherSkipTurn()) {
					if (battle.getOtherMon().getStatus() == 2) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getOtherMon().getName().toUpperCase() + " is paralyzed.~It is unable to move!",
								40, 488);
					} else if (battle.getOtherMon().getStatus() == 3) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getOtherMon().getName().toUpperCase() + " is fast asleep.", 40, 488);
					}
				} else {
					if (battle.getOtherMon().getStatus() == 2) {
						displayText(g, Images.whiteFontIdx, Images.whiteFont,
								battle.getOtherMon().getName().toUpperCase() + " is paralyzed.", 40, 488);
					}
					System.out.println("enemy attack");
					displayText(g, Images.whiteFontIdx, Images.whiteFont,
							"Enemy " + battle.getOtherMon().getName().toUpperCase() + " used~"
									+ battle.getOtherCurrentMove().getName().toUpperCase() + "!",
							40, 488);
				}
				if (!battle.getRoundEnd()) {
					battleState = 4;
					battle.setRoundEnd(true);
				} else {
					battleState = 1;
				}
			}
			// Sprites and Pop-ups
//			g.drawImage(Images.battleSprites[Images.battleSpritesIdx
//					.get(Pokemon.pokeList.get(spriteIdx).getName().toLowerCase())][0], 576, 32, null);
//			g.drawImage(Images.battleSprites[Images.battleSpritesIdx
//					.get(Pokemon.pokeList.get(spriteIdx).getName().toLowerCase())][1], 128, 196, null);
//			g.drawImage(Images.battleMenu[4], 0, 640 - Images.battleMenu[4].getHeight(), null);
// 			g.drawImage(Images.battleMenu[5], 0, 640 - Images.battleMenu[5].getHeight(), null);
// 			g.drawImage(Images.battleMenu[2], 960 - Images.battleMenu[2].getWidth(), 640 - Images.battleMenu[2].getHeight(), null);
//			g.drawImage(Images.battleMenu[0], 48, 64, null);
//			g.drawImage(Images.battleMenu[1], 484, 284, null);

//			for (int i = 0; i < 26; i++) {
//				g.drawImage(Images.battleFont[i], 200 + (i)*30, 200, null);
//				System.out.println(Images.battleFont[i].getWidth());
//			}
//			g.drawImage(Images.battleMenu[3], 512, 496, null);
//			g.drawImage(Images.battleMenu[3], 512, 560, null);
//			g.drawImage(Images.battleMenu[3], 736, 496, null);
//			g.drawImage(Images.battleMenu[3], 736, 560, null);

			// Pokemon Names
//			displayText(g, Images.battleFontIdx, Images.battleFont,
//					Pokemon.pokeList.get(spriteIdx).getName().toUpperCase(), 80, 88);
//			displayText(g, Images.battleFontIdx, Images.battleFont,
//					Pokemon.pokeList.get(spriteIdx).getName().toUpperCase(), 556, 313);
//			displayText(g, Images.battleFontIdx, Images.battleFont, "99", 379, 88);
//			displayText(g, Images.battleFontIdx, Images.battleFont, "99", 850, 312);

			// Attack Display
//			g.drawImage(Images.battleMenu[3], 36, 490, null);
//			g.drawImage(Images.battleMenu[3], 332, 490, null);
//			g.drawImage(Images.battleMenu[3], 36, 554, null);
//			g.drawImage(Images.battleMenu[3], 332, 554, null);
//			try {
//				if (Pokemon.pokeList.get(spriteIdx).getMoves()[0] != null) {
//					displayText(g, Images.attackFontIdx, Images.attackFont,
//							Pokemon.pokeList.get(spriteIdx).getMoves()[0].getName(), 64, 490);
//				}
//				if (Pokemon.pokeList.get(spriteIdx).getMoves()[1] != null) {
//					displayText(g, Images.attackFontIdx, Images.attackFont,
//							Pokemon.pokeList.get(spriteIdx).getMoves()[1].getName(), 360, 490);
//				}
//				if (Pokemon.pokeList.get(spriteIdx).getMoves()[2] != null) {
//					displayText(g, Images.attackFontIdx, Images.attackFont,
//							Pokemon.pokeList.get(spriteIdx).getMoves()[2].getName(), 64, 554);
//				}
//				if (Pokemon.pokeList.get(spriteIdx).getMoves()[3] != null) {
//					displayText(g, Images.attackFontIdx, Images.attackFont,
//							Pokemon.pokeList.get(spriteIdx).getMoves()[3].getName(), 360, 554);
//				}
//			} catch (InterruptedException e) {
//
//			}
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

			} else if (x == 'e' && optionsArrowIdx == 0) {
				battleState = 2;
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
			}
			attackArrowX = attackArrowPositions[attackArrowIdx][0];
			attackArrowY = attackArrowPositions[attackArrowIdx][1];
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
			battleState++;
//			spriteIdx++;
//			System.out.println(Pokemon.pokeList.get(spriteIdx).getName().toLowerCase() + " "
//					+ Images.battleSpritesIdx.get(Pokemon.pokeList.get(spriteIdx).getName().toLowerCase()));
//			spriteTest = Images.battleSprites[Images.battleSpritesIdx
//					.get(Pokemon.pokeList.get(spriteIdx).getName().toLowerCase())];
//			System.out.println(spriteTest);

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
				int idx = map.get("" + text.charAt(i));
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
			g.drawImage(Images.statusIcons[battle.getPlayerMon().getStatus() - 1], 544, 356, null);
		}
		if (battle.getOtherMon().getStatus() > 0) {
			g.drawImage(Images.statusIcons[battle.getOtherMon().getStatus() - 1], 72, 128, null);
		}

	}

	public static void displayHealth(Graphics g) {
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

	}

	public static void displayEffects(Graphics g) {

		displayText(g, Images.whiteFontIdx, Images.whiteFont,
				battle.getPlayerMon().getName().toUpperCase() + " is fast asleep.", 40, 488);
	}
}
