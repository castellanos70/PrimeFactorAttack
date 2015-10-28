package PrimeFactorAttack.mandala;

/*****************************************************************************
 * Edit of Joel's SandStorm class with improvements based on specifications  *
 *                                                                           *
 * @author Sean Chavez                                                       *
 * @version 4/29/11                                                          *
 ****************************************************************************/
import java.util.Random;

import PrimeFactorAttack.Data;
import PrimeFactorAttack.utility.Utility;
import PrimeFactorAttack.Block;
import PrimeFactorAttack.Grid;

public class Mandala_Sean_Chavez extends Mandala
{

  private static int canvasWidth, canvasHeight;

  private static Random rand = new Random();

  private static int updateNeeded;
  private static int updateCount;

  private int factor;
  private static boolean done;
  private Block block;

  private static int[] orbitColor = new int[Data.MAX_FACTOR];

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  private double scale;

  public Mandala_Sean_Chavez(Block curBlock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
   
    block = curBlock;
    factor = block.getHitFactor();
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();

    for (int i = 0; i < factor; i++)
    {
      orbitColor[i] = tmpOrbitColor[i];
    }

    done = false;
    updateCount = 0;
    updateNeeded = factor;

    scale = factor * 10.0;
  }

  public boolean update()
  {
    if (updateCount == 0)
    { // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;
    }
    // ///////////////////////////////////////////////////////////////

    // loop to draw the rose with number of leaves equal to factor

    double r;

    int rgb = orbitColor[updateCount];
    for (double t = 0.0; t <= 2 * Math.PI; t += 0.001)
    {
      // to allow the rose to be able to be seen
      // int scaler = 40;

      if (factor == 2)
      {
        r = updateCount * 2 + 0.5 * Math.abs((scale * Math.sin(t)));

      }
      else
      {
        r = updateCount * 2 + (scale * Math.sin(factor * t));
        // convert polar to rectangular

      }
      int x = Utility.polarToX(r, t, targetX);
      int y = Utility.polarToY(r, t, targetY);
      // plot the point
      plotPoint(x, y, rgb);
    }

    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    return done;

  }

  // ///////////////////////////////////// plotPoint ///////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  // ///////////////////////////////////////////////////////////////////////////
  private static boolean plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return false;
    Data.imageSand.setRGB(x, y, rgb);
    Data.imageTmp.setRGB(x, y, rgb);
    return true;

  }

}
