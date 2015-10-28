package PrimeFactorAttack;

import java.awt.Canvas;
import java.awt.Color;      
import java.awt.GradientPaint;
import java.awt.Graphics;   
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.util.Random;

import javax.swing.ImageIcon;


public class GameCanvas extends Canvas
{
  private static final long serialVersionUID = 1L;
  
  private Image background1, background2, background3;
  private BufferStrategy screenBuffer;
  
  private int canvasWidth, canvasHeight;
  
  private static Random rand = new Random();

  
  private Grid grid;
  private Font graphicsFont;
  private long lastTime=0;
  
  
  
  private static int[] hexColor = { 0x3a242b, 0x3b2426, 0x352325, 0x836454, 0x7d5533, 0x8b7352, 
    0xb1a181, 0xa4632e, 0xbb6b33, 0xb47249, 0xca7239, 0xd29057, 0xe0b87e, 
    0xd9b166, 0xf5eabe, 0xfcfadf, 0xd9d1b0, 0xfcfadf, 0xd1d1ca, 0xa7b1ac, 
    0x879a8c, 0x9186ad, 0x776a8e,0x776a8e,0x776a8e,0x776a8e,0x776a8e,0x3a242b,0x3a242b};

  private static Color levelMsgColor = new Color(112, 48, 160);

  public GameCanvas(Grid grid, int width, int height)
  {
    System.out.println("Draw("+width + ", " + height +") Constructor");
    this.grid = grid;
    setSize(width, height);
    
    canvasWidth = width;
    canvasHeight = height;
    
    String str1 = "background-Level1.png";
    String str2 = "background-Level2.png";
    String str3 = "background-Level3.png";
    
    background1 = new ImageIcon(Data.resourcePath + str1).getImage();
    background2 = new ImageIcon(Data.resourcePath + str2).getImage();
    background3 = new ImageIcon(Data.resourcePath + str3).getImage();
  }

  
  public void setUp()
  {

    /////////////////
    //Active Rendering
    this.setIgnoreRepaint(true);
    this.createBufferStrategy(2);
    screenBuffer = this.getBufferStrategy();

    // Get graphics configuration...
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice gd = ge.getDefaultScreenDevice();
    GraphicsConfiguration gc = gd.getDefaultConfiguration();

    // Create off-screen drawing surface
    Data.image = gc.createCompatibleImage(canvasWidth,canvasHeight, BufferedImage.TYPE_INT_RGB);
    Data.imageSand = gc.createCompatibleImage(canvasWidth,canvasHeight, BufferedImage.TYPE_INT_RGB);
    Data.imageTmp = gc.createCompatibleImage(canvasWidth,canvasHeight, BufferedImage.TYPE_INT_RGB);

    // Objects needed for rendering...
    Data.graph = Data.image.createGraphics();
    Data.graphSand = Data.imageSand.createGraphics();
    Data.graphTmp = Data.imageTmp.createGraphics();
    Data.graphScreen = screenBuffer.getDrawGraphics();

    
    graphicsFont = new Font("SansSerif",Font.BOLD, 20);
    Data.graph.setFont(graphicsFont);
    Data.graphTmp.setFont(graphicsFont);
    Data.graphSand.setFont(graphicsFont);
    Data.graphScreen.setFont(graphicsFont);
    
    Block.fontMetrics = Data.graph.getFontMetrics(graphicsFont);

    newGame();

  }


  public void newGame()
  {
    System.out.println("Draw.newGame()");
    clearBackground(1);

  }
  
  public void clearBackground(int level)
  {
    if (level < 5) 
    { Data.graph.drawImage(background1, 0, 0, null);
    }
    else if (level < 10) 
    { Data.graph.drawImage(background2, 0, 0, null);
    }
    else if (level < 15) 
    { Data.graph.drawImage(background3, 0, 0, null);
    }
    else
    { int x = canvasWidth/2;

      Color c1 = new Color (141,179,226);
      GradientPaint gradient = new GradientPaint(x, 0, c1, x, canvasHeight, Color.WHITE, false);
      Data.graph.setPaint(gradient);
      Data.graph.fillRect(0, 0, canvasWidth,canvasHeight);

    }
    Data.graph.setFont(graphicsFont);
    Data.graphSand.setFont(graphicsFont);
    Data.graphTmp.setFont(graphicsFont);
    Data.graphSand.drawImage(Data.image, 0, 0, null);
    Data.graphTmp.drawImage(Data.image, 0, 0, null);
  }
  
  


  
  
 

