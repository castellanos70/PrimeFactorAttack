package PrimeFactorAttack.bonuslevel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import PrimeFactorAttack.utility.Utility;


// ////////////////////////////////////////////////////////////////////////
// A bonus level for Prime Factor Attack
// By Jeffrey Nichol
// December 7, 2012
// ////////////////////////////////////////////////////////////////////////
public class BonusLevel_Jeffrey_Nichol extends BonusLevel implements
    MouseMotionListener
{
  private static final long serialVersionUID = 1L;
  private BufferedImage buffer;
  private BufferedImage background;
  private Graphics canvas;

  private final int WIDTH = 752;
  private final int HEIGHT = 575;
  private final int NUM_PRIMES = 5;
  private final int RECT_WIDTH = 120;
  private final int RECT_HEIGHT = 50;
  private final int[] PRIME_NUMBER = new int[]
  { 1, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61,
      67, 71, 73, 79, 83, 89, 97, 101
  };

  private int[] rectX = new int[NUM_PRIMES];
  private int[] rectY = new int[NUM_PRIMES];
  private int[] rectSpeedX = new int[NUM_PRIMES];
  private int[] rectSpeedY = new int[NUM_PRIMES];

  private int rectNum;
  private int mouseX, mouseY;
  private int score, frameCount;

  private String str;
  private Font primeFont;
  private Random rand = new Random();
  private int pickPrime;

  // ////////////////////////////////////////////////////////////////////////
  // Constructor for BonusLevel_Jeffrey_Nichol
  // Sets the size of the JPanel
  // Loads the background image
  // Implements the mouseMotionListener
  // ////////////////////////////////////////////////////////////////////////
  public BonusLevel_Jeffrey_Nichol()
  {

    this.setSize(WIDTH, HEIGHT);
    this.addMouseMotionListener(this);
    buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    canvas = buffer.getGraphics();
    background = Utility.loadBufferedImage("bonusLevel/Eye_Stars.png",this);

    canvas.drawImage(background, 0, 0, null);
    primeFont = new Font("", Font.BOLD, 16);
    canvas.setFont(primeFont);
  }

  // ////////////////////////////////////////////////////////////////////////
  // Called whenever the user starts your bonus level.
  // ////////////////////////////////////////////////////////////////////////
  public void init()
  {

    System.out.println("PrimeFactorAttack.bonuslevel.BonusLevel_Jeffrey_Nichol.init()");
    score = 0;
    frameCount = 0;

    for (rectNum = 0; rectNum < NUM_PRIMES; rectNum++)
    {
      rectX[rectNum] = WIDTH - rand.nextInt(WIDTH);
      rectY[rectNum] = HEIGHT - rand.nextInt(HEIGHT);

      rectSpeedX[rectNum] = rand.nextInt(25) - 12;
      rectSpeedY[rectNum] = rand.nextInt(25) - 12;

      pickPrime = rand.nextInt(27);
      str = new String("" + PRIME_NUMBER[pickPrime]);
    }

    repaint();
  }

  // ////////////////////////////////////////////////////////////////////////
  // Called right after init() and every 1/50 of a second afterwards.
  // PrimeFactorAttack stops calling this method when it returns false.
  // ////////////////////////////////////////////////////////////////////////
  public boolean nextFrame()
  {
    frameCount++;

    // Increment the score every second that the player continues to play.
    for (int i = 0; i < 6000; i += 50)
    {
      if (frameCount == i) score++;
    }

    if (frameCount > 6000) return false;

    // Move rect. to new position.
    for (rectNum = 0; rectNum < NUM_PRIMES; rectNum++)
    {
      rectX[rectNum] += rectSpeedX[rectNum];
      rectY[rectNum] += rectSpeedY[rectNum];

      if (rectX[rectNum] < 0) rectSpeedX[rectNum] = rand.nextInt(12) + 1;
      if (rectY[rectNum] < 0) rectSpeedY[rectNum] = rand.nextInt(12) + 1;

      if (rectX[rectNum] > WIDTH - RECT_WIDTH)
      {
        rectSpeedX[rectNum] = -(rand.nextInt(12) + 1);
      }
      if (rectY[rectNum] > HEIGHT - RECT_HEIGHT)
      {
        rectSpeedY[rectNum] = -(rand.nextInt(12) + 1);
      }
    }

    // Draw background over old rect positions
    canvas.fillRect(0, 0, WIDTH, HEIGHT);
    canvas.drawImage(background, 0, 0, null);

    // Draw rect in new location and check for mouse collisions with the rects.
    for (rectNum = 0; rectNum < NUM_PRIMES; rectNum++)
    {
      canvas.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand
          .nextInt(255)));
      canvas.fillRect(rectX[rectNum], rectY[rectNum], RECT_WIDTH, RECT_HEIGHT);

      canvas.setColor(Color.BLACK);
      canvas.drawString(str, rectX[rectNum] + 52, rectY[rectNum] + 30);

      if (checkCollision(mouseX, mouseY, rectX[rectNum], rectY[rectNum],
          RECT_WIDTH, RECT_HEIGHT))
      {
        return false;
      }
    }

    // Display instructions for a short period of time.
    canvas.setColor(Color.WHITE);
    if (frameCount < 350)
    {
      canvas.drawString("Don't Touch the Prime Numbers!!!", 255, 100);
    }

    // Display score.
    canvas.drawString("Score: " + score + "/120", 20, HEIGHT - 20);

    this.repaint();

    return true;
  }



  // ////////////////////////////////////////////////////////////////////////
  // Checks for if the mouse touches any of the rects on screen.
  // ////////////////////////////////////////////////////////////////////////
  private boolean checkCollision(int mouseX, int mouseY, int x, int y,
      int width, int height)
  {
    if ((mouseX > x) && (mouseY > y) && (mouseX < x + width)
        && (mouseY < y + height))
    {
      return true;
    }
    else return false;
  }

  // ////////////////////////////////////////////////////////////////////////
  // Can be called any time after init().
  // Will be called after nextFrame() returns false.
  // Return the current score between 0 and 100.
  // ////////////////////////////////////////////////////////////////////////
  public int getScore()
  {
    return score;
  }

  public void paintComponent(Graphics canvas)
  {
    canvas.drawImage(buffer, 0, 0, null);
  }

  public void mouseDragged(MouseEvent arg0)
  {
  }

  // ////////////////////////////////////////////////////////////////////////
  // Tracks the movement of the mouse on the JPanel.
  // ////////////////////////////////////////////////////////////////////////
  public void mouseMoved(MouseEvent e)
  {
    mouseX = e.getX();
    mouseY = e.getY();
  }
}
