package PrimeFactorAttack;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Container;  //On which to add buttons and JPanel

//Listen to button clicks
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.awt.Insets;

import PrimeFactorAttack.mandala.Mandala;
import PrimeFactorAttack.mandala.Mandala_Cassandra_Shaffer;
import PrimeFactorAttack.mandala.Mandala_Conrad_Woidyla;
import PrimeFactorAttack.mandala.Mandala_Derek_Long;
import PrimeFactorAttack.mandala.Mandala_Evan_King;
import PrimeFactorAttack.mandala.Mandala_Ezra_Stallings;
import PrimeFactorAttack.mandala.Mandala_Nick_Lauve;
import PrimeFactorAttack.mandala.Mandala_Sean_Chavez;
import PrimeFactorAttack.mandala.Mandala_Steven_Kelley;
import PrimeFactorAttack.mandala.Mandala_Tyler_Brandt;
import PrimeFactorAttack.transition.DiffusionLimitedAggregation;
import PrimeFactorAttack.transition.LevelUpScreen;
import PrimeFactorAttack.utility.SandTraveler;
import PrimeFactorAttack.transition.WelcomeScreen;
import PrimeFactorAttack.bonuslevel.BonusLevel;
import PrimeFactorAttack.utility.SoundPlayer;
import PrimeFactorAttack.bonuslevel.BonusLevel_Daniel_Gomez;
import PrimeFactorAttack.bonuslevel.BonusLevel_Jeffrey_Nichol;
import PrimeFactorAttack.bonuslevel.BonusLevel_Marcos_Lemus;
import PrimeFactorAttack.bonuslevel.BonusLevel_Colin_Monroe;
import PrimeFactorAttack.utility.Utility;


public class PrimeFactorAttack extends JFrame implements ActionListener

{
  private static final boolean DEBUG_FACTORS = false;
  private static final long serialVersionUID = 1L;

  public static final boolean DEBUG_DRAWGRID = true;
  public static final int MAX_FACTORS = 10;
  public static final Color WIGET_BACKGROUND = new Color(238, 238, 238);

  private Container contentPane;
  private GameCanvas canvas;
  private Grid grid;
  private ControlPanel control;
  private Block block;
  private ArrayList<Block> deadBlocks = new ArrayList<>();
  
  
  private Timer myTimer;


  private double probKeepSecondHardPrime;
  private double probPerfectPower;
  private boolean easyPrimeOnly;
  private Block.MODE blockMode;
  private int minNumForRemoveHits;
  private int maxPrimeIdx;
  private int maxComposite;
  private int maxFactors;
  private double blockStartSpeed;
  private int[] killHistory = new int[1001];
  
  private int skillLevel;
  //  private int skillLevelAtLastMiss;
  private final int KILLCOUNT_PER_LEVEL = 10;
  private int killCountThisLevel;
  private int killStreak = 0;
  private int killGoal = 10;
  private boolean perfectKills = true;
  private boolean usedSave = false;
  private boolean forfeitBonus = false;
  private boolean usedRewardBonusThisRound = false;
  private int lastFactor = 0;
  public boolean rewarding = false;

  private boolean lastBlockHitGround;
  
  
  private int score;
  private Data.Status gameStatus = Data.Status.READY_TO_START;
  private Random rand;
  
  private Mandala curMandala;
  private Mandala mandalaForDeadBlocks;
  
  private SandTraveler pauseScreen;
  
  private SoundPlayer soundKill, soundHit, soundMiss, soundGround, soundBang;

  private FullPanel fullPanel;
  private WelcomeScreen welcomeScreen;
  private LevelUpScreen levelUp;
  private BonusLevel bonusLevel;
  
  private DiffusionLimitedAggregation diffusionLimitedAggregation;
  
  public final static int INSIDE_WIDTH = 752;
  public final static int INSIDE_HEIGHT = 575;

