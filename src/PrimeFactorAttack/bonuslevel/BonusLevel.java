package PrimeFactorAttack.bonuslevel;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

// PrimeFactorAttack will instantiate your class that extends BonusLevel.
public abstract class BonusLevel extends JPanel
{
  public BonusLevel()
  { //You PROBABLY want to override this. 
  }
  
  //Called whenever the user starts your bonus level.
  abstract public void init();

  //Called right after init() and every 1/50 of a second afterwards.
  //  PrimeFactorAttack stops calling this method when it returns false.
  abstract public boolean nextFrame();
  
  //Can be called anytime after init(). 
  //Will be called after nextFrame() returns false.
  //Return the current score between 0 and 100.
  abstract public int getScore(); 
}
