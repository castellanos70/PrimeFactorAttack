package PrimeFactorAttack.mandala;

//*****************************************************************************
// @version 2011.29.4
//@author Justin Frizzell
import java.util.Random;

import PrimeFactorAttack.Data;
import PrimeFactorAttack.Block;
import PrimeFactorAttack.Grid;

//
//Evan King - 28 April 2011 - Changes
//Explosion effects added
//Comets are modified so that they have a high amount of orbits, creating a cool effect
//  (especially with higher primes)
//The display is updated less often, to compensate for more intensive FX

public class Mandala_Evan_King extends Mandala
{

  // Updated with more boomy, rockety type colors. These are also used for the
  // explosion,
  // so they need to be yellow/orange/red to look right.
  private static int[] myColors = { 0xFF3D0D, 0xFF8C00, 0xFCD116, 0xFFB00F,
      0xFF6600, 0xEE2C2C, 0xFF7F50 };

  private static int canvasWidth, canvasHeight;

  private static Random rand = new Random();

  // timeSteps is the number of moves each comet makes from the first to last
  // frame.
  private static int timeStepsPerUpdate;
  private static int updateNeeded;
  private static int updateCount;

  private int factor;
  private static boolean done;
  private Block block;

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  public Mandala_Evan_King(Block curBlock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    block = curBlock;
    factor = block.getHitFactor();
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();

    updateCount = 0;

    done = false;

    // timeSteps is the number of moves each comet makes from the first to last
    // frame.
    int timeSteps = 10000;
    updateNeeded = factor;
    timeStepsPerUpdate = timeSteps / updateNeeded;
  }

  public boolean update()
  { // creates 4 different points to create a expanding particle effect

    if (updateCount == 0)
    {
      // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;

    }

    for (int t = 0; t < timeStepsPerUpdate; t++)
    {

      int x = targetX;
      int y = targetY;
      int expsize = factor * 10;
      for (int i = 1; i < expsize; i++)
      {
        for (int c = 1; c < myColors.length; c++)
        {
          plotPoint(x, y, myColors[c]);

          int r = rand.nextInt(4);

          if (r == 0) x++;
          else if (r == 1) x--;
          else if (r == 2) y++;
          else y--;

        }
      }
    }

    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    // System.out.println("Mandala_Nick_Lauve.update(): updateCount="+updateCount+", done="+done);
    return done;

  }

  /////////////////////////////////////// plotPoint ///////////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  /////////////////////////////////////////////////////////////////////////////////
  private static void plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return;
    Data.imageSand.setRGB(x, y, rgb);
    Data.imageTmp.setRGB(x, y, rgb);

  }

}
