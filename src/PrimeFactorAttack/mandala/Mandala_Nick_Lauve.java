package PrimeFactorAttack.mandala;

import java.util.Random;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.Utility;
import PrimeFactorAttack.main.Block;
import PrimeFactorAttack.main.Grid;

/*******************************************************
 * @version 2011.0429
 * @author Nick Lauve
 ********************************************************/

public class Mandala_Nick_Lauve extends Mandala
{
  private static int canvasWidth, canvasHeight;

  private static Random rand = new Random();

  // timeSteps is the number of moves each comet makes from the first to last
  // frame.
  private static int timeStepsPerUpdate;
  private static int updateNeeded;
  private static int updateCount;

  private static boolean done;

  // Values of location (angle and radius) and velocity of each comet.
  private static double[] angle = new double[Game.MAX_FACTOR];
  private static double[] radius = new double[Game.MAX_FACTOR];
  private static double[] angularSpeed = new double[Game.MAX_FACTOR];
  private static double[] radialSpeed = new double[Game.MAX_FACTOR];
  private static int[] orbitColor = new int[Game.MAX_FACTOR];

  // sprayCount is the number of sand particles plotted with each step of each
  // comet
  private static int sprayCount = 28; // original value = 10
  private static double sprayDistance = 0.944444444444211;

  private int factor;
  private Block block;

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  public Mandala_Nick_Lauve(Block curblock, int[] tmpOrbitColor)
  {

    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    
    block = curblock;
    factor = block.getHitFactor();
    canvasWidth = Game.image.getWidth();
    canvasHeight = Game.image.getHeight();
    // System.out.println("Mandala_Nick_Lauve("+factor+", "+block+")");
    for (int i = 0; i < factor; i++)
    {
      orbitColor[i] = tmpOrbitColor[i];
      // System.out.println("Set:  orbitColor["+i+"]="+orbitColor[i]);
    }

    updateCount = 0;
    done = false;

    int timeSteps = 1000;
    for (int i = 0; i < factor; i++)
    {
      double r = canvasWidth * (1.0 + rand.nextDouble() / 4.0);

      // Max of two full orbits
      angularSpeed[i] = rand.nextDouble() * 2.0 * Math.PI / timeSteps;

      // At last timeSkip, radius = 0;
      radialSpeed[i] = -r / timeSteps;

      radius[i] = rand.nextDouble() * 4.0 * Math.PI;
      angle[i] = canvasWidth * (1.0 + rand.nextDouble() / 4.0);
    }

    // explosion sizes increase with the factor that killed it
    // increased the value on these a bit, the larger numbers
    // are BIG, they slow down the process which adds a nice
    // "just nailed a high prime factor"-slomo feel to them.

    int base = 1000;

    if (factor == 2) timeSteps = (int) (base / (10.0 / 1.75));
    else if (factor == 3) timeSteps = (int) (base / (10.0 / 2.25));
    else if (factor == 5) timeSteps = (int) (base / (10.0 / 2.75));
    else if (factor == 7) timeSteps = (int) (base / (10.0 / 3.25));
    else if (factor == 11) timeSteps = (int) (base / (10.0 / 3.75));
    else if (factor == 13) timeSteps = (int) (base / (10.0 / 4.25));
    else if (factor == 17) timeSteps = (int) (base / (10.0 / 4.50));
    else if (factor == 19) timeSteps = (int) (base / (10.0 / 5.00));
    else if (factor == 23) timeSteps = (int) (base / (10.0 / 5.25));
    else timeSteps = (int) (base / (10.0 / 5.75));

    updateNeeded = (int) Math.sqrt(factor) + 1;
    timeStepsPerUpdate = timeSteps / updateNeeded;

  }

  public boolean update()
  {
    if (updateCount == 0)
    { // Target is the center of the block.
      targetX = block.getLeft() + block.getWidth() / 2;
      targetY = block.getTop() + Grid.GRID_PIXELS / 2;
    }

    for (int time = 0; time <= timeStepsPerUpdate; time++)
    {
      for (int i = 0; i < factor; i++)
      {
        angle[i] += angularSpeed[i] + 1;
        radius[i] += radialSpeed[i] + 1;

        // Draw the comet and its spray.
        // On the first iteration, n=0 so the random spray distance is
        // multiplied by 0.
        // Thus, the first iteration plots a particle exactly on the orbit.

        // added this to add a sort of spiral effect. but because they're
        // not perfect lines it ended up looking blob-ish.
        // I like it, makes a nice doughnut effect from time to time and
        // looks as if it's the multiple colors smashing into eachother.

        double orBit = angle[i];
        double randBit = rand.nextDouble();

        for (int n = 0; n < sprayCount; n++)
        {
          double rr = radius[i] + n * sprayDistance * (rand.nextDouble());
          int x = Utility.polarToX(rr, orBit, targetX);
          int y = Utility.polarToY(rr, orBit, targetY);
          plotPoint(x, y, orbitColor[i]);
          orBit += randBit;
        }
      }

    }

    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    // System.out.println("Mandala_Nick_Lauve.update(): updateCount="+updateCount+", done="+done);
    return done;

  }

  // ///////////////////////////////////// plotPoint
  // ///////////////////////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  // ///////////////////////////////////////////////////////////////////////////////////////////
  private static boolean plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return false;
    Game.imageSand.setRGB(x, y, rgb);
    Game.imageTmp.setRGB(x, y, rgb);
    return true;

  }

}
