package PrimeFactorAttack;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Random;
import PrimeFactorAttack.utility.Utility;


public class Block
{
  
  //rand must be static so that a new random number generator is not created each time
  //a new block is created.
  private static Random rand = new Random();
  
  
  public enum STATUS
  { FREE_FALL, HIT, FATALLY_HIT, ONGROUND, ZAPPED
  }
  
  public enum MODE
  { REMOVE_HITS, BALLOONS
  }
  
  private static final Color BLOCK_COLOR = new Color(117,146,60); 
  private static final Color BONUSBLOCK_COLOR = new Color(101,77,143); 
  private static final Color DEAD_COLOR = new Color(170,63,60);//new Color(217,149,148);
  public static final Color DESTROYED_COLOR = new Color(255, 255, 255, 128);
  
  public static final double SPEED_VERYSLOW = 0.5;  //pixels per frame
  public static final double SPEED_SLOW = 1.0;  //pixels per frame
  public static final double SPEED_NORMAL = 2.0;  //pixels per frame
  public static final double SPEED_FAST = 3.0;  //pixels per frame
  public static final double SPEED_DROP = 24.0;  //pixels per frame
  public static final double SPEED_MIN = 0.25;//pixels per frame
  public static final double SPEED_UPBUMP = -12.0;  //pixels per frame
  public static final double UPBUMP_DELTA_SPEED = 0.75;  //pixels per frame per frame
  private double currentBaseSpeed = SPEED_NORMAL;

  
  private int origNum;
  private int num; 
  private String numStr;
  private int factorCount;
  private int[] factorList;
  private boolean[] factorBalloon;
  
  private MODE mode;
  
  private STATUS status;
  private int hitFactor;
  
  private Grid grid;
  private int left, width, height;
  private double top;
  private int colLeft, colWidth, row; 
  private double speed;
  private int textOffsetX, textOffsetY;
  private long timeCreated;
  
  public static FontMetrics fontMetrics;

  private static Color[] primeColor = 
  { new Color(0x0314aa), new Color(0x2ecbdc), new Color(0x03ae80), new Color(0x207b06), new Color(0xb2b400), 
    new Color(0xf68a03), new Color(0x9d0102), new Color(0xa90a88), new Color(0x982ceb), new Color(0x84837e) 
  };

  
  public Block(Grid grid, int num, double speed, MODE mode)
  {
    //System.out.println("Block("+num +") Constructor");
    this.grid = grid;
    this.num = num;
    origNum = num;
    this.mode = mode;
    setSpeed(speed);
    status = STATUS.FREE_FALL;
    top = 0;
    row = 0;
    height = Grid.GRID_PIXELS;

    hitFactor = 1;

    
    numStr = String.valueOf(num);

    if (mode == MODE.BALLOONS)
    { 
      //System.out.println("Block("+num +") Constructor: MODE.BALLOONS");
      factorList = Utility.getPrimeFactors(num);
      factorCount = factorList.length;
      factorBalloon = new boolean[factorCount];
      java.util.Arrays.fill(factorBalloon, false);
      //System.out.println("     factorCount="+factorCount);
    }
    else
    { factorCount = Utility.countFactors(num);
    }

    colWidth = 2*factorCount;
    width = colWidth*Grid.GRID_PIXELS;
   
    int numPixelWidth = fontMetrics.stringWidth(numStr);
    textOffsetX = (width - numPixelWidth)/2;
    
    //if (grid.getHeightestRow() < 8)
    //{ 
    //  System.out.println("Block(): Center over deep Trough");
    //  colLeft = 1+(grid.getTroughCenter()-colWidth/2);
    //}
    //else
    //{
    colLeft = rand.nextInt(grid.getColumnCount())-colWidth/2;
    //}
    
    if (colLeft < 0) colLeft = 0;
    else if (colLeft+colWidth >=grid.getColumnCount())
    { colLeft = grid.getColumnCount() - colWidth;
    }
      
    
    left = grid.getPixelLeft(colLeft);
    textOffsetY = height/2 + fontMetrics.getMaxAscent()/2 -1;
    timeCreated = System.currentTimeMillis();
  }
  
  public void setZapped()
  { status = STATUS.ZAPPED;
  }


  public long getCreationTime()
  { return this.timeCreated;
  }
  
  public void removeHitFactor()
  {
    //System.out.println("Block.removeFactor() num="+num + ", col="+colLeft);

    if (status != STATUS.HIT) return;

    speed = SPEED_UPBUMP;
    currentBaseSpeed = SPEED_NORMAL;

    num = num/hitFactor;
    
    if (mode == MODE.REMOVE_HITS)
    {
      numStr = String.valueOf(num);
      factorCount--;
    
      colLeft++;
      colWidth = 2*factorCount;
      width = colWidth*Grid.GRID_PIXELS;
      left = grid.getPixelLeft(colLeft);
    
      int numPixelWidth = fontMetrics.stringWidth(numStr);
      textOffsetX = (width - numPixelWidth)/2;
    }
    else
    { //System.out.println("Block.removeFactor() MODE.BALLOON   factorBalloon.length="+factorBalloon.length);
      for (int i=0; i<factorBalloon.length; i++)
      { //System.out.println("Block.removeFactor() factorList["+i+"]="+factorList[i]);
        if ((!factorBalloon[i]) && (factorList[i] == hitFactor))
        { 
          //System.out.println("Block.removeFactor() factorBalloon["+hitFactor+"]=true");
          factorBalloon[i] = true;
          break;
        }
      }
    }
    
    hitFactor = 1;
    status = STATUS.FREE_FALL;
  }
  
  
  public int restoreOriginalFactors()
  { if (mode == MODE.BALLOONS)
    { num = origNum;
      java.util.Arrays.fill(factorBalloon, false);
    }
    return num;
  }

  
  public int getNumber()
  { return num;
  }
  
