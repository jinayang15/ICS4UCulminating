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
	static int bgShiftPixels = 4;
	static boolean bgAdjusting = false;
	// last bg position
	int saveBGX = -960;
	int saveBGY = -1920;

	long lastActionTime = 0;
	char lastKeyPressed = ' ';
	char lastKeyReleased = ' ';

	public static Rectangle[][] allWalls = new Rectangle[tileMapHeight][tileMapWidth];
	public static Rectangle[][] currentWindowWalls = new Rectangle[tileScreenHeight][tileScreenWidth];
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
			Animations.walk();
			Animations.resetWalk();
			adjustWalls();
			for (int i = 0; i < 40; i++) {
				for (int j = 0; j < 48; j++) {
					if (allWalls[i][j] != null) {
						checkCollision(allWalls[i][j]);
					}
				}
			}
			System.out.println(collisionUp + " " + collisionDown + " " + collisionLeft + " " + collisionRight);
			bgShift();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(currentBG, bgX, bgY, null);
		if (gameState == 2) {
			g.drawImage(Player.getCurrentPlayerImage(), Player.hitbox.x, Player.hitbox.y, null);
//			for (int i = 0; i < 40; i++) {
//				for (int j = 0; j < 48; j++) {
//					if (allWalls[i][j] != null) {
//						g.setColor(Color.RED);
//						g.fillRect(allWalls[i][j].x, allWalls[i][j].y, 64, 64);
//					}
//				}
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

		if (e.getKeyChar() != lastKeyPressed) {
			if (e.getKeyChar() == 'w') {
				Player.setDirection(1);
				Player.setMoving(true);
			} else if (e.getKeyChar() == 's') {
				Player.setDirection(2);
				Player.setMoving(true);
			} else if (e.getKeyChar() == 'a') {
				Player.setDirection(3);
				Player.setMoving(true);
			} else if (e.getKeyChar() == 'd') {
				Player.setDirection(4);
				Player.setMoving(true);
			}
			lastKeyPressed = e.getKeyChar();
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
		} else {
			gameState = 0;
			saveBGX = bgX;
			saveBGY = bgY;
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
		System.out.println(new Player("Fire"));
		System.out.println(new Trainer());
	}

	// background shifts with the player
	public void bgShift() {
		if (Player.getMoving()) {
			if (Player.getDirection() == 1 && !collisionUp) {
				if (bgY + bgShiftPixels > 0)
					bgY = 0;
				else
					bgY += bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			} else if (Player.getDirection() == 2 && !collisionDown) {
				if (bgY - bgShiftPixels < -tileMapHeight * tileSize + screenHeight)
					bgY = -tileMapHeight * tileSize + screenHeight;
				else
					bgY -= bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			} else if (Player.getDirection() == 3 && !collisionLeft) {
				if (bgX + bgShiftPixels > 0)
					bgX = 0;
				else
					bgX += bgShiftPixels;
				collisionUp = false;
				collisionDown = false;
				collisionLeft = false;
				collisionRight = false;
			} else if (Player.getDirection() == 4 && !collisionRight) {
				if (bgX - bgShiftPixels < -tileMapWidth * tileSize + screenWidth)
					bgX = -tileMapWidth * tileSize + screenWidth;
				else
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
					bgX += bgShiftPixels;
				} else if (Player.getDirection() == 4) {
					System.out.println("Slide to the right");
					bgX -= bgShiftPixels;
				}
			} else if (Math.abs(bgY) % tileSize != 0) {
				System.out.println("Vertical Shift " + Math.abs(bgY) % tileSize + " " + Player.getDirection());
				if (Player.getDirection() == 1) {
					System.out.println("Slide up");
					bgY += bgShiftPixels;
				} else if (Player.getDirection() == 2) {
					System.out.println("Slide down");
					bgY -= bgShiftPixels;
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

	void checkCollision(Rectangle wall) {
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
				Player.hitbox.x = wall.x - Player.hitbox.width;
				collisionRight = true;
			} else if (left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2
					&& right2 - left1 < bottom2 - top1) {
				// Player.hitbox collides from right side of the wall
				Player.hitbox.x = wall.x + wall.width;
				collisionLeft = true;
			} else if (bottom1 > top2 && top1 < top2) {
				// Player.hitbox collides from top side of the wall
				Player.hitbox.y = wall.y - Player.hitbox.height;
				collisionDown = true;
			} else if (top1 < bottom2 && bottom1 > bottom2) {
				// Player.hitbox collides from bottom side of the wall
				Player.hitbox.y = wall.y + wall.height;
				collisionUp = true;
			}
		}
	}

	public static void changeDirection() {

	}
}
