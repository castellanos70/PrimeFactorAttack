package PrimeFactorAttack.bonuslevel;

/*CS 152 - edited project
 * This project is a bonus level for the prime numbers attack game. It shows
 * various numbers in balls which bounce around the screen. Clicking on a prime
 * number advances the score. After 6000 frames, the program returns negative
 * in its nextFrame method and returns to the main game.
 * 
 * ORIGINAL PROJECT WORK DONE BY RYAN BLOOM
 * CHANGES BY COLIN MONROE
 * ********************CHANGELOG*******************************
 * ***Many comments added to code
 * ***Original program had balls bouncing off of lower edge and right edge of
 *    frame incorrectly. This is fixed.
 * ***Original program had single digits in balls slightly off. This has been
 *    changed so single digits are centered.
 * ***Original program did not end (return false in nextFrame) after 100 point
 *    score was reached. This has been changed.
 * ***Original program recognized 1 as prime (incorrect). this has been fixed.
 * ***Original program had a bug where the last color in the color array would
 *    never be used for ball colors. this has been fixed.
 * ***Changed text on directions and win message to be more readable.
 * ***Program now waits 100 frames after a win so that the player can read the
 *    message before program returns false (and ends). 
 * ***Original program had an issue where balls would sometimes get stuck at 
 *    the top of the screen. This is fixed.
 * ***Changed point value increments to 5 instead of 10 so game lasts longer.
 * ***Original program was very easy- numbers to click were ranged 0-30.
 *    I have added a difficulty setting, where the numbers generated on 
 *    click/bounce become higher based off of score. Game starts in easy mode.
 *    Score(0-25)= Easy Mode: ball number range 1-30
 *    Score(30-65)= Medium Mode: ball number range 30-63
 *    Score(70-100)= Hard Mode: ball number range 51-99
 * ***Original program did not have any penalties for clicking a composite
 *    number. This has been fixed (-5 to score on composite click). Score does
 *    not drop below 0 even if the player makes continuous mistakes.
 * ***Original program had no background or sounds. I have created a special
 *    background just for this program, and recorded a sound.
 *    (Image created in Inkscape)
 *    (Sound recorded on a mic then edited at filelab.com to linear PCM format)
 * ***Changed ball colors to match the bubbles in the background   
 * ***Instructions, score, and win message have been moved to more easily
 *    readable positions (right below the bonus level title of background).
 * ***Changed ball format so that when they are clicked, if the click is
 *    correct (ball is prime) the sound plays and the ball vanishes. The ball
 *    is actually still there, but is not drawn and does not acknowledge clicks
 *    thanks to an added specification variable on the spec array. The
 *    ball will reappear once it hits a wall and bounces back.
 * ***Changed ball color format: when a 'new' ball appears (though its still
 *    the original ball, just becoming visible again), it will have a different
 *    color, chosen at random from the color array. 
 * ***Changed ball velocity algorithm: Balls begin at speed 1, then after a
 *    click, and on every click thereafter the speed in both x and y axis
 *    directions is randomized between 1 and 3.     
*/
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import PrimeFactorAttack.utility.SoundPlayer;
import PrimeFactorAttack.utility.Utility;

