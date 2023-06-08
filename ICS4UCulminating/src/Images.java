import java.util.*;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
public class Images {
	
	static BufferedImage[] pewterCity = new BufferedImage[4];
	static BufferedImage[] trainerDown = new BufferedImage[3];
	static BufferedImage[] trainerUp = new BufferedImage[3];
	static BufferedImage[] trainerSide = new BufferedImage[3];
 	static BufferedImage fireRedPressStart;
	public static void importAllImages() throws IOException{
		importMisc();
		importPewterCity();
		importTrainer();
	}
	
	public static void importMisc() throws IOException{
		fireRedPressStart = ImageIO.read(new File("FireRedPressStart.png"));
		fireRedPressStart = resizeImage(fireRedPressStart, 960, 640);
	}
	
	public static void importPewterCity() throws IOException {
		BufferedImage pewterCitySheet = ImageIO.read(new File("PewterCitySheet.png"));
		pewterCity[0] = pewterCitySheet.getSubimage(0, 0, 768, 640);
		pewterCity[0] = resizeImage(pewterCity[0], 3072, 2560);
	}
	public static void importTrainer() throws IOException {
		BufferedImage trainerSprites = ImageIO.read(new File("trainerSprites.png"));
		for (int i = 0; i < 3; i++) {
			trainerDown[i] = trainerSprites.getSubimage(8 + 16*i, 36, 16, 20);
			trainerDown[i]  = resizeImage(trainerDown[i], 64, 80);
		}
		for (int i = 0; i < 3; i++) {
			trainerUp[i] = trainerSprites.getSubimage(8 + 16*i, 68, 16, 20);
			trainerUp[i]  = resizeImage(trainerUp[i], 64, 80);
		}
		
		for (int i = 0; i < 3; i++) {
			trainerSide[i]  = trainerSprites.getSubimage(8+16*i, 100, 16, 20);
			trainerSide[i]  = resizeImage(trainerSide[i], 64, 80);
		}
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
}
