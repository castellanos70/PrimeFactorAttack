package PrimeFactorAttack.transition;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import PrimeFactorAttack.Game;

//Sand Traveler 
//Special commission for Sónar 2004, Barcelona
//sand painter implementation of City Traveler + complexification.net

//j.tarbell   May, 2004
//Albuquerque, New Mexico
//complexification.net

//Processing 0085 Beta syntax update
//j.tarbell   April, 2005

//Port from j.tarbell's Processing code to Java
//   Joel Castellanos April, 2011

public class SandTraveler
{
  private static final double TWO_PI = 2 * Math.PI;
  private double t;
  private int num = 200;
  private int maxnum = 201;
  private int cnt = 0;
  private double maxMove;

  private City[] cities;

  private int[] hexColor = { 0x3a242b, 0x3b2426, 0x352325, 0x836454, 0x7d5533,
      0x8b7352, 0xb1a181, 0xa4632e, 0xbb6b33, 0xb47249, 0xca7239, 0xd29057,
      0xe0b87e, 0xd9b166, 0xf5eabe, 0xfcfadf, 0xd9d1b0, 0xfcfadf, 0xd1d1ca,
      0xa7b1ac, 0x879a8c, 0x9186ad, 0x776a8e, 0x000000, 0x000000, 0x000000,
      0x000000, 0x000000, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF,
      0x000000, 0x000000, 0x000000, 0x000000, 0x000000, 0xFFFFFF, 0xFFFFFF,
      0xFFFFFF, 0xFFFFFF, 0xFFFFFF };


  private Random rand;
  private int width, height;

  public SandTraveler()
  {

    width = Game.image.getWidth();
    height = Game.image.getHeight();



    rand = new Random();

    cities = new City[maxnum];

    Game.graphTmp.setColor(Color.WHITE);
    Game.graphTmp.fillRect(0, 0, width, height);

    resetAll();
  }

  public double random(double x)
  {
    return rand.nextDouble() * x;
  }

  public double random(double a, double x)
  {
    return rand.nextDouble() * (x - a) + a;
  }

  public void update()
  {

    // System.out.println("SandTraveler.draw()");
    // move cities
    maxMove = 0;
    for (int n=0; n<5; n++)
    { for (int c = 0; c < num; c++)
      {
        cities[c].move();
      }
    }
    
    //System.out.println("maxMove="+maxMove);
    if (maxMove <= 0.05)
    // cycle limiter
    //if (cnt++ > (120 * 25))
    {
      cnt = 0;
      resetAll();
    }
  }

  private void point(double x, double y, int rgb2, double alpha)
  {
    int ix = (int) x;
    int iy = (int) y;
    if (ix < 0 || iy < 0 || ix >= width || iy >= height) return;

    int rgb1 = Game.imageTmp.getRGB(ix, iy);

    int r1 = (rgb1 & 0x00FF0000) >> 16;
    int g1 = (rgb1 & 0x0000FF00) >> 8;
    int b1 = (rgb1 & 0x000000FF);

    int r2 = (rgb2 & 0x00FF0000) >> 16;
    int g2 = (rgb2 & 0x0000FF00) >> 8;
    int b2 = (rgb2 & 0x000000FF);

    alpha = alpha / 255.0;
    if (alpha < 0) alpha = 0.0;
    else if (alpha > 1.0) alpha = 1.0;

    r1 = (int) (r1 * (1.0 - alpha) + r2 * alpha);
    g1 = (int) (g1 * (1.0 - alpha) + g2 * alpha);
    b1 = (int) (b1 * (1.0 - alpha) + b2 * alpha);
    if (r1 < 0) r1 = 0;
    if (g1 < 0) g1 = 0;
    if (b1 < 0) b1 = 0;
    if (r1 > 255) r1 = 255;
    if (g1 > 255) g1 = 255;
    if (b1 > 255) b1 = 255;
    rgb1 = r1 << 16 | g1 << 8 | b1;
    Game.imageTmp.setRGB(ix, iy, rgb1);
  }

  private void resetAll()
  {
    //System.out.println("SandTraveler.resetAll()");
    // System.out.println("  image size: " +
    // image.getWidth()+", "+image.getHeight());
    Game.graphTmp.setColor(Color.WHITE);
    Game.graphTmp.fillRect(0, 0, width, height);

    double vt = 4.2;
    double vvt = 0.2;
    double ot = random(TWO_PI);
    for (int t = 0; t < num; t++)
    {
      double tinc = ot + (1.1 - t / num) * 2 * t * TWO_PI / num;
      double vx = vt * Math.sin(tinc);
      double vy = vt * Math.cos(tinc);
      //cities[t] = new City(dim / 2 + vx * 2, dim / 2 + vy * 2, vx, vy, t);
      cities[t] = new City(width / 2 + vx * 2, height / 2 + vy * 2, vx, vy, t);
      vvt -= 0.00033;
      vt += vvt;
    }

    for (int t = 0; t < num; t++)
    {
      cities[t].findFriend();
    }

  }

//  private double citydistance(int a, int b)
//  {
//    if (a != b)
//    {
//      // calculate and return distance between cities
//      double dx = cities[b].x - cities[a].x;
//      double dy = cities[b].y - cities[a].y;
//      double d = Math.sqrt(dx * dx + dy * dy);
//      return d;
//    }
//    else
//    {
//      return 0.0;
//    }
//  }

