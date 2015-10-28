package PrimeFactorAttack.main;

import java.awt.Graphics;   
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;



public class FullPanel extends JPanel
{
  private static final long serialVersionUID = 1L;
  
  private BufferedImage offscreenBuffer;
  private Graphics2D canvas;
  
  private final int canvasWidth, canvasHeight;

  public FullPanel(int width, int height)
  {
    System.out.println("FullCanvas("+width + ", " + height +") Constructor");
    canvasWidth = width;
    canvasHeight = height;
    this.setSize(width, height);
    offscreenBuffer = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
    canvas = (Graphics2D)offscreenBuffer.getGraphics();
  }

  public Graphics2D getGraphics2D() 
  { return canvas;
  }
  
  public BufferedImage getOffscreenBuffer() 
  { return offscreenBuffer;
  }
  
  
  
  public void paint(Graphics canvas)
  { 
    canvas.drawImage(offscreenBuffer, 0,0, null);
  }
  
}


