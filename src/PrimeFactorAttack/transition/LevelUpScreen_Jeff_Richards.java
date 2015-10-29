package PrimeFactorAttack.transition;

/*******************************************************
 *Level Up Screen for Prime Factor Game
 * Created by Jeffrey Richards
 * May 4, 2012 
 ********************************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//*******************************************************************
//Major modifications to LevelUpScreen_Wesley_Swedenburg
// Instead of using the Mandelbrot set I used a Julia set
// to draw my fractals. I used a constant for  the changing y 
// but x constant varies to make the appearance of a growing fractal.
// Coloring shading features were preserved, but the colors 
// are changed. Text apperance and animation is also changed 
//*******************************************************************
public class LevelUpScreen_Jeff_Richards extends LevelUpScreen
{

  private BufferedImage offscreenBuffer;
  private BufferedImage offscreenBuffer1;
  private Graphics2D canvas, canvas2;
  
  private static final String STR1 = "Kudos!!!";
  private String str2, str3;
  private String congrats, levelTmp;
  
  private int counter = 0;
  double constantReal = .131;
  float colorChange = 0;
  int congratsMoves = 0, titleMove = 0, titleStart, unlockStart;
  int unlockPixelLength;



  public LevelUpScreen_Jeff_Richards(BufferedImage offscreenBuffer,
      String str2, String str3)
  {
    if (offscreenBuffer!=null) offscreenBuffer.flush();
    int width = offscreenBuffer.getWidth();
    int height = offscreenBuffer.getHeight();
    this.offscreenBuffer = offscreenBuffer;
    canvas = (Graphics2D)offscreenBuffer.getGraphics();
    offscreenBuffer1 = new BufferedImage(width, height, 
        BufferedImage.TYPE_INT_RGB);
    canvas2 = (Graphics2D)offscreenBuffer1.getGraphics();
    
    this.str2 = str2;
  
  
    //Creates the string characteristics and sets sizes
    Font superLargeFont = new Font("DIALOG", Font.ITALIC, 26);
    canvas.setFont(superLargeFont);
    FontMetrics fm = canvas.getFontMetrics(superLargeFont);
    int myStringPixelLength = fm.stringWidth(str3);
    int myFontHeight = fm.getHeight();
    unlockPixelLength = fm.stringWidth(str2);
    

    congrats = "";
    levelTmp = "";
    
    this.str3 = str3;
    titleStart = (width - myStringPixelLength) - 220;
    unlockStart = width / 2 - unlockPixelLength;
    unlockPixelLength = fm.stringWidth(str2);
    
    
    // Unused old code
    
    /*canvas.setColor(new Color(255,250,150));
    canvas.drawString(unlockStr, 200, 300);
    canvas.drawString(title, 200, 500);
    canvas.drawString(levelNum, 200, 100);
    */
    
    
    update();
    constantReal = constantReal - .01;
  }
  //****************************************************************
  //This method (julia) determines where the fractal will show
  //up on screen using the Julia fractal equation that varies.
  //It also determines the original color the fractal
  //will be. Please note that this method uses a mathematical concept. 
  //****************************************************************
  public Color julia(double x, double y)
  {
    float w = (float)offscreenBuffer.getWidth();
    float h = (float)offscreenBuffer.getHeight();
    Color m;
    
    //Sets up and executes the equations for the fractal
  
    x = 1.5 * (x - w / 2) / (0.5 * w);
    y = (y - h / 2) / (0.5 * h);
  
    double cI = .75;
    double xOld = x;
    
    double iteration = 0;
    double max_iteration = 65;

    while(x*x + y*y < 4 && iteration < max_iteration)
    {
      //the actual iteration, the real and imaginary part are calculated
      xOld = x * x - y * y + constantReal;
      y = 2 * x * y + cI;

      x = xOld;
      iteration = iteration + 1;
    }
  
    if (x*x + y*y < 4 || iteration < 0) m = Color.black;
    else
    {
      float c = ((float)iteration / (float)max_iteration);
      m = new Color(c, 0, 0);
    }
   
    return m;
  }
  //*******************************************************************
  //This method (update) updates the picture being seen by the user to
  //display changing animations, this method is called by 
  //LevelUpFrame at a particular time rate. 
  //*******************************************************************
  public boolean update()
  {
    //System.out.println("LevelUpScreen_Jeff_Richards.update()");
    if (counter < 14)
    {
      //Changes the constant variable for each frame, which gives the
      // appearance of a growing fractal.
      constantReal = constantReal - .01;
  
      for(int x = 0; x < offscreenBuffer.getWidth(); x++)
      {
        for(int y = 0; y < offscreenBuffer.getHeight(); y++)
        {
          Color pixelColor = julia(x,y);
          canvas2.setColor(pixelColor);
          canvas2.drawRect(x, y, 1, 1);
        }
      }
    }
 
    //Throwing second offscreenBuffer on screen
    canvas.drawImage(offscreenBuffer1, 0, 0, null);
    counter++;
    colorChange = colorChange + (1.f / 450);
    
    if (colorChange > 1) colorChange = 1;

    //Below are the implementation of the fade, movement, and color
    // effects on the Strings by calling methods for each String  
    // depending on the counter (which frame the String begins to 
    // appear. 
    
    if (counter > 15 && counter < 210) drawCongrats();
    
    if (counter > 100)
    {
      drawTitle();
      drawUnlock();
      titleMove++;
    }
    
    if (counter >= 60) drawLevel();
    
    if (counter <= 450) return true;
    
    return false;
  
  }
  
  //Draws and creates effects on the Level String
  private void drawLevel()
  {
    int tempLev = counter % 15;
    
    if (tempLev == 0 && levelTmp.length() < STR1.length())
    {
      int x = (counter - 60) / 15;
      levelTmp = levelTmp + STR1.charAt(x);
    }
    
    //canvas.setColor(new Color(1 - colorChange, 0, 0, colorChange));
    canvas.setColor(new Color(colorChange, 0, 0));
    canvas.drawString(levelTmp, 55, 250);
  }

  //Draws and creates effects on the Congrats String
  private void drawCongrats()
  {
    String temp = "", temp2 = "Congratulations";
    if (counter > 15 && counter < 77 && counter % 4 == 0)
    {
      int x = (counter - 16) / 4;
      for (int y = 0; y < x; y++)
      {
        temp = temp + temp2.charAt(y);
      }
      congrats = temp;
    }
    canvas.setColor(Color.white);
    canvas.drawString(congrats, congratsMoves * 4, 200);
    congratsMoves++;
  }
  
  //Draws and creates effects on the Title String
  private void drawTitle()
  {
    if (titleStart - titleMove < 56) titleStart++;
    
    //canvas.setColor(new Color(0, 0, 1-colorChange, colorChange));
    canvas.setColor(new Color(0, 0, colorChange));
    canvas.drawString(str3, titleStart - titleMove, 100);
  }

  //Draws and creates effects on the Unlock String
  private void drawUnlock()
  {
    int x = unlockStart + titleMove + unlockPixelLength;
    
    if (x + 20 > offscreenBuffer.getWidth()) unlockStart--;
    
    //canvas.setColor(new Color(0, 1-colorChange, 0, colorChange));
    canvas.setColor(new Color(0, colorChange, 0));
    canvas.drawString(str2, unlockStart + titleMove, 475);
  }
}
