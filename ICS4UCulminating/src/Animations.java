import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

// The Animations class is used to organize the animations! 


public class Animations {
	static int walkIndex = 1;
	static int walkTickSpeed = 10;
	static int walkCurrentTick = 10;
	// animations goes 1 -> 2 -> 1 -> 0 -> 1 -> 2 ....
	static int bounce = 1;

	public static int getWalkIndex() {
		return walkIndex;
	}

	public static void setWalkIndex(int x) {
		walkIndex = x;
	}

	public static int getWalkCurrentTick() {
		return walkCurrentTick;
	}

	public static void setWalkCurrentTick(int x) {
		walkCurrentTick = x;
	}

	// walk animation
	public static void walk() {
		if (Player.getMoving()) {
			walkCurrentTick++;
			if (walkCurrentTick >= walkTickSpeed) {
				walkCurrentTick = 0;
				if (walkIndex == 2) {
					bounce = -1;
				} else if (walkIndex == 0) {
					bounce = 1;
				}
				walkIndex += bounce;
			}
			switch (Player.getDirection()) {
			case 1:
				Player.setCurrentPlayerImage(Images.trainerUp[walkIndex]);
				break;
			case 2:
				Player.setCurrentPlayerImage(Images.trainerDown[walkIndex]);
				break;
			case 3:
				Player.setCurrentPlayerImage(Images.trainerLeft[walkIndex]);
				break;
			case 4:
				Player.setCurrentPlayerImage(Images.trainerRight[walkIndex]);
				break;
			}
		}
	}

	// standing still image
	public static void resetWalk() {
		if (!Player.getMoving()) {
			walkIndex = 1;
			walkCurrentTick = 10;
			switch (Player.getDirection()) {
			case 1:
				Player.setCurrentPlayerImage(Images.trainerUp[1]);
				break;
			case 2:
				Player.setCurrentPlayerImage(Images.trainerDown[1]);
				break;
			case 3:
				Player.setCurrentPlayerImage(Images.trainerLeft[1]);
				break;
			case 4:
				Player.setCurrentPlayerImage(Images.trainerRight[1]);
				break;
			}
		}
	}
}