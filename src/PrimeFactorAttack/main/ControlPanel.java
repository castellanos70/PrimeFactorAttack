package PrimeFactorAttack.main;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import PrimeFactorAttack.Game;
import PrimeFactorAttack.PrimeFactorAttack;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;



public class ControlPanel extends JPanel implements ActionListener, KeyListener
{
  
  
  private static final int PRIME_BUTTON_PIXELS = 70;
  private PrimeFactorAttack parent;

  private JButton butStart, butTimeStop;
  private JLabel labelScore, labelLevel;
  private int lastScore;
  private JButton[] primeButtons = new JButton[Game.PRIME.length];
  private ImageIcon[] icons = new ImageIcon[10];
  private ImageIcon[] rollIcons = new ImageIcon[10];
  private DecimalFormat commaFormat = new DecimalFormat("#,###,##0");
  
  private int maxPrimeIdx;
  private char lastKeyPressed;
  
  private int timestopChargeCount;
  private int timestopNextChargeScore;
  

  public ControlPanel(PrimeFactorAttack frame, int width, int height)
  {
    parent = frame;
    this.setSize(width, height);
    
    this.setLayout(null);


    butStart = new JButton("Start");
    this.add(butStart);
    butStart.addActionListener(this);
    
    butTimeStop = new JButton();
    this.add(butTimeStop);
    butTimeStop.addActionListener(this);
    
   
    labelScore = new JLabel(Game.version);
    labelLevel = new JLabel();
    this.add(labelScore);
    this.add(labelLevel);
    
    int butStartWidth = 100;
    int butTimestopWidth = 150;
    int butHeight = 30;
    int row1 = 5;
    int row2 = row1+PRIME_BUTTON_PIXELS+10;
    int butStartLeft = (width-butStartWidth)/2;
    int butTimestopLeft = butStartLeft-butTimestopWidth-10;
    
    butStart.setBounds(butStartLeft, row2, butStartWidth, butHeight); 
    butTimeStop.setBounds(butTimestopLeft, row2, butTimestopWidth, butHeight); 
    
    int scoreLeft = butStartLeft+butStartWidth+20;
    int scoreWidth = width-20-butStartLeft;
    labelScore.setBounds(scoreLeft, row2, scoreWidth, butHeight); 
    labelLevel.setBounds(10, row2, butStartWidth, butHeight); 
    
    Font buttonFont = new Font("DialogInput",Font.BOLD, 18);
    
    for (int i=0; i<primeButtons.length; i++)
    {
      primeButtons[i] = new JButton();
      this.add(primeButtons[i]);
      primeButtons[i].addActionListener(this);
      primeButtons[i].setBounds(7+i*(PRIME_BUTTON_PIXELS+4), row1, 
          PRIME_BUTTON_PIXELS, PRIME_BUTTON_PIXELS);
      //System.out.println(5+i*(PRIME_BUTTON_PIXELS+2)+PRIME_BUTTON_PIXELS);
    }
    labelScore.setFont(buttonFont);
    labelLevel.setFont(buttonFont);
    
    loadButtonImages();
    this.addKeyListener(this);
    
  }
  
  
  private void loadButtonImages()
  {    
    for( int i=0; i<primeButtons.length; i++ )
    {
      //Populate our image and rollover image arrays
      
      
      String str1 = "resources/buttons/Nicholas_Antonio/" + Game.PRIME[i] + ".png";
      String str2 = "resources/buttons/Nicholas_Antonio/" + Game.PRIME[i] + "r.png";
      
      
      //icons[i] = new ImageIcon("resources/buttons/Nicholas_Antonio/" + PrimeFactorAttack.PRIME[i] + ".png");
      //rollIcons[i] = new ImageIcon("resources/buttons/Nicholas_Antonio/" + PrimeFactorAttack.PRIME[i] + "r.png");
      
      icons[i] = new ImageIcon(Game.resource.getResource(str1));
      rollIcons[i] = new ImageIcon(Game.resource.getResource(str2));
      
      
      //Change the button icons
      primeButtons[i].setIcon(icons[i]);
      primeButtons[i].setRolloverIcon(rollIcons[i]);

      //Make sure rollovers are enabled
      primeButtons[i].setRolloverEnabled(true);
      
      //Disable the buttons by default - they will be enabled when the game starts
      primeButtons[i].setEnabled(false);
      //System.out.println("Loading Image " + i);
    }
  }
  
  public void newGame()
  { timestopChargeCount=1;
    timestopNextChargeScore=5000;
    lastScore = 0;
    setScore(0);
  }

