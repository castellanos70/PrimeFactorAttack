package PrimeFactorAttack.mandala;

import java.awt.Color;
import java.util.Random;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.main.Block;
import PrimeFactorAttack.main.Grid;

public class Mandala_Tyler_Brandt extends Mandala

{
  private static Random rand = new Random();
  

  private static int [] explodeColor = 
  { 0xDC143C, 0x8B4789, 0x6959CD, 0x0000EE, 0x0C86EE, 0x00F5FF, 0x00FF7F, 
    0x00CD00, 0xFFFF00, 0xFFA500, 0xCD2626, 0xDC143C, 0x8B4789, 0x6959CD, 
    0x0000EE, 0x0C86EE, 0x00F5FF, 0x00FF7F, 0x00CD00, 0xFFFF00, 0xFFA500, 
    0xCD2626, 0xDC143C, 0x8B4789, 0x6959CD, 0x0000EE, 0x0C86EE, 0x00F5FF, 
    0x00FF7F, 0x00CD00, 0xFFFF00, 0xFFA500, 0xCD2626, 0xDC143C, 0x8B4789, 
    0x6959CD, 0x0000EE, 0x0C86EE, 0x00F5FF, 0x00FF7F, 0x00CD00, 0xFFFF00, 
    0xFFA500, 0xCD2626
  };

  private static Color[] xColor;
  

  
  //timeSteps is the number of moves each comet makes from the first to last frame.
  private static int timeStepsPerUpdate;
  private static int updateNeeded;
  private static int updateCount;
  
  
  private int factor;
  private static boolean done;
  private Block block;
  
  
  
  //Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;
  
  
  

  
  
  public Mandala_Tyler_Brandt(Block curBlock,int[] tmpOrbitColor)
  { 
    //Copy input value into class variable so it can be accessed by other methods in this class.

    block = curBlock;
    factor = block.getHitFactor();
    
    
    updateCount=0;
    
    done = false;

    ////////////////////////////////////////////////////////////////////////////////////
    //Create Sand colors
    //Since sandColor is static, it will only be null the first time this method is called.

    if (xColor == null)
    {
      xColor = new Color[explodeColor.length];
      for (int i=0; i <explodeColor.length; i++)
      {
        xColor[i] = new Color(explodeColor[i]);
      }
    }
    ////////////////////////////////////////////////////////////////////////////////////
   
    

    
    //timeSteps is the number of moves each comet makes from the first to last frame.
    int timeSteps = 1000;
    updateNeeded =factor;
    timeStepsPerUpdate = timeSteps/updateNeeded;

  
    
  }
  
  
  
  public boolean update()
  { //creates 4 different points to create a expanding particle effect
    
    if (updateCount == 0)
    {
      //Target is the center of the block.
      targetX = block.getLeft() + block.getWidth()/2;
      targetY = block.getTop() + Grid.GRID_PIXELS/2;

    }
    
    
    Game.graphSand.setColor(xColor[updateCount]);
    int r = (rand.nextInt(15) + 10); // random size
    int a = (rand.nextInt(100)+10); // random location around orbit
    
    Game.graphSand.fillOval(targetX-((r/2) + a), targetY+((r/2)-a), r, r);
    Game.graphSand.fillOval(targetX-((r/2) + a), targetY-((r/2)-a), r, r);
    Game.graphSand.fillOval(targetX+((r/2) + a), targetY+((r/2)-a), r, r);
    Game.graphSand.fillOval(targetX+((r/2) + a), targetY-((r/2)-a), r, r);
    updateCount++;
    if (updateCount >=updateNeeded) done=true;
    
    //System.out.println("Mandala_Nick_Lauve.update(): updateCount="+updateCount+", done="+done);
    return done;
  }
}
