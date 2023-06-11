import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// The Player class is used to store the user's Pokemon, items, and money.
// It will be also used in battle to determine what the user will do, such as attack, switch pokemon,
// or use items. 

public class Player {
//	private int money;\
	static int playerX;
	static int playerY;
	static int playerHeight = 80;
	static int playerWidth = 64;
	static Rectangle hitbox = new Rectangle(new Dimension(48,64));
	static BufferedImage currentPlayerImage;
	// 1 - up, 2 - down, 3 - left, 4 - right
	static int direction;
	// determines if current player is moving
	static boolean moving;
//  something for items
	private Pokemon[] pokemonList = new Pokemon[3];
	
	public Player (String type) {
		playerX = 512 - playerWidth;
		playerY = 384 - playerHeight;
		hitbox.x = playerX + 8;
		hitbox.y = playerY + 8;
		direction = 1;
		moving = false;
		currentPlayerImage = Images.trainerUp[1];
		if (type.equals("Grass")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(1));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(31));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(55));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
		else if (type.equals("Fire")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(4));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(27));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(40));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
		else if (type.equals("Water")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(7));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(38));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(42));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
	}
	
	public static int getPlayerX() {
		return playerX;
	}
	public static int getPlayerY() {
		return playerY;
	}
	public static void setPlayerX(int x) {
		playerX = x;
	}
	public static void setPlayerY(int y) {
		playerY = y;
	}
	
	public static int getPlayerHeight() {
		return playerHeight;
	}

	public static int getPlayerWidth() {
		return playerWidth;
	}
	public static int getDirection() {
		return direction;
	}
	public static void setDirection(int x) {
		direction = x;
	}
	
	public static boolean getMoving() {
		return moving;
	}
	public static void setMoving(boolean b) {
		moving = b;
	}
	
	public static BufferedImage getCurrentPlayerImage() {
		return currentPlayerImage;
	}
	public static void setCurrentPlayerImage(BufferedImage b) {
		currentPlayerImage = b;
	}
	
	public static Rectangle getHitbox() {
		return hitbox;	
	}
}