  public PrimeFactorAttack() {
    System.out.println("PrimeFactorAttack()");

    this.setBounds(0, 0, INSIDE_WIDTH, INSIDE_HEIGHT);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    Insets edges = this.getInsets();
    int outsideWidth = INSIDE_WIDTH + edges.left + edges.right;
    int outsideHeight = INSIDE_HEIGHT + edges.top + edges.bottom;

    this.setBounds(0, 0, outsideWidth, outsideHeight);


    contentPane = this.getContentPane();
    contentPane.setLayout(null);
    contentPane.setBackground(WIGET_BACKGROUND);
    
    fullPanel = new FullPanel(INSIDE_WIDTH, INSIDE_HEIGHT);
    contentPane.add(fullPanel);
    fullPanel.setLocation(0, 0);
    
    welcomeScreen = new WelcomeScreen(fullPanel);
    
    
    int width = 752;
    rand = Data.rand;
    
    int controlHeight = 120;
    int drawHeight = 450;//462;
    
    //int drawWidth = Grid.GRID_PIXELS*(width/Grid.GRID_PIXELS);
    int drawWidth = 750;//726;

    
    grid = new Grid(drawWidth, drawHeight);
    
    canvas = new GameCanvas(grid, drawWidth, drawHeight);
    contentPane.add(canvas);
    int drawLeft = (width - drawWidth) / 2;
    canvas.setLocation(drawLeft, 0);
    canvas.setUp();
    
    control = new ControlPanel(this, width, controlHeight);
    contentPane.add(control);
    control.setBackground(WIGET_BACKGROUND);
    control.setLocation(0, drawHeight);
    control.updateButtons();
    this.addKeyListener(control);
    contentPane.addKeyListener(control);
    canvas.addKeyListener(control);
    
    
    SandStorm.setUp();

    pauseScreen = new SandTraveler();
    myTimer = new Timer(Data.FRAME_RATE, this);
    System.out.println("PrimeFactorAttack.init(): myTimer=" + myTimer.isRunning());
    

    try
    {
      //soundKill = new SoundPlayer(Data.resourcePath + "sounds/fireworks.wav");
      soundHit = new SoundPlayer(Data.resourcePath + "sounds/laser.wav");
      soundMiss = new SoundPlayer(Data.resourcePath + "sounds/wind.wav");
      soundGround = new SoundPlayer(Data.resourcePath + "sounds/rockHitCement.wav");
      soundKill = new SoundPlayer(Data.resourcePath + "sounds/sand.wav");
      soundBang = new SoundPlayer(Data.resourcePath + "sounds/bang.wav");
    } catch (Exception e) { //Print out an error and stack trace, but keep running.
      System.out.println("PrimeFactorAttack:: *** ERROR Creating Sound effect ****\n" +
              "     " + e.getMessage());
      e.printStackTrace();
    }
    
    //BufferedImage offscreenBuffer,  String unlock, int lev, String tit)
    diffusionLimitedAggregation = new DiffusionLimitedAggregation();
    diffusionLimitedAggregation.setup();
    
    setStatus(Data.Status.WELCOME);

  }
  
  
  public void setStatus(Data.Status newStatus)
  {

    if (newStatus == Data.Status.WELCOME)
    {
      fullPanel.setVisible(true);
      control.setVisible(false);
      canvas.setVisible(false);
      start();
    }
    
    if (newStatus == Data.Status.READY_TO_START)
    {
      fullPanel.setVisible(false);
      control.setVisible(true);
      canvas.setVisible(true);
    }
    
    if (newStatus == Data.Status.RUNNING)
    {

      fullPanel.setVisible(false);
      control.setVisible(true);
      canvas.setVisible(true);
      if ((gameStatus == Data.Status.READY_TO_START) || (gameStatus == Data.Status.ENDED))
      {
        startGame();
      }

      else if ((gameStatus == Data.Status.LEVELUP))
      {
        canvas.clearBackground(skillLevel);
        deadBlocks.clear();
        grid.clear();
      }
    }

    else if (newStatus == Data.Status.LEVELUP)
    {
      fullPanel.setVisible(true);
      control.setVisible(false);
      canvas.setVisible(false);
      
      BufferedImage buf = fullPanel.getOffscreenBuffer();
      levelUp = LevelUpScreen.create(skillLevel, buf);
    }

    else if (newStatus == Data.Status.BONUS_LEVEL)
    {
      int r = rand.nextInt(4);
      if (r == 0) bonusLevel = new BonusLevel_Colin_Monroe();
      else if (r == 1) bonusLevel = new BonusLevel_Daniel_Gomez();
      else if (r == 3) bonusLevel = new BonusLevel_Jeffrey_Nichol();
      else bonusLevel = new BonusLevel_Marcos_Lemus();

      fullPanel.setVisible(false);
      control.setVisible(false);
      canvas.setVisible(false);
      contentPane.add(bonusLevel);
      bonusLevel.setLocation(0, 0);
      bonusLevel.init();
    }

    else if (newStatus == Data.Status.ENDED)
    {
      endGame();
    }

    else if (newStatus == Data.Status.TIMESTOP)
    {
      block.moveToTop();
    }
    
    gameStatus = newStatus;

    control.updateButtons();
  }
  
