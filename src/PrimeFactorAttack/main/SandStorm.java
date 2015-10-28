package PrimeFactorAttack.main;

import java.awt.Color;
import java.util.Random;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.Utility;
/*******************************************************

********************************************************/

public class SandStorm
{
  
  private static int canvasWidth, canvasHeight;
  private static GameCanvas canvas;
  
  
  
  private static Random rand = new Random();
  
  private static int[] sandColor = { 0x3a242b, 0x3b2426, 0x352325, 0x836454, 0x7d5533, 0x8b7352, 
      0xb1a181, 0xa4632e, 0xbb6b33, 0xb47249, 0xca7239, 0xd29057, 0xe0b87e, 
      0xd9b166, 0xf5eabe, 0xfcfadf, 0xd9d1b0, 0xfcfadf, 0xd1d1ca, 0xa7b1ac, 
      0x879a8c, 0x9186ad, 0x776a8e,0x776a8e,0x776a8e,0x776a8e,0x776a8e,0x3a242b,0x3a242b};
  
  
  
  //sprayCount is the number of sand particles plotted with each step of each comet 
  private static final int SPRAY_COUNT_MISS = 60; 
  private static final double SPRAY_DISTANCE_MISS = 1; 
  
  private static final int SPRAY_COUNT_HIT = 28; 
  private static final double SPRAY_DISTANCE_HIT = 26;


  
  //timeSteps is the number of moves each comet makes from the first to last frame.
  private static final int TIME_STEPS_PER_UPDATE = 100;
  private static final int UPDATES_PER_SANDSTORM = 10;
  private static final int TIME_STEPS = TIME_STEPS_PER_UPDATE*UPDATES_PER_SANDSTORM;
  
  
  //Values of location (angle and radius) and velocity of each comet.
  private static double[] angle = new double[Game.MAX_FACTOR];
  private static double[] radius = new double[Game.MAX_FACTOR];
  private static double[] angularSpeed = new double[Game.MAX_FACTOR];
  private static double[] radialSpeed = new double[Game.MAX_FACTOR];
  

  //The origin of each comet's orbit is the center of the brick when hit is true.
  //However, if hit is false, each comet has a different and random center.
  private static int[] originX = new int[Game.MAX_FACTOR];
  private static int[] originY = new int[Game.MAX_FACTOR];
    
  private static int[] orbitColor = new int[Game.MAX_FACTOR];
  
  //Target is the center of the block. It is only used when "hit" is true.
  private static int targetX, targetY;
  
  
  private static boolean stormInProgress = false;
  private static int updateIdx;
  private static boolean stormHit= false;
  private static int factor;
  private static Block block;
  //private static int timeStepCount;
  
  
  public static void setUp()
  { 
    System.out.println("startStorm.setUp()");
    stormInProgress = false;
    stormHit = false;
  }
  
  public static boolean isStormInProgress()
  { return stormInProgress;
  }
  
  public static boolean isHit()
  { return stormHit;
  }
  
  public static int[] getColors()
  { return orbitColor;
  }
  
 
  public static void startStorm(Block attackBlock, int primeFactor, boolean hit)
  { 
    
    //System.out.println("startStormMiss("+primeFactor+")");
    //Copy input value into class variable so it can be accessed by other methods in this class.

    stormHit = hit;
    block = attackBlock;
    factor = primeFactor;
    canvasWidth = Game.image.getWidth();
    canvasHeight = Game.image.getHeight();
    
    stormInProgress = true;
    updateIdx = 0;
    //timeStepCount=0;
      
    //Copy the background "permanent" image into both the sand layer and the temporary frame layer.
    Game.graphSand.drawImage(Game.image, 0, 0, null);
    Game.graphTmp.drawImage(Game.image, 0, 0, null);
    //canvas.drawBlock(block);
    
    targetX = block.getLeft() + block.getWidth()/2;
    targetY = block.getTop() + Grid.GRID_PIXELS/2;
      
    ////////////////////////////////////////////////////////////////////////////////////////////
    //Initialize each comet's start values.
      
    for (int i=0; i<factor; i++)
    { 
      orbitColor[i] = sandColor[rand.nextInt(sandColor.length)];
      
      
      int x = rand.nextInt(canvasWidth);
      int y = rand.nextInt(canvasHeight);
      if (rand.nextBoolean())
      { if (rand.nextBoolean()) y = 0; else y = canvasHeight-1;
      }
      else
      { if (rand.nextBoolean()) x = 0; else x = canvasWidth-1;
      }
      
      
      if (hit)
      { angle[i] = getPolarTheta(x,y,targetX, targetY);
        radius[i] = getPolarR(x,y,targetX, targetY);
        
        //System.out.println("SandStorm(): startOrbit ("+x+", "+y+
        //   ") -> ("+polarToX(radius[i], angle[i],targetX) +
        //    ", " + polarToY(radius[i], angle[i], targetY) +
        //    ")   polor("+angle[i]+", "+radius[i]);
        
        //Max of two full orbits
        angularSpeed[i] = rand.nextDouble()*2.0*Math.PI/TIME_STEPS;
          
        //At last timeSkip, radius = 0;
        radialSpeed[i] = -radius[i]/TIME_STEPS;  
        
      }
      
      else
      { 
        //The midpoint between the target and a random location within the 
        //  center 1/4 of the canvas
        originX[i] = (targetX + rand.nextInt(canvasWidth/2)+canvasWidth/4)/2;
        originY[i] = (targetY + rand.nextInt(canvasHeight/2)+canvasHeight/4)/2;
     
        angle[i] = getPolarTheta(x,y,originX[i], originY[i]);
        radius[i] = getPolarR(x,y,originX[i], originY[i]);
     
        //Max of 1/2 orbit
        
        //Joel: angularSpeed[i] = rand.nextDouble()*0.5*Math.PI/timeSteps;
        angularSpeed[i] = rand.nextDouble()*5.0*Math.PI/TIME_STEPS;
        if (rand.nextBoolean()) angularSpeed[i] = -angularSpeed[i];
          
        //At last timeSkip, radius = negative the original radius;
        //radialSpeed[i] = -2.0*radius[i]/TIME_STEPS;
        radialSpeed[i] = -2.0*radius[i]/TIME_STEPS;
      }   

                 
    }
    
    //stormInProgress = false;
    if (!stormInProgress) return;
    
    update();
    
  }
    
