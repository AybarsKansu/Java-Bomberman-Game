package main;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.sound.sampled.*;

public class Sound {
	Clip clip;
	File sounds[] = new File[2];
	
	public Sound() throws MalformedURLException {
		sounds[0] = new File("C:\\Users\\Aybars\\eclipse-workspace\\Homework\\image\\sound\\bomberman_theme.wav");
		sounds[1] = new File("C:\\Users\\Aybars\\eclipse-workspace\\Homework\\image\\sound\\minecraft bomb effect.wav");
		
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(sounds[i]);
			clip = AudioSystem.getClip();
			clip.open(in);
		} catch (Exception e) {			
			e.printStackTrace();
		} 
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
	
	public void setVolume(int volumeLevel) {
		float volume = (float) (Math.log(volumeLevel / 100.0) / Math.log(10.0) * 20.0);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    gainControl.setValue(volume);
    }
}
