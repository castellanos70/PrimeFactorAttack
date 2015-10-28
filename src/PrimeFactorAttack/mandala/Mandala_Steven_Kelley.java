package PrimeFactorAttack.mandala;

/**
 * @version 2011.0429
 * @author for version: steven kelley
 */
import java.util.Random;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.main.Block;
import PrimeFactorAttack.main.Grid;

public class Mandala_Steven_Kelley extends Mandala
{

  private static int canvasWidth, canvasHeight;

  private static Random rand = new Random();

  private static int updateNeeded;
  private static int updateCount;

  private int factor;
  private static boolean done;
  private Block block;

  private static int[] primeColor = 
  { 0x0314aa, 0x2ecbdc, 0x03ae80, 0x207b06, 0xb2b400, 
    0xf68a03, 0x9d0102, 0xa90a88, 0x982ceb, 0x84837e 
  };

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  private int factorIdx;

  public Mandala_Steven_Kelley(Block curBlock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    
    block = curBlock;
    factor = block.getHitFactor();
    canvasWidth = Game.image.getWidth();
    canvasHeight = Game.image.getHeight();

    updateCount = 0;

    done = false;

    factorIdx = Game.getIndexOfPrime(factor);

    updateNeeded = 1;

  }

  public boolean update()
  { 

    if (updateCount == 0)
    {
      // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;

    }

    // draws a pattern based on factor when a kill hit happens
    for (double i = 0; i < factor; i += 0.0001)
    {
      double r = (i * factor * 4) * Math.sin(4 * i * Math.PI);
      double x = r * Math.cos(1.5 * i * factor * Math.PI);
      double y = r * Math.sin(1.5 * i * factor * Math.PI);
      int m = (int) (targetX + x);
      int u = (int) (targetY + y);
      plotPoint(m, u, primeColor[factorIdx]);

    }
    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    return done;

  }

  /////////////////////////////////////// plotPoint//////////////////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  //////////////////////////////////////////////////////////////////////////////////////
  private static boolean plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return false;
    Game.imageSand.setRGB(x, y, rgb);
    Game.imageTmp.setRGB(x, y, rgb);
    return true;

  }

}