  public void start()
  {
    myTimer.start();
  }

  public void stop()
  { //Pause game when user leaves the browser page.
    myTimer.stop();
  }
  
  
  private void endGame()
  {
    if (myTimer.isRunning())
    {
      myTimer.stop();
      
    }
    gameStatus = Data.Status.ENDED;
    diffusionLimitedAggregation.endGame();
  }
  
  
  private void startGame()
  {
    score = 0;
    skillLevel = 1;
    //killStreakLength = 0;
    //killStreak_ToLevelUp = 5;
    killCountThisLevel = 0;
    //skillLevelAtLastMiss = 0;
    maxFactors = 2;
    maxPrimeIdx = 4;
    maxComposite = 100;
    easyPrimeOnly = true;
    probKeepSecondHardPrime = 0.0;
    probPerfectPower = 0.0;
    minNumForRemoveHits = 0;
    blockStartSpeed = Block.SPEED_MIN;
    lastBlockHitGround = true;
    for (int i = 0; i < killHistory.length; i++)
    {
      killHistory[i] = 0;
    }
    
    //Uncomment for testing effects with large primes
//    maxPrimeIdx =  Game.PRIME.length-1;
//    for (int i=0; i<Game.PRIME.length; i++)  probability[i] = 5; 
//    maxComposite = 1000;
    
    gameStatus = Data.Status.RUNNING;
    
    deadBlocks.clear();
    grid.clear();
    control.newGame();
    control.setLevel(skillLevel, maxPrimeIdx);
    if (myTimer.isRunning())
    {
      myTimer.stop();
    }
    canvas.newGame();
    
    
    curMandala = null;
    
    createBlock();
    
    myTimer.start();
  }
  
  
  public Data.Status getGameStatus()
  {
    return gameStatus;
  }
  
  
  public int getFallingComposite()
  {
    if (block == null) return -1;
    return block.getNumber();
  }
  
  
  private void createBlock()
  {
    if (Data.showHelp)
    {
      block = new Block(grid, 15, blockStartSpeed, Block.MODE.REMOVE_HITS);
    }
    else
    {
      blockMode = Block.MODE.REMOVE_HITS;

      int num = generateCompositeNumber();
      double speed = blockStartSpeed;
      if (lastBlockHitGround) speed = Math.min(speed, Block.SPEED_VERYSLOW);
      
      if (skillLevel >= 3)
      {
        if (num <= minNumForRemoveHits)
        {
          blockMode = Block.MODE.BALLOONS;
        }
        else if (num < killHistory.length)
        {
          if (killHistory[num] >= 2) blockMode = Block.MODE.BALLOONS;
        }
      }
      block = new Block(grid, num, speed, blockMode);
    }
  }

  public boolean attack(int factor, long timeOfClick)
  {
    if (block == null) return false;
    if (curMandala != null) return false;
    if (block.getCreationTime() > timeOfClick) return false;


    if (lastFactor == 0) lastFactor = factor;
      // check to see if the user is inputting smallest to largest values
    else if (factor < lastFactor)
    {
      perfectKills = false;
      rewarding = false;
      lastFactor = factor;
    } else lastFactor = factor;
    
    boolean hit = false;
    boolean kill = false;
    
    //If the block was already in the process of being hit, then 
    //  remove the old factor before working on the new factor.
    if (block.isHit())
    {
      block.removeHitFactor();
    }


    int num = block.getNumber();

    if (num % factor == 0) {
      score += (factor * factor) * (block.getFactorCount());
      
      //System.out.println("score=" +score + ", num="+num+", factor="+factor);
      
      control.setScore(score);
      
      
      hit = true;
      if (num < killHistory.length) killHistory[num]++;
      if (num == factor)
      {
        kill = true;
        lastBlockHitGround = false;
      }
      block.setHit(factor, kill);
      
      //Must be called after block.setHit(factor, kill)
      if (kill) setMandala();


    }
    else
    { //if (block.getMode() == Block.MODE.BALLOONS) block.restoreOriginalFactors();
      block.setSpeed(Block.SPEED_DROP);
    }
    
    
    if (!SandStorm.isStormInProgress())
    {
      SandStorm.startStorm(block, factor, hit);
    }
    
    if (hit) soundHit.play();
    else soundMiss.play();

    return hit;
  }
  
  
  private void distroyBlock()
  {
    //System.out.println("PrimeFactorAttack.distroyBlock("+block+")");
    if (Data.showHelp) Data.showHelp = false;
    curMandala = null;
    block.setZapped();
    canvas.drawBlock(block);
    killCountThisLevel++;

    if (killCountThisLevel >= KILLCOUNT_PER_LEVEL)
    {
      increaseSkillLevel();
      //displayLevel();
      control.setLevel(skillLevel, maxPrimeIdx);
      //if (skillLevel - skillLevelAtLastMiss > 1) killStreak_ToLevelUp = 3;
      killCountThisLevel = 0;
    }
    createBlock();
  }

