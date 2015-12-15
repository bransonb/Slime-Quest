package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class AlwaysSolidBlock extends BlockType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage activeImgRC;
	public static BufferedImage inactiveImgRC;
	
	public static void loadImages()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/alwayssolidblockON.png", "Always Solid Block ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/alwayssolidblockOFF.png", "Always Solid Block OFF");
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		if(state > 0)
			g.drawImage(AlwaysSolidBlock.activeImgRC, xPos, yPos, null);
		else
			g.drawImage(AlwaysSolidBlock.inactiveImgRC, xPos, yPos, null);
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		Rectangle rSelf = new Rectangle((int)myLoc.x, (int)myLoc.y, Cell.WIDTH, Cell.HEIGHT);
		return rSelf.intersects(r);
	}

	@Override
	public int toInt() {
		return Cell.ALWAYS_SOLID_BLOCK;
	}
	
}
