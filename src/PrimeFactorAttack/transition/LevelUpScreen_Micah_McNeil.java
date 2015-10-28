package PrimeFactorAttack.transition;
/*Author: Micah McNeil
   * Created On: May 4, 2012
   * Description: This class creates an unlocked level screen in an off screen
   * buffer that gets called by LevelUp Frame. It is designed to congratulate 
   * the player, and tell them which level they have unlocked. I got inspiration
   * for the "CONGRATULATIONS" text from Michelle Godfrey, but I added my own 
   * twist. The text gets larger as it is coming to the player then fades out. 
   * I added the rest of the text in a similar fashion. I used the 
   * fireworks system developed by Micahel Asplund to add a little flare while
   * telling the player which level was unlocked. After some time, the picture 
   * fades to black.
   */

//Imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;



public class LevelUpScreen_Micah_McNeil extends LevelUpScreen
{
	private BufferedImage offscreenBuffer;
	private Graphics2D canvas;
	private Random rand;
	
	//uses upper left corner of screen
	private rocket[] rock = new rocket[1000];
	private rocket[][] explode = new rocket[1000][100];
	
	
	private String title;
	private String unlockStr;
	private String levelStr;
	private static int frameCounter = 0;
	private int count = 0;
	private int wordFader = 0;

	//Font sizes and positioning for all texts
	private int fontSize_1 = 40;
	private float fontSize_2 = 40;
	private int fontSize_3 = 40;
	private int fontXPosition_1 = 230;
	private int fontYPosition_1 = 320;
	private int fontXPosition_2 = 230;
	private float fontYPosition_2 = 320;
	private int fontXPosition_3 = 230;
	private int fontYPosition_3 = 320;
	private int fontXPosition_4 = 230;
	private int fontYPosition_4 = 320;
	
	//Switch for when to draw
	private boolean drawTextOne = true;
	private boolean drawTextTwo = false;
	
