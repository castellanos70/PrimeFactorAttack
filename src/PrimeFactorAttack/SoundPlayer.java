package PrimeFactorAttack;

import java.applet.Applet;
import java.applet.AudioClip;
import java.lang.IllegalArgumentException;


//plays simple .wav files for sound effects. 
//SoundPlayer should only be used to play short sounds because each 
//sound is fully loaded into memory and kept there for rapid replay. 
//Large sound files such as songs, should be streamed, not fully read into memory and then fully played. 
//SoundPlayer supports only linear PCM audio files. 
//If SoundPlayer is given a .wav file that is not in the simple linear PCM format, 
//then there will not be an error thrown, but no sound will play.



public class SoundPlayer  
{ private AudioClip soundEffect; // Sound player
 
  public SoundPlayer(String wavfile) throws Exception
  { 
    //Note: if wavfile is null, then this will through a NullPointerException
    if (!wavfile.endsWith(".wav") && !wavfile.endsWith(".WAV"))
    { throw new IllegalArgumentException("SoundPlayer(): File name must end with .wav");
    }
    
    try
    { 
      
      soundEffect = Applet.newAudioClip(this.getClass().getResource(wavfile));
      System.out.println("SoundPlayer("+wavfile+")");
    }
    catch  (Exception e)
    { throw new IllegalArgumentException("SoundPlayer(): Cannot open file:"+wavfile);
    }
  }


  //Plays in a new thread
  public void play() 
  { //System.out.println("soundEffect="+soundEffect.toString());
    soundEffect.play(); // Play only once
    
  }
  
  public void stop()
  { 
    soundEffect.stop();
  }
  
}
