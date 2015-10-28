package PrimeFactorAttack;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;



//Game constants
public class Data
{
  public static final String version = "Version: 2015-OCT-28";
  
  public static final int[] PRIME      = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
  
  public static final int MAX_FACTOR = PRIME[PRIME.length-1];
  public static final int MAX_FACTOR_COUNT = 11;
  
  
  public static final int FRAME_RATE = 30; //millisec
  
  public static enum Status 
  { WELCOME,
    READY_TO_START,
    RUNNING, 
    LEVELUP,
    TIMESTOP,
    ENDED,
  };
  
  //When this is true, pressing 'n' will advance to next level
  public static final boolean CHEAT_ON = true;
  
  public static BufferedImage image, imageSand, imageTmp;
  public static Graphics2D graph, graphSand, graphTmp;
  public static Graphics graphScreen;
  
  
  
  public static String resourcePath = "resources/";
  public static Random rand = new Random();
  
  public static boolean showHelp = true;
  
  
  public static int getIndexOfPrime(int n)
  {
    for (int i=0; i<PRIME.length; i++)
    { if (PRIME[i] == n) return i;
    }
    return -1;
  }
}
