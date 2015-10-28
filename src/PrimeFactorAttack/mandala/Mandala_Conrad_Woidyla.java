package PrimeFactorAttack.mandala;

import java.awt.Color;
import java.util.Random;

import PrimeFactorAttack.Data;
import PrimeFactorAttack.utility.Utility;
import PrimeFactorAttack.Block;
import PrimeFactorAttack.Grid;


/*******************************************************************************
 * @version 2011.0429
 * @author Conrad Woidyla
 * 
 *         Original Author Joel Castellanos Majority of additions start at line
 *         197
 *******************************************************************************/

public class Mandala_Conrad_Woidyla extends Mandala
{
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

  public Mandala_Conrad_Woidyla(Block curblock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    block = curblock;
    factor = block.getHitFactor();
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();

    updateCount = 0;

    done = false;

    updateNeeded = 5;// (int)Math.sqrt(factor)+1;
    // timeStepsPerUpdate = timeSteps/updateNeeded;

  }

  public boolean update()
  {

    if (updateCount == 0)
    { // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;
    }

    int rad = 1; // used to increase projections from the center
    int con = 2; // counter to initiate second access of for loops
    int expC, R, G, B;

    Color[] color = new Color[3];
    color[0] = new Color(255, 0, 0);
    color[1] = new Color(0, 255, 0);
    color[2] = new Color(0, 0, 255);

    // chooses random number to call on random color in color array
    expC = rand.nextInt(color.length);

    // grabs red green and blue values of random color
    R = color[expC].getRed();
    G = color[expC].getGreen();
    B = color[expC].getBlue();

    // used to create spiral effect
    double theta2 = ((2 * Math.PI / 270));

    // r is radius which increases 20 pixels outside of center
    for (int r = 20; r <= 110; r++)
    {
      // every 15 steps, random color changes to a different color
      if (r % 15 == 0)
      {
        if (R > G && R > B)
        {
          color[expC] = new Color(R, (2 * r), 0);

        }
        else if (G > R && G > B)
        {
          color[expC] = new Color((2 * r), G, 0);

        }
        else if (B > R && B > G)
        {
          color[expC] = new Color((2 * r), 0, B);

        }

      }

      // loop to paint dots around the origin of the killed block
      // paints the dots one layer at a time
      for (int t = 1; t <= (rad * factor); t++)
      {
        // divides pie into small pieces for legs of spiral
        // theta becomes smaller for visual effects
        double theta = ((2 * Math.PI) / (rad * factor));

        // rotates placement of dot around origin to create spiral effect
        theta = (theta * (t) + (theta2 * (r - 20)));

        // stores x and y values from method
        int x = Utility.polarToX(r, theta, targetX) + rand.nextInt(3) - 1;
        int y = Utility.polarToY(r, theta, targetY) + rand.nextInt(3) - 1;

        // plots points from above method
        plotPoint(x, y, color[expC]);
        // variations in x and y give spiral more breadth
        plotPoint((x + 1), (y + 1), color[expC]);
        plotPoint((x - 1), (y - 1), color[expC]);
      }

      if (r % 11 == 0)
      {// updates spiral image every 11 frames to give a growing effect
        try
        {
          Thread.sleep(5);// slows down growing effect to be seen by user
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        con++;// increases counter
        rad++;// increases radian multiplier
      }

      // allows reaccess of for loop to draw counterclockwise spiral
      if (con % 11 == 0)
      {
        r = 20;
        rad = 1;
        // allows spiral to be drawn counterclockwise
        theta2 = -theta2;
        // gives second spiral new color
        R = G;
        G = B;
        B = 0;
        // counter set to number that won't allow access of this code again
        con = 1;

      }

    }

    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    //System.out.println("Mandala_Conrad_Woidyla.update(): updateCount="
    //    + updateCount + ", done=" + done);
    return done;
  }

  /////////////////////////////////////// plotPoint /////////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  ///////////////////////////////////////////////////////////////////////////////
  private static void plotPoint(int x, int y, Color pointColor)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return;
    Data.imageSand.setRGB(x, y, pointColor.getRGB());
    Data.imageTmp.setRGB(x, y, pointColor.getRGB());
  }

}
