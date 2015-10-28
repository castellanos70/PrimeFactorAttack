package PrimeFactorAttack.transition;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/*
 * Taken from Sean Hutchinson's level up screen source code.
 * I've had to tab some methods because they weren't inline.
 * A lot of the variables throughout the project were either
 * initilazed more than once, or in random and sloppy spots.
 * I made a few drastic changes to Sean's code. One being I
 * took out his beginning matrix animation. I thought it was
 * added animation that didn't fit. I had also made it so that
 * the particles would have a specific starting spot insteasd
 * of being randomly spread. I've also added in two other arrays
 * of particles. One is that background of the originial text,
 * the other is the background animation that I tried to make
 * it draw attention to the middle of the screen. Some other 
 * little bits I did was to make the particles run around
 * randomly but stay to the outside. I was actually trying
 * to make the particles do circles but ended up with what 
 * you see. 
 * 
 * @author Luke Stankus
 * @date 2012.0507
 * 
 */

public class LevelUpScreen_Luke_Stankus extends LevelUpScreen
{
  private Graphics2D canvas;
  private boolean done = false;
  private double scale, minX;
  private int width, height, currentCol, fontHeight, backgroundColor; 
  private int str1Width, str2Width, str3Width;
  
  private int ctr, ctr2, textPixelCount, shadowPixelCount, shiftX;
  private Particle[] tArray, sArray, dArray;
  private boolean[][]lockedTPArray, lockedSPArray, lockedDPArray;
  private Random r;
  private Color textColor, shadowColor, darkColor;
  private BufferedImage offscreenBuffer;
  private Font myFont = new Font("Optima", Font.BOLD, 45);
  private int frameCount=0;
  
  private static final String STR1 = "Kudos!!!";
  private String str2, str3;
    
