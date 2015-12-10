package PrimeFactorAttack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

  private JButton butStart, butTimeStop, soundToggle;
  private JLabel labelScore, labelLevel;
  private int lastScore;
  private JButton[] primeButtons = new JButton[Data.PRIME.length];
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

    soundToggle = new JButton();
    this.add(soundToggle);
    soundToggle.addActionListener(this);
    soundToggle.setText("Sound");
    
   
    labelScore = new JLabel(Data.version);
    labelLevel = new JLabel();
    this.add(labelScore);
    this.add(labelLevel);
    
    int butStartWidth = 100;
    int butTimestopWidth = 150;
    int soundToggleWidth = 75;
    int butHeight = 30;
    int row1 = 5;
    int row2 = row1+PRIME_BUTTON_PIXELS+10;
    int butStartLeft = (width-butStartWidth)/2;
    int butTimestopLeft = butStartLeft-butTimestopWidth-10;
    int soundToggleLeft = width - soundToggleWidth - 7;
    
    butStart.setBounds(butStartLeft, row2, butStartWidth, butHeight); 
    butTimeStop.setBounds(butTimestopLeft, row2, butTimestopWidth, butHeight);
    soundToggle.setBounds(soundToggleLeft, row2, soundToggleWidth, butHeight);

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
      
      
      String str1 = "buttons/Nicholas_Antonio/" + Data.PRIME[i] + ".png";
      String str2 = "buttons/Nicholas_Antonio/" + Data.PRIME[i] + "r.png";
      

      icons[i] = new ImageIcon(Data.resourcePath + str1);
      rollIcons[i] = new ImageIcon(Data.resourcePath + str2);
      
      
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
    
    if (parent.getGameStatus() == Data.Status.RUNNING)
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
    { for(int i=0; i<primeButtons.length; i++ )
      {
        primeButtons[i].setEnabled(false);
      }
      butTimeStop.setEnabled(false);
      
      if (parent.getGameStatus() == Data.Status.READY_TO_START)
      { butStart.setText("START");
      }
      else if (parent.getGameStatus() == Data.Status.LEVELUP)
      { butStart.setText("Resume");
      }
      else if (parent.getGameStatus() == Data.Status.TIMESTOP)
      { butStart.setText("Resume");
        butTimeStop.setText("Time-Turner x"+timestopChargeCount);
      }
      else if (parent.getGameStatus() == Data.Status.ENDED)
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
    { if (parent.getGameStatus() == Data.Status.LEVELUP)
      { parent.setStatus(Data.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Data.Status.READY_TO_START)
      { 
        parent.setStatus(Data.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Data.Status.TIMESTOP)
      { 
        parent.setStatus(Data.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Data.Status.ENDED)
      { 
        parent.setStatus(Data.Status.RUNNING);
      }
      else if (parent.getGameStatus() == Data.Status.RUNNING)
      { 
        parent.setStatus(Data.Status.ENDED);
      }
    }
    
    else if (obj == butTimeStop) 
    { if (parent.getGameStatus() != Data.Status.RUNNING) return;
      if (timestopChargeCount <= 0)
      { timestopChargeCount=0;
        updateButtons();
        return;
      }
      timestopChargeCount--;
      parent.setStatus(Data.Status.TIMESTOP);
    }

    else if (obj == soundToggle)
    {
      //if (parent.getGameStatus() != Data.Status.RUNNING) return;
      if (parent.sound)
      {
        parent.sound = false;
      }
      else if(!parent.sound)
      {
        parent.sound = true;
      }
    }

    else 
    { 
      if (parent.getGameStatus() != Data.Status.RUNNING) return;
      for (int i=0; i<primeButtons.length; i++)
      {
        //System.out.println("====>("+primeButtons[i].toString());
        if (obj == primeButtons[i])
        { int factor = Data.PRIME[i];
          parent.attack(factor, timeOfClick);
        }
      }
    }
    this.requestFocus();
  }
  
  
  
  public void keyTyped(KeyEvent e) 
  {
    char c = e.getKeyChar();

    if (c == 'p')

    {
      if(parent.getGameStatus() == Data.Status.TIMESTOP)
      {
        parent.setStatus(Data.Status.RUNNING);
        return;
      }

      { if (parent.getGameStatus() != Data.Status.RUNNING) return;
        if (timestopChargeCount <= 0)
        { timestopChargeCount=0;
          updateButtons();
          return;
        }
        timestopChargeCount--;
        parent.setStatus(Data.Status.TIMESTOP);
      }
    }
    if (c == 's')
    {
      if (parent.sound)
      {
        parent.sound = false;
      }
      else if(!parent.sound)
      {
        parent.sound = true;
      }
    }

    if (parent.getGameStatus() != Data.Status.RUNNING) return;


    
    //System.out.println("ContgrolPanel.keyTyped("+c+")");
    int prime = -1;
    
    if (Data.CHEAT_ON && (c == 'n'))
    { parent.cheatLevelUp();
      return; 
    }
    if (c == 'd')
    {
      parent.rewarding = false;
      parent.destroyLastDeadBlock();
    }


    
    
    if (lastKeyPressed == ' ')
    { if (c == '2')
      { if (Data.PRIME[maxPrimeIdx] < 23) prime = 2;
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
      int idx = Data.getIndexOfPrime(prime);
      primeButtons[idx].doClick();
    }
    else
    { lastKeyPressed = c;
    }
   
    
    
  }
  public void keyPressed(KeyEvent e) {}
  public void keyReleased(KeyEvent e) {}

}


