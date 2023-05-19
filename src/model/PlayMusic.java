package model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class PlayMusic {

    public static void playMusic(String musicPath, int time) //循环多少次
    {
        try
        {
            File sound = new File(musicPath);

            if(sound.exists())
            {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(sound);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if(time == 0) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.loop(time-1);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
