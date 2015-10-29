package PrimeFactorAttack.transition;
/*******************************************
 *@version 2012.0504
 *@author Ben Mixon-Baca 
 ******************************************/

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;

/**************************************************************
 * This Class extends the abstract class LevelUpScreen 
 * so it can be called with the same methods and Constructor
 * as those called in LevelUpFrame.
 *************************************************************/
public class LevelUpScreen_Ben_Mixon_Baca extends LevelUpScreen
{
  //Instance variables for this class specifically
  private BufferedImage myBufferedImage;
  private String myUnlockStr, myTitle;
  private String congratulations,congratulations2;
  private String[] myUnlockChars;
  private int width, height, midX, midY, oneQuarterX, threeQuartersX;
  private int counter, counter2;
  private float shadeFactor = 1;
  private int ballWidth = 0, ballHeight = 0;
  private Graphics2D canvas;
  private GradientPaint ballColor;
  private GeneralPath[] triangles = new GeneralPath[10];
  private GeneralPath[] fillTriangles = new GeneralPath[10];
  private Color transparentGlass = new Color(0,25,100,25);
  private Color transparentWindow = new Color(190,190,255,150);
  private Color transparentBlack = new Color(5,5,5,170);
  private Font myFont = new Font("Sansarif", Font.BOLD, 45);


  /************************************
   * empty/default Constructor.
   ***********************************/
  public LevelUpScreen_Ben_Mixon_Baca()
  {


  }

  /***************************************
   * This Constructor is the one used in 
   * LevelUpFrame to build the different 
   * extensions of the abstract class 
   * LevelUpScreen.
   **************************************/
  public LevelUpScreen_Ben_Mixon_Baca(BufferedImage offscreenBuffer, String unlockStr, int level, String title)
  {
    width = offscreenBuffer.getWidth();//sets the width int of this levelupScreen
    height = offscreenBuffer.getHeight();//sets the height int of this levelupScreen 
    midX = width / 2;//sets the middle X value of this levelupScreen 
    midY = height / 2;//sets the middle Y value of this levelupScreen
    oneQuarterX = midX/2;//sets the quarter length value of this levelupScreen 
    threeQuartersX = midX+oneQuarterX;//sets the three quarters length of this levelupScreen 


    this.myBufferedImage = offscreenBuffer; // sets myBuffered image equal to the one passed through the constructor.
    this.canvas = (Graphics2D) this.myBufferedImage.getGraphics();//sets this instance of canvas to a Graphics2D object.
    this.canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, //  turn on anti-aliasing
        RenderingHints.VALUE_ANTIALIAS_ON);
    this.myUnlockStr = unlockStr;//
    this.myTitle = title;//
    this.congratulations ="Congratulations!"; //
    this.congratulations2 = "You made it to level "+level;//
    this.canvas.setColor(transparentWindow);//
    this.canvas.fillRect(0,0,width,height);//

    this.myUnlockChars = new String[myUnlockStr.length()];//