  public void cheatLevelUp()
  {
    killCountThisLevel = KILLCOUNT_PER_LEVEL;
    distroyBlock();
  }
  
  
  private void setMandala()
  {
    
    
    int[] sandColor = SandStorm.getColors();
    
    if (skillLevel < 5)
    {
      //Sand Puff
      curMandala = new Mandala_Nick_Lauve(block, sandColor);
    }
    else if (skillLevel < 10)
    { //Sand Explosion
      curMandala = new Mandala_Cassandra_Shaffer(block, sandColor);
    }
    else if (skillLevel < 15)
    { //Polar Equation mandalas with factor-fold rotational symmetry
      curMandala = new Mandala_Sean_Chavez(block, sandColor);
    }
    else if (skillLevel < 20)
    { //Paint-Ball
      curMandala = new Mandala_Derek_Long(block, sandColor);
    }
    else if (skillLevel < 25)
    { //Solar Flare
      curMandala = new Mandala_Evan_King(block, sandColor);
    }
    else if (skillLevel < 30)
    { //Blocks in a Wild Ride
      curMandala = new Mandala_Ezra_Stallings(block, sandColor);
    }
    else if (skillLevel < 35)
    { //deadly cross of pokodots
      curMandala = new Mandala_Tyler_Brandt(block, sandColor);
    }
    else if (skillLevel < 40)
    { //whirling dervish
      curMandala = new Mandala_Conrad_Woidyla(block, sandColor);
    }
    else
    { //Crossing Circles
      curMandala = new Mandala_Steven_Kelley(block, sandColor);
    }
  }
  
  private void setMandala(Block block)
  {


    int[] sandColor = SandStorm.getColors();

    if (skillLevel < 5)
    {
      //Sand Puff
      mandalaForDeadBlocks = new Mandala_Nick_Lauve(block, sandColor);

    }
    else if (skillLevel < 10) { //Sand Explosion
      mandalaForDeadBlocks = new Mandala_Cassandra_Shaffer(block, sandColor);
    }
    else if (skillLevel < 15)
    { //Polar Equation mandalas with factor-fold rotational symmetry
      mandalaForDeadBlocks = new Mandala_Sean_Chavez(block, sandColor);
    }
    else if (skillLevel < 20)
    { //Paint-Ball
      mandalaForDeadBlocks = new Mandala_Derek_Long(block, sandColor);
    }
    else if (skillLevel < 25)
    { //Solar Flare
      mandalaForDeadBlocks = new Mandala_Evan_King(block, sandColor);
    }
    else if (skillLevel < 30)
    { //Blocks in a Wild Ride
      mandalaForDeadBlocks = new Mandala_Ezra_Stallings(block, sandColor);
    }
    else if (skillLevel < 35)
    { //deadly cross of pokodots
      mandalaForDeadBlocks = new Mandala_Tyler_Brandt(block, sandColor);
    }
    else if (skillLevel < 40)
    { //whirling dervish
      mandalaForDeadBlocks = new Mandala_Conrad_Woidyla(block, sandColor);
    }
    else
    { //Crossing Circles
      mandalaForDeadBlocks = new Mandala_Steven_Kelley(block, sandColor);
    }
  }

