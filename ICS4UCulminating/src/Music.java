// The music class is used to coordinate, you guessed it, MUSIC!

import java.io.File;

import javax.sound.sampled.*;

public class Music {
	public static Clip opening, pewter, gym;
	
	public static void initializeMusic() {
		AudioInputStream sound;
		FloatControl volume; // Used to control the volume of the music!
		try {
			sound = AudioSystem.getAudioInputStream(new File ("opening.wav"));
			opening = AudioSystem.getClip();
			opening.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("pewter.wav"));
			pewter = AudioSystem.getClip();
			pewter.open(sound);
			
			sound = AudioSystem.getAudioInputStream(new File ("gym.wav"));
			gym = AudioSystem.getClip();
			gym.open(sound);
		}
		catch (Exception e) {
			
		}
		playMusic();
	}
	
	public static void playMusic() {
		if (Main.gameState==0) {
			gym.stop();
			gym.setFramePosition(0);
			opening.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==2) {
			opening.stop();
			opening.setFramePosition(0);
			pewter.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (Main.gameState==3) {
			pewter.stop();
			pewter.setFramePosition(0);
			gym.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
}
