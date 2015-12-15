package cells.celltypes;

import foundation.Game;

public abstract class DiceCell extends CellType{
	public static int number_of_sides;
	
	public int counter = 0;

	public void singularUpdate(int sides)
	{
		counter++;
		if(counter > sides)
		{
			counter = 0;
		}
	}
	
	public short update(int x, int y, Game game, int sides) {
		if(counter == sides)
			return 1;
		else return 0;
	}
	
}
