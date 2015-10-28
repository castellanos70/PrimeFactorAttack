package PrimeFactorAttack.mandala;

public abstract class Mandala
{
  //Classes that override this class are are used to make the explosions when
  //  a number is completed.
  //Returns false when more not finished.
  //Returns true after the last frame of the explosion.
  public boolean update()
  {
    return true;
  }
}
