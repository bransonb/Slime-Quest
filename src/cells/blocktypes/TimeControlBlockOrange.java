package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class TimeControlBlockOrange extends TimeControlBlock{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	
	public static void loadImages()
	{

		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/time_orange.png", "Time Control Orange ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/time_orange_off.png", "Time Control Orange OFF");
	}
	@Override
	public void singularUpdate(Game game, Cell c, double x, double y)
	{
		if(c.getState() > 0 && c.getLastState() <= 0)
		{
		while(game.getFlipDelay() > ORANGE_DELAY)
			game.decrementFlipDelay();
		while(game.getFlipDelay() < ORANGE_DELAY)
			game.incrementFlipDelay();
		}
		if(c.getCellTypeInt() == Cell.ISOLATED_CELL)
			if(game.getFlipDelay() == ORANGE_DELAY)
				c.setStateSubtle(1);
			else
				c.setStateSubtle(0);
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		if(state > 0)
			g.drawImage(activeImg, xPos, yPos, null);
		else
			g.drawImage(inactiveImg, xPos, yPos, null);
		super.draw(g, xPos, yPos, state);
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		return false;
	}

	@Override
	public int toInt() {
		return Cell.TIME_BLOCK_ORANGE;
	}
	
}
