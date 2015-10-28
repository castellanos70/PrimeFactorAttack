package PrimeFactorAttack.mandala;

import java.awt.Color;

import PrimeFactorAttack.Data;
import PrimeFactorAttack.Block;
import PrimeFactorAttack.Grid;

public class Mandala_Ezra_Stallings extends Mandala
{

  private static int canvasWidth, canvasHeight;

  private static int updateNeeded;
  private static int updateCount;

  private int factor;
  private static boolean done;
  private Block block;

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  private double blocks[][];// holds the X and Y values of each block
  private double mods[][];

  public Mandala_Ezra_Stallings(Block curBlock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    
    block = curBlock;
    factor = block.getHitFactor();
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();

    updateCount = 0;

    done = false;

    blocks = new double[factor][2]; // holds the X and Y values of each block
    mods = new double[factor][2]; // holds the modification values for blocks

    updateNeeded = 51;
  }

  public boolean update()
  {
    if (updateCount == 0)
    {
      // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;

      for (int i = 0; i < factor; i++) // set starting values for entire array
      {
        blocks[i][0] = targetX;
        blocks[i][1] = targetY;
        mods[i][0] = (Math.random() * 2) - 1;
        mods[i][1] = (Math.random() * 2) - 1;
      }
    }

    Data.graphTmp.setColor(Color.RED);// draw in red

    for (int i = 0; i < factor; i++) // go through the entire list each frame
    {

      // draw each block
      Data.graphTmp.fillRect((int) blocks[i][0], (int) blocks[i][1],
          (int) (updateCount / 2.0), (int) (updateCount / 2.0));

      blocks[i][0] += mods[i][0];
      blocks[i][1] += mods[i][1]; // modify the values along current trajectory

      mods[i][0] += (Math.random() * 2) - 1; // then modify the trajectory a
                                             // little
      mods[i][1] += (Math.random() * 2) - 1;

    }

    updateCount++;
    if (updateCount >= updateNeeded)
    {
      done = true;

      for (int i = 0; i < factor; i++) // then draw the end result on the
                                       // graphSand layer
      // NOTE i think it looks better without this, but it was in the
      // requirements
      {
        Data.graphSand.setColor(Color.BLACK);
        Data.graphSand.fillRect((int) blocks[i][0], (int) blocks[i][1], 25, 25);
      }
    }

    return done;
  }
}