  public int getOrgNumber()
  { return origNum;
  }
  
  
  public void moveToTop()
  {
    top = 0;
    row = 0;
  }
  

  
  public void draw(Graphics g)
  { 
    int iTop = (int)top;
    if (status == STATUS.ONGROUND) g.setColor(DEAD_COLOR);
    else if (mode ==MODE.REMOVE_HITS) g.setColor(BLOCK_COLOR);
    else g.setColor(BONUSBLOCK_COLOR);
    g.fillRect(left, iTop, width, height);
    
    g.setColor(Color.WHITE);
    g.drawString(numStr, left+textOffsetX, iTop+textOffsetY);

    //System.out.println("left="+left+", top="+top+", width="+width+", height= "+height);


    if ((status != STATUS.ONGROUND) && (mode == MODE.BALLOONS))
    { 
      
      int balloonCount = 0;
      for (int i=0; i<factorBalloon.length; i++)
      { if (factorBalloon[i])
        { 
          int x = balloonCount*60 + left;
          int factorIdx = Data.getIndexOfPrime(factorList[i]);
          
          int size = 50;
          if (factorList[i]<11) size = 40;
          g.setColor(primeColor[factorIdx]);
          g.fillOval(x,iTop-size, size,size);
          
          g.setColor(Color.WHITE);
          g.drawString(String.valueOf(factorList[i]), x+15, iTop-size/3);
          //System.out.println("drawBalloon: factor="+factorList[i]);
          balloonCount++;
        }
      }
    }
  }
  
  public int move()
  {
    if (status == STATUS.ONGROUND) return (int)top;
    if (status == STATUS.ZAPPED) return -1;

    if (speed < currentBaseSpeed) speed += UPBUMP_DELTA_SPEED;
    //System.out.println("Block.move(): speed="+speed);
    top += speed;
    
//    if (speed < SPEED_NORMAL)
//    { long curTime = System.currentTimeMillis();
//      if ((curTime - timeOfLastSlowSpeed) > Game.millsecondsOfSlow)
//      { 
//        //System.out.println("Block.move(): curTime="+curTime+", timeOfLastSlowSpeed"+timeOfLastSlowSpeed);
//        setSpeed(SPEED.NORMAL);
//      }
//    }
    
    //System.out.println("Block.move(): speed="+speed);
    
    
    boolean dead=false;
    int bottom = (int)top + Grid.GRID_PIXELS;
    row = grid.pixelToRow(bottom);
    
    int colRight = colLeft + colWidth;
    if (row == grid.getRowCount()) dead=true;
    for (int i=colLeft; i<colRight; i++)
    {  if (dead) break;
       if (!grid.isEmpty(i,row)) dead=true;
    }
    if (dead) {row--; hitGround(row);}
    return row;
  }
  
  private void hitGround(int row)
  {
    top = grid.getPixelTop(row);
    if (status == STATUS.FATALLY_HIT)
    { speed = 0;
      return;
    }
    
    status = STATUS.ONGROUND;
    if (mode == MODE.BALLOONS) num = origNum;
    numStr = Utility.buildFactorString(num);
    int numPixelWidth = fontMetrics.stringWidth(numStr);
    textOffsetX = (width - numPixelWidth)/2;
  }
  

  
  public int getFactorCount()
  { return factorCount;
  }
  
  public boolean isHit()
  { if (status == STATUS.HIT) return true;
    if (status == STATUS.FATALLY_HIT) return true;
    return false;
  }
  
  public boolean isFatallyHit()
  { 
    if (status == STATUS.FATALLY_HIT) return true;
    if (status == STATUS.ZAPPED) return true;
    return false;
  }
  
  public boolean isOnGround()
  { if (status == STATUS.ONGROUND) return true;
    return false;
  }
  
  public boolean isZAPPED()
  { if (status == STATUS.ZAPPED) return true;
    return false;
  }


  public void setFreefall()
  { status = STATUS.FREE_FALL;
  } 
  
  public void setHit(int factor, boolean kill)
  { if (kill) status = STATUS.FATALLY_HIT;
    else status = STATUS.HIT;
    
    //if (speed >= SPEED_NORMAL) setSpeed(SPEED_SLOW);
    //else setSpeed(SPEED_VERYSLOW);
    hitFactor = factor;
  }
  
  public int getHitFactor()
  { return hitFactor;
  }
  
  public int getLeft()
  { return left;
  }
  
  public int getTop()
  { return (int)top;
  }
  
  public int getWidth()
  { return width;
  }
  
  public int getHeight()
  { return height;
  }
  
  public int getColumnLeft()
  { return colLeft;
  }
  
  public int getColumnWidth()
  {
    return colWidth;
  }
  
  public int getRow()
  { return row;
  }
  
  public MODE getMode()
  { return mode;
  }

  public int[] getFactorList()
  {
    return factorList;
  }
  
  

  
  public void setSpeed(double dy)
  { 
    if (dy <= SPEED_MIN) speed = SPEED_MIN;
    else speed = dy;
  }
  
  public String toString()
  { int row = grid.pixelToRow((int)top);
    String str = "Block:[col="+colLeft+", colWidth="+colWidth+", row="+row+", num="+num+
       ", factors={"+Utility.buildFactorString(num)+"}]";
    return str;
  }
}
