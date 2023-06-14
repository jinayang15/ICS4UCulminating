import java.util.*;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.AffineTransformOp;

public class Images {

	public static BufferedImage[] pewterCity = new BufferedImage[4];
	public static BufferedImage[] trainerDown = new BufferedImage[3];
	public static BufferedImage[] trainerUp = new BufferedImage[3];
	public static BufferedImage[] trainerLeft = new BufferedImage[3];
	public static BufferedImage[] trainerRight = new BufferedImage[3];
	public static BufferedImage[] battleMenu = new BufferedImage[10];
	public static BufferedImage[] battleFont = new BufferedImage[76];
	public static BufferedImage fireRedPressStart;
	public static BufferedImage battleBackground;

	public static void importAllImages() throws IOException {
		importMisc();
		importPewterCity();
		importTrainer();
		importBattleBackground();
		importBattleMenu();
	}

	public static void importMisc() throws IOException {
		fireRedPressStart = ImageIO.read(new File("FireRedPressStart.png"));
		fireRedPressStart = resizeImage(fireRedPressStart, 960, 640);
	}

	public static void importPewterCity() throws IOException {
		BufferedImage pewterCitySheet = ImageIO.read(new File("PewterCitySheetTiles.png"));
		pewterCity[0] = pewterCitySheet.getSubimage(0, 0, 768, 640);
		pewterCity[0] = resizeImage(pewterCity[0], Main.tileSize * Main.tileMapWidth,
				Main.tileSize * Main.tileMapHeight);
	}

	public static void importTrainer() throws IOException {
		BufferedImage trainerSprites = ImageIO.read(new File("trainerSprites.png"));
		for (int i = 0; i < 3; i++) {
			trainerDown[i] = trainerSprites.getSubimage(8 + 16 * i, 36, 16, 20);
			trainerDown[i] = resizeImage(trainerDown[i], 64, 80);
		}
		for (int i = 0; i < 3; i++) {
			trainerUp[i] = trainerSprites.getSubimage(8 + 16 * i, 68, 16, 20);
			trainerUp[i] = resizeImage(trainerUp[i], 64, 80);
		}

		for (int i = 0; i < 3; i++) {
			trainerLeft[i] = trainerSprites.getSubimage(8 + 16 * i, 100, 16, 20);
			trainerLeft[i] = resizeImage(trainerLeft[i], 64, 80);
			// flips the image horizontally
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-trainerLeft[i].getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			// -------------------------------------------
			trainerRight[i] = op.filter(trainerLeft[i], null);
		}
	}
	public static void importBattleBackground() throws IOException{
		BufferedImage battleBackgroundSheet = ImageIO.read(new File("BattleBackgrounds.png"));
		battleBackground = battleBackgroundSheet.getSubimage(6, 235, 240, 112);
		battleBackground = resizeImage(battleBackground, 960, 640);
	}
	public static void importBattleMenu() throws IOException {
		BufferedImage battleMenuSheet = ImageIO.read(new File("InBattleMenu.png"));
		battleMenu[0] = battleMenuSheet.getSubimage(0, 0, 112, 32); // enemy hp and lvl
		battleMenu[1] = battleMenuSheet.getSubimage(0, 40, 112, 44); // our hp and lvl
		battleMenu[2] = battleMenuSheet.getSubimage(144, 4, 124, 50); // options
		battleMenu[3] = battleMenuSheet.getSubimage(268, 4, 7, 17); // arrow
		battleMenu[4] = battleMenuSheet.getSubimage(297,4, 240, 48); // attacks
		battleMenu[5] = battleMenuSheet.getSubimage(297, 56, 240, 48); // text box
		
		
		
		for (int i = 0; i < battleMenu.length; i++) {
			if (battleMenu[i] != null) {
				battleMenu[i] = resizeImage(battleMenu[i], battleMenu[i].getWidth()*4, battleMenu[i].getHeight()*4);
			}
		}
		for (int i = 0; i < 28; i++) {
			System.out.println(i + " ");
			battleFont[i] = battleMenuSheet.getSubimage(170 + i*6, 123, 6, 12);
			battleFont[i] = resizeImage(battleFont[i], battleFont[i].getWidth()*4, battleMenu[i].getHeight()*4);
		}
		for (int i = 0; i < 28; i++) {
			battleFont[i] = battleMenuSheet.getSubimage(170 + i*6, 142, 6, 12);
			battleFont[i] = resizeImage(battleFont[i], battleFont[i].getWidth()*4, battleMenu[i].getHeight()*4);
		}

	}

	public static BufferedImage resizeImage(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
}
