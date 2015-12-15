package PrimeFactorAttack.transition;
import java.awt.image.BufferedImage;


public abstract class LevelUpScreen
{
  
  public LevelUpScreen()
  {
    
    
  }
  public LevelUpScreen(BufferedImage offscreenBuffer, String unlockStr, String title)
  {
    
    
  }
  
  
  public static LevelUpScreen create(int level, BufferedImage buf)
  { 
    LevelUpScreen levelUp;
    if (level == 5)
    {
      levelUp = new LevelUpScreen_Luke_Stankus(buf,
          "You Have Reached Level 5", "Exploding Sands");
    }
    else if (level == 10)
    {
      levelUp = new LevelUpScreen_Jeff_Richards(buf, 
          "You Have Reached Level 10", "Mayan Sand Flowers");
    }
    
    else if (level == 15)
    {
      levelUp = new LevelUpScreen_Micah_McNeil(buf, 
          "Unlocked 19", level, "Next Level: Paint-Ball");
    }
    else if (level == -1)
    {
      levelUp = new EndGameScreen_Evan_Pierce(buf,
          "GAME OVER", "Try Again");
    }
    else //if (level == 20)
    {
      levelUp = new LevelUpScreen_Ben_Mixon_Baca(buf, 
          "Unlocked 23", level, "Next Level: Solar Flare");
    }
    
     

//    levelMsg.add(new String[] {"Unlocked 29", "Level 25:", "Mr. Block's Wild Ride"});
//    levelMsg.add(new String[] {"Epic Level 30:", "Deadly Cross of Pokodots"});
//    levelMsg.add(new String[] {"Epic Level 35:", "Whirling Dervish"});
//    levelMsg.add(new String[] {"Epic Level 40:", "Crossing Circles"});

//    levelIdx=-1;
//    setLevelScreen();
    return levelUp;
    
  }
  
  //Returns false when the level is done.
  //Otherwise returns true.
  public boolean update()
  { 
    return false;
  }
  
  
}

