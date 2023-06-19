NOTES BEFORE YOU PLAY:
- IF YOUR TEAM HAS FAINTED, DO NOT START A NEW BATTLE - GO TO THE POKEMON CENTER TO HEAL FIRST!!
- IF YOU SEE THE CONSOLE KEEPS GETTING ERRORS (PROBABLY DURING BATTLE) TRY TO STILL PLAY (HIT WASD). IF IT WORKS, YOU WILL
  SEE NORMAL TEXT SHOW UP IN THE CONSOLE - THE GAME IS STILL GOOD. HOWEVER, IF NOTHING IS WORKING, THEN YOU MUST RESTART
	- KEEP SPAMMING, USUALLY IT WILL KEEP RUNNING 
- Sometimes, it may look like your attack did not register (such as Growlithe's take down). However, the attack
  most likely missed (you can check the console text to see, it will say "Missed!") 


For this assignment, Jina and Ray were partners. Tasks were divided so that Jina was responsible
for all the graphical parts (character movement, collision detection in the town, battle sprites,
battle text, etc). Ray was responsible for all of the battle components (getting basic attacks, 
type effectiveness to work, status effects, switching Pokemon, etc). However, whenever one of us needed
help, the other one would give some assistance. 

In our initial proposal, we said that we would let the player explore the town, including the Pokemon Center
and the Poke Mart (to buy items). We also said that we would implement the EXP system and let Pokemon evolve.
Most importantly, though, our goal was to focus mainly on getting the battle system to work. This means 
determining how each Pokemon would attack, what would happen when a Pokemon faints, switching Pokemon, etc. 
In the end, we were able to accomplish most of these things! 


What we accomplished:
- Graphics
	- We were able to get the actual sprites used in the Pokemon game Pokemon Fire Red. This includes
	  the background of the town, the image of the player and all of its movement sprites, the fonts for
	  the text, the images in the battle (the part that says the name, HP, etc), and, of course,
	  the Pokemon themselves. 
	- The Pokemon have both front AND back sprites! In battle, users will be able to see the back of their
	  own Pokemon and the front of their opponents! 
	- We also got the walking and background scrolling to work! Whenever the player moves around the city, 
	  they can see the animation of their character walking around! 
	- In short, EVERYTHING YOU SEE was done beautifully by Jina!!!
	- Let's take a moment and appreciate the battle. From the text showing up, the health bar reduction,
	  the status, the FREAKING SWITCHING POKEMON (which is actually crazy), the graphics were INSANE.
	  Jina is actually a GOD oh my goodness. 

- Collision Detection
	- We were able to add the collision detection, so players cannot walk through buildings, walls, trees,
	  or off the map!

- Pokemon statistics 
	- Base stats of every Pokemon we have is the same as the real deal.
	- The type system works, with some Pokemon having multiple types!
	- These base stats affect how the Pokemon works in battle. For example, base speed stats are
	  factored in the calculation of the turn-based system, as the Pokemon with the higher speed 
	  will go first. Similarily, attack + special attack stats are factored in to determine how
	  much damage an attack will do, and defense + special defense stats are used to determine
	  how much damage will be taken. 

- Battle System
	- Turn based system: To determine who goes first, checks were done to see if priority moves
	  were being used (moves that are usually guarenteed to go first). If this was not the case,
	  the base speed stat was used, and the Pokemon with the higher speed stat will go first.
	- Damage Dealing / Damage Taking: We used the damage formula that the actual games used to 
	  determine how much damage was dealt and taken by the Pokemon. This damage is determined 
	  not only by base attack/special attack and defence/special defense stats, but also the attack 
	  power of the move, the type of the move (fire, water, grass, etc), the type of the defending
	  Pokemon (for type effectiveness), and more! 
	- Type Effectiveness: Like the actual games, some attacks are super effective against certain 
	  types while ineffective against others. This is made even more complicated when some Pokemon
	  have multiple types. We ended up only implementing 8 types, but the effectiveness works!
	- Statuses: This key component can help turn the tides of battle, and we were able to implement 
	  them. We were able to get Sleep, Paralysis, Poison, and Burn to work (which is what we proposed). 
	  These statuses have the same effects as the actual games. 

What we could not do:
- Items. It would have required more time, and the battle system as a whole was already extremely difficult.
  Adding items to work and curing statuses, healing HP, and more would have taken MUCH more time. 
- Because we did not include items, it made no sense to add a Poke Mart, which helped save us plenty of time. 
- Bag function. Since we had no items, there was also no need for a bag. This also means that in the battle 
  screen, you will not be able to interact with the bag option 
- Evolution. Unfortunately, because of how intensive graphics and battling was, we had no time to implement evolving.
- No level up. Again, not enough time. 

BUGS:
- Collision in the town. If you walk up or down RIGHT beside a wall, it will look like you are running into nothing.
  However, if you move slightly away, walking works like normal. Also, when you enter the Pokemon Center,
  pressing W will not let you move - there seems to be an invisible barrier. Clicking other movement keys will fix this.
  Sometimes (and rarely), when you go to heal your Pokemon, the music will not play. 
- In order to switch Pokemon after you faint, you need to do an attack. 
- After an enemy faints, you must attack first before it switches out 
- Screen update does not match with text, and sometimes the attack effect does not display
- If a status affects the Pokemon right before it dies, the opponent may not switch, causing the battle to not work.
- If one of your Pokemon faints and you are forced to switch and the Pokemon coming it does not have 4 moves,
  NullPointerException may be thrown, but THE GAME IS OKAY. 
- Badly Poisoned does not work
- Sometimes bugs and battle does not work properly. For example, sometimes when you win, it crashes. 
- Sometimes, when you switch into a Pokemon, attacks may not work, crashing the game.
- Moves like absorb and mega drain do not work as intended (health regeneration) 