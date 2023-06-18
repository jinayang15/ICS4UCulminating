import java.util.*;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.image.AffineTransformOp;

// The Image class is used to import all of the images that we need. 
// This includes the backgrounds, Pokemon, fonts, and more!

public class Images {

	// ALL THE IMAGE VARIABLES 
	
	public static BufferedImage[] pewterCity = new BufferedImage[4];
	public static BufferedImage[] trainerDown = new BufferedImage[3];
	public static BufferedImage[] trainerUp = new BufferedImage[3];
	public static BufferedImage[] trainerLeft = new BufferedImage[3];
	public static BufferedImage[] trainerRight = new BufferedImage[3];
	public static BufferedImage[] battleMenu = new BufferedImage[6];
	public static BufferedImage[] healthBars = new BufferedImage[3];
	static HashMap<String, Integer> battleFontIdx = new HashMap<String, Integer>();
	public static BufferedImage[] battleFont = new BufferedImage[78];
	static HashMap<String, Integer> battleSpritesIdx = new HashMap<String, Integer>();
	public static BufferedImage[][] battleSprites = new BufferedImage[150][2]; // other - 0, player - 1
	static HashMap<String, Integer> moveFontIdx = new HashMap<String, Integer>();
	public static BufferedImage[] moveFont = new BufferedImage[78];
	static HashMap<String, Integer> whiteFontIdx = new HashMap<String, Integer>();
	public static BufferedImage[] whiteFont = new BufferedImage[78];
	public static BufferedImage[] statusIcons = new BufferedImage[4];
	public static BufferedImage[] pkmnMenuIcons = new BufferedImage[6];
	

	public static BufferedImage fireRedPressStart;
	public static BufferedImage battleBackground;
	public static BufferedImage aboutUs;
	public static BufferedImage instructions;
	public static BufferedImage instructions2;
	public static BufferedImage start1;
	public static BufferedImage start2;
	public static BufferedImage pokemonCenter;
	public static BufferedImage winScreen;
	public static BufferedImage loseScreen;
	public static BufferedImage pkmnMenuBG;

	
	// This method is called in the Main to import all of the images
	public static void importAllImages() throws IOException {
		importMisc();
		importPewterCity();
		importPokemonCenter();
		importTrainer();
		importBattleBackground();
		importBattleMenu();
		importBattleSprites();
		importAttackFont();
		importWhiteFont();
		importStatusIcons();
		importPokemonMenu();
	}

	// All of these next methods are used to import the images.
	// All pretty self explanatory 
	// All of them take in no parameters
	// All of them return nothing
	
	public static void importMisc() throws IOException {
		fireRedPressStart = ImageIO.read(new File("FireRedPressStart2.png"));
		fireRedPressStart = resizeImage(fireRedPressStart, 960, 640);
		aboutUs = ImageIO.read(new File ("aboutUs.png"));
		instructions = ImageIO.read(new File ("instructions.png"));
		instructions2=ImageIO.read(new File ("instructions2.png"));
		start1 = ImageIO.read(new File ("start1.png"));
		start2 = ImageIO.read(new File ("start2.png"));
		winScreen = ImageIO.read(new File ("winScreen.png"));
		loseScreen = ImageIO.read(new File ("loseScreen.png"));
	}

	public static void importPewterCity() throws IOException {
		BufferedImage pewterCitySheet = ImageIO.read(new File("PewterCitySheetTiles.png"));
		pewterCity[0] = pewterCitySheet.getSubimage(0, 0, 768, 640);
		pewterCity[0] = resizeImage(pewterCity[0], Main.tileSize * Main.tileMapWidth,
				Main.tileSize * Main.tileMapHeight);
	}
	
