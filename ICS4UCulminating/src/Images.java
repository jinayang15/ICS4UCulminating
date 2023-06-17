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
	static HashMap<String, Integer> battleSpritesIdx = new HashMap<String, Integer>();
	public static BufferedImage[][] battleSprites = new BufferedImage[150][2]; // attack - 0, defense - 1
	static HashMap<String, Integer> attackFontIdx = new HashMap<String, Integer>();
	public static BufferedImage[] attackFont = new BufferedImage[78];
	public static BufferedImage fireRedPressStart;
	public static BufferedImage battleBackground;
	public static BufferedImage aboutUs;
	public static BufferedImage instructions;
	public static BufferedImage instructions2;
	public static BufferedImage start1;
	public static BufferedImage start2;
	public static BufferedImage pokemonCenter;

	public static void importAllImages() throws IOException {
		importMisc();
		importPewterCity();
		importPokemonCenter();
		importTrainer();
		importBattleBackground();
		importBattleMenu();
		importBattleSprites();
		importAttackFont();
	}

	public static void importMisc() throws IOException {
		fireRedPressStart = ImageIO.read(new File("FireRedPressStart2.png"));
		fireRedPressStart = resizeImage(fireRedPressStart, 960, 640);
		aboutUs = ImageIO.read(new File ("aboutUs.png"));
		instructions = ImageIO.read(new File ("instructions.png"));
		instructions2=ImageIO.read(new File ("instructions2.png"));
		start1 = ImageIO.read(new File ("start1.png"));
		start2 = ImageIO.read(new File ("start2.png"));
	}

	public static void importPewterCity() throws IOException {
		BufferedImage pewterCitySheet = ImageIO.read(new File("PewterCitySheetTiles.png"));
		pewterCity[0] = pewterCitySheet.getSubimage(0, 0, 768, 640);
		pewterCity[0] = resizeImage(pewterCity[0], Main.tileSize * Main.tileMapWidth,
				Main.tileSize * Main.tileMapHeight);
	}
	
	public static void importPokemonCenter() throws IOException {
		pokemonCenter = ImageIO.read(new File ("test.png"));
		pokemonCenter = resizeImage(pokemonCenter, 3072, 2560);
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
		battleBackground = battleBackgroundSheet.getSubimage(6, 234, 240, 112);
		battleBackground = resizeImage(battleBackground, 960, 448);
	}

	// subImage is exclusive corner coordinate
	public static void importBattleMenu() throws IOException {
		BufferedImage battleMenuSheet = ImageIO.read(new File("InBattleMenu.png"));
		battleMenu[0] = battleMenuSheet.getSubimage(0, 0, 112, 32); // enemy hp and lvl
		battleMenu[1] = battleMenuSheet.getSubimage(0, 40, 112, 44); // our hp and lvl
		battleMenu[2] = battleMenuSheet.getSubimage(146, 3, 120, 48); // options
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
			if (i == 24 || i == 19 || i == 8) {
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

	public static void importBattleSprites() throws IOException {
		BufferedImage battleSpritesSheet = ImageIO.read(new File("BattleSprites.png"));
		int xPos = 11;
		int yPos = -119;
		for (int i = 0; i < 150; i++) {
			if (i % 15 == 0) {
				xPos = 11;
				yPos += 164;
			}
			battleSprites[i][0] = battleSpritesSheet.getSubimage(xPos, yPos, 64, 64);
			battleSprites[i][0] = resizeImage(battleSprites[i][0], battleSprites[i][0].getWidth() * 4,
					battleSprites[i][0].getHeight() * 4);
			battleSprites[i][1] = battleSpritesSheet.getSubimage(xPos, yPos + 65, 64, 64);
			battleSprites[i][1] = resizeImage(battleSprites[i][1], battleSprites[i][1].getWidth() * 4,
					battleSprites[i][1].getHeight() * 4);
			xPos += 130;
		}
		battleSpritesIdx.put("bulbasaur", 0);
		battleSpritesIdx.put("ivysaur", 1);
		battleSpritesIdx.put("venusaur", 2);
		battleSpritesIdx.put("charmander", 3);
		battleSpritesIdx.put("charmeleon", 4);
		battleSpritesIdx.put("charizard", 5);
		battleSpritesIdx.put("squirtle", 6);
		battleSpritesIdx.put("wartortle", 7);
		battleSpritesIdx.put("blastoise", 8);
		battleSpritesIdx.put("pidgey", 15);
		battleSpritesIdx.put("pidgeotto", 16);
		battleSpritesIdx.put("pidgeot", 17);
		battleSpritesIdx.put("rattata", 18);
		battleSpritesIdx.put("raticate", 19);
		battleSpritesIdx.put("ekans", 22);
		battleSpritesIdx.put("arkbok", 23);
		battleSpritesIdx.put("pikachu", 24);
		battleSpritesIdx.put("raichu", 25);
		battleSpritesIdx.put("sandshrew", 26);
		battleSpritesIdx.put("sandslash", 27);
		battleSpritesIdx.put("nidoran", 28);
		battleSpritesIdx.put("nidorina", 29);
		battleSpritesIdx.put("nidoqueen", 30);
		battleSpritesIdx.put("nidoran", 31);
		battleSpritesIdx.put("nidorino", 32);
		battleSpritesIdx.put("nidoking", 33);
		battleSpritesIdx.put("vulpix", 36);
		battleSpritesIdx.put("ninetails", 37);
		battleSpritesIdx.put("zubat", 40);
		battleSpritesIdx.put("golbat", 41);
		battleSpritesIdx.put("oddish", 42);
		battleSpritesIdx.put("gloom", 43);
		battleSpritesIdx.put("vileplume", 44);
		battleSpritesIdx.put("diglett", 49);
		battleSpritesIdx.put("dugtrio", 50);
		battleSpritesIdx.put("meowth", 51);
		battleSpritesIdx.put("perisan", 52);
		battleSpritesIdx.put("psyduck", 53);
		battleSpritesIdx.put("golduck", 54);
		battleSpritesIdx.put("growlithe", 57);
		battleSpritesIdx.put("arcanine", 58);
		battleSpritesIdx.put("poliwag", 59);
		battleSpritesIdx.put("poliwhirl", 60);
		battleSpritesIdx.put("poliwrath", 61);
		battleSpritesIdx.put("tentacool", 71);
		battleSpritesIdx.put("tentacruel", 72);
		battleSpritesIdx.put("grimer", 87);
		battleSpritesIdx.put("muk", 88);
		battleSpritesIdx.put("voltorb", 99);
		battleSpritesIdx.put("electrode", 100);
		battleSpritesIdx.put("cubone", 103);
		battleSpritesIdx.put("marowak", 104);
		battleSpritesIdx.put("koffing", 108);
		battleSpritesIdx.put("weezing", 109);
		battleSpritesIdx.put("tangela", 113);
		battleSpritesIdx.put("electabuzz", 124);
		battleSpritesIdx.put("magmar", 125);
		battleSpritesIdx.put("magikarp", 128);
		battleSpritesIdx.put("gyarados", 129);
	}
	public static void importAttackFont() throws IOException {
		BufferedImage attackFontSheet = ImageIO.read(new File("AttackFont.png"));
		int xPos = 0;
		for (int i = 0; i < 28; i++) {
			if (i == 24 || i == 19 || i == 8) {
				attackFont[i] = attackFontSheet.getSubimage(xPos, 0, 6, 12);
				attackFont[i] = resizeImage(attackFont[i], attackFont[i].getWidth() * 4, attackFont[i].getHeight() * 4);
				xPos += 6;
			} else {
				attackFont[i] = attackFontSheet.getSubimage(xPos, 0, 7, 12);
				attackFont[i] = resizeImage(attackFont[i], attackFont[i].getWidth() * 4, attackFont[i].getHeight() * 4);
				xPos += 7;
			}
			if (i < 26) {
				attackFontIdx.put("" + (char) ('A' + i), i);
			}
		}
		attackFontIdx.put(".", 26);
		attackFontIdx.put(",", 27);
		xPos = 0;
		for (int i = 0; i < 26; i++) {
			if (i == 8) {
				attackFont[i + 28] = attackFontSheet.getSubimage(xPos, 16, 4, 12);
				attackFont[i + 28] = resizeImage(attackFont[i + 28], attackFont[i + 28].getWidth() * 4,
						attackFont[i + 28].getHeight() * 4);
				xPos += 4;
			} else if (i == 11) {
				attackFont[i + 28] = attackFontSheet.getSubimage(xPos, 16, 5, 12);
				attackFont[i + 28] = resizeImage(attackFont[i + 28], attackFont[i + 28].getWidth() * 4,
						attackFont[i + 28].getHeight() * 4);
				xPos += 5;
			} else if (i == 9 || i == 15) {
				attackFont[i + 28] = attackFontSheet.getSubimage(xPos, 16, 6, 12);
				attackFont[i + 28] = resizeImage(attackFont[i + 28], attackFont[i + 28].getWidth() * 4,
						attackFont[i + 28].getHeight() * 4);
				xPos += 6;
			} else {
				attackFont[i + 28] = attackFontSheet.getSubimage(xPos, 16, 7, 12);
				attackFont[i + 28] = resizeImage(attackFont[i + 28], attackFont[i + 28].getWidth() * 4,
						attackFont[i + 28].getHeight() * 4);
				xPos += 7;
			}
			attackFontIdx.put("" + (char) ('a' + i), i + 28);
		}
		xPos = 0;
		for (int i = 0; i < 10; i++) {
			if (i == 1) {
				attackFont[i + 54] = attackFontSheet.getSubimage(xPos, 32, 6, 12);
				attackFont[i + 54] = resizeImage(attackFont[i + 54], attackFont[i + 54].getWidth() * 4,
						attackFont[i + 54].getHeight() * 4);
				xPos += 6;
			} else {
				attackFont[i + 54] = attackFontSheet.getSubimage(xPos, 32, 7, 12);
				attackFont[i + 54] = resizeImage(attackFont[i + 54], attackFont[i + 54].getWidth() * 4,
						attackFont[i + 54].getHeight() * 4);
				xPos += 7;
			}
			attackFontIdx.put("" + (char) ('0' + i), i + 54);

		}
		xPos = 0;
		for (int i = 0; i < 13; i++) {
			if (i == 0) {
				attackFont[i + 65] = attackFontSheet.getSubimage(xPos, 48, 4, 12);
				attackFont[i + 65] = resizeImage(attackFont[i + 65], attackFont[i + 65].getWidth() * 4,
						attackFont[i + 65].getHeight() * 4);
				xPos += 4;
			} else {
				attackFont[i + 65] = attackFontSheet.getSubimage(xPos, 48, 7, 12);
				attackFont[i + 65] = resizeImage(attackFont[i + 65], attackFont[i + 65].getWidth() * 4,
						attackFont[i + 65].getHeight() * 4);
				xPos += 7;
			}
			attackFontIdx.put("!", 65);
			attackFontIdx.put("?", 66);
			attackFontIdx.put("boyB", 67);
			attackFontIdx.put("girlB", 68);
			attackFontIdx.put("/", 69);
			attackFontIdx.put("-", 70);
			attackFontIdx.put("..", 71);
			attackFontIdx.put("\"f", 72);
			attackFontIdx.put("\"b", 73);
			attackFontIdx.put("\'f", 74);
			attackFontIdx.put("\'b", 75);
			attackFontIdx.put("boy", 76);
			attackFontIdx.put("girl", 77);
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
