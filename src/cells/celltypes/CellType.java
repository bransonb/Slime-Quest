package cells.celltypes;

import java.awt.Graphics;

import foundation.Game;

public abstract class CellType {

	public abstract short update(int x, int y, Game game);
	
	public abstract int toInt();
	
	public void singularUpdate()
	{}
	
	public abstract void draw(Graphics g, int xPos, int yPos);

}
