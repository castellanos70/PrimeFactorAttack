package PrimeFactorAttack.bonuslevel;
/***************************************************************
 @version 2015.1209
 Original Author: Ethan Parks
 Updated by: Daniel Gomez
 New Updates by:  Jarek Kwiecinski
 Background image created by Daniel Gomez
 *
 ***************************************************************/

import PrimeFactorAttack.utility.Utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


public class BonusLevel_Daniel_Gomez extends BonusLevel implements MouseMotionListener, MouseListener, KeyListener
{

  private BufferedImage buffer, background;
  private Graphics2D canvas;
  private static final int WIDTH = 752;
  private static final int HEIGHT = 575;
  private static final int MAX_HEALTH=100;
  private static final int MONSTER_DAMAGE=5;
  private static final int SCORE_BOOST=5;

  private Random rand = new Random();

  private int score,frameCount;

  private int mouseX,mouseY;

  private int level;

  private final static int BLOCK_SIZE = 50; //player base size

  private int health = MAX_HEALTH;

  private static final int FREEZE_TIME = 50; //how long a monster stays frozen when hit
  private ArrayList<Monster> monsters =new ArrayList<>();
  private Player player=new Player();
  private static final int MONSTER_STEP=4;
  private static final int PLAYER_STEP=7;


  //bullet variables
  private double angle;
  private int bulletTimer = 0;//keeps track how long a bullet is on screen
  private int bulletReloadTime = 20; //time between bullets
  private int totalBullets = 500;
  private int bulletCount = 0; //number of bullets on screen
  private int[] bulletStatus = new int[totalBullets]; //bullet alive or dead
  private int[] bulletX = new int[totalBullets]; //bullet X coordinate
  private int[] bulletY = new int[totalBullets]; //bullet Y coordinate
  private int[] bulletEndX = new int[totalBullets]; //bullet end X coordinate
  private int[] bulletEndY = new int[totalBullets]; //bullet end Y coordinate
  private double[] bulletXVel = new double[totalBullets]; //bullet X velocity
  private double[] bulletYVel = new double[totalBullets]; //bullet Y velocity
  private final int sw = 20; //the width of the shot
  private final int sv = 40; //the velocity of the shot

  private int currentPrime = 0; //prime number attached to current bullet
  private int maxNumber = 60;

  // sets up the buffered image, instructions
  //and the mouse motion listener
  public BonusLevel_Daniel_Gomez()
  {
    this.setSize(WIDTH, HEIGHT);

    buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    canvas = (Graphics2D)buffer.getGraphics();
    init();
    addMouseMotionListener(this);
    addMouseListener(this);
    addKeyListener(this);
    this.setFocusable(true);
    this.requestFocusInWindow();
  }

  // Called whenever the user starts your bonus level.
  // creates a clean and new start for each time it is
  // called.
  public void init()
  {

    System.out.println("PrimeFactorAttack.bonuslevel.BonusLevel_Daniel_Gomez.init()");
    background = Utility.loadBufferedImage("bonusLevel/Daniel_Gomez_background.png", this);

    bulletCount = 0;
    frameCount = 0; //initialize frame count
    level = 20;
    score = 50;
    health = 100;
    //set background
    canvas.setColor(Color.WHITE);
    canvas.fillRect(0, 0, WIDTH, HEIGHT);
    //draw background image
    canvas.drawImage(background, 0, 0, null);
    //draw center circle
    canvas.setColor(Color.RED);
    drawPlayer();
    createMonster();//create first monster

    this.repaint();

  }

