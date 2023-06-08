import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Animations {
	static int walkIndex = 1;
	static int walkTickSpeed = 10;
	static int walkCurrentTick = 6;
	// animations goes 1 -> 2 -> 1 -> 0 -> 1 -> 2 ....
	static int bounce = 1;

	// walk animation
	public static void walk() {
		if (Player.getMoving()) {
			walkCurrentTick++;
			if (walkCurrentTick >= walkTickSpeed) {
				walkCurrentTick = 0;
				if (walkIndex == 2) {
					bounce = -1;
				}
				else if (walkIndex == 0) {
					bounce = 1;
				}
				walkIndex += bounce;
			}
			switch (Player.getDirection()) {
			case 1:
				Images.currentPlayerImage = Images.trainerUp[walkIndex];
				break;
			case 2:
				Images.currentPlayerImage = Images.trainerDown[walkIndex];
				break;
			case 3:
				Images.currentPlayerImage = Images.trainerLeft[walkIndex];
				break;
			case 4:
				Images.currentPlayerImage = Images.trainerRight[walkIndex];
				break;
			}
		}
	}
	// standing still image
	public static void resetWalk() {
		walkIndex = 1;
		walkCurrentTick = 6;
		switch (Player.getDirection()) {
		case 1:
			Images.currentPlayerImage = Images.trainerUp[1];
			break;
		case 2:
			Images.currentPlayerImage = Images.trainerDown[1];
			break;
		case 3:
			Images.currentPlayerImage = Images.trainerLeft[1];
			break;
		case 4:
			Images.currentPlayerImage = Images.trainerRight[1];
			break;
		}
	}
}