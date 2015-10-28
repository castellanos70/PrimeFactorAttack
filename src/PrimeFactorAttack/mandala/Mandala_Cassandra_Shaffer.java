package PrimeFactorAttack.mandala;

/******************************************************************************
 * @author Cassandra Shaffer
 * @version 2011.0429
 *****************************************************************************/

import java.util.Random;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.Utility;
import PrimeFactorAttack.main.Block;
import PrimeFactorAttack.main.Grid;

//The SandStorm class creates the swirling sandstorm effect which hits the 
//block as a button is pressed if it is the correct answer, and swirls off
//randomly if it is incorrect. Also, if it is a "kill" a fireworks of sorts
//is emitted with a size in relation to the amount of the factor used in
//the kill

public class Mandala_Cassandra_Shaffer extends Mandala
{

  //////////////////////////////////  startStorm ////////////////////////////////////////////////
  //Input Values:
  //   sandData: Graphic layers and canvas size.
  //   canvas:   Pointer to the Draw Class used to call public methods of that class.
  //   block:    The composite number Block being assailed.
  //   hit:      True if the block's number contains the prime clicked by the user.
  //   kill:     True if the block's number is down to one prime and it was clicked by the user.
  //   factor:   The prime number clicked by the user.
  //
  //Description:
  //   Draws a number of comets equal to factor.
  //   If hit is true, each comet "orbits" the center of block.
  //   If hit is false, each comet "orbits" a random location.
  //   Each comet starts in a random location off screen with a random angular speed.
  //   If hit is true, each comet starts with a negative radial velocity that will move it 
  //     to the center of the block by the end of its orbit.
  //   If hit is false, each comet starts with a negative radial velocity that will move it 
  //     through its random center point half way through its orbit, the on out the other side of 
  //     the canvas.
  //   Each comet leaves a sand particle on the path of its orbit.
  //   At each angel of its orbit, each comet leaves a stray of random sand particles with a
  //     a radius from the orbit's center that is a random amount greater than the comet.
  //
  //
  //TODO:  Project 3a: Sandstorm to Mandalas
  //  Total Points: 50
  //  10 Points: Be working on this during lab class April 21-22.
  //
  //  10 Points: Change the comet spray so that the density of particles is noticeably greater
  //        near the orbit than farther away from it.
  //
  //  10 Points: Make the comets more interesting / beautiful while maintaining the requirements:
  //        a) Number of comets equals factor.
  //        b) Comets hit the block when hit is true.
  //        c) Comets miss or pass through the block when hit is false. You might change the
  //           way each comet's orbit origin is randomized or you may want to totally change
  //           the angular and radial velocity thing.
  //        d) The game speed does not slow down too much.
  //        e) The Comet trail/spray is displayed while being drawn.
  //
  //  20 Points: When kill is true draw a some sort of geometric pattern around the block.
  //        a) Each value of factor must result in a different pattern that clearly represents  
  //           the factor's value.
  //        b) Each pattern must have a noticeable amount of randomness while not so much
  //           randomness as to make its pattern unrecognizable as representing the value of factor.
  //        c) The pattern must be drawn with at least 5 frames of development with a screen
  //           refresh between each frame. The development may be getting larger, or getting smaller, 
  //           or spinning, increasing in density, or some other change.
  //        d) At least the last frame must be drawn in the sand layer. You may or may not choose 
  //           to draw any of the earlier frames in the same layer - do what works best for your 
  //           effect.
  //        e) Your pattern drawing cannot delay the game too much.
  //        f) Your pattern frames must start drawing after the comets have finished or when 
  //           they are almost done with their orbits.
  //        g) Your patterns must be beautiful / wonderful to behold.
  //        h) You may create other methods as you see fit.
  //
  //  -5 Points: If your code does not conform to CS-152 standards
  //
  //  Deliverable: Your modified version of SandStorm.java.
  //        For this milestone, do not make changes to other files in the PrimeFactorAttack project.
  //           
  ////////////////////////////////////////////////////////////////////////////////////////////////
 
  
  