  public int generateCompositeNumber()
  {
    if (DEBUG_FACTORS) System.out.print("-----> ");

    int factorCount = 0;
    int num = 1;
    int largestFactor = 1;
    boolean done = false;
    boolean hard = false;
    
    if (rand.nextDouble() < probPerfectPower)
    {
      num = getPowerOfPrime();
      factorCount = 2;
      blockMode = Block.MODE.BALLOONS;
      done = true;
    }
    
    while (!done)
    {
      int nextPrime = Data.PRIME[rand.nextInt(maxPrimeIdx + 1)];
      if (DEBUG_FACTORS) System.out.print(nextPrime + " ");
      
      if (easyPrimeOnly)
      {
        nextPrime = getEasyPrime();
      }
      else
      {
        if (hard)
        {
        }
        else if ((largestFactor >= 11) && (nextPrime >= 11)) hard = true;
        else if ((largestFactor >= 7) && (nextPrime >= 13)) hard = true;
        else if ((largestFactor >= 13) && (nextPrime >= 7)) hard = true;
        else if ((num % 49 == 0) && (nextPrime >= 7)) hard = true;

        if (hard)
        {
          if (rand.nextDouble() >= probKeepSecondHardPrime)
          {
            nextPrime = getEasyPrime();
            if (DEBUG_FACTORS) System.out.print("<-(" + nextPrime + ") ");
          }
        }
      }


      /////////////////////////////////////////
      if ((factorCount < 2) || (num * nextPrime <= maxComposite))
      {
        if ((largestFactor >= 11) && (nextPrime >= 11)) done = true;
        if (nextPrime > largestFactor) largestFactor = nextPrime;
        num *= nextPrime;
        factorCount++;
      }
      else
      {
        done = true;
      }


      if (factorCount >= maxFactors) done = true;
      else if (largestFactor >= 11)
      {
        if (num > 50)  //allows stop on 29*3=87, 13*7=91, 23*3=69, 17*3=51
        {
          if (rand.nextDouble() < 0.5) {
            if (DEBUG_FACTORS) System.out.print("quit ");
            done = true;
          }
        }
      }
      else if (factorCount >= 2)
      {
        if (num < killHistory.length)
        {
          if ((killHistory[num] >= 2) && (killHistory[num] < 5)) done = true;
        }
      }
      
    }
    if (DEBUG_FACTORS) System.out.println(" ==> " + num);
    return num;
  }
  
  
  private int getPowerOfPrime()
  {
    int num = Data.PRIME[rand.nextInt(maxPrimeIdx + 1)];
    if (skillLevel < 10) num = num * num;
    else if (num == 2) num = power(2, rand.nextInt(7) + 4);
    else if (num == 3) num = power(3, rand.nextInt(6) + 3);
    else if (num == 5) num = power(5, rand.nextInt(5) + 2);
    else if (num == 7) num = power(7, rand.nextInt(3) + 2);
    else num = num * num;
    if (DEBUG_FACTORS) System.out.print("power:" + num + " ");
    return num;
  }
  
  private static int power(int x, int y)
  {
    int p = 1;
    for (int i = 0; i < y; i++)
    {
      p *= x;
    }
    return p;
  }
  
  ///////////////////////////////////////////////////////////////////////////////
  private void increaseSkillLevel()
  {
    skillLevel++;
    if (skillLevel >= 10) minNumForRemoveHits = 100;

    if (skillLevel % 5 == 0)
    {
      setStatus(Data.Status.LEVELUP);
      easyPrimeOnly = false;
      usedRewardBonusThisRound = false;
      if (maxPrimeIdx < Data.PRIME.length - 1)
      {
        maxPrimeIdx++;
        System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxPrime="
                + Data.PRIME[maxPrimeIdx]);
        return;
      }
    }


    if (blockStartSpeed < Block.SPEED_NORMAL)
    {
      blockStartSpeed = Block.SPEED_NORMAL;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", blockStartSpeed="
              + blockStartSpeed);
      return;
    }
    
    if (easyPrimeOnly)
    {
      easyPrimeOnly = false;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", easyPrimeOnly=" + easyPrimeOnly);
      return;
    }
    
    if (maxFactors < 3)
    {
      maxFactors++;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxFactors="
              + maxFactors);
      return;
    }
    
    if (probPerfectPower < 0.05)
    {
      probPerfectPower = 0.08;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", probPerfectPower="
              + probPerfectPower);
      return;
    }
    
    if (blockStartSpeed < Block.SPEED_FAST)
    {
      blockStartSpeed = Block.SPEED_FAST;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", blockStartSpeed="
              + blockStartSpeed);
    }

    if (maxComposite < 250)
    {
      maxComposite += 50;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxComposite="
              + maxComposite);
      return;
    }


    if (maxFactors < 6)
    {
      maxFactors++;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxFactors="
              + maxFactors);
      return;
    }


    if (maxComposite < 1000)
    {
      maxComposite += 50;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxComposite="
              + maxComposite);
      return;
    }


    if (probKeepSecondHardPrime < .05)
    {
      probKeepSecondHardPrime = 0.05;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", probKeepSecondHardPrime="
              + probKeepSecondHardPrime);
      return;
    }


    if (maxFactors < 10)
    {
      maxFactors++;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", maxFactors="
              + maxFactors);
      return;
    }


    if (probKeepSecondHardPrime < 0.5)
    {
      probKeepSecondHardPrime += 0.05;
      System.out.println("increaseSkillLevel(): level=" + skillLevel + ", probKeepSecondHardPrime="
              + probKeepSecondHardPrime);
      return;
    }

  }

