package PrimeFactorAttack.transition;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;

import PrimeFactorAttack.Data;


public class DiffusionLimitedAggregation
{

  private static final int PARTICLE_COUNT = 30000;
  private static int[] x = new int[PARTICLE_COUNT]; 
  private static int[] y = new int[PARTICLE_COUNT]; 
  private static int[] color = new int[PARTICLE_COUNT]; 
  

  private static final int background_rgb = (Color.WHITE).getRGB();
  
  private static BufferedImage sandStoneTexture;
  
  private static Random rand = new Random();

  
  private static final int WHITE = Color.WHITE.getRGB();
  
  private static final int EDGE = 75;
  
  private static Font supperLargeFont, largeFont;
  
  //private static ArrayList<String[]> levelMsg = new ArrayList<String[]>();
  private static final String STR1 = "Prime Factor Attack";
  private static final String STR2 = "Level 1: Sand Storm on Titan";
  private static final String STR3 = "To Play: Click START";
  
  private static int levelIdx;
  
  
  private static int canvasWidth, canvasHeight;
  
  public void setup()
  { 
    
    System.out.println("IntroScreen.setUp()");
    canvasWidth = Data.image.getWidth();
    canvasHeight = Data.image.getHeight();
    
    String str = "sandstoneTexture.png";
    
    Image texture = new ImageIcon(Data.resourcePath + str).getImage();
    sandStoneTexture = new BufferedImage(canvasWidth,canvasHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = sandStoneTexture.createGraphics();
    g.drawImage(texture,0,0,null);
    
    supperLargeFont = new Font("SansSerif",Font.BOLD, 56);
    largeFont = new Font("SansSerif",Font.BOLD, 28);
    
    levelIdx=-1;
    setLevelScreen();

    
  }
  
  public void endGame()
  { levelIdx=0;
  }
  
  public void setLevelScreen()
  { 
    levelIdx++;
    
    
    Data.graph.setPaint(Color.WHITE);
    Data.graph.fillRect(0, 0, canvasWidth, canvasHeight);
    
    Data.graphSand.drawImage(Data.image, 0, 0, null);
    
    //String[] msg = levelMsg.get(levelIdx);
    
    Data.graphSand.setFont(supperLargeFont);
    Data.graphSand.setColor(Color.BLACK);
    Data.graphSand.drawString(STR1, 100,220);
    
    Data.graphSand.setFont(largeFont);
    
    
    Data.graphSand.drawString(STR2, 120,290);
    Data.graphSand.drawString(STR3, 120,330);
    
    
    for (int xx=0; xx<canvasWidth; xx++)
    { 
      for (int yy=0; yy<canvasHeight; yy++)
      { 
        int rgb = Data.imageSand.getRGB(xx,yy);
        if (rgb != WHITE)
        { rgb = sandStoneTexture.getRGB(xx,yy);
        Data.imageSand.setRGB(xx,yy, rgb);
        }
      }
    }
    
    
    
    
    Data.graphTmp.drawImage(Data.imageSand, 0, 0, null);
    
    for (int i=0; i<PARTICLE_COUNT; i++)
    { x[i] = rand.nextInt(canvasWidth);
      y[i] = rand.nextInt(canvasHeight);
      int rgb = sandStoneTexture.getRGB(x[i],y[i]);
      Data.imageTmp.setRGB(x[i], y[i], rgb);
    }
    
  }
  
  
  
  public void update()
  { 
    //System.out.println("IntroScreen.update()");
    
    Data.graphTmp.drawImage(Data.imageSand, 0, 0, null);
    
    for (int i=0; i<PARTICLE_COUNT; i++)
    { int d=rand.nextInt(4);
      if (d==0) 
      { x[i]++;
        if (x[i] > canvasWidth+EDGE) x[i] = canvasWidth+EDGE;
      }
      else if (d==1) 
      { x[i]--;
        if (x[i] < -EDGE) x[i] = -EDGE;
      }
      else if (d==2) 
      { y[i]++;
        if (y[i] > canvasHeight+EDGE) y[i] = canvasHeight+EDGE;
      }
      else 
      { y[i]--;
        if (y[i] < -EDGE) y[i] = -EDGE;
      }
      boolean crystallize = false;
      crystallize = crystallizeIfNextToCrystal(x[i], y[i], 1, 0);
      if (!crystallize) crystallize = crystallizeIfNextToCrystal(x[i], y[i], -1, 0);
      if (!crystallize) crystallize = crystallizeIfNextToCrystal(x[i], y[i], 0, -1);
      if (!crystallize) crystallize = crystallizeIfNextToCrystal(x[i], y[i], 0, 1);
    
      if (crystallize) setNewParticalOnEdge(x, y, i);
      plotPoint(x[i], y[i], color[i]);

    }
    
    //if (refreshDisplay) canvas.updateDisplay(); 
  }
  
  private boolean plotPoint(int x, int y, int rgb)
  { 
    if (x<0 || y<0 || x>=canvasWidth || y >=canvasHeight) return false;
    Data.imageTmp.setRGB(x, y, rgb);
    return true;
    
    
  }

  private boolean crystallizeIfNextToCrystal(int x, int y, int dx, int dy)
  { 
    if (x<=0 || x >=canvasWidth-1) return false;
    if (y<=0 || y >=canvasHeight-1) return false;
    
    int rgb = Data.imageSand.getRGB(x+dx, y+dy);
    if (rgb != background_rgb)
    { 
      rgb=sandStoneTexture.getRGB(x, y);
      Data.imageSand.setRGB(x, y, rgb);
      Data.imageTmp.setRGB(x, y, rgb);
      return true;
    }
    return false;
  }
  
  private void setNewParticalOnEdge(int[] x, int[] y, int idx)
  { 
    if (rand.nextDouble() < 0.5)
    { x[idx] = rand.nextInt(canvasWidth);
      if (rand.nextDouble() < 0.5) y[idx] = -EDGE; else y[idx] = canvasHeight+EDGE;
    }
    else
    { y[idx] = rand.nextInt(canvasHeight);
      if (rand.nextDouble() < 0.5) x[idx] = -EDGE; else x[idx] = canvasWidth+EDGE;
    }
    
  }
  
//  public static void timeStopSetup(int x, int y)
//  { timeStopX = x;
//    timeStopY = y;
//  }
//  public static void timeStopUpdate()
//  { 
//    
//    for (int i=0; i<20; i++)
//    { 
//      
//      int rgb=sandStoneTexture.getRGB(timeStopX, timeStopY);
//      data.imageSand.setRGB(timeStopX, timeStopY, rgb);
//      data.imageTmp.setRGB(timeStopX, timeStopY, rgb);
//      
//      rgb=sandStoneTexture.getRGB(timeStopX+1, timeStopY);
//      data.imageSand.setRGB(timeStopX+1, timeStopY, rgb);
//      data.imageTmp.setRGB(timeStopX+1, timeStopY, rgb);
//      
//      rgb=sandStoneTexture.getRGB(timeStopX, timeStopY+1);
//      data.imageSand.setRGB(timeStopX, timeStopY+1, rgb);
//      data.imageTmp.setRGB(timeStopX, timeStopY+1, rgb);
//      
//      int d=rand.nextInt(4);
//      if (d==0) 
//      { timeStopX++;
//        if (timeStopX >= canvas.getWidth()) timeStopX = canvas.getWidth()-1;
//      }
//      else if (d==1) 
//      { timeStopX--;
//        if (timeStopX < 0) timeStopX = 1;
//      }
//      else if (d==2) 
//      { timeStopY++;
//        if (timeStopY >= canvas.getHeight()) timeStopY = canvas.getHeight()-1;
//      }
//      else if (d==3) 
//      { timeStopY--;
//        if (timeStopY < 0) timeStopY = 1;
//      }
//    }
//  }
}