	//Switch for when to fade
	private boolean fadeSwitch = true;
	


//initializing strings, booleans and int.
public LevelUpScreen_Micah_McNeil(BufferedImage offscreenBuffer, 
		String unlockStr,int level, String title)
{
  this.title=title;
  this.unlockStr=unlockStr;
  this.offscreenBuffer = offscreenBuffer;
  this.levelStr="Level "+level;           
  canvas = (Graphics2D) offscreenBuffer.getGraphics();
  
  //create a random that will be used
  rand = new Random();
  
  // resets values
  wordFader= 0;
  frameCounter = 0;
  fontSize_1 = 40;
  fontXPosition_1 = 230;
  fontYPosition_1 = 320;
}

//handles the updates
public boolean update()
{
	// keep track of duration
	frameCounter++;
	
	//keep track of how many frames
	//System.out.println(frameCounter);
  
	//draw Congratulations text
	if (drawTextOne)
	{
		DrawCongratulationsText();
	}
	
	//Draw the Unlocked text
	if (drawTextTwo)
	{
		DrawUnlockedText();
	}
	
	//tells when to start firing ze rockets!
	if (frameCounter > 225)
	{
		if(rand.nextInt(6)==0 && frameCounter < 375)
	    {
	      launch(count);
	    }
	    fly();
	}

  	

	// fade everything out
	if (frameCounter >= 399)
	{
		canvas.setColor(new Color(0,0,0, 25));
		canvas.fillRect(0, 0, offscreenBuffer.getWidth(), 
				  offscreenBuffer.getWidth());
	}
	// ends the game
	if (frameCounter >= 450)
	{
		return true;
	}
	return false;
}

//handles first text fade in/out and scaling and offset
private void DrawCongratulationsText()
{
  	if (wordFader <= 250 && fadeSwitch == true)
  	{
  		wordFader+=5;
  		
		if (wordFader == 255)
		{
			fadeSwitch = false;
		}
		
		fontSize_1 += 1;
		fontXPosition_1 -= 4;
		
		canvas.setFont(new Font("SansSerif", Font.BOLD, fontSize_1));
		canvas.setColor(new Color(wordFader,wordFader,wordFader));
		canvas.drawString("Congratulations", fontXPosition_1, fontYPosition_1);
		
		canvas.setColor(new Color(0,0,0, 25));
		canvas.fillRect(0, 0, offscreenBuffer.getWidth(), 
				offscreenBuffer.getWidth());
  	}
  	if (wordFader >= 3 && fadeSwitch == false)
  	{
	  wordFader -= 3;
	  // switch off to next text
	  if (wordFader == 0)
	  {
		  fadeSwitch= true; 
		  
		  drawTextOne = false;
		  drawTextTwo = true;
		  
		  fontSize_2 = 1;
		  fontSize_3 = 20;
		  fontXPosition_2 = 230;
		  fontYPosition_2 = 280;
	  }

	  fontSize_1 += 1;
	  fontXPosition_1-= 4;

  	  canvas.setFont(new Font("SansSerif", Font.BOLD, fontSize_1));
	  canvas.setColor(new Color(wordFader,wordFader,wordFader));
	  canvas.drawString("Congratulations", fontXPosition_1, fontYPosition_1);
	  
	  canvas.setColor(new Color(0,0,0, 25));
	  canvas.fillRect(0, 0, offscreenBuffer.getWidth(), 
			  offscreenBuffer.getWidth());
  	}
}

//handles first text fade in/out and scaling and offset
private void DrawUnlockedText()
{
  	if (wordFader <= 250 && fadeSwitch == true)
  	{
  		wordFader+=5;
  		
  		// switch the fade switch to false when we reach text RGB peak
		if (wordFader == 255)
		{
			fadeSwitch = false;
		}
		
		fontSize_2 += 0.75;
		fontSize_3 += 1;
		fontXPosition_2 -= 4;
		fontYPosition_2 -= 3.5;
		fontXPosition_3 -= 4;
		fontYPosition_3 -= 3;
		fontXPosition_4 -= 4;
		fontYPosition_4 += 3;
		
		canvas.setFont(new Font("SansSerif", Font.BOLD, (int)fontSize_2));
		canvas.setColor(new Color(wordFader,wordFader,wordFader));
		canvas.drawString("Unlocked " + levelStr, fontXPosition_2, 
				(int)fontYPosition_2);
		canvas.setFont(new Font("SansSerif", Font.BOLD, fontSize_3));
		canvas.drawString(title, fontXPosition_3, fontYPosition_3);
		canvas.drawString(unlockStr, fontXPosition_4, fontYPosition_4);
		
		canvas.setColor(new Color(0,0,0, 25));
		canvas.fillRect(0, 0, offscreenBuffer.getWidth(), 
				offscreenBuffer.getWidth());
  	}
}

//handles the launch of the fireworks
private void launch(int x)
{
  rock[x] = new rocket();
  rock[x].posX = offscreenBuffer.getWidth()/2 + rand.nextInt(100)-50;
  rock[x].posY = offscreenBuffer.getHeight();
  rock[x].red = rand.nextInt(256);
  rock[x].green = rand.nextInt(256);
  rock[x].blue = rand.nextInt(256);
  Color rocketColor = new Color(rock[x].red,
    rock[x].green, rock[x].blue);
  canvas.setColor(rocketColor);
  canvas.fillRect(rock[x].posX, rock[x].posY, 4, 6);
  int velocity = rand.nextInt(10) + 25;
  double dir = rand.nextDouble()*Math.PI+Math.PI/4;
  rock[x].velX = velocity*Math.cos(dir)/2;
  rock[x].velY = velocity*Math.sin(dir);
  count++;
}

//handles all movement of the fireworks
private void fly()
{
  canvas.setColor(Color.black);
  for(int x = 0; x < count; x++)
  {
    canvas.fillRect(rock[x].posX, rock[x].posY, 4, 6);
  }
  for(int x = 0; x < count; x++)
  {
    if(rock[x].velY <= -3)
    {
      explode(x);
    }
    else
    {
      rock[x].posY-=rock[x].velY;
      rock[x].posX+=rock[x].velX;
      rock[x].velY-=.9;
      Color rocketColor = new Color(rock[x].red, rock[x].green,rock[x].blue);
      canvas.setColor(rocketColor);
      canvas.fillRect(rock[x].posX, rock[x].posY, 4, 6);
    }
  }
}

//handles the explosion and the movement of the particles
private void explode(int x)
{
  if(rock[x].exp == 0)
  {  
    canvas.setColor(Color.white);
    //make circle
    canvas.fillOval(rock[x].posX -5, rock[x].posY -5, 20, 20);  
    rock[x].exp++;
    for( int i = 0; i < rand.nextInt(50)+50; i++)
    {
      explode[x][i] = new rocket();
      explode[x][i].blue = rock[x].blue;
      explode[x][i].red = rock[x].red;
      explode[x][i].green = rock[x].green;
      explode[x][i].posX = rock[x].posX;
      explode[x][i].posY = rock[x].posY;
      rock[x].ecount++;
      int velocity = rand.nextInt(7) + 7;
      double dir = rand.nextDouble()*Math.PI*2;
      explode[x][i].velX = velocity*Math.cos(dir);
      explode[x][i].velY = velocity*Math.sin(dir);
    }
  }
  else if(rock[x].exp == 1)
  {
    canvas.setColor(Color.black);
    canvas.fillOval(rock[x].posX -5, rock[x].posY -5, 20, 20);
    rock[x].exp++;
  }
  else
  {
    for(int i = 0; i<rock[x].ecount; i++)
    {
      canvas.setColor(Color.black);
      canvas.fillRect(explode[x][i].posX, explode[x][i].posY, 2, 2);
      
      explode[x][i].posY-=explode[x][i].velY;
      explode[x][i].posX+=explode[x][i].velX;
      explode[x][i].velY-=.9;
      Color rocketColor = new Color(explode[x][i].red, explode[x][i].green,
          explode[x][i].blue);
      canvas.setColor(rocketColor);
      canvas.fillRect(explode[x][i].posX, explode[x][i].posY, 2, 2);
    }
  }
}

//rockets public class
public class rocket
{
  public int posX, posY;
  public int red, blue, green;
  public double velX, velY;
  public int exp=0, ecount=0;
}

}
