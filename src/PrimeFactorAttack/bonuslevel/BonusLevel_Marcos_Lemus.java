package PrimeFactorAttack.bonuslevel;
/**********************************************************************
 * Updated by Marcos Lemus --- Original Author Ryan Bloon
 * 2013.0519
 * 
 * Prime number fish mini game.
 ****************************************/

import PrimeFactorAttack.utility.Utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;


public class BonusLevel_Marcos_Lemus extends BonusLevel
{
  ////////////////////////////////////////////////////////////////////////////////////////
  //user -- [0] = x_pos, [1] = y_pos, [2] = circumference, [3] = speed, [4] = user value
  //balls -- [0] = xpos, [1] = ypos, [2] = Color, [3] = value, [4] = circumference
  ////////////////////////////////////////////////////////////////////////////////////////
  private static final int NUMBALLS = 7; //Max Balls on Screen
  private final int WIDTH = 752;
  private final int HEIGHT = 575;
  private final boolean DEBUG = false;

  private int[][] balls = new int[NUMBALLS][5]; // see above
  private int[] user = new int[5];  // see above
  private int animation = 0; // cycle through animations
  private double[][] ball_speed = new double[NUMBALLS][1];
  private boolean[] prime = new boolean[NUMBALLS]; // is the ith ball prime
  private boolean swimming = true; // true = right false = left
  private BufferedImage[][] direction; // determine direction of users fish
  private BufferedImage[][] leftOrRight; // determine direction of blue fish
  private BufferedImage[][] blueFish_left = new BufferedImage[3][30]; // resized versions of images
  private BufferedImage[][] blueFish_right = new BufferedImage[3][30];// resized versions of images
  private BufferedImage[][] greenFish_right = new BufferedImage[6][30];// resized versions of images
  private BufferedImage[][] greenFish_left = new BufferedImage[6][30];// resized versions of images
  private BufferedImage BG; //background

  private BufferedImage buffer;
  private Graphics canvas;
  private int score;
  private int framecount;
  private Random rand = new Random();
  private Point mouse_position;
  private Font font;

  // Converts integers into string form.
  // From original code
  public static String convertInteger(int n)
  {
    return Integer.toString(n);
  }

  // Checks numbers to see whether they are prime or not.
  private boolean isPrime(int n)
  {
    for(int i = 2; i < n; i++)
    {
      if(n % i == 0) return false;
    }
    return true;
  }

  // Takes known values and creates balls with them.
  private void drawBall(int i)
  {
    double b = ball_speed[i][0];
    int y = balls[i][1];
    int x = balls[i][0];
    int x_spacing, y_spacing;
    balls[i][4] =  balls[i][3] + 12;
    int attack_speed = (int)(user[3]/1.5); // prime numbers attack user
    int attack_range = 100;

    if(x >= WIDTH)
    {
      balls[i][0] = WIDTH;
      ball_reset(i, false);
      if(b > 0) b = rand.nextInt(5) - 6;
    }
    else if(x < -40)
    {
      balls[i][0] = -40;
      ball_reset(i, false);
      if(b < 0) b = rand.nextInt(5) + 1;
    }

    double dx = (user[0]) - balls[i][0] - 30;
    double dy = (user[1]) - (balls[i][1] + (balls[i][4]/2)) - 30;
    double distance = Math.sqrt(dx*dx + dy*dy);

    if(distance < attack_range && prime[i])
    {
      double theta = Math.atan2(dy, dx);
      balls[i][0] += attack_speed*Math.cos(theta);
      balls[i][1] += attack_speed*Math.sin(theta);
    }
    else
    {
      balls[i][0] += b;
      ball_speed[i][0] = b;
    }

    if(x > balls[i][0]) leftOrRight = blueFish_right;
    else leftOrRight = blueFish_left;

    canvas.drawImage(leftOrRight[animation][31 - balls[i][3]], x, y,null );

    if(leftOrRight.equals(blueFish_right))
    {
      x_spacing = leftOrRight[animation][30-balls[i][3]].getWidth()/3;
      y_spacing = leftOrRight[animation][30-balls[i][3]].getHeight()/3 + balls[i][3];
    }
    else
    {
      if(balls[i][3] < 10) 
      {
        x_spacing = (leftOrRight[animation][30-balls[i][3]].getWidth()/3) + 4*balls[i][3];
        y_spacing = leftOrRight[animation][30-balls[i][3]].getHeight()/3 + 2*balls[i][3];
      }
      else 
      {
        x_spacing = (leftOrRight[animation][30-balls[i][3]].getWidth()/3) + 2*balls[i][3];
        y_spacing = leftOrRight[animation][30-balls[i][3]].getHeight()/3 + balls[i][3];
      }
    }
    canvas.setColor(Color.YELLOW);

    int size = (int)Math.round(Math.sqrt(balls[i][3]) + 11);
    font = new Font(Font.SANS_SERIF, Font.BOLD, size);
    canvas.setFont(font);

    canvas.drawString(convertInteger(balls[i][3]), 
        x + x_spacing,
        y + y_spacing);

    collide(i);
  }
  // resets ith balls position
  private void ball_reset(int i, boolean collision)
  {
    balls[i][1] = rand.nextInt(HEIGHT - 30);
    balls[i][3] = rand.nextInt(28) + 2;
    prime[i] = isPrime(balls[i][3]);

    if(collision)
    {
      if(rand.nextInt(2) == 1)
      {
        balls[i][0] = 0;
        ball_speed[i][0] = rand.nextInt(3) + 1;
      }
      else
      {
        balls[i][0] = WIDTH;
        ball_speed[i][0] = rand.nextInt(3) - 4;
      }
    }
  }