  public void copySandLayerToTempLayer()
  { Data.graphTmp.drawImage(Data.imageSand, 0, 0, null);
  }
  
  
  public void drawBlock(Block block)
  { 
    if (!block.isOnGround()) 
    { 
      if (Data.showHelp)
      { int x0 = block.getLeft() + block.getWidth() / 2;
        int y0 = block.getTop() + Grid.GRID_PIXELS;
        
        int n = block.getNumber();
        if ((n % 2) == 0) drawArrow(Data.graphTmp, Color.BLUE, x0, y0, 45, canvasHeight);
        if ((n % 3) == 0) drawArrow(Data.graphTmp, Color.BLUE, x0, y0, 115, canvasHeight);
        if ((n % 5) == 0) drawArrow(Data.graphTmp, Color.BLUE, x0, y0, 190, canvasHeight);
        if ((n % 7) == 0) drawArrow(Data.graphTmp, Color.BLUE, x0, y0, 262, canvasHeight);
        
        int y = (y0+canvasHeight)/2;
        String msg1 = "Click button showing a Prime Factor of the falling Composite Number.";
        int textWidth = Block.fontMetrics.stringWidth(msg1);

        Data.graphTmp.setColor(Color.WHITE);
        Data.graphTmp.fillRect(10,y-Block.fontMetrics.getMaxAscent(),textWidth,20+Block.fontMetrics.getMaxAscent());
        Data.graphTmp.setColor(Color.BLACK);
        Data.graphTmp.drawString(msg1 , 10, y);
        Data.graphTmp.drawString("-OR-   Type prime factor on keyboard." ,
            30, y+20);
      }
      block.draw(Data.graphTmp);
    }
    else 
    { block.draw(Data.graph);
      block.draw(Data.graphSand);
      Data.graphTmp.drawImage(Data.imageSand, 0, 0, null);
    }
  }
  
  public void drawlineOfRandomSand(int y)
  { for (int x=0; x<canvasWidth; x++)
    { 
      int rgb = hexColor[rand.nextInt(hexColor.length)];
      Data.imageTmp.setRGB(x, y, rgb);
    }
  }
  

  
  public void drawBox(int x, int y, int size, Color c)
  { Data.graphTmp.setColor(c);
  Data.graphTmp.fillRect(x, y, size, size);
  }
  
  
  public void drawArrow(Graphics g, Color c, int x0, int y0, int x1,int y1)
  {
    g.setColor(c);
    int deltaX = x1 - x0;
    int deltaY = y1 - y0;
    double frac = 0.1;

    g.drawLine(x0,y0,x1,y1);
    g.drawLine(x0-1,y0,x1,y1);
    g.drawLine(x0+1,y0,x1,y1);
    g.drawLine(x0-2,y0,x1,y1);
    g.drawLine(x0+2,y0,x1,y1);
    g.drawLine(x0-3,y0,x1,y1);
    g.drawLine(x0+3,y0,x1,y1);
    
    int x3 = x0 + (int)((1-frac)*deltaX + frac*deltaY);
    int y3 = y0 + (int)((1-frac)*deltaY - frac*deltaX);
    
    int x4 = x0 + (int)((1-frac)*deltaX - frac*deltaY);
    int y4 = y0 + (int)((1-frac)*deltaY + frac*deltaX);
    

    g.drawLine(x3, y3, x1, y1);
    g.drawLine(x3+1, y3, x1, y1);
    g.drawLine(x3-1, y3, x1, y1);
    
    g.drawLine(x4, y4, x1, y1);
    g.drawLine(x4+1, y4, x1, y1);
    g.drawLine(x4-1, y4, x1, y1);
    
  }
  
  
  public void displayLevel(String[] levelMsg)
  { 
    Data.graphSand.setFont(graphicsFont);
    Data.graphSand.setColor(levelMsgColor);
    for (int i=0; i<levelMsg.length; i++)
    { Data.graphSand.drawString(levelMsg[i], 10, 25+20*i);
    }
  }
  


  public void updateDisplay()
  { 
    Data.graphScreen.drawImage(Data.imageTmp, 0, 0, null );
    if( !screenBuffer.contentsLost() ) screenBuffer.show();
    else
    { System.out.println("Draw.updateDisplay() contentsLost=true");
      
      screenBuffer = this.getBufferStrategy();
      Data.graphScreen = screenBuffer.getDrawGraphics();
     
    }
    
  }
  
  

}