public class BonusLevel_Colin_Monroe extends BonusLevel
implements MouseListener, MouseMotionListener
{ 
  private static final long serialVersionUID = 1L;//get rid of warning		
  private static final int NUMBALLS = 10;  //set up number of balls: 10
  private static final int SPECIFICATIONS = 5; //used for ball specifications.
  private static final int NUMCOLORS = 6;
  //^total # of ball colors. note: the 6th color slot is not used in its array.
  private int [] [] balls = new int [NUMBALLS] [SPECIFICATIONS];
  //^setup array with number of balls & specifications.
  // balls array layout: [CURRENT BALL NUMBER] [SPECS]
  // ball specs numbers: 0 = x coordinate, 1 = y coordinate, 2 = ball color,
  // 3 = the number within the ball, 4 = ball visibility marker
  private Color [] ballColor = new Color [NUMCOLORS];
  //^array for ball colors, 5 slots
  private double [] [] ballDirections = new double [NUMBALLS] [2];
  //^setup ball directions variables
  // ball directions layout: [CURRENT BALL NUMBER] [DIRECTION]
  // ball direction numbers: 0 = speed in Y axis, 1 = speed in X axis
  private boolean [] prime = new boolean [NUMBALLS];
  //^set up a boolean variable to mark primes true or false
  private BufferedImage buffer;
  private Graphics canvas,bground;
  private final int WIDTH = 770;
  private final int HEIGHT = 623;
  private int score; 
  private int framecount;
  private Random rand = new Random();
  private int winCounter = 0;
  //Method that converts integers into string form.
  public static String convertInteger(int n) 
  {
    return Integer.toString(n);
  }
  //Method that checks numbers to see whether they are prime or not.
  private boolean isPrime(int n)
  {
	if (n == 1) return false;  
    for (int i=2; i<n; i++)
    {
      if(n%i == 0) return false;
    }
    return true;
  }
  //Method that takes known values and creates balls with them.
  private void drawBall(int i)
  {
    int y = balls [i][1];
    //^create local int variable y: set to current ball coordinate for y value
    int x = balls [i][0];
    //^create local int variable x: set to current ball coordinate for x value
    
    double m = ballDirections[i][0];
    //^create local double variable m: set to y speed of current ball
    double b = ballDirections[i][1];
    //^create local double variable b: set to x speed of current ball
    
    if (y + 80 >= HEIGHT)
    //^if the y location of any ball goes under the bottom of the screen...
      {
        m = -m; //reverse y speed
        Random random = new Random();
        //generate new ball number
        if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
        //^easyMode: ball# range 1-30
        else if (score >25 && score <70) balls [i][3] = random.nextInt(33)+30;
        //^medMode: ball# range 30-63
        else if (score >=70) balls [i][3] = random.nextInt(44)+51;
        //^hardMode: ball# range 51-99
        prime [i] = isPrime(balls [i][3]);//check to see if new number is prime
        y = HEIGHT - 81; //set y location away from edge
        if (balls[i][4] == 0)//if ball is invisible...
        {	
         balls[i][4] = 1; //set ball to visible
         balls [i][2] = random.nextInt(NUMCOLORS-1); //give ball a new color
        }
      }
    if (y <= 0)
    //^if the y location of any ball goes over the top of the screen... 
      {
        m = -m; //reverse y speed
        Random random = new Random();
        //generate new ball number
        if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
        //^easyMode: ball# range 1-30
        else if (score >25 && score <70) balls [i][3] = random.nextInt(33)+30;
        //^medMode: ball# range 30-63
        else if (score >=70) balls [i][3] = random.nextInt(44)+51;
        //^hardMode: ball# range 51-99
        prime [i] = isPrime(balls [i][3]);//check to see if new number is prime
        y = 1; //set y location away from edge
        if (balls[i][4] == 0)//if ball is invisible...
        {	
         balls[i][4] = 1;//set ball to visible
         balls [i][2] = random.nextInt(NUMCOLORS-1); //give ball a new color
        }
      }
    
    if (x + 50 >= WIDTH)
    //^if the x location of any ball goes over the right of the screen...
      {
        b = -b; //reverse x direction
        Random random = new Random();
        //generate new ball number
        if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
        //^easyMode: ball# range 1-30
        else if (score >25 && score <70) balls [i][3] = random.nextInt(33)+30;
        //^medMode: ball# range 30-63
        else if (score >=70) balls [i][3] = random.nextInt(44)+51;
        //^hardMode: ball# range 51-99
        prime [i] = isPrime(balls [i][3]);//check to see if new number is prime
        x = WIDTH - 51; //set x location away from edge
        if (balls[i][4] == 0)//if ball is invisible...
          {	
           balls[i][4] = 1; //set ball to visible
           balls [i][2] = random.nextInt(NUMCOLORS-1); //give ball a new color
          }
      }
    if (x <= 0)
    //^if the x location of any ball goes over the left of the screen...
      {
        b = -b; //reverse x direction
        Random random = new Random();
        //generate new ball number
        if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
        //^easyMode: ball# range 1-30
        else if (score >25 && score <70) balls [i][3] = random.nextInt(33)+30;
        //^medMode: ball# range 30-63
        else if (score >=70) balls [i][3] = random.nextInt(44)+51;
        //^hardMode: ball# range 51-99
        prime [i] = isPrime(balls [i][3]);//check to see if new number is prime
        x = 1; //set x location away from edge
        if (balls[i][4] == 0)//if ball is invisible...
          {
           balls[i][4] = 1;//set ball to visible
           balls [i][2] = random.nextInt(NUMCOLORS-1); //give ball a new color
          }
      }
    x = (int) (x + b);
    //^ball movement on x axis: increase by ballDirections variable (slot 0)
    y = (int) (y + m);
    //^ball movement on y axis: increase by ballDirections variable (slot 1)
  
    balls [i][0] = x;
    //^sets ball x coordinate in array (current ball, x coordinate)
    balls [i][1] = y;
    //^sets ball y coordinate in array (current ball, y coordinate)
    canvas.setColor(ballColor[balls[i][2]]);
    //^set each ball color to whatever color is in the array

    if (balls[i][4] == 1) canvas.drawOval(x, y, 30, 30); 
    //^if ball array value is set to visible...
    // draw an oval in a 30x30 square from x/y coordinate
    
    canvas.setColor(Color.LIGHT_GRAY);
    //^set canvas color to light gray to draw integers
    String num = convertInteger(balls [i][3]);
    //^convert the ball number of each ball to a string
    if (balls[i][4] == 1) //if ball array value is set to visible...
      {
       if (num.length() == 1)
    	 {
    	  canvas.drawChars(num.toCharArray(), 0, num.length(), x+12, y+20); 	
    	 }
       else canvas.drawChars(num.toCharArray(), 0, num.length(), x+9, y+20); 
       //^convert each number from string to chars,
       // and draw each number in each ball.
       // if the number is only one digit,
       // then draw the number a little more to the right.
      }
    ballDirections[i][0] = m;
    //^set y speed number in ballDirections array to current speed
    ballDirections[i][1] = b;
    //^set x speed number in ballDirections array to current speed
  }
  public BonusLevel_Colin_Monroe()
  {
    this.setSize(WIDTH, HEIGHT);
    buffer = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_RGB);
    canvas = buffer.getGraphics();
    bground = buffer.getGraphics();
  }
  public static void main(String[] args)
  {
   //empty main method.
  }
  @Override
  public void mouseDragged(MouseEvent evt)
    {
	 //This method MUST be implemented due to the class using MouseListener 
    }
  @Override
  public void mouseMoved(MouseEvent arg0)
  {
	//This method MUST be implemented due to the class using MouseListener
  }
  @Override
  public void mouseClicked(MouseEvent arg0) 
  {
	//This method MUST be implemented due to the class using MouseListener  
  }
  //Listens for mouse clicks and checks to see if a ball was clicked.
  @Override
  public void mousePressed(MouseEvent arg0)
  {
    int x = arg0.getX();//x location of click
    int y = arg0.getY();//y location of click
    
    for (int i=0; i<NUMBALLS; i++)
    {
      int xloc = balls[i][0];
      //^^set new value of x location of each ball (upper left corner)
      int yloc = balls[i][1];
      //^set new value of y location of each ball (upper left corner)
      if(x>xloc && x<xloc+30 && y>yloc && y<yloc+30)
      //^if a click occurs in the 30x30 square from the x/y coordinate...
        {
          Random random = new Random();
          //change ball direction
          ballDirections [i][0] = (random.nextDouble()+0.5)*2.0;
          if(rand.nextInt(2) == 1 )//randomly determine x direction
            {
        	 ballDirections [i][1] = (random.nextDouble()+0.5)*2.0; 
            }
          else ballDirections [i][1] = -((random.nextDouble()+0.5)*2.0);
          
          if(prime[i] && balls [i][4] == 1)
          //^if the number in the circle is prime and the ball is visible...
          {

            if(score <= 95)
              {
               score+=5;//increase score by 5 if under the 100 pt limit
               balls [i][4] = 0;//set ball to invisible via array value
              }
            //generate new ball number
            if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
            //^easyMode: ball# range 1-30
            else if (score >25 && score <70)balls [i][3]=random.nextInt(33)+30;
            //^medMode: ball# range 30-63
            else if (score >=70) balls [i][3] = random.nextInt(44)+51;
            //^hardMode: ball# range 51-99
            prime [i] = isPrime(balls [i][3]);
            //^check to see if new number is prime
            canvas.setColor(ballColor[balls[i][2]]);
            //^keep ball color
          }
          else //if the number clicked is NOT a prime...
          {
        	if(score > 0) score-=5;//decrease score by 5 if above 0
            //generate new ball number
            if (score >=0 && score <=25) balls [i][3] = random.nextInt(29)+1;
            //^easyMode: ball# range 1-30
            else if (score >25 && score <70)balls [i][3]=random.nextInt(33)+30;
            //^medMode: ball# range 30-63
            else if (score >=70) balls [i][3] = random.nextInt(44)+51;
            //^hardMode: ball# range 51-99
            prime [i] = isPrime(balls [i][3]);
            //^check to see if new number is prime
          }
          break;
        }
    }
  }
  @Override
  public void mouseReleased(MouseEvent arg0) 
  { 
	//This method MUST be implemented due to the class using MouseListener
  }
  //Gets informations from strings and sets up the game.
  @Override
  public void init() 
  {  
    addMouseListener(this);
    score = 0;
    framecount = 0;
    //setup sound for bubble pop- display error message if sound file missing

    //initialize ball colors
    ballColor[0] = Color.BLUE;
    ballColor[1] = Color.CYAN;
    ballColor[2] = Color.GREEN;
    ballColor[3] = Color.MAGENTA;
    ballColor[4] = Color.YELLOW;
    
   Random random = new Random();
   for (int i=0; i< NUMBALLS; i++) 
   {
    //initialize ball specifications for each ball
    balls [i][0] = random.nextInt(WIDTH-1); //initial x positions
    balls [i][1] = random.nextInt(HEIGHT-1); //initial y positions
    balls [i][2] = random.nextInt(NUMCOLORS-1); //initialize colors
    balls [i][3] = random.nextInt(29)+1; //initialize numbers
    balls [i][4] = 1; //set all ball visibility to 1
    prime [i] = isPrime(balls [i][3]);//initialize prime checks for each number
    //initialize ball direction in y direction
    if(rand.nextInt(2) == 1 ) ballDirections [i][0] = 1; 
    else ballDirections [i][0] = -1;    
    //initialize ball direction in x direction
    if(rand.nextInt(2) == 1 ) ballDirections [i][1] = 1; 
    else ballDirections [i][1] = -1;
   }
  }
  //Keeps track of the current frame #, gives directions, and a win message.
  @Override
  public boolean nextFrame() 
  {
   framecount++;
   BufferedImage bg= Utility.loadBufferedImage("bonusLevel/BG_Colin_Monroe.png", this);
   bground.drawImage(bg,0,0,null);

   if (framecount > 6000) return false; //game times out after 6000 frames
   //draw each ball
   for (int i=0; i<NUMBALLS; i++) //draw each ball using method
     {
       drawBall (i);
     }
   if(score == 100) //if score reaches limit (100) then initiate win sequence
     { 
	 canvas.setFont(new Font("TimesRoman", Font.PLAIN, 30));
     String message = "You WIN!!";
     canvas.drawChars(message.toCharArray(), 0, message.length(), 350, 160);
     canvas.setFont(new Font("TimesRoman", Font.PLAIN, 14));
     winCounter ++;
     if (winCounter == 100) return false;
     }
   else if(framecount <= 200)
   //^at the beginning of level, (first 200 frames) display instructions
     {
	 canvas.setFont(new Font("TimesRoman", Font.PLAIN, 30));  
     String message = "Click the Prime Numbers!";
     canvas.drawChars(message.toCharArray(), 0, message.length(), 240, 160);
     canvas.setFont(new Font("TimesRoman", Font.PLAIN, 14));
     }
   else //display score throughout gameplay
     {
     String message = "Score:" + Integer.toString(getScore());
     canvas.drawChars(message.toCharArray(), 0, message.length(), 370, 160);
     }   
   repaint();
   return true;
  }
  @Override
  //Method that returns score
  public int getScore() 
  {
    return score;
  }
  public void paint (Graphics canvas)
  {
    canvas.drawImage(buffer, 0, 0, null);
  }
  @Override
  public void mouseEntered(MouseEvent arg0)
    {
    //This method MUST be implemented due to the class using MouseListener
    }
  @Override
  public void mouseExited(MouseEvent arg0) 
    {
	//This method MUST be implemented due to the class using MouseListener
    }
}
