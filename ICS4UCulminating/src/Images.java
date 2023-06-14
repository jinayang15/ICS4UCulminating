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
	static HashMap<String, Integer> battleFontIdx = new HashMap<String, Integer>();
	public static BufferedImage[] battleFont = new BufferedImage[78];
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

	public static void importBattleBackground() throws IOException {
		BufferedImage battleBackgroundSheet = ImageIO.read(new File("BattleBackgrounds.png"));
		battleBackground = battleBackgroundSheet.getSubimage(6, 235, 240, 112);
		battleBackground = resizeImage(battleBackground, 960, 640);
	}

	// subImage is exclusive corner coordinate
	public static void importBattleMenu() throws IOException {
		BufferedImage battleMenuSheet = ImageIO.read(new File("InBattleMenu.png"));
		battleMenu[0] = battleMenuSheet.getSubimage(0, 0, 112, 32); // enemy hp and lvl
		battleMenu[1] = battleMenuSheet.getSubimage(0, 40, 112, 44); // our hp and lvl
		battleMenu[2] = battleMenuSheet.getSubimage(146, 3,120, 48); // options
		battleMenu[3] = battleMenuSheet.getSubimage(268, 3, 7, 17); // arrow
		battleMenu[4] = battleMenuSheet.getSubimage(297, 3, 240, 48); // attacks
		battleMenu[5] = battleMenuSheet.getSubimage(297, 55, 240, 48); // text box

		for (int i = 0; i < battleMenu.length; i++) {
			if (battleMenu[i] != null) {
				battleMenu[i] = resizeImage(battleMenu[i], battleMenu[i].getWidth() * 4, battleMenu[i].getHeight() * 4);
			}
		}
		int xPos = 170;
		for (int i = 0; i < 28; i++) {
			if (i == 24 || i == 19  || i ==  8) {
				battleFont[i] = battleMenuSheet.getSubimage(xPos, 122, 6, 12);
				battleFont[i] = resizeImage(battleFont[i], battleFont[i].getWidth() * 4, battleFont[i].getHeight() * 4);
				xPos += 6;
			} else {
				battleFont[i] = battleMenuSheet.getSubimage(xPos, 122, 7, 12);
				battleFont[i] = resizeImage(battleFont[i], battleFont[i].getWidth() * 4, battleFont[i].getHeight() * 4);
				xPos += 7;
			}
			if (i < 26) {
				battleFontIdx.put("" + (char) ('A' + i), i);
			}
		}
		battleFontIdx.put(".", 26);
		battleFontIdx.put(",", 27);
		xPos = 170;
		for (int i = 0; i < 26; i++) {
			if (i == 8) {
				battleFont[i + 28] = battleMenuSheet.getSubimage(xPos, 141, 4, 12);
				battleFont[i + 28] = resizeImage(battleFont[i + 28], battleFont[i + 28].getWidth() * 4,
						battleFont[i + 28].getHeight() * 4);
				xPos += 4;
			} else if (i == 11) {
				battleFont[i + 28] = battleMenuSheet.getSubimage(xPos, 141, 5, 12);
				battleFont[i + 28] = resizeImage(battleFont[i + 28], battleFont[i + 28].getWidth() * 4,
						battleFont[i + 28].getHeight() * 4);
				xPos += 5;
			} else if (i == 9 || i == 15) {
				battleFont[i + 28] = battleMenuSheet.getSubimage(xPos, 141, 6, 12);
				battleFont[i + 28] = resizeImage(battleFont[i + 28], battleFont[i + 28].getWidth() * 4,
						battleFont[i + 28].getHeight() * 4);
				xPos += 6;
			} else {
				battleFont[i + 28] = battleMenuSheet.getSubimage(xPos, 141, 7, 12);
				battleFont[i + 28] = resizeImage(battleFont[i + 28], battleFont[i + 28].getWidth() * 4,
						battleFont[i + 28].getHeight() * 4);
				xPos += 7;
			}
			battleFontIdx.put("" + (char) ('a' + i), i + 28);
		}
		xPos = 170;
		for (int i = 0; i < 10; i++) {
			if (i == 1) {
				battleFont[i + 54] = battleMenuSheet.getSubimage(xPos, 157, 6, 12);
				battleFont[i + 54] = resizeImage(battleFont[i + 54], battleFont[i + 54].getWidth() * 4,
						battleFont[i + 54].getHeight() * 4);
				xPos += 6;
			} else {
				battleFont[i + 54] = battleMenuSheet.getSubimage(xPos, 157, 7, 12);
				battleFont[i + 54] = resizeImage(battleFont[i + 54], battleFont[i + 54].getWidth() * 4,
						battleFont[i + 54].getHeight() * 4);
				xPos += 7;
			}
			battleFontIdx.put("" + (char) ('0' + i), i + 54);

		}
		xPos = 170;
		for (int i = 0; i < 13; i++) {
			if (i == 0) {
				battleFont[i + 65] = battleMenuSheet.getSubimage(xPos, 174, 4, 12);
				battleFont[i + 65] = resizeImage(battleFont[i + 65], battleFont[i + 65].getWidth() * 4,
						battleFont[i + 65].getHeight() * 4);
				xPos += 4;
			} else {
				battleFont[i + 65] = battleMenuSheet.getSubimage(xPos, 174, 7, 12);
				battleFont[i + 65] = resizeImage(battleFont[i + 65], battleFont[i + 65].getWidth() * 4,
						battleFont[i + 65].getHeight() * 4);
				xPos += 7;
			}
			battleFontIdx.put("!", 65);
			battleFontIdx.put("?", 66);
			battleFontIdx.put("boyB", 67);
			battleFontIdx.put("girlB", 68);
			battleFontIdx.put("/", 69);
			battleFontIdx.put("-", 70);
			battleFontIdx.put("..", 71);
			battleFontIdx.put("\"f", 72);
			battleFontIdx.put("\"b", 73);
			battleFontIdx.put("\'f", 74);
			battleFontIdx.put("\'b", 75);
			battleFontIdx.put("boy", 76);
			battleFontIdx.put("girl", 77);
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
