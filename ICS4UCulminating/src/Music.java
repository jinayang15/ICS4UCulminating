// The music class is used to coordinate, you guessed it, MUSIC!

import java.io.File;

import javax.sound.sampled.*;

public class Music {
	public static Clip opening, lab, pewter, gym, pokeCenter, heal;
	
	// Initializing all of the music and importing it all
	public static void initializeMusic() {
		AudioInputStream sound;
		FloatControl volume; // Used to control the volume of the music!
		try {
			sound = AudioSystem.getAudioInputStream(new File ("opening.wav"));
			opening = AudioSystem.getClip();
			opening.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("lab.wav"));
			lab = AudioSystem.getClip();
			lab.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("pewter.wav"));
			pewter = AudioSystem.getClip();
			pewter.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("gym.wav"));
			gym = AudioSystem.getClip();
			gym.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("pokeCenter.wav"));
			pokeCenter = AudioSystem.getClip();
			pokeCenter.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("heal.wav"));
			heal = AudioSystem.getClip();
			heal.open(sound);
		}
		catch (Exception e) {
			
		}
	}
	// The playMusic method is used to play music
	// It takes in no parameters
	// Returns nothing
	public static void playMusic() {
		if (Main.gameState==0 || Main.gameState==1 || Main.gameState==9) {
			lab.stop();
			lab.setFramePosition(0);
			pewter.stop();
			pewter.setFramePosition(0);
			gym.stop();
			gym.setFramePosition(0);
			opening.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==2) {
			lab.stop();
			lab.setFramePosition(0);
			gym.stop();
			gym.setFramePosition(0);
			pokeCenter.stop();
			pokeCenter.setFramePosition(0);
			pewter.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==3) {
			pewter.stop();
			pewter.setFramePosition(0);
			gym.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==11 || Main.gameState==14) {
			opening.stop();
			opening.setFramePosition(0);
			lab.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==13) {
			pewter.stop();
			pewter.setFramePosition(0);
			pokeCenter.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	// The healMusic method is used when the user is healing. 
	// No parameters
	// Nothing returned
	public static void healMusic() {
		pokeCenter.stop();
		pokeCenter.setFramePosition(0);
		heal.start();
		heal.setFramePosition(0);
	}
 }
