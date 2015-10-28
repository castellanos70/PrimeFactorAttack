package PrimeFactorAttack.mandala;

/**************************************************************
 * @version 2011.0428
 * @author Derek Long
 * 
 * modified version of the SandStorm class which will create an
 * explosion effect on kill and modify the disburesment of the
 * sand trail and color
 */

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

import PrimeFactorAttack.Data;
import PrimeFactorAttack.utility.Utility;
import PrimeFactorAttack.Block;
import PrimeFactorAttack.Grid;

public class Mandala_Derek_Long extends Mandala
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

  private Point[] upperLeft, upperRight, lowerLeft, lowerRight;
//  private static final Color[] myColors = { Color.BLACK, Color.RED,
//      Color.ORANGE };
  
  private static int[] primeColor = 
  { 0x0314aa, 0x2ecbdc, 0x03ae80, 0x207b06, 0xb2b400, 
    0xf68a03, 0x9d0102, 0xa90a88, 0x982ceb, 0x84837e 
  };
  private int factorIdx;
  private static final int GRAY = Color.GRAY.getRGB();

  public Mandala_Derek_Long(Block curBlock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.

    block = curBlock;
    factor = block.getHitFactor();
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();

    updateCount = 0;

    done = false;
    factorIdx = Data.getIndexOfPrime(factor);

    upperLeft = new Point[factor * 6];
    upperRight = new Point[factor * 6];
    lowerLeft = new Point[factor * 6];
    lowerRight = new Point[factor * 6];

    int timeSteps = factor * 25;
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

      for (int i = 0; i < upperLeft.length; i++)
      {
        upperLeft[i] = new Point(targetX, targetY);
        upperRight[i] = new Point(targetX, targetY);
        lowerLeft[i] = new Point(targetX, targetY);
        lowerRight[i] = new Point(targetX, targetY);

      }
    }

    for (int t = 0; t < timeStepsPerUpdate; t++)
    {
      int i = timeStepsPerUpdate * updateCount + t;
      // to clear sandstorm for kill explosion after a few frames
      if (i == 3)
      {
        Data.graphSand.drawImage(Data.image, 0, 0, null);
        Data.graphTmp.drawImage(Data.image, 0, 0, null);
      }
      for (int j = 0; j < upperLeft.length; j++)
      { // for an expanding pattern from the origin
        double rr = factor * i / 10 * Math.cos(6 * j) + (factor * i / 10 + 2) * Math.cos(4 * j);
        int x = Utility.polarToX(rr, j, targetX);
        int y = Utility.polarToY(rr, j, targetY);
        plotPoint(upperLeft[j].x, upperLeft[j].y, GRAY);
        plotPoint(upperRight[j].x, upperRight[j].y, GRAY);
        plotPoint(lowerLeft[j].x, lowerLeft[j].y, GRAY);
        plotPoint(lowerRight[j].x, lowerRight[j].y, GRAY);

        int r = rand.nextInt(4);
        int r1 = rand.nextInt(4);
        int r2 = rand.nextInt(4);
        int r3 = rand.nextInt(4);

        if (r == 0) upperLeft[j].x--;
        if (r == 1) upperLeft[j].x++;
        if (r == 2) upperLeft[j].y++;
        if (r == 3) upperLeft[j].y--;

        if (r1 == 0) upperRight[j].x++;
        if (r1 == 1) upperRight[j].y++;
        if (r1 == 2) upperRight[j].x--;
        if (r1 == 3) upperRight[j].y--;

        if (r2 == 0) lowerRight[j].x++;
        if (r2 == 1) lowerRight[j].y--;
        if (r2 == 2) lowerRight[j].x--;
        if (r2 == 3) lowerRight[j].y++;

        if (r3 == 0) lowerLeft[j].x--;
        if (r3 == 1) lowerLeft[j].y--;
        if (r3 == 2) lowerLeft[j].x++;
        if (r3 == 3) lowerLeft[j].y++;

        plotPoint(x, y, primeColor[factorIdx]);
        plotPoint(upperLeft[j].x, upperLeft[j].y, primeColor[factorIdx]);
        plotPoint(upperRight[j].x, upperRight[j].y, primeColor[factorIdx]);
        plotPoint(lowerLeft[j].x, lowerLeft[j].y, primeColor[factorIdx]);
        plotPoint(lowerRight[j].x, lowerRight[j].y, primeColor[factorIdx]);          
        
      }
    }
    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    // System.out.println("Mandala_Nick_Lauve.update(): updateCount="+updateCount+", done="+done);
    return done;

  }

  // ///////////////////////////////////// plotPoint /////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  // /////////////////////////////////////////////////////////////////////////
  private static void plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return;
    // for the explosion, no color change
    Data.imageSand.setRGB(x, y, rgb);
    Data.imageTmp.setRGB(x, y, rgb);

  }
  
  


}