  public void setScore(int score)
  { 
    int increase = score-lastScore;
    if (increase > 0)
    { labelScore.setText("Score: +" + increase + " = " + commaFormat.format(score));
    }
    else labelScore.setText("Score: "+score);
    lastScore = score;
    
    if (score >= timestopNextChargeScore) 
    { timestopChargeCount++;
      timestopNextChargeScore += 5000;
      butTimeStop.setText("Time-Turner x"+timestopChargeCount);
      butTimeStop.setEnabled(true);
    }
    
    labelScore.repaint();
  }
  
  public void setLevel(int skillLevel, int maxPrimeIdx)
  { labelLevel.setText("Level: " + skillLevel);
    this.maxPrimeIdx = maxPrimeIdx;
    updateButtons();
  }
  
  public void updateButtons()
  { 
    lastKeyPressed = ' ';
    
    if (parent.getGameStatus() == Game.Status.RUNNING)
    { 
      butStart.setText("End Game");
      
      for( int i=0; i<=maxPrimeIdx; i++ )
      {
        primeButtons[i].setEnabled(true);
      }
      
      butTimeStop.setText("Time-Turner x"+timestopChargeCount);
      if (timestopChargeCount > 0) butTimeStop.setEnabled(true);
      else butTimeStop.setEnabled(false);
    }
  
    else 
    { for( int i=0; i<primeButtons.length; i++ )
      {
        primeButtons[i].setEnabled(false);
      }
      butTimeStop.setEnabled(false);
      
      if (parent.getGameStatus() == Game.Status.READY_TO_START)
      { butStart.setText("START");
      }
      else if (parent.getGameStatus() == Game.Status.LEVELUP)
      { butStart.setText("Resume");
      }
      else if (parent.getGameStatus() == Game.Status.TIMESTOP)
      { butStart.setText("Resume");
        butTimeStop.setText("Time-Turner x"+timestopChargeCount);
      }
      else if (parent.getGameStatus() == Game.Status.ENDED)
      { butStart.setText("New Game");
      }
    }
  }



  public void actionPerformed(ActionEvent event)
  {
    long timeOfClick = event.getWhen();
    
    Object obj = event.getSource();
    //System.out.println("actionPerformed("+obj.toString());
    if (obj == butStart) 
    { if (parent.getGameStatus() == Game.Status.LEVELUP)
      { parent.setStatus(Game.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Game.Status.READY_TO_START) 
      { 
        parent.setStatus(Game.Status.RUNNING);   
      }
      else if (parent.getGameStatus() == Game.Status.TIMESTOP) 
      { 
        parent.setStatus(Game.Status.RUNNING);   
      }
      else if (parent.getGameStatus() == Game.Status.ENDED) 
      { 
        parent.setStatus(Game.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Game.Status.RUNNING) 
      { 
        parent.setStatus(Game.Status.ENDED);
      }
    }
    
    else if (obj == butTimeStop) 
    { if (parent.getGameStatus() != Game.Status.RUNNING) return;
      if (timestopChargeCount <= 0)
      { timestopChargeCount=0;
        updateButtons();
        return;
      }
      timestopChargeCount--;
      parent.setStatus(Game.Status.TIMESTOP);
    }

    else 
    { 
      if (parent.getGameStatus() != Game.Status.RUNNING) return;
      for (int i=0; i<primeButtons.length; i++)
      {
        //System.out.println("====>("+primeButtons[i].toString());
        if (obj == primeButtons[i])
        { int factor = Game.PRIME[i];
          parent.attack(factor, timeOfClick);
        }
      }
    }
    this.requestFocus();
  }
  
  
  
  public void keyTyped(KeyEvent e) 
  { 
    if (parent.getGameStatus() != Game.Status.RUNNING) return;
    
    char c = e.getKeyChar();
    //System.out.println("ContgrolPanel.keyTyped("+c+")");
    int prime = -1;
    
    if (Game.CHEAT_ON && (c == 'n'))
    { parent.cheatLevelUp();
      return; 
    }
    
    
    if (lastKeyPressed == ' ')
    { if (c == '2')
      { if (Game.PRIME[maxPrimeIdx] < 23) prime = 2;
        else if (parent.getFallingComposite() % 2 == 0) prime = 2;
      }
      else if (c == '3') prime = 3;
      else if (c == '5') prime = 5;
      else if (c == '7') prime = 7;
    }
    else if (lastKeyPressed == '1')
    { if (c == '1') prime = 11;
      else if (c == '3') prime = 13;
      else if (c == '7') prime = 17;
      else if (c == '9') prime = 19;
      else c = ' ';
    }
    else if (lastKeyPressed == '2')
    { if (c == '3') prime = 23;
      else if (c == '9') prime = 29;
      else c = ' ';
    }

    else c = ' ';
    
    
    if (prime > 0)
    { lastKeyPressed = ' ';
      int idx = Game.getIndexOfPrime(prime);
      primeButtons[idx].doClick();
    }
    else
    { lastKeyPressed = c;
    }
   
    
    
  }
  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e) {}

}