  class City
  {

    double x, y;
    int friend;
    double vx, vy;
    int idx;
    int myc = getRandomColor();
    int lastdx, lastdy; 

    // sand painters
    int numsands = 3;
    SandPainter[] sands = new SandPainter[numsands];

    City(double Dx, double Dy, double Vx, double Vy, int Idx)
    {

      //System.out.println("SandTraveler.City(" + Dx + ", " + Dy + ", " + Vx
      //    + ", " + Vy + ", " + Idx);

      // position
      x = Dx;
      y = Dy;
      vx = Vx;
      vy = Vy;
      idx = Idx;

      // create sand painters
      for (int n = 0; n < numsands; n++)
      {
        sands[n] = new SandPainter();
      }
    }

    private void move()
    {
      vx += (cities[friend].x - x) / 1000;
      vy += (cities[friend].y - y) / 1000;

      vx *= .936;
      vy *= .936;
      
      
      x += vx;
      y += vy;
      
      //if ((Math.abs(vx) < .1) && ( Math.abs(vy) < .1))
      //System.out.println("move("+idx+"):"+Math.abs(vx) +", "+Math.abs(vy));
      
      if (Math.abs(vx) > maxMove) maxMove = Math.abs(vx);
      if (Math.abs(vy) > maxMove) maxMove = Math.abs(vy);
      

      drawTravelers();
    }

    private void findFriend()
    {
      friend = (idx + (int) (random(num / 5))) % num;
    }

    private void drawTravelers()
    {
      // System.out.println("SandTraveler.City.drawTravelers()");
      int nt = 11;
      for (int i = 0; i < nt; i++)
      {
        // pick random distance between city
        t = random(TWO_PI);
        // draw traveler
        double dx = Math.sin(t) * (x - cities[friend].x) / 2
            + (x + cities[friend].x) / 2;
        double dy = Math.sin(t) * (y - cities[friend].y) / 2
            + (y + cities[friend].y) / 2;
        

        
        if (random(1000) > 990)
        {
          // noise
          dx += random(3) - random(3);
          dy += random(3) - random(3);
        }

        point(dx, dy, cities[friend].myc, 48);
        //if (dx-last[
        
        
        // draw anti-traveler
        dx = -1 * Math.sin(t) * (x - cities[friend].x) / 2
            + (x + cities[friend].x) / 2;
        dy = -1 * Math.sin(t) * (y - cities[friend].y) / 2
            + (y + cities[friend].y) / 2;
        if (random(1000) > 990)
        {
          // noise
          dx += random(3) - random(3);
          dy += random(3) - random(3);
        }
        point(dx, dy, cities[friend].myc, 48);
      }
    }

//    private void drawSandPainters()
//    {
//      for (int s = 0; s < numsands; s++)
//      {
//        sands[s].render(x, y, cities[friend].x, cities[friend].y);
//
//      }
//    }
  }

  class SandPainter
  {

    double p;
    int c;
    double g;

    SandPainter()
    {

      p = random(1.0);
      c = getRandomColor();
      g = random(0.01, 0.1);
    }

//    private void render(double x, double y, double ox, double oy)
//    {
//      // draw painting sweeps
//      point(ox + (x - ox) * Math.sin(p), oy + (y - oy) * Math.sin(p), c, 28);
//
//      g += random(-0.050, 0.050);
//      double maxg = 0.22;
//      if (g < -maxg) g = -maxg;
//      if (g > maxg) g = maxg;
//      p += random(-0.050, 0.050);
//      if (p < 0) p = 0;
//      if (p > 1.0) p = 1.0;
//
//      double w = g / 10.0;
//      for (int i = 0; i < 11; i++)
//      {
//        double a = 0.1 - i / 110.0;
//        point(ox + (x - ox) * Math.sin(p + Math.sin(i * w)), oy + (y - oy)
//            * Math.sin(p + Math.sin(i * w)), c, a * 256);
//        point(ox + (x - ox) * Math.sin(p - Math.sin(i * w)), oy + (y - oy)
//            * Math.sin(p - Math.sin(i * w)), c, a * 256);
//
//      }
//    }

  }

  private int getRandomColor()
  {
    // pick some random good color
    // return goodcolor[int(random(goodcolor.length))];
    return hexColor[rand.nextInt(hexColor.length)];
  }



}