  public LevelUpScreen_Luke_Stankus(BufferedImage offscreenBuffer, 
      String str2, String str3)
  {

    //Initialize Variables
    this.offscreenBuffer = offscreenBuffer;
    this.str2 = str2;
    this.str3 = str3;
    width = offscreenBuffer.getWidth();
    height = offscreenBuffer.getHeight();
    textColor = new Color(255, 255, 255);
    shadowColor = new Color(255, 160, 30);
    darkColor = new Color(255, 20, 20);
    canvas = (Graphics2D)offscreenBuffer.getGraphics();
    r = new Random();
    
    canvas.setColor(Color.BLACK);
    canvas.fillRect(0, 0, width, height);
    canvas.setColor(Color.WHITE);
    

    //Used to determine whether or not a particle at that x/y location is locked.
    //Three made for text and the shadow text and the dark color
    lockedTPArray = new boolean[width][height];
    lockedSPArray = new boolean[width][height];
    lockedDPArray = new boolean[width][height];
    for(int x=0;x<width;x++)
    {
      for(int y=0;y<height;y++)
      {
        lockedTPArray[x][y] = false;
        lockedSPArray[x][y] = false;
        lockedDPArray[x][y] = false;
      }
    }

    //Set up text font.
    canvas.setFont(myFont);

    //Draw Text

    FontMetrics fm = canvas.getFontMetrics(myFont);
    fontHeight = fm.getHeight();
    str1Width = fm.stringWidth(STR1);
    str2Width = fm.stringWidth(str2);
    str3Width = fm.stringWidth(str3);
    
    int row1 = 235;
    int row2 = ((height-fontHeight)/2)+95;
    int row3 = ((height-fontHeight)/2 + fontHeight)+95;
    
    
    canvas.drawString(STR1, ((width-str1Width)/2)-5, row1);
    canvas.drawString(str2, ((width - str2Width)/2)-5, row2);
    canvas.drawString(str3, ((width-str3Width)/2)-5, row3);
    
    //Shadow text
    canvas.setColor(shadowColor);
    
    canvas.drawString(STR1, ((width-str1Width)/2)-5, row1+3);
    canvas.drawString(str2, ((width - str2Width)/2)-5, row2+3);
    canvas.drawString(str3, ((width-str3Width)/2)-5, row3+3);
    
    //Get total non-background pixels for text.
    textPixelCount = 0;
    backgroundColor = offscreenBuffer.getRGB(0, 0);
    //made rgb color int so each array wouldn't grab particles that were
    //the wrong color.
    int rgbShadow = shadowColor.getRGB();
    int rgbText = textColor.getRGB();
    for(int x=0; x<width; x++)
    {
      for(int y=0; y<height; y++)
      {
        if(offscreenBuffer.getRGB(x, y) != backgroundColor && offscreenBuffer.getRGB(x, y) != rgbShadow)
        {
          textPixelCount++;
        }
      }
    }
    
    //Get total non-background pixels for shadow.
    shadowPixelCount = 0;
    backgroundColor = offscreenBuffer.getRGB(0, 0);
    for(int x=0; x<width; x++)
    {
      for(int y=0; y<height; y++)
      {
        if(offscreenBuffer.getRGB(x, y) != backgroundColor && offscreenBuffer.getRGB(x, y) != rgbText)
        {
          shadowPixelCount++;
        }
      }
    }
    
    //Create new Particles for each text pixel and give them a starting position.
    tArray = new Particle[textPixelCount];
    ctr=0;
    for(int x=0; x<width; x++)
    {
      for(int y=0; y<height; y++)
      {
        if(offscreenBuffer.getRGB(x, y) != backgroundColor && offscreenBuffer.getRGB(x, y) != rgbShadow)
        {
          tArray[ctr] = new Particle(x, y, 1, 80);
          ctr++;
        }
      }
    }
    
    //Create new Particles for each shadow pixel and give them a starting position.
    sArray = new Particle[shadowPixelCount];
    ctr2=0;
    for(int x=0; x<width; x++)
    {
      for(int y=0; y<height; y++)
      {
        if(offscreenBuffer.getRGB(x, y) != backgroundColor && offscreenBuffer.getRGB(x, y) != rgbText)
        {
          sArray[ctr2] = new Particle(x, y, width-1, height-80);
          ctr2++;
        }
      }
    }
    
    //Create new Particles for each dark pixel and give them a starting position. First four are the box.
    //The last eight are the growing lines tht converge to the middle.
    dArray = new Particle[6000];
    for(int x=0; x<6000; x++)
    {
      if (x <= 500)
      {
        dArray[x] = new Particle((width/2), (height/2), (width - 40), 40 + r.nextInt(height - 80));
      }
      else if (x <= 1000)
      {
        dArray[x] = new Particle((width/2), (height/2), 40 + r.nextInt(width - 80), 40);
      }
      else if (x <= 1500)
      {
        dArray[x] = new Particle((width/2), (height/2), 40, 40 + r.nextInt(height - 80));
      }
      else if (x <= 2000)
      {
        dArray[x] = new Particle((width/2), (height/2), 40 + r.nextInt(width - 80), (height-40));
      }
      else if (x <= 2500)
      {
        dArray[x] = new Particle((width/2), (height/2), 1, 1);
      }
      else if (x <= 3000)
      {
        dArray[x] = new Particle((width/2), (height/2), (width-1), (height-1));
      }
      else if (x <= 3500)
      {
        dArray[x] = new Particle((width/2), (height/2), (width -1), 1);
      }
      else if (x <= 4000)
      {
        dArray[x] = new Particle((width/2), (height/2), 1, (height-1));
      }
      else if (x <= 4500)
      {
        dArray[x] = new Particle((width/2), (height/2), (width/2), 1);
      }
      else if (x <= 5000)
      {
        dArray[x] = new Particle((width/2), (height/2), (width/2), (height-1));
      }
      else if (x <= 5500)
      {
        dArray[x] = new Particle((width/2), (height/2), (width-1), (height/2));
      }
      else if (x <= 6000)
      {
        dArray[x] = new Particle((width/2), (height/2), 1, (height/2));
      }
    }
    
    //Clear canvas to hide display text.
    canvas.setColor(Color.black);
    canvas.fillRect(0, 0, width, height);
  }