	public static void importPokemonCenter() throws IOException {
		pokemonCenter = ImageIO.read(new File ("test.png"));
		pokemonCenter = resizeImage(pokemonCenter, 1440, 960);
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
		healthBars[0] = battleMenuSheet.getSubimage(117, 8, 1, 3); // green bar
		healthBars[1] = battleMenuSheet.getSubimage(117, 12, 1, 3); // yellow bar
		healthBars[2] = battleMenuSheet.getSubimage(117, 16, 1, 3); // red bar
		battleMenu[0] = battleMenuSheet.getSubimage(0, 0, 112, 32); // enemy hp and lvl
		battleMenu[1] = battleMenuSheet.getSubimage(0, 40, 112, 44); // our hp and lvl
		battleMenu[2] = battleMenuSheet.getSubimage(146, 3, 120, 48); // options
		battleMenu[3] = battleMenuSheet.getSubimage(268, 3, 7, 17); // arrow
		battleMenu[4] = battleMenuSheet.getSubimage(297, 3, 240, 48); // attacks
		battleMenu[5] = battleMenuSheet.getSubimage(297, 55, 240, 48); // text box
		for (int i = 0; i < healthBars.length; i++) {
			if (healthBars[i] != null) {
				int width = healthBars[i].getWidth();
				int height = healthBars[i].getHeight();
				healthBars[i] = resizeImage(healthBars[i], width * 4, height * 4);
			}
		}
		for (int i = 0; i < battleMenu.length; i++) {
			if (battleMenu[i] != null) {
				int width = battleMenu[i].getWidth();
				int height = battleMenu[i].getHeight();
				battleMenu[i] = resizeImage(battleMenu[i], width * 4, height * 4);
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
		BufferedImage moveFontSheet = ImageIO.read(new File("MoveFont.png"));
		int xPos = 0;
		for (int i = 0; i < 28; i++) {
			if (i == 24 || i == 19 || i == 8) {
				moveFont[i] = moveFontSheet.getSubimage(xPos, 0, 6, 12);
				moveFont[i] = resizeImage(moveFont[i], moveFont[i].getWidth() * 4, moveFont[i].getHeight() * 4);
				xPos += 6;
			} else {
				moveFont[i] = moveFontSheet.getSubimage(xPos, 0, 7, 12);
				moveFont[i] = resizeImage(moveFont[i], moveFont[i].getWidth() * 4, moveFont[i].getHeight() * 4);
				xPos += 7;
			}
			if (i < 26) {
				moveFontIdx.put("" + (char) ('A' + i), i);
			}
		}
		moveFontIdx.put(".", 26);
		moveFontIdx.put(",", 27);
		xPos = 0;
		for (int i = 0; i < 26; i++) {
			if (i == 8) {
				moveFont[i + 28] = moveFontSheet.getSubimage(xPos, 16, 4, 12);
				moveFont[i + 28] = resizeImage(moveFont[i + 28], moveFont[i + 28].getWidth() * 4,
						moveFont[i + 28].getHeight() * 4);
				xPos += 4;
			} else if (i == 11) {
				moveFont[i + 28] = moveFontSheet.getSubimage(xPos, 16, 5, 12);
				moveFont[i + 28] = resizeImage(moveFont[i + 28], moveFont[i + 28].getWidth() * 4,
						moveFont[i + 28].getHeight() * 4);
				xPos += 5;
			} else if (i == 9 || i == 15) {
				moveFont[i + 28] = moveFontSheet.getSubimage(xPos, 16, 6, 12);
				moveFont[i + 28] = resizeImage(moveFont[i + 28], moveFont[i + 28].getWidth() * 4,
						moveFont[i + 28].getHeight() * 4);
				xPos += 6;
			} else {
				moveFont[i + 28] = moveFontSheet.getSubimage(xPos, 16, 7, 12);
				moveFont[i + 28] = resizeImage(moveFont[i + 28], moveFont[i + 28].getWidth() * 4,
						moveFont[i + 28].getHeight() * 4);
				xPos += 7;
			}
			moveFontIdx.put("" + (char) ('a' + i), i + 28);
		}
		xPos = 0;
		for (int i = 0; i < 10; i++) {
			if (i == 1) {
				moveFont[i + 54] = moveFontSheet.getSubimage(xPos, 32, 6, 12);
				moveFont[i + 54] = resizeImage(moveFont[i + 54], moveFont[i + 54].getWidth() * 4,
						moveFont[i + 54].getHeight() * 4);
				xPos += 6;
			} else {
				moveFont[i + 54] = moveFontSheet.getSubimage(xPos, 32, 7, 12);
				moveFont[i + 54] = resizeImage(moveFont[i + 54], moveFont[i + 54].getWidth() * 4,
						moveFont[i + 54].getHeight() * 4);
				xPos += 7;
			}
			moveFontIdx.put("" + (char) ('0' + i), i + 54);

		}
		xPos = 0;
		for (int i = 0; i < 13; i++) {
			if (i == 0) {
				moveFont[i + 65] = moveFontSheet.getSubimage(xPos, 48, 4, 12);
				moveFont[i + 65] = resizeImage(moveFont[i + 65], moveFont[i + 65].getWidth() * 4,
						moveFont[i + 65].getHeight() * 4);
				xPos += 4;
			} else {
				moveFont[i + 65] = moveFontSheet.getSubimage(xPos, 48, 7, 12);
				moveFont[i + 65] = resizeImage(moveFont[i + 65], moveFont[i + 65].getWidth() * 4,
						moveFont[i + 65].getHeight() * 4);
				xPos += 7;
			}
			moveFontIdx.put("!", 65);
			moveFontIdx.put("?", 66);
			moveFontIdx.put("boyB", 67);
			moveFontIdx.put("girlB", 68);
			moveFontIdx.put("/", 69);
			moveFontIdx.put("-", 70);
			moveFontIdx.put("..", 71);
			moveFontIdx.put("\"f", 72);
			moveFontIdx.put("\"b", 73);
			moveFontIdx.put("\'f", 74);
			moveFontIdx.put("\'b", 75);
			moveFontIdx.put("boy", 76);
			moveFontIdx.put("girl", 77);
		}
	}

	public static void importWhiteFont() throws IOException {
		BufferedImage whiteFontSheet = ImageIO.read(new File("WhiteFont.png"));
		int xPos = 0;
		for (int i = 0; i < 28; i++) {
			if (i == 24 || i == 19 || i == 8) {
				whiteFont[i] = whiteFontSheet.getSubimage(xPos, 0, 6, 12);
				whiteFont[i] = resizeImage(whiteFont[i], whiteFont[i].getWidth() * 4, whiteFont[i].getHeight() * 4);
				xPos += 6;
			} else {
				whiteFont[i] = whiteFontSheet.getSubimage(xPos, 0, 7, 12);
				whiteFont[i] = resizeImage(whiteFont[i], whiteFont[i].getWidth() * 4, whiteFont[i].getHeight() * 4);
				xPos += 7;
			}
			if (i < 26) {
				whiteFontIdx.put("" + (char) ('A' + i), i);
			}
		}
		whiteFontIdx.put(".", 26);
		whiteFontIdx.put(",", 27);
		xPos = 0;
		for (int i = 0; i < 26; i++) {
			if (i == 8) {
				whiteFont[i + 28] = whiteFontSheet.getSubimage(xPos, 16, 4, 12);
				whiteFont[i + 28] = resizeImage(whiteFont[i + 28], whiteFont[i + 28].getWidth() * 4,
						whiteFont[i + 28].getHeight() * 4);
				xPos += 4;
			} else if (i == 11) {
				whiteFont[i + 28] = whiteFontSheet.getSubimage(xPos, 16, 5, 12);
				whiteFont[i + 28] = resizeImage(whiteFont[i + 28], whiteFont[i + 28].getWidth() * 4,
						whiteFont[i + 28].getHeight() * 4);
				xPos += 5;
			} else if (i == 9 || i == 15) {
				whiteFont[i + 28] = whiteFontSheet.getSubimage(xPos, 16, 6, 12);
				whiteFont[i + 28] = resizeImage(whiteFont[i + 28], whiteFont[i + 28].getWidth() * 4,
						whiteFont[i + 28].getHeight() * 4);
				xPos += 6;
			} else {
				whiteFont[i + 28] = whiteFontSheet.getSubimage(xPos, 16, 7, 12);
				whiteFont[i + 28] = resizeImage(whiteFont[i + 28], whiteFont[i + 28].getWidth() * 4,
						whiteFont[i + 28].getHeight() * 4);
				xPos += 7;
			}
			whiteFontIdx.put("" + (char) ('a' + i), i + 28);
		}
		xPos = 0;
		for (int i = 0; i < 10; i++) {
			if (i == 1) {
				whiteFont[i + 54] = whiteFontSheet.getSubimage(xPos, 32, 6, 12);
				whiteFont[i + 54] = resizeImage(whiteFont[i + 54], whiteFont[i + 54].getWidth() * 4,
						whiteFont[i + 54].getHeight() * 4);
				xPos += 6;
			} else {
				whiteFont[i + 54] = whiteFontSheet.getSubimage(xPos, 32, 7, 12);
				whiteFont[i + 54] = resizeImage(whiteFont[i + 54], whiteFont[i + 54].getWidth() * 4,
						whiteFont[i + 54].getHeight() * 4);
				xPos += 7;
			}
			whiteFontIdx.put("" + (char) ('0' + i), i + 54);

		}
		xPos = 0;
		for (int i = 0; i < 13; i++) {
			if (i == 0) {
				whiteFont[i + 65] = whiteFontSheet.getSubimage(xPos, 48, 4, 12);
				whiteFont[i + 65] = resizeImage(whiteFont[i + 65], whiteFont[i + 65].getWidth() * 4,
						whiteFont[i + 65].getHeight() * 4);
				xPos += 4;
			} else {
				whiteFont[i + 65] = whiteFontSheet.getSubimage(xPos, 48, 7, 12);
				whiteFont[i + 65] = resizeImage(whiteFont[i + 65], whiteFont[i + 65].getWidth() * 4,
						whiteFont[i + 65].getHeight() * 4);
				xPos += 7;
			}
			whiteFontIdx.put("!", 65);
			whiteFontIdx.put("?", 66);
			whiteFontIdx.put("boyB", 67);
			whiteFontIdx.put("girlB", 68);
			whiteFontIdx.put("/", 69);
			whiteFontIdx.put("-", 70);
			whiteFontIdx.put("..", 71);
			whiteFontIdx.put("\"f", 72);
			whiteFontIdx.put("\"b", 73);
			whiteFontIdx.put("\'f", 74);
			whiteFontIdx.put("\'b", 75);
			whiteFontIdx.put("boy", 76);
			whiteFontIdx.put("girl", 77);
		}
	}

	public static void importStatusIcons() throws IOException {
		BufferedImage statusIconSheet = ImageIO.read(new File("StatusIcons.png"));
		for (int i = 0; i < 4; i++) {
			statusIcons[i] = statusIconSheet.getSubimage(6 + i * 32, 80, 20, 8);
			statusIcons[i] = resizeImage(statusIcons[i], statusIcons[i].getWidth() * 4, statusIcons[i].getHeight() * 4);
		}
	}
	
	public static void importPokemonMenu() throws IOException {
		BufferedImage pokemonMenuSheet = ImageIO.read(new File("PokemonMenu.png"));
		pkmnMenuBG = pokemonMenuSheet.getSubimage(250, 5, 240, 160);
		pkmnMenuBG = resizeImage(pkmnMenuBG, 960, 640);
		
		pkmnMenuIcons[0] = pokemonMenuSheet.getSubimage(317, 170, 84, 57);
		pkmnMenuIcons[1] = pokemonMenuSheet.getSubimage(406, 170, 84, 57);
		pkmnMenuIcons[2] = pokemonMenuSheet.getSubimage(162, 178, 150, 24);
		pkmnMenuIcons[3] = pokemonMenuSheet.getSubimage(162, 203, 150, 24);
		pkmnMenuIcons[4] = pokemonMenuSheet.getSubimage(6, 251, 54, 24);
		pkmnMenuIcons[5] = pokemonMenuSheet.getSubimage(65, 251, 54, 24);
		for (int i = 0; i < 6; i++) {
			pkmnMenuIcons[i] = resizeImage(pkmnMenuIcons[i], pkmnMenuIcons[i].getWidth() * 4, pkmnMenuIcons[i].getHeight() * 4);
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
