package common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	public static final String BOSS = "/bgm/boss.wav";
	public static final String ENDING = "/bgm/ending.wav";
	public static final String MAINTHEME = "/bgm/maintheme.wav";
	public static final String STAGE2 = "/bgm/stage2.wav";
	public static final String STAGE3 = "/bgm/stage3.wav";
	public static final String ALLCLEAR = "/effects/allclear.wav";
	public static final String BOSSDEAD = "/effects/bossdead.wav";
	public static final String BOSSINTRO = "/effects/bossintro.wav";
	public static final String ENEMYDEAD = "/effects/enemydead.wav";
	public static final String GAMEOVER = "/effects/gameover.wav";
	public static final String GAMESTART = "/effects/gamestart.wav";
	public static final String GOTITEM = "/effects/gotitem.wav";
	public static final String HEALED = "/effects/healed.wav";
	public static final String HITBOSS = "/effects/hitboss.wav";
	public static final String HOT = "/effects/hot.wav";
	public static final String STAGECLEAR = "/effects/stageclear.wav";
	public static final String YUPPDUK = "/effects/yuppduk.wav";

	private static boolean mute = false;
	private static Map<String, Clip> soundMap = new HashMap<>();
	private static String[] str = new String[17];

	static{
		str[0] = BOSS;
		str[1] = ENDING;
		str[2] = MAINTHEME;
		str[3] = STAGE2;
		str[4] = STAGE3;
		str[5] = ALLCLEAR;
		str[6] = BOSSDEAD;
		str[7] = BOSSINTRO;
		str[8] = ENEMYDEAD;
		str[9] = GAMEOVER;
		str[10] = GAMESTART;
		str[11] = GOTITEM;
		str[12] = HEALED;
		str[13] = HITBOSS;
		str[14] = HOT;
		str[15] = STAGECLEAR;
		str[16] = YUPPDUK;
		for(String s : str){
			try {
				Clip soundClip = AudioSystem.getClip();	
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File("sounds" + s));
				soundClip.open(inputStream);
				soundMap.put(s, soundClip);

			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private Clip soundClip;

	public Sound(String sound) {
		soundClip = soundMap.get(sound);
	}

	public void playMusic(boolean repeat) {
		if(!mute) {
			soundClip.setFramePosition(0);
			soundClip.start();
			if(repeat) {
				soundClip.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
	}

	public void stopMusic() {
		if(soundClip != null && soundClip.isRunning()){
			soundClip.stop();
		}
	}

	public static void mute() {
		for(String s : str) {
			Clip clip = soundMap.get(s);
			if(clip != null && clip.isRunning())
				clip.stop();
		}
		mute = true;
	}

	public static void muteOff() {
		mute = false;
	}

}
