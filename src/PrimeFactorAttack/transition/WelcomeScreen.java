package PrimeFactorAttack.transition;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Random;

import PrimeFactorAttack.FullPanel;

/**
 * @version 2011.1109
 * @author Max Ottesen
 * Class for drawing the level up screen in an offscreen buffer
 */
public class WelcomeScreen
{
  
  private FullPanel panel;
  private Graphics2D canvas;
  private Random rand = new Random();
  private Color myColor;
  private static final String STR1 = "University of New Mexico's";
  private static final String STR2 = "CS-152 Students";
  private static final String STR3 = "Present...";
  
  
  private String tempUnlock, tempLevel, tempTitle;
  
  
  private int width, height;
  private int[] charPos = new int[3];
  //private static final int[] STR_LEFT = {110, 385, 90};
  private static final int[] STR_LEFT = {25, 275, 400};
  private int frameCount;
  private Font superLargeFont;   //ridiculous number of variables for various things
  private String[] randString = new String[18];
  private int[] velocity = new int[18];
  private int[] newVelocity = new int[18];


  
  
  /**
   * Class for setting up the rest of the animation and
   *  drawing the first frame
   * @param offscreenBuffer
   * @param unlock what the player unlocked
   * @param lev the level the player is advancing to
   * @param tit the title of the level
   */
  public WelcomeScreen(FullPanel panel)
  { 
    this.panel = panel;
    width = panel.getWidth();
    height = panel.getHeight();
    canvas = panel.getGraphics2D();
    
    superLargeFont = new Font("Courier", Font.PLAIN, 45);
    canvas.setFont(superLargeFont);
    myColor = new Color(15, 160, 15);
    canvas.setColor(myColor);
    

    tempUnlock = "";
    tempLevel = "";
    tempTitle = "";
    
    for (int x=0; x<18; x++)
    { 
      randString[x] = ""; 
      for (int y=0; y<70; y++)
      {      
        randString[x] += rand.nextInt(10);
      }
      velocity[x] = rand.nextInt(11);
      while (velocity[x] < 6) velocity[x] = rand.nextInt(11);
      canvas.drawString(randString[x], width, 33*(x+1));
    }
  }
  
  /**
   * Class for moving the strings of numbers across the screen
   * @return false if there is more to draw
   */
  public boolean update()
  { frameCount++;
    //if (frameCount == 451) return true;
    if (frameCount >0 ) return true;
    canvas.setColor(Color.BLACK);
    canvas.fillRect(0, 0, 1000, 1000); //erases screen
    canvas.setColor(myColor);
    
    for (int x=0; x<18; x++)
    {
      for (int y=0; y<5; y++)
      {
        randString[x] = replaceRandChar(randString[x]); //randomly replace digit
      }
      canvas.drawString(randString[x], (width)-newVelocity[x], 33*(x+1));
      newVelocity[x] += velocity[x]; //change x-position of string
      
    }
    printChar(frameCount); //checks to see if it needs to start printing info
    panel.repaint();
    return false;
  
  }
  
  /**
   * randomly picks a number in the string and replaces it  
   *  with another random number
   * @param s String of numbers to be changed
   * @return changed string
   */
  public String replaceRandChar(String s) 
  { 
    String number = "";
    int pos = rand.nextInt(s.length());  //replaces a single number in a string with a random one
    int num = rand.nextInt(11);
    if (num == 10) number += " "; //sometimes it gets replaced with a space
    else number += num;
    
    return s.substring(0,pos) +number+ s.substring(pos+1); //Concatenates new string
  }
  
  /**
   * Draws the level, unlock, and title when the number string
   *  gets to a certain point. Draws it by adding single character
   *  to it every 5 frames
   * @param fC the frame that the drawing is on
   */
  public void printChar(int fC) //was going to make 1 method and send info to it so i didn't have to 
  {                             //  copy paste, but too many numbers had to be sent through
    if (newVelocity[4] > 638) //unlock string
    { canvas.setColor(Color.BLACK);
      canvas.fillRect(0, 132, 27*tempUnlock.length()+STR_LEFT[0]+2, 35); //erases only how much space the 
      canvas.setColor(myColor);                                // string currently takes
      if (fC%5 == 0) //adds character every 5 frames
      { if (charPos[0] > STR1.length()-1);
        else tempUnlock += STR1.substring(charPos[0], charPos[0]+1);
        charPos[0]++;
      }
      canvas.drawString(tempUnlock, STR_LEFT[0], 165);
    }
    
    if (newVelocity[9] > 383)  //level number
    { canvas.setColor(Color.BLACK);
      canvas.fillRect(0, 330, 27*tempLevel.length()+STR_LEFT[1]+2, 35); 
      canvas.setColor(myColor);
      if (fC%5 == 0)
      { if (charPos[1] > STR2.length()-1);
        else tempLevel += STR2.substring(charPos[1], charPos[1]+1);
        charPos[1]++;
      }
      canvas.drawString(tempLevel, STR_LEFT[1], 363); 
    }
    
    if (newVelocity[13] > 678)  //title
    { canvas.setColor(Color.BLACK);
      canvas.fillRect(0, 462, 27*tempTitle.length()+STR_LEFT[2]+2, 35); 
      canvas.setColor(myColor);
      if (fC%5 == 0)
      { if (charPos[2] > STR3.length()-1);
        else tempTitle += STR3.substring(charPos[2], charPos[2]+1); 
        charPos[2]++;
      }
      canvas.drawString(tempTitle, STR_LEFT[2], 495);
    }   
  }   
}