  private static Random rand = new Random();

  // timeSteps is the number of moves each comet makes from the first to last
  // frame.
  private static int timeStepsPerUpdate;
  private static int updateNeeded;
  private static int updateCount;

  private static boolean done;
  
  private static int canvasWidth, canvasHeight;

  // Values of location (angle and radius) and velocity of each comet.
  private static double[] angle = new double[Game.MAX_FACTOR];
  private static double[] radius = new double[Game.MAX_FACTOR];
  private static double[] angularSpeed = new double[Game.MAX_FACTOR];
  private static double[] radialSpeed = new double[Game.MAX_FACTOR];
  private static int[] orbitColor = new int[Game.MAX_FACTOR];

  // sprayCount is the number of sand particles plotted with each step of each
  // comet
  private static int sprayCount = 29; // original value = 10
  private static double sprayDistance = 0.944444444444211;

  private int factor;
  private int multipler;
  private Block block;

  // Target is the center of the block. It is only used when "hit" is true.
  private int targetX, targetY;

  public Mandala_Cassandra_Shaffer(Block curblock, int[] tmpOrbitColor)
  {
    // Copy input value into class variable so it can be accessed by other
    // methods in this class.
    block = curblock;
    factor = block.getHitFactor();
    canvasWidth = Game.image.getWidth();
    canvasHeight = Game.image.getHeight();
    
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

      radius[i] = canvasWidth * (1.0 + rand.nextDouble() / 4.0);

      // Max of two full orbits
      angularSpeed[i] = rand.nextDouble() * 5.0 * Math.PI / timeSteps;

      radialSpeed[i] = -radius[i] / timeSteps;

      radius[i] = rand.nextDouble() * 20 * Math.PI;
      angle[i] = canvasWidth * (1.0 + rand.nextDouble() / 5.0);
    }

    boolean clearBottomRow = false;
    // explosion sizes increase with the factor that killed it
    multipler = 17;
    if (clearBottomRow) timeSteps = (int) (timeSteps / (10.0 / 3.75));
    else if (factor <= 3)
    {
      timeSteps = (int) (timeSteps / (10.0 / .75));
      multipler = 2;
    }
    else if (factor <= 11)
    {
      timeSteps = (int) (timeSteps / (10.0 / 1.25));
      multipler = 3;
    }
    else
    {
      timeSteps = (int) (timeSteps / (10.0 / 1.75));
      multipler = 5;
    }

    if (rand.nextInt(4) == 0) timeSteps = 1000;

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
      for (int i = 0; i < multipler; i++)
      {
        angle[i] += angularSpeed[i] + 1;
        radius[i] += radialSpeed[i] + 1;

        // Draw the comet and its spray.
        // On the first iteration, n=0 so the random spray distance is
        // multiplied by 0.
        // Thus, the first iteration plots a particle exactly on the orbit.
        // for (int n=0; n<sprayCount*factor; n++)
        for (int n = 0; n < sprayCount * multipler; n++)
        {
          double rr = radius[i] + n * sprayDistance
              * (rand.nextDouble() * (multipler / 2));
          int x = Utility.polarToX(rr, angle[i], targetX);
          int y = Utility.polarToY(rr, angle[i], targetY);
          plotPoint(x, y, orbitColor[i]);

        }
      }

    }

    updateCount++;
    if (updateCount >= updateNeeded) done = true;

    // System.out.println("Mandala_Nick_Lauve.update(): updateCount="+updateCount+", done="+done);
    return done;

  }

  // ///////////////////////////////////// plotPoint ////////////////////////////////
  // Plots a point(x,y) in pointColor in both the sand layer and the temporary
  // screen layer.
  ///////////////////////////////////////////////////////////////////////////////////
  private static boolean plotPoint(int x, int y, int rgb)
  {
    if (x < 0 || y < 0 || x >= canvasWidth || y >= canvasHeight) return false;
    Game.imageSand.setRGB(x, y, rgb);
    Game.imageTmp.setRGB(x, y, rgb);
    return true;

  }

}

