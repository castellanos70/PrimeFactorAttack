package PrimeFactorAttack.utility;

//plays simple .wav files for sound effects.
//SoundPlayer should only be used to play short sounds because each
//sound is fully loaded into memory and kept there for rapid replay.
//Large sound files such as songs, should be streamed, not fully read into memory and then fully played.
//SoundPlayer supports only linear PCM audio files.
//If SoundPlayer is given a .wav file that is not in the simple linear PCM format,
//then there will not be an error thrown, but no sound will play.

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class SoundPlayer
{ private Clip clip;

  public SoundPlayer(String soundFilePath) throws Exception
  {
    // specify the sound to play
    // (assuming the sound can be played by the audio system)
    File soundFile = new File(soundFilePath);
    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

    // load the sound into memory (a Clip)
    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
    clip = (Clip) AudioSystem.getLine(info);
    clip.open(sound);
  }


  //Plays in a new thread
  public void play()
  { //System.out.println("soundEffect="+soundEffect.toString());
    clip.setFramePosition(0);
    clip.start(); // Play only once

  }

  public void stop()
  {
    clip.stop();
  }

}
