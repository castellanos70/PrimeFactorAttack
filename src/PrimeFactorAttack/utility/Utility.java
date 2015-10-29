package PrimeFactorAttack.utility;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import PrimeFactorAttack.Data;
import javax.swing.ImageIcon;

//Utility is collection of static helper methods.

public class Utility
{
  // List of all prime numbers less than 1009.
  public static final int[] PRIME =
  {  2,    3,    5,    7,   11,   13,   17,   19,   23,   29, 
    31,   37,   41,   43,   47,   53,   59,   61,   67,   71,
    73,   79,   83,   89,   97,  101,  103,  107,  109,  113,
    127,  131,  137,  139,  149,  151,  157,  163,  167,  173,
    179,  181,  191,  193,  197,  199,  211,  223,  227,  229,
    233,  239,  241,  251,  257,  263,  269,  271,  277,  281,
    283,  293,  307,  311,  313,  317,  331,  337,  347,  349,
    353,  359,  367,  373,  379,  383,  389,  397,  401,  409,
    419,  421,  431,  433,  439,  443,  449,  457,  461,  463,
    467,  479,  487,  491,  499,  503,  509,  521,  523,  541,
    547,  557,  563,  569,  571,  577,  587,  593,  599,  601,
    607,  613,  617,  619,  631,  641,  643,  647,  653,  659,
    661,  673,  677,  683,  691,  701,  709,  719,  727,  733,
    739,  743,  751,  757,  761,  769,  773,  787,  797,  809,
    811,  821,  823,  827,  829,  839,  853,  857,  859,  863,
    877,  881,  883,  887,  907,  911,  919,  929,  937,  941,
    947,  953,  967,  971,  977,  983,  991,  997
  };

  ////////////////////////////////////////////////////////////////////////
  //Loads a image file with the given path into a new bufferedImage. 
  //   Blocks until the image has finished loading.
  //   widit is the component on which the images will eventually be drawn.
  //
  //Returns a buffered image containing the loaded image.
  ////////////////////////////////////////////////////////////////////////

  public static BufferedImage loadBufferedImage(String imagePath, Component wigit)
  {

    //ImageIcon icon = new ImageIcon(Data.resourcePath + imagePath);
    //int w = icon.getIconWidth();
    //int h = icon.getIconHeight();
    //BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    
    // Create a MediaTracker instance, to montior loading of images
    MediaTracker tracker = new MediaTracker(wigit);

    // load each image and register it, 
    // using the MediaTracker.addImage (Image, int) method. 
    // It takes as its first parameter an image, 
    // and the idcode of the image as its second parameter. 
    // The idcode can be used to inquire about the status of 
    // a particular image, rather than a group of images.

    // Load the image
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image loadedImage = tk.getImage(Data.resourcePath + imagePath);

    // Register it with media tracker
    tracker.addImage(loadedImage, 1);
    try
    { tracker.waitForAll();
    }
    catch (Exception e){}
    
    int width = loadedImage.getWidth(null);
    int height = loadedImage.getHeight(null);
    BufferedImage imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = imageBuffer.getGraphics();
    g.drawImage(loadedImage, 0, 0, null);
    return imageBuffer;
  }

  
  ////////////////////////////////////////////////////////////////////////
  //  Returns true iff n is prime
  ////////////////////////////////////////////////////////////////////////
  public static boolean isPrime(long n)
  {
    if (n < 2) return false;
    
    int max = (int)Math.sqrt(n);
    for (int i=2; i<=max; i++)
    { 
      if (n % i == 0) return false;
    }
    return true;
  }
  
  

  
  
  ////////////////////////////////////////////////////////////////////////
  //Given an integer n, greater than 1
  //  Returns the number of prime factors it has.
  //  For example: if n = 20 the returned value is 3.
  ////////////////////////////////////////////////////////////////////////
  public static int countFactors(int n)
  { int factorCount=0;
    int primeIdx = 0;
    while (n>1)
    { int prime = PRIME[primeIdx];
      while (n % prime == 0) 
      { 
        factorCount++;        
        n = n / prime;
      }
      primeIdx++;
      if (primeIdx == PRIME.length)
      { throw new IllegalArgumentException("Utility.countFactors("+n+"): " +
      		"has a prime factor larger than " + PRIME[PRIME.length-1]);
      }
    }
    return factorCount;
  }
  
  ////////////////////////////////////////////////////////////////////////
  //Given an integer n, greater than 1
  //  Returns an array of its prime factors.
  //  For example: if n = 20 the returned array is {2,2,5}.
  ////////////////////////////////////////////////////////////////////////
  public static int[] getPrimeFactors(int n)
  { 
    int factorCount = countFactors(n);
    int[] list = new int[factorCount];
    
    int primeIdx = 0;
    int factorIdx = 0;
    while (n>1)
    { int prime = PRIME[primeIdx];
      while (n % prime == 0) 
      { 
        list[factorIdx] = prime;
        factorIdx++;        
        n = n / prime;
      }
      primeIdx++;
      if (primeIdx == PRIME.length)
      { throw new IllegalArgumentException("Utility.countFactors("+n+"): " +
          "has a prime factor larger than " + PRIME[PRIME.length-1]);
      }
    }
   
    return list;
  }
  
  
  ////////////////////////////////////////////////////////////////////////
  //Given an long n, greater than 1
  //  Returns an array of its prime factors.
  //  For example: if n = 20 the returned array is {2,2,5}.
  ////////////////////////////////////////////////////////////////////////
  public static long[] getPrimeFactorsWithLargePrimes(long n)
  { 
    ArrayList<Long> factorList = new ArrayList<>();
    long max = (long)Math.sqrt(n);
    System.out.println("n="+n);
    System.out.println("max="+max);
    System.out.println("max2="+max*max);
    System.out.println("n%757="+n%757);
    for (long i=2L; i<=max; i++)
    { while (n % i == 0L) 
      { 
        factorList.add(new Long(i));       
        n = n / i;
      }
      if (n==1L) break;
    }
    if (n != 1L)
    {
      factorList.add(new Long(n));
    }
    
    long[] list = new long[factorList.size()];
    for (int i=0; i<factorList.size(); i++)
    { list[i] = factorList.get(i);
    }
    return list;
  }
  
  
  
  
  ////////////////////////////////////////////////////////////////////////
  //Given an integer n, greater than 1
  //  Returns a string that contains all its factors.
  //  For example: if n = 20 the returned string is "2 2 5".
  ////////////////////////////////////////////////////////////////////////
  public static String buildFactorString(int n)
  { String str = "";
    int divisor = 2;
    while (n>1)
    { if (n % divisor == 0) 
      { 
        str += divisor;
        n = n / divisor;
        if (n>1) str+=" ";
      }
      else divisor++;
    }
    return str;
  }
  

  
  public static int polarToX(double r, double theta, int originX)
  { 
    double x = r*Math.cos(theta);
    return (int)x + originX;
  }
      

  public static int polarToY(double r, double theta, int originY)
  { 
    double y = r*Math.sin(theta);
    return (originY)-(int)y;

  }
}
