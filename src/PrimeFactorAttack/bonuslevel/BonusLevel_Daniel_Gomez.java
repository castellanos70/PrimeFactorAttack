package PrimeFactorAttack.bonuslevel;
/***************************************************************
@version 2013.0501
Original Author: Ethan Parks
Updated by: Daniel Gomez
Background image created by Daniel Gomez
 * 
 ***************************************************************/

import PrimeFactorAttack.utility.Utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.event.MouseInputListener;


public class BonusLevel_Daniel_Gomez extends BonusLevel implements MouseMotionListener, MouseListener
{

	private BufferedImage    buffer, background;
	private Graphics2D         canvas;
	private final int        WIDTH                 = 752;
	private final int        HEIGHT                = 575;

	private Random           rand                  = new Random();

	private int              score, frameCount;

	private int mouseX, mouseY, mouseClickX, mouseClickY;

	private int level;

	private final static int BLOCK_SIZE            = 50; //player base size
	
	int health = 100;

	//monster variables
	private int totalMonsters = 500; //maximum number of monsters
	private int monsterSize = 40; //monster box size
	private int monsterCount = 0; //how many monsters have been created
	private int currentMonsters = 0; //how many monsters are on screen
	private int[] monsterStatus = new int[totalMonsters]; //monster alive or dead
	private int[] monsterHit = new int[totalMonsters]; //monster hit by bullet
	private int[] monsterNum = new int[totalMonsters]; //monsters composite number
	private int[] monsterSide = new int[totalMonsters]; //monster spawn side
	private int[] monsterX = new int[totalMonsters]; //monster X coordinate
	private int[] monsterY = new int[totalMonsters]; //monster Y coordinate
	private final int freezeTime = 50; //how long a monster stays frozen when hit

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

	private int[] components = {4,6,8,9,10,12,14,15,16,18,20,21,22,24,25,26,27,28,30,32,33,34,35,36,
			38,39,40,42,44,45,46,48,49,50,51,52,54,55,56,57,58,60};
	private int[] primes = {2,3,5,7,11,13,17,19,23,29,31,37,41,43,47,53,59};
	private int largest = 60;