  // Called right after init() and every 1/50 of a second afterwards.
  // PrimeFactorAttack stops calling this method when it returns false.
  // Returns false if the score is 100 or the frame count has reached
  // 6000. Moves all the numbers and restarts the number if necessary.
  public boolean nextFrame()
  {
    frameCount++;

    // Check to see if the game has reached the time limit
    if (frameCount > 6000 || health < 1||score<=0||score>=100)
    {
      canvas.setColor(Color.WHITE);
      canvas.fillRect(0, 0, WIDTH, HEIGHT);

      return false;
    }

    //hide mouse cursor
    setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "cursor"));
    canvas.setFont(new Font("TimesRoman", Font.PLAIN, 16));
    //set a limit to how quickly we spawn monsters
    if(frameCount%(level*5)==0)  createMonster();


    //how often we update the game
    this.requestFocusInWindow();
    clearMonsters();
    drawMonsters();
    drawPlayer();
    drawBullets();
    updateMonsters();
    updateBullets();
    //check reload time
    if (bulletTimer >= 1) bulletTimer++;
    if (bulletTimer >= bulletReloadTime) bulletTimer = 0;



    //draw aimer
    drawCursor(mouseX,mouseY);

    canvas.setColor(Color.WHITE);
    canvas.setFont(new Font("TimesRoman", Font.PLAIN, 16));
    String xS = "Score: " + score;
    canvas.drawString(xS, 175, 30);
    String xH = "Health: " + health;
    canvas.drawString(xH, 575, 30);

    if (frameCount < 500) //display instructions
    {
      canvas.setColor(Color.WHITE);
      canvas.setFont(new Font("TimesRoman", Font.PLAIN, 24));
      String instructions1 = "Shoot composite numbers and let primes reach your base!"+"\n" +
                             "If a composite hits your base you will lose health and points!"+"\n" +
                              "Don't let you health or score reach zero.";
      String instructions2 = "If a composite hits your base you will lose health!";
      canvas.drawString(instructions1, 40,  HEIGHT-100);
      canvas.drawString(instructions2, 40,  HEIGHT-60);
    }

    this.repaint(); //repaint

    return true;
  }

  // Can be called anytime after init().
  // Will be called after nextFrame() returns false.
  // Return the current score between 0 and 100.
  public int getScore()
  {
    return score;
  }

  // called if the mouse is moved while being clicked.
  // Does nothing when called.
  @Override
  public void mouseDragged(MouseEvent arg0)
  {
  }

  // called if the mouse is moved without being clicked.
  // draws a sword where the mouse is located and checks
  // to see if the cursor is over a number. If it is over
  // a number it recycles the number and increments the
  // score if the number was prime.
  @Override
  public void mouseMoved(MouseEvent e)
  {
    mouseX = e.getX(); //get the mouse x and y position
    mouseY = e.getY();
  }

  public void mouseClicked(MouseEvent e)
  {

  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    int mouseClickX, mouseClickY;
    if (bulletTimer < 1) //check for our bullet delay
    {
      mouseClickX = e.getX(); //get mouse coordinates
      mouseClickY = e.getY();
      createBullet(mouseClickX, mouseClickY, currentPrime); //create bullet
      for(int i=monsters.size()-1;i>=0;i--)
      {
        Monster currentMonster=monsters.get(i);
        int leftXBound=currentMonster.x-BLOCK_SIZE/2;
        int rightXBound=currentMonster.x+BLOCK_SIZE/2;
        int topYBound=currentMonster.y-BLOCK_SIZE/2;
        int botYBound=currentMonster.y+BLOCK_SIZE/2;
        if((mouseClickX>leftXBound&&mouseClickX<rightXBound)&&(mouseClickY>topYBound&&mouseClickY<botYBound))
        {
          if(!Utility.isPrime(currentMonster.number))
          {
            score+=SCORE_BOOST;
            monsters.remove(currentMonster);
            if (level > 4) level--;
          }
          else
          {
            score-=SCORE_BOOST;
            currentMonster.isHit=true;
          }
        }
      }
    }
    currentPrime = 0; //reset prime
  }
  private void drawCursor(int x, int y)
  {
    //draw a dashed line from the player to the mouse
    final float dash1[] = {10.0f};
    final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    canvas.setStroke(dashed);
    canvas.setColor(Color.BLACK);
    canvas.drawLine(WIDTH/2, HEIGHT/2, x, y);
    canvas.fillOval(x-5, y-5, 10, 10); //draw circle at mouse point
    canvas.fillOval(WIDTH/2-10, HEIGHT/2-10, 20, 20);
  }

  //check if a monster reached the player
  private boolean isAtPlayer(int x, int y) //x and y are the monsters coordinates
  {
    if((x>=(player.x-BLOCK_SIZE/2)&&x<=(player.x+BLOCK_SIZE/2))&&(y>=(player.y-BLOCK_SIZE/2)&&y<=(player.y+BLOCK_SIZE/2)))
    {
      System.out.println(true);
      return true;
    }

    return false;
  }

  //clear monsters and redraw background
  private void clearMonsters()
  {
    //set background
    canvas.setColor(Color.WHITE);
    canvas.fillRect(0, 0, WIDTH, HEIGHT);
    //draw background image
    canvas.drawImage(background,0,0,Color.WHITE,null);
    //draw center square
    /*canvas.setColor(Color.RED);
    canvas.fillRect((WIDTH / 2) - (BLOCK_SIZE / 2), (HEIGHT / 2) - (BLOCK_SIZE / 2), BLOCK_SIZE, BLOCK_SIZE);*/
  }

  //create a new monster
  private void createMonster()
  {
  /*   //determine component number attached to new monster
    monsterNum[monsterCount] = number;
    currentMonsters++;*/
    int newMonsterX=rand.nextInt(WIDTH);
    int newMonsterY=rand.nextInt(HEIGHT);
    Monster newMonster =new Monster(newMonsterX, newMonsterY);
    monsters.add(newMonster);
  }

  //update the monsters positions
  private void updateMonsters()
  {

    for(int i=monsters.size()-1;i>=0;i--)
    {
      Monster currentMonster=monsters.get(i);
      if(isAtPlayer(currentMonster.x, currentMonster.y))
      {
        if(Utility.isPrime(currentMonster.number))
        {
          score+=SCORE_BOOST;
          if(health<MAX_HEALTH-MONSTER_DAMAGE)
          {
            health+=MONSTER_DAMAGE;
          }
          else if(health>MAX_HEALTH-MONSTER_DAMAGE&&health<MAX_HEALTH)
          {
            health+=MAX_HEALTH-health;
          }
        }
        else
        {
          health-=MONSTER_DAMAGE;
        }
        monsters.remove(currentMonster);
      }
      else if(currentMonster.isHit)
      {
        currentMonster.isFrozen=true;
        currentMonster.freezeTimer++;
        if(currentMonster.freezeTimer>=FREEZE_TIME)
        {
          currentMonster.isHit=false;
          currentMonster.isFrozen=false;
          currentMonster.freezeTimer=0;
        }
      }
      else
      {
        double monsterAngle=Math.atan2(-(player.y-currentMonster.y),player.x-currentMonster.x);
        currentMonster.x=(int)(currentMonster.x+MONSTER_STEP*Math.cos(monsterAngle));
        System.out.println(currentMonster.x+" "+monsters.get(i).x);
        currentMonster.y=(int)(currentMonster.y-MONSTER_STEP*Math.sin(monsterAngle));
      }
    }
  }

  //draw monsters on screen
  private void drawMonsters()
  {
    canvas.setColor(Color.BLUE);
    for (Monster monster:monsters) //for every monster in play
    {
      if (!monster.isFrozen)
      {
        //draw monster
        canvas.setColor(Color.BLUE);
        canvas.fillRect(monster.x-BLOCK_SIZE/2, monster.y-BLOCK_SIZE/2, BLOCK_SIZE, BLOCK_SIZE);
        //draw monster number
        canvas.setColor(Color.WHITE);
        canvas.drawString(Integer.toString(monster.number),monster.x, monster.y);
      }
      else
      {
        //draw monster
        canvas.setColor(Color.YELLOW);
        canvas.fillRect(monster.x-BLOCK_SIZE/2, monster.y-BLOCK_SIZE/2, BLOCK_SIZE, BLOCK_SIZE);
        //draw monster number
        canvas.setColor(Color.RED);
        canvas.drawString(Integer.toString(monster.number),monster.x, monster.y);
      }
    }
  }

  private void drawPlayer()
  {
    canvas.setColor(Color.RED);
    canvas.fillRect(player.x - BLOCK_SIZE / 2, player.y - BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE);
  }

  //create a new bullet
  public void createBullet(int clickX, int clickY, int prime)
  {
    double xVel; //bullet speeds
    double yVel;

    double mouseX = clickX - (WIDTH/2); //difference between player and the mouse click
    double mouseY = clickY - (HEIGHT/2);

    angle = Math.atan2(mouseY, mouseX); //TRIGONOMETRYYYYYYYYYYYYY
    xVel =  Math.round(sv*Math.cos(angle));
    yVel =  Math.round(sv*Math.sin(angle));

    //set all bullet parameters
    bulletStatus[bulletCount] = 1; //bullet is active
    bulletX[bulletCount] = WIDTH/2-(sw/2); //bullet starting location
    bulletY[bulletCount] = HEIGHT/2-(sw/2);
    bulletEndX[bulletCount] = clickX; //bullet end point (not implemented yet)
    bulletEndY[bulletCount] = clickY;
    bulletXVel[bulletCount] = xVel; //bullet velocity
    bulletYVel[bulletCount] = yVel;
    bulletCount++; //increment number of bullets shot
    bulletTimer++; //start bullet timer (delay between bullets)
  }

  public void updateBullets()
  {
    for (int i = 0; i < bulletCount; i++) //for every bullet in play
    {
      if (bulletStatus[i] == 1) //if the bullet is active
      {
        bulletX[i] += bulletXVel[i];
        bulletY[i] += bulletYVel[i];
      }
      //check collision with monster
      if (bulletX[i] < bulletEndX[i]+20 && bulletX[i] > bulletEndX[i]-20 &&
              bulletY[i] < bulletEndY[i]+20 && bulletY[i] > bulletEndY[i]-20)
      {
        bulletStatus[i] = 0;
      }
    }
  }

  public void drawBullets()
  {
    for (int i = 0; i < bulletCount; i++) //for every bullet in play
    {
      if (bulletStatus[i] == 1)
      {
        canvas.setColor(Color.white);
        canvas.fillOval(bulletX[i], bulletY[i], sw, sw);
      }
    }
  }

  class Monster
  {
    public boolean isFrozen=false;
    public boolean isHit=false;
    public int freezeTimer=0;
    public int number=rand.nextInt(maxNumber)+1;
    public int x, y;
    public Monster(int x, int y)
    {
      this.x=x;
      this.y=y;
    }
  }

  class Player
  {
    public int x=WIDTH/2;
    public int y=HEIGHT/2;
  }




  public void paintComponent(Graphics canvas)
  {
    canvas.drawImage(buffer, 0, 0, null);
  }

  @Override
  public void mouseEntered(MouseEvent arg0)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void mouseExited(MouseEvent arg0)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void mousePressed(MouseEvent arg0)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void keyTyped(KeyEvent e)
  {

  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    int pressedKey=e.getKeyCode();
    System.out.println(pressedKey);

    if(pressedKey==KeyEvent.VK_W||pressedKey==KeyEvent.VK_UP)
    {
      System.out.print(player.y + "-> ");
      if(player.y>0) player.y-=PLAYER_STEP;
      System.out.print(player.y);
    }
    else if(pressedKey==KeyEvent.VK_S||pressedKey==KeyEvent.VK_DOWN)
    {
      if(player.y<HEIGHT) player.y+=PLAYER_STEP;
    }
    else if(pressedKey==KeyEvent.VK_A||pressedKey==KeyEvent.VK_LEFT)
    {
      if(player.x>0) player.x-=PLAYER_STEP;
    }
    else if(pressedKey==KeyEvent.VK_D||pressedKey==KeyEvent.VK_RIGHT)
    {
      if(player.x<WIDTH) player.x+=PLAYER_STEP;
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {

  }
}
