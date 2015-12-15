package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import cells.Cell;
import foundation.Game;

public abstract class BlockType {

	public abstract void draw(Graphics g, int xPos, int yPos, short state);
	public abstract boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state);
	public abstract int toInt();
	
	public void singularUpdate(Game game, Cell c, double x, double y)
	{
		
	}
	
	public void onTickAction(Game game, double xPos, double yPos, short state)
	{
		
	}
	
}