  public static void update()
  {
    
    if (stormHit) drawHit(); 
    else drawMiss();
    
    updateIdx++;
    if (updateIdx >= UPDATES_PER_SANDSTORM) abort();

    //System.out.println("SandStorm.update(): time="+updateIdx);
  }
  
  public static void abort()
  {
    stormInProgress = false;
    stormHit = false;
  }
  
  private static void drawMiss()
  {
  
    //Draw the comet's trail
    for (int time=0; time<=TIME_STEPS_PER_UPDATE; time++)
    { 

      for (int i=0; i<factor; i++)
      { 
        angle[i]  += angularSpeed[i];
        radius[i] += radialSpeed[i];
        
        
        int x = Utility.polarToX(radius[i], angle[i], originX[i]);
        int y = Utility.polarToY(radius[i], angle[i], originY[i]);
        //For speed, if the comet is off screen, do not draw its spray
        if (plotPoint(x,y,orbitColor[i]) == false) continue;
         
        //Draw the comet and its spray.
        //On the first iteration, n=0 so the random spray distance is multiplied by 0.
        //Thus, the first iteration plots a particle exactly on the orbit. 
        for (int n=1; n<SPRAY_COUNT_MISS; n++)
        {  
          double rr = radius[i]+n*SPRAY_DISTANCE_MISS*(rand.nextDouble());
          x = Utility.polarToX(rr, angle[i], originX[i]);
          y = Utility.polarToY(rr, angle[i], originY[i]);
          plotPoint(x,y,orbitColor[i]);
        }
      }
    }
  } 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  public static void drawHit()
  { 
   
    
    targetX = block.getLeft() + block.getWidth()/2;
    targetY = block.getTop() + Grid.GRID_PIXELS/2;


    ////////////////////////////////////////////////////////////////////////////////////////////
    //Draw the comet's trail
    for (int time=0; time<TIME_STEPS_PER_UPDATE; time++)
    { //timeStepCount++;
      double sprayDist = SPRAY_DISTANCE_HIT;
      for (int i=0; i<factor; i++)
      { 
        
        angle[i]  += angularSpeed[i];
        radius[i] += radialSpeed[i];
        
        //if (time==0 && SandStorm.updateIdx==0)  System.out.print("     polor("+angle[i]+", "+radius[i]);
        
        
        if (sprayDist > radius[i]) sprayDist = radius[i];
       
        
        
        
        int x = Utility.polarToX(radius[i], angle[i], targetX);
        int y = Utility.polarToY(radius[i], angle[i], targetY);
        //if (time==0 && SandStorm.updateIdx==0)  System.out.println("     plot("+x+", "+y);
        //For speed, if the comet is off screen, do not draw its spray
        if (plotPoint(x,y,orbitColor[i]) == false) continue;
         
        //Draw the comet and its spray.
        //On the first iteration, n=0 so the random spray distance is multiplied by 0.
        //Thus, the first iteration plots a particle exactly on the orbit. 
        
        for (int n=1; n<SPRAY_COUNT_HIT; n++)
        { 
          
          double rr = radius[i]+n*sprayDist*(rand.nextDouble());
          x = Utility.polarToX(rr, angle[i], targetX);
          y = Utility.polarToY(rr, angle[i], targetY);
          plotPoint(x,y,orbitColor[i]);
        }
      }
      
      
//      //Subtracts the calculated sprayDistance if time is >= 550
//      if (time >= 550)
//      { sprayDistance-=(26.0-1.0)/450;
//      }
      
      
    }      
  
    
    
  } 
  
  
  
  
  /////////////////////////////////////// plotPoint ///////////////////////////////////////////
  //Plots a point(x,y) in pointColor in both the sand layer and the temporary screen layer.
  /////////////////////////////////////////////////////////////////////////////////////////////
  private static boolean plotPoint(int x, int y, int rgb)
  { 
    if (x<0 || y<0 || x>=canvasWidth || y >=canvasHeight) return false;
    Game.imageSand.setRGB(x, y, rgb);
    Game.imageTmp.setRGB(x, y, rgb);
    return true;
 
  }

  
  
  
    

    
    private static double getPolarR(int x, int y, int originX, int originY)
    { 
      //Theta=ArcTan(Y/X)
      return Math.sqrt((x-originX)*(x-originX) + (originY-y)*(originY-y));

    }
    
    private static double getPolarTheta(int x, int y, int originX, int originY)
    { 
      //Theta=ArcTan(Y/X)
      double xx = (x-originX);
      double yy = (originY-y);
      return Math.atan2(yy,xx);

    }
    
  }


