# Slime-Quest
A java-based game I started in college.
  
  I was kinda lazy, so I just uploaded my whole project folder to github.  All the code is in the src folder.
  
What you see here is the result of about 3 weeks of work in-between classes.  
It is just a private project, so there isn't really anything in the way of documentation.  
If you're analyzing the code, the classes to keep in mind are:  
foundation.GameCore - this is the class where the program begins, regardless of if it is launched as an applet or not.  
foundation.Game - this is where pretty much everything happens.  It contains pretty much all of the program's variables and contains the loop that keeps the game running from tick to tick.  
foundation.InputBlock - the purpose of this class is just to contain user input in a convenient, pollable form.  
entities.MobileEntity - this is the shared code for anything object that moves around in the game world.  Everything from the player to enemies inherits from this.  
entities.Player - this is the code for the player.  It uses InputBlock to determine what buttons are being pressed and calls methods it inherits from MobileEntity to move.  
entities.playerstates.PSGroundControl - There are countless player states so that each can override how the player behaves based on the context without having a single, ultra-complicated class.  This state is the most general.  It covers situations where the player is on the ground and in complete control of their movement.  
entities.enemies.BlockDisguise - this is the only currently implemented enemy.  The framework to make more is in place, but this is the only existing example.  It functions similarly to entities.Player, except it makes decisions internally, instead of taking the user's input.  
cells.Sector - the world in the game is made up of infinitely many sectors (more are loaded/created if the player goes to the edge of the loaded area.  When new sectors are loaded/created, the furthest ones are saved and cleared from memory).  
cells.Cell - each sector is a 2d array of cells.  Cells are blocks that the player and other MobileEntities can collide with.  A cell has three parts: cellType, blockType, and state that determine how it acts.  cellType and blockType are classes while state is just a number (although the code can allow for more, I've only used states of 0 or 1 as a boolean.  It is left as a number so that I may make states such as 2 that means twice as 'on' or 3 that means three times as 'on'.  
cells.blocktypes.BlockType - all the blockTypes inherit from this.  A block type is what the cell is made of (such as dirt, stone, air, etc).  Each block type behaves differently based on the state.  
cells.blocktypes.SolidBlock - a basic example of a blockType.  Appears in the game as a stone block.  It is solid when the state is > 0 and non-solid when the state is <= 0.  
cells.blocktypes.DeathBlock - this blocktype is a spike-ball.  When the state is > 0, it will create hitboxes that will KO the player if touched.  While the state is <= 0, it is harmless.  
cells.blocktypes.HatchBlock - this block spawns enemies when the state is > 0.  
cells.celltypes.CellType - all the cellTypes inherit from this.  A cell type determines what the cell's next state will be.  
cells.celltypes.CopyLeftCell - this cell type will make this cell simply copy the state of the cell to its left.  
cells.celltypes.AndHorzCell - this cell type will make this cell active if the cell to its left and right are both active.  Otherwise, it will be inactive.  
cell.celltypes.LifeCell - Each type of cell will determine its next state differently.  The LifeCell functions using Conway's Game of Life.  
  
There are some other classes that do unique things such as the cell type that is a button (cell.celltypes.ButtonCell) or entities.PlayerDroplet which represents the form of a player while using a power that splits them into pieces / is used in the death animation.  I believe, however, that those listed above are sufficient to fully understand the code.  In fact, it may have more than necessary.  Analyzing just one cellType and blockType should be sufficient.  
  
To see the code in action, it is in Applet form @ http://branson.altervista.org/ where the controls and other details are listed.  This page also has a downloadable jar that can be used to run the game.  