  //displayes and moves the particles.
  public boolean update()
  { 
    frameCount++;
    
    for(Particle t : tArray)
    { 
      if(r.nextInt(30)==0)
      {
        t.show();
      }
    }
    
    for(Particle s : sArray)
    { 
      if(r.nextInt(30)==0)
      {
        s.show();
      }
    }
    
    for(Particle d : dArray)
    { 
      if(r.nextInt(40)==0)
      {
        d.show();
      }
    }
    
    //Move column over. Works like a conducter, tells when particles can go
    if(currentCol < width)
    {
      currentCol+=3;
    }

    //Particles for Text
    for(Particle t : tArray)
    {
      //locked particles and hidden particles are not moved.
      if(!t.isLocked() && t.isVisible())
      {
        //Erases previous position of particle only.getY() if there is not 
        //another locked particle in this position.
        if(!lockedTPArray[t.getX()][t.getY()])
        {
          canvas.setColor(Color.black);
          canvas.fillRect(t.getX(), t.getY(), 1, 1);
        }

        //If particle is within 3 pixels from it's correct position, move
        //it to that position and lock it.
        if(Math.abs((t.getX() - t.getStartX())) <= 3 &&
            Math.abs((t.getY() - t.getStartY())) <= 3) 
        {
          t.lock();
          t.setX(t.getStartX());
          t.setY(t.getStartY());
          lockedTPArray[t.getX()][t.getY()] = true;
          canvas.setColor(textColor);
          canvas.fillRect(t.getX(), t.getY(), 1, 1);
        }
        else
        {
          //Sets new X/Y
          t.setX((int)(t.getX()+Math.round(t.getXVel())));
          t.setY((int)(t.getY()+Math.round(t.getYVel())));

          //Draw particle
          canvas.setColor(textColor);
          canvas.fillRect(t.getX(), t.getY(), 1, 1);

          //Bounce off the walls
          if(t.getX()+1 > width ||
              t.getX() < 0)
          {
            t.setXVel(-t.getXVel());
            if(t.getX()>0) t.setX(width-1);
            else t.setX(0);
          }
          
          if(t.getY()+1 > height ||
              t.getY() < 0)
          {
            t.setYVel(-(int)t.getYVel());
            if(t.getY()>0) t.setY(height-1);
            else t.setY(0);
          }

          //Add Gravity. Only happens 2/3 of time.  Maximum velocity 10.
          int bool = r.nextInt(3);
          if(bool != 0 && currentCol >= t.getStartX())
          {
            if(t.getX() > t.getStartX())
            {
              if((double)t.getX() - t.getStartX() < 10)
              {
                t.setXVel(-1);
              }
              else t.setXVel(t.getXVel() - 1);
            }
            else
            {
              if((double)t.getStartX() - t.getX() < 10)
              {
                t.setXVel(1);
              }
              else t.setXVel(t.getXVel() + 1);
            }

            if(t.getY() > t.getStartY())
            {
              if((double)t.getY() - t.getStartY() < 10)
              {
                t.setYVel(-1);
              }
              else t.setYVel(t.getYVel() - 1);
            }
            else
            {
              if((double)t.getStartY() - t.getY() < 10)
              {
                t.setYVel(1);
              }
              else t.setYVel(t.getYVel() + 1);
            }
          }
        }
      }
    }
    
    //Particles for Shadow
    for(Particle s : sArray)
    {
      //locked particles and hidden particles are not moved.
      if(!s.isLocked() && s.isVisible())
      {
        //Erases previous position of particle only.getY() if there is not 
        //another locked particle in this position.
        if(!lockedSPArray[s.getX()][s.getY()])
        {
          canvas.setColor(Color.black);
          canvas.fillRect(s.getX(), s.getY(), 1, 1);
        }

        //If particle is within 3 pixels from it's correct position, move
        //it to that position and lock it.
        if(Math.abs((s.getX() - s.getStartX())) <= 3 &&
            Math.abs((s.getY() - s.getStartY())) <= 3) 
        {
          s.lock();
          s.setX(s.getStartX());
          s.setY(s.getStartY());
          lockedSPArray[s.getX()][s.getY()] = true;
          canvas.setColor(shadowColor);
          canvas.fillRect(s.getX(), s.getY(), 1, 1);
        }
        else
        {
          //Sets new X/Y
          s.setX((int)(s.getX()+Math.round(s.getXVel())));
          s.setY((int)(s.getY()+Math.round(s.getYVel())));

          //Draw particle
          canvas.setColor(shadowColor);
          canvas.fillRect(s.getX(), s.getY(), 1, 1);

          //Bounce off the walls
          if(s.getX()+1 > width ||
              s.getX() < 0)
          {
            s.setXVel(-s.getXVel());
            if(s.getX()>0) s.setX(width-1);
            else s.setX(0);
          }
          
          if(s.getY()+1 > height ||
              s.getY() < 0)
          {
            s.setYVel(-(int)s.getYVel());
            if(s.getY()>0) s.setY(height-1);
            else s.setY(0);
          }

          //Add Gravity. Only happens 2/3 of time.  Maximum velocity 10.
          int bool = r.nextInt(3);
          if(bool != 0 && currentCol >= s.getStartX())
          {
            if(s.getX() > s.getStartX())
            {
              if((double)s.getX() - s.getStartX() < 10)
              {
                s.setXVel(-1);
              }
              else s.setXVel(s.getXVel() - 1);
            }
            else
            {
              if((double)s.getStartX() - s.getX() < 10)
              {
                s.setXVel(1);
              }
              else s.setXVel(s.getXVel() + 1);
            }
            
            if(s.getY() > s.getStartY())
            {
              if((double)s.getY() - s.getStartY() < 10)
              {
                s.setYVel(-1);
              }
              else s.setYVel(s.getYVel() - 1);
            }
            else
            {
              if((double)s.getStartY() - s.getY() < 10)
              {
                s.setYVel(1);
              }
              else s.setYVel(s.getYVel() + 1);
            }
          }
        }
      }
    }
    
    //For the dark particles
    for(Particle d : dArray)
    {
      //locked particles and hidden particles are not moved.
      if(!d.isLocked() && d.isVisible())
      {
        //Erases previous position of particle only.getY() if there is not 
        //another locked particle in this position.
        if(!lockedDPArray[d.getX()][d.getY()])
        {
          canvas.setColor(Color.black);
          canvas.fillRect(d.getX(), d.getY(), 1, 1);
        }
        
        //Sets new X/Y
        minX = 1;
        scale = width/2;
        shiftX = width/2;
        double t = (d.getX()/1000);
        double rad = polarEqu(t);
        int x = polarToX(rad, t);
        int y = polarToY(rad, t);
        d.setX(d.getX()+x-(int)d.getXVel());
        d.setY(d.getY()+y-(int)d.getYVel());
        
        //Draw particle
        canvas.setColor(darkColor);
        canvas.fillRect(d.getX(), d.getY(), 1, 1);
        
        //Bounce off the walls
        if(d.getX() >= width-1 ||
            d.getX() <= 1)
        {
          if(d.getX()>= width -1)
          {
            d.setX(width-5);
            d.setXVel(-d.getXVel());
          }
          else
          {
            d.setX(5);
            d.setXVel(-d.getXVel());
          }
        }
        if(d.getY() >= height-1 ||
            d.getY() <= 1)
        {
          if(d.getY() >= height-1)
          {
            d.setY(height-5);
            d.setYVel(-d.getYVel());
          }
          else
          {
            d.setY(5);
            d.setYVel(-d.getYVel());
          }
        }

        //Add Gravity. Only happens 2/3 of time.  Maximum velocity 10.
        int bool = r.nextInt(3);
        if(bool != 0 && currentCol >= d.getStartX())
        {
          if(d.getX() > d.getStartX())
          {
            if((double)d.getX() - d.getStartX() < 10)
            {
              d.setXVel(-.3);
            }
            else d.setXVel(d.getXVel() - .3);
          }
          else
          {
            if((double)d.getStartX() - d.getX() < 10)
            {
              d.setXVel(.3);
            }
            else d.setXVel(d.getXVel() + .3);
          }

          if(d.getY() > d.getStartY())
          {
            if((double)d.getY() - d.getStartY() < 10)
            {
              d.setYVel(-.3);
            }
            else d.setYVel(d.getYVel() - .3);
          }
          else
          {
            if((double)d.getStartY() - d.getY() < 10)
            {
              d.setYVel(.3);
            }
            else d.setYVel(d.getYVel() + .3);
          }
        }
      }
    }
    if (frameCount > 500) return true;
    return false;
  }
  
