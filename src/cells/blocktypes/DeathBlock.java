package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class DeathBlock extends BlockType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage activeImgRC;
	public static BufferedImage inactiveImgRC;
	public static final Rectangle2D.Double HITBOX_OFFSET = new Rectangle2D.Double(2, 2, 28, 28);
	
	public static void loadImages()
	{		
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/death_block_OFF.png", "Death Block ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/death_block_OFF.png", "Death Block OFF");
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		
		if(state > 0)
			g.drawImage(DeathBlock.activeImg, xPos, yPos, null);
		else
			g.drawImage(DeathBlock.inactiveImg, xPos, yPos, null);
	}
	
	public void onTickAction(Game game, double xPos, double yPos, short state)
	{
		if(state > 0)
			game.addEnemyHitbox(new Rectangle2D.Double(xPos + HITBOX_OFFSET.x, yPos + HITBOX_OFFSET.y, HITBOX_OFFSET.width, HITBOX_OFFSET.height));
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		return false;
	}
	
	@Override
	public int toInt() {
		return Cell.DEATH_BLOCK;
	}
}
