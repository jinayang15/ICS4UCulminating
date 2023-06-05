import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial") // funky warning, just suppress it. It's not gonna do anything.
// will probably be moved to main or rename, empty frame is the temp name
public class EmptyFrame extends JPanel implements Runnable, KeyListener, MouseListener {
	/*
	 * 0 - initial menu 
	 * 1 - Options Menu
	 * 2 - Pewter City 
	 * 3 - Battle 
	 * 4 - PokeCenter 
	 * 5 - PokeMart 
	 */
	public static int gameState = 0;
	// self explanatory variables
	int FPS = 60;
	Thread thread;
	int screenWidth = 960;
	int screenHeight = 640;
	BufferedImage currentBG;
	// background top-left corner position, x and y value
	int bgX = 0;
	int bgY = 0;

	public EmptyFrame() {
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

	public void initialize() throws IOException {
		// setups before the game starts running
		try {
			Images.importAllImages();
		} catch (FileNotFoundException e) {
		}

	}

	public void update() {
		// update stuff

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (gameState == 0)
			currentBG = Images.fireRedPressStart;
		else if (gameState == 1) {
			
		}
		else if (gameState == 2) {
			currentBG = Images.pewterCity[0];
			bgX = -722;
			bgY = -1920;
		}

		g.drawImage(currentBG, bgX, bgY, null);

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (gameState == 0)
			gameState++;
		else
			gameState = 0;
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

	public static void main(String[] args) {

		// The following lines creates your window

		// makes a brand new JFrame
		JFrame frame = new JFrame("PokemonFireRed");
		// makes a new copy of your "game" that is also a JPanel
		EmptyFrame myPanel = new EmptyFrame();
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
}
