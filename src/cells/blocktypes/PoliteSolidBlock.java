package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class PoliteSolidBlock extends BlockType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage activeImgRC;
	public static BufferedImage inactiveImgRC;
	public static BufferedImage topActiveImg;
	public static BufferedImage topInactiveImg;
	public static final int POP_FRAME_COUNT = 4;
	public static BufferedImage popImg[] = new BufferedImage[POP_FRAME_COUNT];
	public int popFrame = 0;
	public static long nextPopFrame;
	public static boolean updatePopFrame = false;
	public static final long POP_FRAME_DELAY = 50000000;
	
	public static void loadImages()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/solidBlockON.png", "Polite Solid Block ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/solidBlockOFF.png", "Polite Solid Block OFF");
		topActiveImg = Game.getGame().robustLoadImage("rec/img/blocks/solidBlockON_G.png", "Polite Solid Block ON (TOP)");
		topInactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/solidBlockOFF_G.png", "Polite Solid Block OFF (TOP)");
	}
	
	public static void updateFrame(long currentTime)
	{
		updatePopFrame = false;
		if(nextPopFrame == 0)
			nextPopFrame = System.nanoTime() + POP_FRAME_DELAY;
		if(currentTime > nextPopFrame)
		{
			updatePopFrame = true;
			nextPopFrame += POP_FRAME_DELAY;
		}
	}
	
	public void startPop()
	{
		popFrame = 1;
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		BufferedImage src;
		boolean top = false;
		if(popFrame > 0)
		{
			if(updatePopFrame)
			{
			popFrame++;
			if(popFrame >= POP_FRAME_COUNT)
				popFrame = 0;
			}
		}
		if(popFrame > 0)
			g.drawImage(PoliteSolidBlock.popImg[popFrame - 1], xPos, yPos, null);
		
		Game game = Game.getGame();
		Point2D.Double cellID = game.screenToWorld(new Point2D.Double(xPos + 1, yPos + 1));
		cellID = game.worldToCell(cellID);
		cellID.y = cellID.y -1;
		Cell c = game.getCell((int)(cellID.x), (int)(cellID.y));
		if(c != null)
			if(c.getBlockTypeInt() != Cell.POLITE_SOLID_BLOCK || (c.getState() <= 0 && state > 0))
			top = true;
		if(top)
		{
			if(state > 0)
				src = PoliteSolidBlock.topActiveImg;
			else
				src = PoliteSolidBlock.topInactiveImg;
		}
		else
		{
		if(state > 0)
			src = PoliteSolidBlock.activeImg;
		else
			src = PoliteSolidBlock.inactiveImg;
		}
		g.drawImage(src, xPos, yPos, null);
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		if(state <= 0)
			return false;
		Rectangle rSelf = new Rectangle((int)myLoc.x, (int)myLoc.y, Cell.WIDTH, Cell.HEIGHT);
		return rSelf.intersects(r);
	}
	
	@Override
	public int toInt() {
		return Cell.POLITE_SOLID_BLOCK;
	}
}
