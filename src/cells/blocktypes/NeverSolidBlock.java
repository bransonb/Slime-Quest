package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class NeverSolidBlock extends BlockType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage activeImgRC;
	public static BufferedImage inactiveImgRC;
	
	public static void loadImages()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/neversolidblockON.png", "Never Solid Block ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/neversolidblockOFF.png", "Never Solid Block OFF");
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		if(state > 0)
			g.drawImage(NeverSolidBlock.activeImgRC, xPos, yPos, null);
		else
			g.drawImage(NeverSolidBlock.inactiveImgRC, xPos, yPos, null);
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		return false;
	}
	
	@Override
	public int toInt() {
		return Cell.NEVER_SOLID_BLOCK;
	}
}