	// sets up the buffered image, instructions
	//and the mouse motion listener
	public BonusLevel_Daniel_Gomez()
	{
		this.setSize(WIDTH, HEIGHT);

		buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		canvas = (Graphics2D)buffer.getGraphics();

		this.repaint();

		addMouseMotionListener(this);
		addMouseListener(this);
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
		score = 0;
		health = 100;
		//set background
		canvas.setColor(Color.WHITE);
		canvas.fillRect(0, 0, WIDTH, HEIGHT);
		//draw background image
		canvas.drawImage(background,0,0,null);
		//draw center circle
		canvas.setColor(Color.RED);
		canvas.fillRect((WIDTH / 2) - (BLOCK_SIZE / 2), (HEIGHT / 2) - (BLOCK_SIZE / 2), BLOCK_SIZE, BLOCK_SIZE);

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
		if (frameCount > 6000 || health < 1)
		{
			canvas.setColor(Color.WHITE);
			canvas.fillRect(0, 0, WIDTH, HEIGHT);

			return false;
		}

		//hide mouse cursor
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "cursor"));
		canvas.setFont(new Font("TimesRoman", Font.PLAIN, 16));
		//set a limit to how quickly we spawn monsters
		if (frameCount%(10*level) == 0)
		{
			createMonster();
		}

		//how often we update the game
		if (frameCount%1 == 0)
		{
			clearMonsters();
			drawMonsters();
			drawBullets();
			updateMonsters();
			updateBullets();
			//check reload time
			if (bulletTimer >= 1) bulletTimer++;
			if (bulletTimer >= bulletReloadTime) bulletTimer = 0;
		}


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
		    String instructions1 = "Shoot composite numbers and let primes reach your base to gain points!";
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
		if (bulletTimer < 1) //check for our bullet delay
		{
			mouseClickX = e.getX(); //get mouse coordinates
			mouseClickY = e.getY();
			createBullet(mouseClickX, mouseClickY, currentPrime); //create bullet

			for (int i = 0; i < monsterCount; i++) //for every monster on screen
			{
				//if player clicks within a monster
				if (mouseClickX > monsterX[i] && mouseClickX < monsterX[i]+monsterSize
						&& mouseClickY > monsterY[i] && mouseClickY < monsterY[i]+monsterSize && monsterHit[i] == 0)
				{
					for (int j=0; j < components.length; j++)
					{
						if (monsterNum[i] == components[j]) //if player hit a component
						{
							score++; //add to score
							monsterStatus[i] = 0; //kill monster
							if (level > 4) level--; //increase difficulty
						}
					}
					monsterHit[i] = 1; //monster was hit
				}
			}
			currentPrime = 0; //reset prime
		}
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
	private boolean isInMiddle(int x, int y) //x and y are the monsters coordinates
	{
		if (x <= (WIDTH / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE &&
				x >= (WIDTH / 2) - (BLOCK_SIZE / 2) &&
				y >= (HEIGHT / 2) - (BLOCK_SIZE / 2) &&
				y <= (HEIGHT / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE) return true;

		if (x <= (WIDTH / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE &&
				x >= (WIDTH / 2) - (BLOCK_SIZE / 2) &&
				y >= (HEIGHT / 2) - (BLOCK_SIZE / 2) &&
				y <= (HEIGHT / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE) return true;

		if (x <= (WIDTH / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE &&
				x >= (WIDTH / 2) - (BLOCK_SIZE / 2) &&
				y >= (HEIGHT / 2) - (BLOCK_SIZE / 2) &&
				y <= (HEIGHT / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE) return true;

		if (x <= (WIDTH / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE &&
				x >= (WIDTH / 2) - (BLOCK_SIZE / 2) &&
				y >= (HEIGHT / 2) - (BLOCK_SIZE / 2) &&
				y <= (HEIGHT / 2) - (BLOCK_SIZE / 2) + BLOCK_SIZE) return true;

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
		canvas.setColor(Color.RED);
		canvas.fillRect((WIDTH / 2) - (BLOCK_SIZE / 2), (HEIGHT / 2) - (BLOCK_SIZE / 2), BLOCK_SIZE, BLOCK_SIZE);
	}

	//create a new monster
	private void createMonster()
	{
		int number = rand.nextInt(largest)+1; //determine component number attached to new monster
		monsterNum[monsterCount] = number;
		currentMonsters++;

		int side = rand.nextInt(8); //determine which side the monster will spawn
		monsterSide[monsterCount] = side;
		if (side == 0)
		{
			monsterX[monsterCount] = 10; //monster starting coordinates
			monsterY[monsterCount] = 10;
		} else if (side == 1)
		{
			monsterX[monsterCount] = 376-monsterSize/2; //monster starting coordinates
			monsterY[monsterCount] = 10;
		} else if (side == 2)
		{
			monsterX[monsterCount] = 742-monsterSize; //monster starting coordinates
			monsterY[monsterCount] = 10;
		} else if (side == 3)
		{
			monsterX[monsterCount] = 742-monsterSize; //monster starting coordinates
			monsterY[monsterCount] = 287-monsterSize/2;
		} else if (side == 4)
		{
			monsterX[monsterCount] = 742-monsterSize; //monster starting coordinates
			monsterY[monsterCount] = 565-monsterSize;
		} else if (side == 5)
		{
			monsterX[monsterCount] = 376-monsterSize/2; //monster starting coordinates
			monsterY[monsterCount] = 565-monsterSize;
		} else if (side == 6)
		{
			monsterX[monsterCount] = 10; //monster starting coordinates
			monsterY[monsterCount] = 565-monsterSize;
		} else if (side == 7)
		{
			monsterX[monsterCount] = 10; //monster starting coordinates
			monsterY[monsterCount] = 287-monsterSize/2;
		}
		monsterStatus[monsterCount] = 1;
		monsterCount++; //increment monster count
	}

	//update the monsters positions
	private void updateMonsters()
	{
		for (int i = 0; i < monsterCount; i++) //for every monster in play
		{
			if (monsterStatus[i] == 1) //if the monster is alive
			{
				if (monsterHit[i] == 0) //if the monster is not frozen by a hit
				{
					//check if monsters reached center
					if (isInMiddle(monsterX[i]+monsterSize/2,monsterY[i]+monsterSize/2) == true)
					{
						for (int j=0; j < primes.length; j++)
						{
							if (monsterNum[i] == primes[j]) score++; //if a prime reached the center, add to score
						}
						for (int j=0; j < components.length; j++)
						{
							if (monsterNum[i] == components[j]) health -= 5; //if a component reached the center, lose health
						}
						monsterStatus[i] = 0; //kill monster
						currentMonsters--;
					}
					//move all monsters towards center
					else if (monsterSide[i] == 0)
					{
						if (monsterX[i]+monsterSize/2 < (WIDTH-BLOCK_SIZE)/2) monsterX[i] += 1;
						if (monsterY[i]+monsterSize/2 < (HEIGHT-BLOCK_SIZE)/2) monsterY[i] += 1;
					} else if (monsterSide[i] == 1)
					{
						if (monsterY[i]+monsterSize/2 < (HEIGHT-BLOCK_SIZE)/2) monsterY[i] += 1;
					} else if (monsterSide[i] == 2)
					{
						if (monsterX[i]+monsterSize/2 > (WIDTH-50)/2 + BLOCK_SIZE) monsterX[i] -= 1;
						if (monsterY[i]+monsterSize/2 < (HEIGHT-BLOCK_SIZE)/2) monsterY[i] += 1;
					} else if (monsterSide[i] == 3)
					{
						if (monsterX[i]+monsterSize/2 > (WIDTH-50)/2 + BLOCK_SIZE) monsterX[i] -= 1;
					} else if (monsterSide[i] == 4)
					{
						if (monsterX[i]+monsterSize/2 > (WIDTH-50)/2 + BLOCK_SIZE) monsterX[i] -= 1;
						if (monsterY[i]+monsterSize/2 > (HEIGHT-BLOCK_SIZE)/2 + BLOCK_SIZE) monsterY[i] -= 1;
					} else if (monsterSide[i] == 5)
					{
						if (monsterY[i]+monsterSize/2 > (HEIGHT-BLOCK_SIZE)/2 + BLOCK_SIZE) monsterY[i] -= 1;
					} else if (monsterSide[i] == 6)
					{
						if (monsterX[i]+monsterSize/2 < (WIDTH-50)/2) monsterX[i] += 1;
						if (monsterY[i]+monsterSize/2 > (HEIGHT-BLOCK_SIZE)/2 + BLOCK_SIZE) monsterY[i] -= 1;
					} else if (monsterSide[i] == 7)
					{
						if (monsterX[i]+monsterSize/2 < (WIDTH-50)/2) monsterX[i] += 1;
					}
				} else if (monsterHit[i] >= 1) //if the monster was hit by a bullet
				{
					monsterHit[i]++; //freeze monster until monsterHit reaches our freeze timer
					if (monsterHit[i] >= freezeTime) monsterHit[i] = 0;
				}
			}
		}
	}

	//draw monsters on screen
	private void drawMonsters()
	{
		canvas.setColor(Color.BLUE);
		for (int i = 0; i < monsterCount; i++) //for every monster in play
		{
			if (monsterStatus[i] == 1)
			{
				if (monsterHit[i] == 0)
				{
					//draw monster
					canvas.setColor(Color.BLUE);
					canvas.fillRect(monsterX[i], monsterY[i], monsterSize, monsterSize);
					//draw monster number
					canvas.setColor(Color.WHITE);
					canvas.drawString(Integer.toString(monsterNum[i]),monsterX[i]+2, monsterY[i]+15);
				} else
				{
					//draw monster
					canvas.setColor(Color.YELLOW);
					canvas.fillRect(monsterX[i], monsterY[i], monsterSize, monsterSize);
					//draw monster number
					canvas.setColor(Color.RED);
					canvas.drawString(Integer.toString(monsterNum[i]),monsterX[i]+2, monsterY[i]+15);
				}
			}
		}
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

}