//  private void displayLevel()
//  {
//    String[] levelMsg = null;
//    int i=0;
//    if (blockmode == Block.MODE.REMOVE_HITS)
//    { levelMsg = new String[7];
//      levelMsg[i++] = "Level " + skillLevel;
//    }
//    else
//    { levelMsg = new String[8];
//      levelMsg[i++] = "Epic Level " + skillLevel;
//      levelMsg[i++] = "  Probability of easy prime: " + (int)(100.0*probEasyPrime)+"%";
//    }  
//    levelMsg[i++] = "  Maximum Prime: " + Game.PRIME[maxPrimeIdx];
//    levelMsg[i++] = "  Maximum Composite: " + maxComposite;
//    levelMsg[i++] = "  Maximum Primes per Composite: " + maxFactors;
//    levelMsg[i++] = "  Block Start Speed (pixels/sec): " + (int)(blockStartSpeed*1000)/Game.FRAME_RATE;
//    
//    levelMsg[i++] = "  Probability of Tricky Perfect Power = " + (int)(100.0*probPerfectPower)+"%";
//    levelMsg[i++] = "  Probability of Keeping a 2nd Hard Prime = " + (int)(100.0*probKeepSecondHardPrime)+"%";;
//    canvas.displayLevel(levelMsg);
//  }
  
  
  private int getEasyPrime()
  {
    int r = rand.nextInt(3);
    return Data.PRIME[r];
  }
  

  public void nextTurn()
  {
    if (block == null) return;

    canvas.copySandLayerToTempLayer();

    if (SandStorm.isStormInProgress())
    {
      boolean stillTargeting = SandStorm.update();
    }
    else {
      if (block.isFatallyHit())
      {
        block.setZapped();
      }
      if (block.isZAPPED())
      {
        if (curMandala != null)
        {
          boolean done = curMandala.update();
          if (done)
          {
            distroyBlock();
            soundKill.play();
            killStreak++;
            lastFactor = 0;
            //System.out.println("soundKill.play()");
          }
        }
      }
      else if (block.isHit()) block.removeHitFactor();
    }
    

    block.move();

    if (block.isOnGround())
    {
      blockHitBottom();
      killStreak = 0;
      perfectKills = true; // reset perfectKills
      lastFactor = 0;
    }
    
    canvas.drawBlock(block);
  }
  
  private void blockHitBottom()
  {
    lastBlockHitGround = true;

    //    killStreakLength = 0;
    //    killStreak_ToLevelUp = 10;
    //skillLevelAtLastMiss = skillLevel;
    
    int num = block.getNumber();
    if (num < killHistory.length)
    {
      killHistory[num] = 0;
      int orgNum = block.getOrgNumber();
      if (orgNum < killHistory.length) killHistory[orgNum] = 0;
    }


    int row = block.getRow();
    canvas.drawBlock(block);
    if (row == 0)
    {
      endGame();
      return;
    } else {
      int colLeft = block.getColumnLeft();
      int colRight = colLeft + block.getColumnWidth();
      for (int k = colLeft; k < colRight; k++)
      {
        grid.setFilled(k, row);
      }
      deadBlocks.add(block);
      soundMiss.stop();
      soundGround.play();
      if (rewarding) killGoal++;
      rewarding = false;
      createBlock();
    }
  }

  private void drawDeadBlocks()
  {
    for (Block b : deadBlocks)
    {
      canvas.drawBlock(b);
    }
  }
  

  private void rewardStreak()
  {
    if (!forfeitBonus)
    {
      rewarding = true;
      if (rewarding && block.isZAPPED())
      {
        soundKill.play();
        destroyLastDeadBlock();
      }
    }
  }

  private void destroyAllDeadBlocks()
  {
    if (!usedRewardBonusThisRound && killStreak >= killGoal &&
            deadBlocks.size() > 0)
    {
      soundBang.play();
      for (Block b : deadBlocks)
      {
        destroyDeadBlock(b);
      }
      grid.resetHighestRow();
      deadBlocks.clear();
      killStreak -= killGoal;
      killGoal++;
      usedRewardBonusThisRound = true;
    }
    else if (perfectKills && killStreak >= killGoal && deadBlocks.size() > 0) rewardStreak();
  }

  public void destroyLastDeadBlock()
  {
    int size = deadBlocks.size();
    if (size > 0)
    {
      if (!rewarding)
      {
        usedSave = true;
        forfeitBonus = true;
      }
      Block lastDeadBlock = deadBlocks.get(size - 1);
      destroyDeadBlock(lastDeadBlock);
      deadBlocks.remove(size - 1);
      soundKill.play();
    }
  }

  private void destroyDeadBlock(Block b)
  {
    setMandala(b);

    int row = b.getRow();

    int colLeft = b.getColumnLeft();
    int colRight = colLeft + b.getColumnWidth();

    for (int k = colLeft; k < colRight; k++)
    {
      grid.setEmpty(k, row);
    }

    if (mandalaForDeadBlocks != null)
    {
      int next = 0;
      int[] factorList = Utility.getPrimeFactors(b.getOrgNumber());
      do
      {
        if (next < factorList.length)
        {
          int factor = factorList[next];
          b.setHit(factor, true);

          if (usedSave)
          {
            score -= (factor * factor) * (block.getFactorCount());
            control.setScore(score);
          }
          else if (rewarding)
          {
            score += (factor * factor) * (block.getFactorCount());
            control.setScore(score);
          }

          b.removeHitFactor();
          next++;
        }
      }
      while (!mandalaForDeadBlocks.update());
    }
    canvas.drawBlock(b);
    if (row == grid.getHighestRow()) grid.revertToLastHighest();
    mandalaForDeadBlocks = null;
    usedSave = false;
  }


  public void actionPerformed(ActionEvent arg0)
  {
    //System.out.println("PrimeFactorAttac.actionPerformed()" + this.is);
    //System.out.println("PrimeFactorAttac.actionPerformed()");
//    long curTime = System.currentTimeMillis();
//    if (curTime-lastTimerEvent > Game.FRAME_RATE+2)
//    { System.out.println("FRAME RATE TOO SLOW BY: " + 
//        ((curTime-lastTimerEvent)-Game.FRAME_RATE) + " milliseconds");
//    }
//    lastTimerEvent = curTime;


    if (gameStatus == Data.Status.RUNNING)
    {
      //control.requestFocus();
      nextTurn();
      drawDeadBlocks();

      // if kill Goal is met, all blocks are destroyed
      destroyAllDeadBlocks();
//       destroyLastDeadBlock();
      canvas.updateDisplay();
    }

    else if (gameStatus == Data.Status.WELCOME)
    {
      boolean done = welcomeScreen.update();
      if (done) setStatus(Data.Status.READY_TO_START);
    }

    else if (gameStatus == Data.Status.READY_TO_START)
    {
      diffusionLimitedAggregation.update();
      canvas.updateDisplay();
    }

    else if (gameStatus == Data.Status.LEVELUP)
    {
      //System.out.println("......gameStatus == Game.Status.LEVELUP");
      boolean stillRunning = levelUp.update();
      if (!stillRunning)
      {
        this.setStatus(Data.Status.BONUS_LEVEL);
      }
      else fullPanel.repaint();
    }

    else if (gameStatus == Data.Status.BONUS_LEVEL && !forfeitBonus)
    {
     // System.out.println("......gameStatus == Game.Status.LEVELUP");
      boolean stillRunning = bonusLevel.nextFrame();
      if (!stillRunning) this.setStatus(Data.Status.RUNNING);
      fullPanel.repaint();
    }

    else if (forfeitBonus)
    {
      this.setStatus(Data.Status.RUNNING);
      forfeitBonus = false;
    }

    else if (gameStatus == Data.Status.TIMESTOP)
    {
      pauseScreen.update();
      canvas.updateDisplay();
    }
  }


  public static void main(String[] args)
  {
    new PrimeFactorAttack();
  }
}

