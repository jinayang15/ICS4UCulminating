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
	public static int gameState = 0;
	public static Player player;
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
			this.repaint();
			update();
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
			GameFunctions.importEverything();
			Images.importAllImages();
			PokeType.addToChart();
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
			g.drawImage(Images.battleMenu[5], 0, 640-Images.battleMenu[5].getHeight(), null);
			g.drawImage(Images.battleMenu[2], 960-Images.battleMenu[2].getWidth(), 640-Images.battleMenu[2].getHeight(), null);
//			for (int i = 0; i < 26; i++) {
//				g.drawImage(Images.battleFont[i], 200 + (i)*30, 200, null);
//				System.out.println(Images.battleFont[i].getWidth());
//			}
			displayText(g, Images.battleFontIdx, Images.battleFont, "Elephant", 200, 300);
//			displayText(g, Images.battleFontIdx, Images.battleFont, "abcdefghijklmnopqrstuvwxyz", 200, 250);
//			displayText(g, Images.battleFontIdx, Images.battleFont, "0123456789.,!?/-", 200, 300);
//			displaySymbol(g, Images.battleFontIdx, Images.battleFont, "boy", 200, 350);
			
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
		} else if (gameState == 3) {
			gameState = 0;
			bgX = 0;
			bgY = 0;
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
		initialize();
		Player player = new Player("Fire");
		System.out.println();
		System.out.println(new Trainer());
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

	public static void startBattle() {
	}

	public static void displayText(Graphics g, HashMap<String, Integer> map, BufferedImage[] images, String text, int x,
			int y) {
		int xPos = x;
		for (int i = 0; i < text.length(); i++) {
			int idx = map.get(""+text.charAt(i));
			g.drawImage(images[idx], xPos, y, null);
			xPos += images[idx].getWidth()-4;

		}

	}

	public static void displaySymbol(Graphics g, HashMap<String, Integer> map, BufferedImage[] images, String sym,
			int x, int y) {
		int idx = map.get(sym);
		g.drawImage(images[idx], x, y, null);

	}
}