    for(int x = 0; x<myUnlockStr.length();x++)
    {
      this.myUnlockChars[x] = myUnlockStr.substring(x,x+1);//
    }

  }

  /********************************************************
   * private GeneralPath makeTriangles(int x, int counter)
   * 
   * @param x
   * @param counter:
   *
   * This method contains a bunch of control statements
   * that use some of the other class variables such as
   * midX, midY, width, height, oneQuarterX,threeQuartersX
   * to seperate the screen into 10 triangles which are
   * used to simulate the shattering of glass.
   * 
   * This method only draws the outline of the glass
   * fragments
   * 
   * @return GeneralPath. This method returns a GeneralPath
   *         object.(Outline of the glass)
   *******************************************************/
  private GeneralPath makeTriangles(int x,int counter)
  {

    GeneralPath triangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD,3);

    if(x==0)
    {
      triangle.moveTo(0+counter,midY+(((counter*counter)/2)));
      triangle.lineTo(midX+(counter/2),midY+(((counter*counter)/2)));
      triangle.lineTo(0+(counter*counter),0+(((counter*counter)/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==1)
    {
      triangle.moveTo(0+counter,0+(((counter*counter)/4)));
      triangle.lineTo(midX-counter,midY+(((counter*counter)/3)));
      triangle.lineTo(oneQuarterX+counter,0+(((counter*counter)/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==2)
    {
      triangle.moveTo(oneQuarterX+counter,0+(counter*counter));
      triangle.lineTo(midX-counter,midY+(counter*counter)/2);
      triangle.lineTo(threeQuartersX-counter,0+(counter*counter));
      triangle.closePath();
      return triangle;
    }

    else if(x==3)
    {
      triangle.moveTo(threeQuartersX-counter,0+((counter*counter/3)));
      triangle.lineTo(midX,midY+((counter*counter/2)));
      triangle.lineTo(width-counter,+((counter*counter*counter/4)));
      triangle.closePath();
      return triangle;
    }

    else if(x==4)
    {
      triangle.moveTo(width-(counter*3),0+((counter*counter/2)));
      triangle.lineTo(midX+counter,midY+((counter*counter/3)));
      triangle.lineTo(width-(counter*2),midY+((counter*counter/4)));
      triangle.closePath();
      return triangle;
    }

    else if(x==5)
    {
      triangle.moveTo(counter,midY+((counter*counter/4)));
      triangle.lineTo(midX-counter,midY+((counter*counter/5)));
      triangle.lineTo(counter,height+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }

    else if(x==6)
    {
      triangle.moveTo(0,height+((counter*counter/7)));
      triangle.lineTo(midX+counter,midY+((counter*counter/8)));
      triangle.lineTo(oneQuarterX-counter,height+((counter*counter/9)));
      triangle.closePath();
      return triangle;
    }

    else if(x==7)
    {
      triangle.moveTo(oneQuarterX-counter*3 , height+((counter*counter/2)));
      triangle.lineTo(midX+counter*2 , midY+((counter*counter/2)));
      triangle.lineTo(threeQuartersX-counter,height+((counter*counter/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==8)
    {
      triangle.moveTo(threeQuartersX+counter,height+((counter*counter/6)));
      triangle.lineTo(midX+counter*2, midY+((counter*counter/2)));
      triangle.lineTo(width-counter,height+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }

    else
    {
      triangle.moveTo(width-(counter*counter),height+((counter*counter/2)));
      triangle.lineTo(midX-(counter*2),midY+((counter*counter/7)));
      triangle.lineTo(width-(counter*5),midY+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }
  }

  /********************************************************
   * private GeneralPath fillTriangles(int x, int counter)
   * 
   * @param x
   * @param counter
   * 
   * This method contains a bunch of control statements
   * that use some of the other class variables such as
   * midX, midY, width, height, oneQuarterX,threeQuartersX
   * to seperate the screen into 10 triangles which are
   * used to simulate the shattering of glass.
   * 
   * This method fills the glass
   * fragments outline.
   * 
   * @return GeneralPath. This method returns a GeneralPath
   *         object (filled in pieces of glass)
   * 
   *******************************************************/
  private GeneralPath fillTriangles(int x,int counter)
  {

    GeneralPath triangle = new GeneralPath(GeneralPath.WIND_EVEN_ODD,3);

    if(x==0)
    {
      triangle.moveTo(1+counter,(midY+1)+((((counter*counter)/2))));
      triangle.lineTo(((midX-1)+(counter/2)),(midY-1)+(((counter*counter)/2)));
      triangle.lineTo(1+(counter*counter), 1+(((counter*counter)/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==1)
    {
      triangle.moveTo(1+counter,1+(((counter*counter)/4)));
      triangle.lineTo((midX-1)-counter,((midY-1)+(((counter*counter)/3))));
      triangle.lineTo(((oneQuarterX-1)+counter),1+(((counter*counter)/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==2)
    {
      triangle.moveTo(((oneQuarterX+1)+counter),1+(counter*counter));
      triangle.lineTo((midX)-counter,((midY+1)+(counter*counter)/2));
      triangle.lineTo((threeQuartersX-1)-counter,1+(counter*counter));
      triangle.closePath();
      return triangle;
    }

    else if(x==3)
    {
      triangle.moveTo((threeQuartersX+1)-counter,1+((counter*counter/3)));
      triangle.lineTo((midX+1),(midY-1)+((counter*counter/2)));
      triangle.lineTo((width-1)-counter,1+((counter*counter*counter/4)));
      triangle.closePath();
      return triangle;
    }

    else if(x==4)
    {
      triangle.moveTo((width-1)-(counter*3),1+((counter*counter/2)));
      triangle.lineTo((midX+1)+counter,(midY-1)+((counter*counter/3)));
      triangle.lineTo((width-1)-(counter*2),(midY-1)+((counter*counter/4)));
      triangle.closePath();
      return triangle;
    }

    else if(x==5)
    {
      triangle.moveTo(1+counter,(midY+1)+((counter*counter/4)));
      triangle.lineTo((midX-1)-counter,(midY+1)+((counter*counter/5)));
      triangle.lineTo(1+counter,(height-1)+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }

    else if(x==6)
    {
      triangle.moveTo(1,(height-1)+((counter*counter/7)));
      triangle.lineTo((midX-1)+counter,(midY+1)+((counter*counter/8)));
      triangle.lineTo((oneQuarterX-1)-counter,(height-1)+((counter*counter/9)));
      triangle.closePath();
      return triangle;
    }

    else if(x==7)
    {
      triangle.moveTo((oneQuarterX+1)-counter*3 , (height+1)+((counter*counter/2)));
      triangle.lineTo((midX+1)+counter*2 , (midY+1)+((counter*counter/2)));
      triangle.lineTo((threeQuartersX-1)-counter,(height-1)+((counter*counter/2)));
      triangle.closePath();
      return triangle;
    }

    else if(x==8)
    {
      triangle.moveTo((threeQuartersX+1)+counter,(height+1)+((counter*counter/6)));
      triangle.lineTo((midX+1)+counter*2, (midY+1)+((counter*counter/2)));
      triangle.lineTo((width-1)-counter,(height-1)+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }

    else
    {
      triangle.moveTo((width-1)-(counter*counter),(height+1)+((counter*counter/2)));
      triangle.lineTo((midX+1)-(counter*2),(midY+1)+((counter*counter/7)));
      triangle.lineTo((width-1)-(counter*5),(midY-1)+((counter*counter/6)));
      triangle.closePath();
      return triangle;
    }
  }

  /*******************************************************
   * private void drawBall(int ballwidth, int ballheight)
   *
   * @param ballwidth
   * @param ballheight
   *        this two arguments are actually passed
   *        the same number when called in the method
   *        update. It was just more convientent to 
   *        treat the number as x and y components 
   *        individually.
   ******************************************************/
  private void drawBall(int ballwidth, int ballheight)
  {

    // shadeFactor is used to dynamically shade the ball, 
    // you should notice the ball getting darker as it 
    // gets close to the screen
    shadeFactor+=.02;


    // Intially fills the image buffer with a plain white background
    // so the ball can be drawn on top.
    this.canvas.setColor(Color.WHITE);
    this.canvas.fillRect(0,0,width,height);


    // Control statement that allows the ball to bounce away from
    // the screen after it reaches a certain frame count
    // (ballWidth is used to test where the ball should be
    // relative to the closest object in the image i.e. glass
    // that shatters. As well as scale the ball size and keep 
    // it centered in the screen)
    if(ballwidth>120 && ballheight>120)
    {
      ballWidth -=20;
      ballHeight -=20;
      ballColor = new GradientPaint((width/2)-(ballWidth/2)* shadeFactor, (height/2)-(ballHeight/2),
          Color.WHITE, (width/2)+(ballWidth/2), (height/2)+ (ballHeight/2)* shadeFactor, Color.RED);
      canvas.setPaint(ballColor); 
      canvas.fillOval((width/2)-(ballWidth/2), ((height/2)-(ballHeight/2))+(((ballheight%120)*(ballheight%120))/2), ballWidth, ballHeight);
    }

    else if((ballwidth<=120 && ballheight<=120))
    {
      ballWidth +=5;
      ballHeight +=5;
      ballColor = new GradientPaint((width/2)-(ballWidth/2)* shadeFactor, (height/2)-(ballHeight/2), 
          Color.WHITE, ((width/2)+(ballWidth/2)),(height-2) + (ballHeight/2), Color.RED);
      canvas.setPaint(ballColor); 
      canvas.fillOval(((width/2)-(ballWidth/2)), ((height/2)-(ballHeight/2)), ballWidth , ballHeight);
    } 

    canvas.setColor(transparentBlack);

    if(ballwidth<120)
    {
      canvas.drawString(myUnlockStr, 150, (3*ballwidth)/2);    
    }  
    if(ballheight<122)
    {
      this.canvas.setColor(transparentWindow);
      this.canvas.fillRect(0,0,width,height);
    }
  }

  /***************************************************
   * private Color myGlassColors()
   *
   * 
   * @return Color: returns a Color object unique
   *                to each fragment of glass
   *                at a unique frame count number.
   **************************************************/
  private GradientPaint myGlassColors(int x, int counter)
  {
  
  
  
  //I think this method is overly complicated but I am still trying to figure out how to light the
  //glass properly based on the ball position.
     int r = 0; 
     int g = 25;
     int b = 100; 
     int alpha = (counter%120);
     Color myColor; 
     myColor = new Color(r,g,b,alpha);
     GradientPaint myGradientColor;
     
     if(x==0)
     {
  
       myGradientColor =new GradientPaint(0, 0, Color.WHITE, midX, midY, myColor);
       return myGradientColor;
     }
     
     else if(x == 1)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(0, 0,Color.WHITE, midX, midY,  myColor);
       return myGradientColor;
     }
     
     else if(x == 2)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(0, 0, Color.WHITE, midX, midY, myColor);
       return myGradientColor;
     }
     
     else if(x == 3)
     {
       //myColor = new Color(r,g+(counter%230),b,alpha+(counter%230));
       myGradientColor =new GradientPaint(midX, 0, Color.WHITE, threeQuartersX, midY, myColor);
       return myGradientColor;
     }
     
     else if(x == 4)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(midX, 0,  Color.WHITE, width, height,myColor);
       return myGradientColor;
     }
     
     else if(x == 5)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(0, midY, Color.WHITE, oneQuarterX, height, myColor);
       return myGradientColor;
     }
     
     else if(x == 6)
     {
       //myColor = new Color(r+(counter%255),g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(0, midY, Color.WHITE, oneQuarterX, height, myColor);
       return myGradientColor;
     }
     
     else if(x == 7)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(oneQuarterX, midY, Color.WHITE, width, threeQuartersX, myColor);
       return myGradientColor;
     }
     else if(x == 8)
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(midX, midY, Color.WHITE, width, height, myColor);
       return myGradientColor;
     }
     else
     {
       //myColor = new Color(r,g,b,alpha+(counter%230));
       myGradientColor =new GradientPaint(midX, midY, Color.WHITE, width, height, myColor);
       return myGradientColor;
     }
  }  

  /*************************************************************
   * private void drawFallingString()
   *
   * 
   * This method seperates the unlockString into its individual
   * characters using method substring(int, i1, int i2)
   * so they can be drawn and given individual downward
   * velocities.
   *
   ************************************************************/
  private void drawFallingString()
  {
    for(int x = 0 ; x <myUnlockStr.length();x++)
    {
      if(x==5)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+(counter2/+3),170+((counter2*counter)/3));
      }
      else if(x%8==0)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+((counter2/4)),170+((counter2*counter)/5));        
      }
      else if(x%8==1)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)-(counter2/3),170+((counter2*counter)/7));   
      }
      else if(x%8==2)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+(counter2/4),170+((counter2*counter)/5));   
      }
      else if(x%8==3)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)-(counter2/3),170+((counter2*counter)/7));   
      }
      else if(x%8==4)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+(counter2/3),170+((counter2*counter)/11));   
      }
      else if(x%8==5)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)-(counter2/2),170+((counter2*counter)/9));   
      }
      else if(x%8==6)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+(counter2/5),170+((counter2*counter)/6));   
      }
      else if(x%8==7)
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)-(counter2/2),170+((counter2*counter)/8));   
      }
      else
      {
        canvas.drawString(myUnlockChars[x],((x*30)+150)+(counter2/4),170+((counter2*counter)/7));           
      }
    }
  }

  /***************************************************
   * private void drawNonFallingStrings()
   * 
   * This method draws the strings seen on screen(non-
   *  moving strings)
   * which appear after the glass is shattered 
   **************************************************/
  private void drawNonFallingStrings()
  {
    canvas.drawString(myTitle, myTitle.length()/2,200);
    canvas.drawString(congratulations2, congratulations.length()/2 ,150);
    canvas.drawString(congratulations, congratulations2.length()/2,100);
  }

  /***************************************************
   * public boolean update()
   * 
   * method inherited from the abstract class
   * LevelUpScreen written by Joel Castellanos.
   **************************************************/
  public boolean update()
  { 
    canvas.setFont(myFont);//sets the Strings font
    counter++;// Frame counter used to animate the  screen and to 
    // stop the animation as needed

    if(counter<250)//max frame count currently allowed is 250 frames.
    {

      this.drawBall(counter, counter); // draws the ball that appears to be moving 
      //closer and close to the screen

      if(counter>120)
      {

        counter2++;
        canvas.setColor(transparentBlack);

        this.drawFallingString();

        this.drawNonFallingStrings();//draws the Strings that do not fall

        canvas.setColor(transparentBlack);

        // These two loops are used to constantly change the triangles' x and y locations
        // can look like they are tumbling  
        for(int x = 0; x<10;x++)
        {   
          triangles[x] = makeTriangles(x,counter2); 
          canvas.draw(triangles[x]);
        }



        for(int x = 0;x<10;x++)
        {

          fillTriangles[x] = fillTriangles(x,counter2); 
          canvas.setPaint(myGlassColors(x, counter));
          canvas.fill(fillTriangles[x]);
        }
      }
      return true;//returns true if the frame count is less than 450.
    }

    else
    {
      return false; //returns false if the frame count exceeds 450.
    }
  }
}