  private void draw_user()
  {
    double x = user[0];
    double y = user[1];
    double dx = 0;
    double dy = 0;
    int x_spacing;
    int y_spacing;
    mouse_position = getMousePosition();
    if(mouse_position != null)
    {
      if(Math.abs((x - mouse_position.getX())) > 2 ||
          Math.abs(y - mouse_position.getY()) > 2)
      {
        x = mouse_position.getX();
        y = mouse_position.getY();

        dx = x - user[0];
        dy = y - user[1];
        double theta = Math.atan2(dy,dx);

        user[0] += (user[3]*Math.cos(theta));
        user[1] += (user[3]*Math.sin(theta));
      }
    }

    if(dx < -2)
    {
      direction = greenFish_right;
    }
    else if(dx > 2)
    {
      direction = greenFish_left;
    }

    if(direction.equals(greenFish_right))
    {
      x_spacing = direction[animation][30-user[4]].getWidth()/5 - user[4]/2;
      y_spacing = user[4]/4;
    }
    else
    {
      x_spacing = user[4] - direction[animation][30-user[4]].getWidth()/4;
      y_spacing = user[4]/4;
    }

    int size = (int)Math.round(Math.sqrt(user[4]) + 11);
    font = new Font(Font.SANS_SERIF, Font.BOLD, size);
    canvas.setFont(font);

    canvas.setColor(Color.BLACK);
    canvas.drawImage(direction[animation][30-user[4]], 
        user[0] - direction[animation][30-user[4]].getWidth()/2, 
        user[1] - direction[animation][30-user[4]].getHeight()/2, null);
    canvas.drawString(convertInteger(user[4]), user[0] - x_spacing, user[1] + y_spacing);
  }
  //collision detection
  // fairly well broken
  // in need of rewriting, doesn't work for number 29 for some reason
  // set DEBUG true to see hit boxes
  private void collide(int i)
  {
    int[][] ball_box = new int[3][2];
    int[][] user_box = new int[3][2];
    int user_length = user[2];
    int user_width = user[2];
    boolean y_collision = false;
    boolean x_collision = false;

    if(leftOrRight.equals(blueFish_left))
    {
      ball_box[0][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/2;           // Top Left
      ball_box[0][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.BLUE);
        canvas.fillRect(ball_box[0][0], ball_box[0][1], 5, 5);
      }

      ball_box[1][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/2
          + leftOrRight[animation][30-balls[i][3]].getWidth()/3;  // Top Right
      ball_box[1][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.YELLOW);
        canvas.fillRect(ball_box[1][0], ball_box[1][1], 5, 5);
      }

      ball_box[2][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/2;     //Bottom Left
      ball_box[2][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()
          - leftOrRight[animation][30-balls[i][3]].getHeight()/4;
      if(DEBUG)
      {
        canvas.setColor(Color.GREEN);
        canvas.fillRect(ball_box[2][0], ball_box[2][1], 5, 5);
      }
    }
    else
    {
      ball_box[0][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/4;           // Top Left
      ball_box[0][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.BLUE);
        canvas.fillRect(ball_box[0][0], ball_box[0][1], 5, 5);
      }

      ball_box[1][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/2
          + leftOrRight[animation][30-balls[i][3]].getWidth()/3;  // Top Right
      ball_box[1][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.YELLOW);
        canvas.fillRect(ball_box[1][0], ball_box[1][1], 5, 5);
      }

      ball_box[2][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/4;     //Bottom Left
      ball_box[2][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()
          - leftOrRight[animation][30-balls[i][3]].getHeight()/4;
      if(DEBUG)
      {
        canvas.setColor(Color.GREEN);
        canvas.fillRect(ball_box[2][0], ball_box[2][1], 5, 5);
      }
    }

    if(balls[i][3] == 29)
    {
      ball_box[0][0] = balls[i][0];           // Top Left
      ball_box[0][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.BLUE);
        canvas.fillRect(ball_box[0][0], ball_box[0][1], 5, 5);
      }

      ball_box[1][0] = balls[i][0] + leftOrRight[animation][30-balls[i][3]].getWidth()/2
          + leftOrRight[animation][30-balls[i][3]].getWidth()/3;  // Top Right
      ball_box[1][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()/3;
      if(DEBUG)
      {
        canvas.setColor(Color.YELLOW);
        canvas.fillRect(ball_box[1][0], ball_box[1][1], 5, 5);
      }

      ball_box[2][0] = balls[i][0];   //Bottom Left
      ball_box[2][1] = balls[i][1] + leftOrRight[animation][30-balls[i][3]].getHeight()
          - leftOrRight[animation][30-balls[i][3]].getHeight()/4;
      if(DEBUG)
      {
        canvas.setColor(Color.GREEN);
        canvas.fillRect(ball_box[2][0], ball_box[2][1], 5, 5);
      }
    }


    user_box[0][0] = user[0] - user[2]/2;
    user_box[0][1] = user[1] - user[2]/2;
    if(DEBUG)
    {
      canvas.setColor(Color.BLUE);
      canvas.fillRect(user_box[0][0], user_box[0][1], 5, 5);
    }
    user_box[1][0] = user[0] + user_length - user[2]/2;
    user_box[1][1] = user[1] - user[2]/2;
    if(DEBUG)
    {
      canvas.setColor(Color.YELLOW);
      canvas.fillRect(user_box[1][0], user_box[1][1], 5, 5);
    }
    user_box[2][0] = user[0] - user[2]/2;
    user_box[2][1] = user[1] + user_width - user[2]/2;
    if(DEBUG)
    {
      canvas.setColor(Color.GREEN);
      canvas.fillRect(user_box[2][0], user_box[2][1], 5, 5);
    }
    if(user_box[0][1] > ball_box[0][1] && user_box[0][1] < ball_box[2][1]) y_collision = true;
    else if(user_box[2][1] < ball_box[2][1] && user_box[2][1] > ball_box[0][1]) y_collision = true;
    else if(user_box[0][1] < ball_box[0][1] && user_box[2][1] > ball_box[2][1]) y_collision = true;

    if(user_box[0][0] > ball_box[0][0] && user_box[0][0] < ball_box[1][0]) x_collision = true;
    else if(user_box[1][0] < ball_box[1][0] && user_box[1][0] > ball_box[0][0]) x_collision = true;
    else if(user_box[0][0] < ball_box[0][0] && user_box[1][0] > ball_box[1][0]) x_collision = true;

    if(y_collision && x_collision)
    {
      if(prime[i]) user[4]--;//= balls[i][3];
      else user[4] ++;//= balls[i][3];
      user[2] = user[4] + 10;
      ball_reset(i, true);
    }
  }                                                                                                                     

  public BonusLevel_Marcos_Lemus()
  {
    this.setSize(WIDTH, HEIGHT);

    buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    canvas = buffer.getGraphics();
  }

  // Gets informations from strings and sets up the game.
  @Override
  public void init()
  {

    System.out.println("PrimeFactorAttack.bonuslevel.BonusLevel_Marcos_Lemus.init()");
    font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    canvas.setFont(font);
    canvas.setColor(Color.BLACK);
    score = 10;
    framecount = 0;

    images();

    for(int i = 0; i < NUMBALLS; i++)
    {
      if(rand.nextInt(2) == 1)
      {
        balls[i][0] = 0;
        ball_speed[i][0] = rand.nextInt(3) + 1;
      }
      else
      {
        balls[i][0] = WIDTH;
        ball_speed[i][0] = rand.nextInt(3) - 4;
      }
      balls[i][1] = rand.nextInt(HEIGHT - 30);
      balls[i][3] = rand.nextInt(28) + 2;
      prime[i] = isPrime(balls[i][3]);

    }
    user[4] = 10;
    user[3] = 5; //speed
    user[2] = 25; // size
    user[1] = (HEIGHT/2) - (user[2]/2); //y position
    user[0] = (WIDTH/2) - (user[2]/2);  //x position
  }
  // all images loaded here
  private void images()
  {
    BG = Utility.loadBufferedImage("bonusLevel/BG.png",this);
    canvas.drawImage(BG, 0, 0, null);
    font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    canvas.setFont(font);
    canvas.setColor(Color.BLACK);
    canvas.drawString("Directions: Eat the compound Numbers Aviod the primes!", 50 , HEIGHT/2 );

    BufferedImage blue1 = Utility.loadBufferedImage("bonusLevel/blueFish.png",this);
    BufferedImage blue2 = Utility.loadBufferedImage("bonusLevel/blueFish1.png",this);
    BufferedImage blue3 = Utility.loadBufferedImage("bonusLevel/blueFish2.png",this);
    BufferedImage blue4 = Utility.loadBufferedImage("bonusLevel/blueFishRight.png",this);
    BufferedImage blue5 = Utility.loadBufferedImage("bonusLevel/blueFish1Right.png",this);
    BufferedImage blue6 = Utility.loadBufferedImage("bonusLevel/blueFish2Right.png",this);

    BufferedImage green1 = Utility.loadBufferedImage("bonusLevel/greenFish.png",this);
    BufferedImage green2 = Utility.loadBufferedImage("bonusLevel/greenFish2.png",this);
    BufferedImage green3 = Utility.loadBufferedImage("bonusLevel/greenFish3.png",this);
    BufferedImage green4 = Utility.loadBufferedImage("bonusLevel/greenFish_right.png",this);
    BufferedImage green5 = Utility.loadBufferedImage("bonusLevel/greenFish2_right.png",this);
    BufferedImage green6 = Utility.loadBufferedImage("bonusLevel/greenFish3_right.png",this);

    for(int i = 1; i < 30; i++)
    {
      blueFish_right[0][i] = resize(i, blue1);
      blueFish_right[1][i] = resize(i, blue2);
      blueFish_right[2][i] = resize(i, blue3);
      blueFish_left[0][i] = resize(i, blue4);
      blueFish_left[1][i] = resize(i, blue5);
      blueFish_left[2][i] = resize(i, blue6);

      greenFish_right[0][i] = resize(i, green1);
      greenFish_right[1][i] = resize(i, green2);
      greenFish_right[2][i] = resize(i, green3);

      greenFish_left[0][i] = resize(i, green4);
      greenFish_left[1][i] = resize(i, green5);
      greenFish_left[2][i] = resize(i, green6);

      direction = greenFish_right;
    }
  }

  // resize images to meet specific numbers
  private BufferedImage resize(double factor, BufferedImage img)
  {
    factor = (int) (2*Math.sqrt(factor + 2));
    BufferedImage temp = new BufferedImage((int)Math.round(img.getWidth()*(1.0/factor)), 
        (int)Math.round(img.getHeight()*(1.0/factor)), 
        BufferedImage.TYPE_INT_ARGB);
    int rgb;
    int x = 0;
    for(int x2 = 0; x2 < temp.getWidth(); x2 ++)
    {
      int y = 0;
      for(int y2 = 0; y2 < temp.getHeight(); y2 ++)
      {
        rgb = img.getRGB(x, y);
        temp.setRGB(x2, y2, rgb);
        y+= factor;
      }
      x+= factor;
    }

    return temp;
  }

  // Keeps track of the current frame number, gives directions, and a win
  // message.
  @Override
  public boolean nextFrame()
  {
    framecount++;
    score = user[4];
    if(framecount > 6000 || user[4] < 2) return false;

    canvas.drawImage(BG, 0, 0, null);
    for(int i = 0; i < NUMBALLS; i++)
    {
      drawBall(i);
    }
    if(user[4] >= 30)
    {
      String message = "You WIN!!";
      canvas.drawChars(message.toCharArray(), 0, message.length(), 0, 10);
      return false;
    }
    else if(framecount <= 200)
    {
      font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
      canvas.setFont(font);
      canvas.setColor(Color.BLACK);
      canvas.drawString("Directions: Eat the compound Numbers Aviod the primes!", 50 , HEIGHT/2 );
    }
    else
    {
      String message = "Score:" + Integer.toString(getScore());
      canvas.drawChars(message.toCharArray(), 0, message.length(), 0, 10);
    }
    draw_user();
    repaint();

    if(animation == 2) swimming = false;
    else if(animation == 0) swimming = true;

    if(swimming) animation++;
    else animation--;


    return true;
  }

  @Override
  public int getScore()
  {
    return score;
  }

  public void paintComponent(Graphics canvas)
  {
    canvas.drawImage(buffer, 0, 0, null);
  }

  public static void main(String[] args)
  {

  }
}
