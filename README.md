# PrimeFactorAttack
Educational Game for 4th Grade Math Students written in Java

TODO (add more items or place your name on things you want to do):<br>
1) Replace the blocks (that now are simple colored rectangles) with some more interesting graphic.
    The colors must stand out form the background and the numbers on the blocks must be clearly readable.

2) Add background images to levels past 3.

3) Greatly improve transition screens.

4) Greatly improve bonus level games/add new games.  
   Jarek:  I plan to add a new bonus level (that extends BonusLevel.java) in which a player must avoid composite 
   numbers entering the screen at random locations.  The player is given ammunition consisting of the prime numbers im
   the initial number's prime factorization.  When the player shoots a composite number with a divisible prime number,
   the prime number will be shown inside of the block drawn for the composite number.  If the player successfully his 
   the composite number with all of the values in its prime factorization, the ammunition used will be dropped by the 
   composite number block.  New composite number monsters will be spawn in addition to prime number ammunition.  
   If the player hits a composite number monster with a prime number that is not divisible, the composite number will
   increase in size based on a difficulty factor.
   
   (I plan on creating a new bonus level as well. I'll probably squeeze it in directly after the first level is finished, as to give the kids an opportunity to play it. I'm not sure how far in the game they will get. The bonus level I'm thinking of doing is something similar to the Space Invaders arcade game. -Josh Rhodes)
   
   Sam: Improve fish bonus level: fix transparency issues with image display; add timer end condition rather than score, and display said timer; implement WASD movement in place of mouse

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


9) Chase: (a) When a user develops a "kill streak" and meets a "kill goal" the screen is cleared and all of the blocks are removed and explosions are left behind. Kill Goal is Incremented by one. Completed 11/9/15 6:10 P.M. Updated 11/9/15 6:33 P.M., 11/10/15 4:22 A.M. 
(b) If a user, during their "kill streak" always chooses lowest factor possible then a bonus level will load after the screen clears when the user meets a "kill goal" and the bonus level will only activate once per title screen. Completed 11/10/15 5:30 PM Updated 11/14/15 2:20 P.M.
(c) At any time the user may press "d" to destroy the last dead block added to the deadBlock ArrayList, but the user loses points equal to the maximum possible gain from the dead block that was destroyed if the user hadn't missed it in the first place and the user forfeits any opportunity for a bonus level until after the upcoming Congratulations title screen passes and the new level starts. Completed 11/14/15 2:20 P.M.
(d) I will add a sound effect for win the user meets a kill goal Completed 11/16/15 1:59 PM
(e) I will update (b) to instead just destroy all dead blocks without "making" the user play a bonus level. If they don't get a perfect streak, meaning, they don't always go from smallest factor to highest then the game will cease to produce new blocks and the user will have a time limit to destroy individual blocks that are dead on the screen, sort of like a mini game and any block destroyed from lowest to largest factor during this mini game will go towards their points, otherwise they will just be destroyed. If the user gets the primes wrong, that block is skipped and if a block below it is destroyed the blocks will fall down to fill in the space. After the mini game is over the game plays as usual.

10) Evan: I will add a game over screen so that the user does not have to press the end game button when they lose.

11) Michael: I will change the blocks into some cool sprite graphics and make it dynamic so that future developers can easily     change them for different levels and block sizes.

12) Scott: Improve instructions display at the start of the game so they are easier to read

13) Tim: Add a pause button (Press 'p')

14) Aakash: Add a Hint Button. When a player press hint button then the factor of the prime number is displayed. At the beginning there will only be one hint available. And, as the user playes the game, the hint is increased. 