  //Class for individual particles.
  //Contains starting coordinates, ending coordinates,
  //velocity, and locked/visible status.
  public class Particle
  {
    private int startX, startY;
    private int endX, endY;
    private double xVelocity, yVelocity;
    private boolean locked, visible;
    
    public Particle(int endX, int endY, int startX, int startY)
    {
      //Initialize Variables.
      this.endX = endX;
      this.endY = endY;
      this.startX = startX;
      this.startY = startY;
      locked = false;
      visible = false;
      xVelocity = 0;
      yVelocity = 0;
    }
    
    public boolean isVisible()
    {
      return visible;
    }
    
    public void hide()
    {
      visible = false;
    }
    
    public void show()
    {
      visible = true;
    }
    
    public int getStartX()
    {
      return endX;
    }
    
    public int getStartY()
    {
      return endY;
    }
    
    public int getX()
    {
      return startX;
    }
    
    public int getY()
    {
      return startY;
    }
    
    public void setX(int x)
    {
      this.startX = x;
    }
    
    public void setY(int y)
    {
      this.startY = y;
    } 
    
    public double getXVel()
    {
      return xVelocity;
    }
    
    public double getYVel()
    {
      return yVelocity;
    }
    
    public void setXVel(double x)
    {
      if(x>10) x = 10;
      if(x<-10) x = -10;
      xVelocity = x;
    }
    
    public void setYVel(double y)
    {
      if(y>10) y = 10;
      if(y<-10) y = -10;
      yVelocity = y;
    }
    
    public void lock()
    {
      locked = true;
    }
    
    public void unlock()
    {
      locked = false;
    }
    
    public boolean isLocked()
    {
      return locked;
    }
  }
  
  //Methods to randomize the moving patterns of the dark particles
  //Tried to make them go in cirlces but this worked out better.
  private int polarToX(double rad, double theta)
  {
    double x = rad * Math.cos(theta);
    return worldToScreenX(x);
  }
  
  private int polarToY(double rad, double theta)
  {
    double y = rad * Math.sin(theta);
    return worldToScreenX(y);
  }
  
  private double polarEqu(double t)
  {
    double rad = (t/22.0)* Math.sin(6.0*t);
    return rad;
  }

  private int worldToScreenX(double x)
  {
    int ix = (int) ((x-minX) * scale);
    return ix+shiftX;
  }
  
}

