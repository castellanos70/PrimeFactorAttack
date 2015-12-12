# PrimeFactorAttack
Educational Game for 4th Grade Math Students written in Java

TODO (add more items or place your name on things you want to do):<br>
1) Replace the blocks (that now are simple colored rectangles) with some more interesting graphic.
    The colors must stand out form the background and the numbers on the blocks must be clearly readable.

2) Add background images to levels past 3.
AYeong : I will add background images to level 1,2,3 differently. 

3) Greatly improve transition screens.

4) Greatly improve bonus level games/add new games.  
   Jarek:  Instead of adding a new bonus level, I plan to add some of the ideas I suggested earlier to 
   BonusLevel_DanielGomez.java.  This game contains basic shooting mechanics but the player is immobile, enemies spawn
   randomly, and they move in a predictable pattern.  I plan to improve this game by allowing the player to be moved with the
   keyboard.  I will update the enemy AI so that monsters follow the player. -DONE
      
   Sam: Improve fish bonus level: fix transparency issues with sprite display; add timer end condition rather than score, and      display said timer; implement WASD movement in place of mouse -TRANSPARENCY FIXED

5) Different button styles at different levels.                                                                              
   Josh: I plan to change the look of the buttons after every bonus level. For instance, the first 5 levels will be the black buttons at the beginning of the game then the buttons will have a different look on levels 6-10, 11-15, etc. until you reach the end of the game.

6) 1 IS NOT A PRIME!!!  A prime number is a natural
   number ***greater than 1*** that has no positive divisors other than 1 and itself! 

7) Theme shift: Many of the explosions are both nice looking and mathematical. I think
   it would be nice to add "famous mathematicians" theme that ties all the levels together.
    I would like to see a few of the main classic dudes featured
    (i.e. Euclid, Newton, Pythagoras, Archimedes, Euler, Turing, Hypatia, Ada Lovelace, Florence Nightingale)
    but with a greater number of modern
    mathemagicians (https://en.wikipedia.org/wiki/Category:21st-century_mathematicians).
    For example, the background images could be low contrast bust superimposed on abstract
    landscapes that include symbols and elements of the depicted mathematician's work.
    One level could have 5 or 6 different but related backgrounds so in one game,
    one background is shown
    and in a different game at the same level a different background is shown.

8) Different sound effects or each level. Not sound effects that you find on the web,
   but sounds that you create and record yourself.
   Jaehee: I already changed that sound when I shoot the number(original sound:fireworks.wav -> changed sound: sand.wav) 
   And I plan to make this sound as various versions sounds for each level.


9) Chase: (a) When a user develops a "kill streak" and meets a "kill goal" the screen is cleared and all of the blocks are removed and explosions are left behind. Kill Goal is Incremented by one. This only happens once per update screen. Completed 11/9/15 6:10 P.M. Updated 11/9/15 6:33 P.M., 11/10/15 4:22 A.M. 12/3/15 3:30 P.M.
(b) If a user, during their "kill streak" always chooses lowest factor possible then for every kill beyond the kill goal a dead block will be destroyed along with the falling block and the user will get points for the dead block. Completed 11/10/15 5:30 PM Updated 11/14/15 2:20 P.M. 12/3/15 3:30 P.M.
(c) At any time the user may press "d" to destroy the last dead block added to the deadBlock ArrayList, but the user loses points equal to the maximum possible gain from the dead block that was destroyed if the user hadn't missed it in the first place and the user forfeits any opportunity for a bonus level until after the upcoming Congratulations title screen passes and the new level starts. Completed 11/14/15 2:20 P.M.
(d) I will add a sound effect for win the user meets a kill goal Completed 11/16/15 1:59 PM


10) Evan: I will add a game over screen so that the user does not have to press the end game button when they lose.

11) Michael: I will change the blocks into some cool sprite graphics and make it dynamic so that future developers can easily     change them for different levels and block sizes.

12) Scott: Improve instructions display at the start of the game so they are easier to read

13) Tim: Add a pause button (Press 'p') DONE


14)Aakash: Add hint button. 

15) Soyeon An : I will change 'end game' button to 'new game' button, when the game ends. DONE

16) I will give the cursor a new design when it's in the game window. Won't be doing a bonus level anymore. -Josh Rhodes

17) I will change the blocks graphics slightly to make them easier to see against the background. - Colton Decker DONE

18) I will add a sound toggle button (on/off), and add a shortcut ('s'). DONE
