package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class RudeSolidBlock extends BlockType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage activeImgS;
	public static BufferedImage inactiveImgS;
	public static BufferedImage waterTex;
	public static long lastTime;
	public static final double X_MOVE_RATE = 0.000000001;
	public static final double Y_MOVE_RATE = 0.00000001;
	public static double xOff = 0;
	public static double yOff = 0;
	
	public static void loadImages()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/push_solid_block_ON.png", "Rude Solid Block ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/push_solid_block_OFF.png", "Rude Solid Block OFF");
		activeImgS = Game.getGame().robustLoadImage("rec/img/blocks/push_solid_block_ON_S.png", "Rude Solid Block OFF");
		inactiveImgS = Game.getGame().robustLoadImage("rec/img/blocks/push_solid_block_OFF_S.png", "Rude Solid Block OFF");
		waterTex = Game.getGame().robustLoadImage("rec/img/blocks/water_texture.png", "Water Texture");
		
		lastTime = System.nanoTime();
	}
	
	public static void updateFrame(long currentTime)
	{
		double delta = currentTime - lastTime;
		lastTime = currentTime;
		xOff += delta * X_MOVE_RATE;
		yOff += delta * Y_MOVE_RATE;
		xOff = xOff % waterTex.getWidth();
		yOff = yOff % waterTex.getHeight();
		
	}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		BufferedImage src;
		boolean top = false;
		Game game = Game.getGame();
		Point2D.Double cellID = game.screenToWorld(new Point2D.Double(xPos + 1, yPos + 1));
		cellID = game.worldToCell(cellID);
		cellID.y = cellID.y -1;
		Cell c = game.getCell((int)(cellID.x), (int)(cellID.y));
		if(c != null)
			if(c.getBlockTypeInt() != Cell.RUDE_SOLID_BLOCK)
			top = true;
		if(top)
		{
			if(state > 0)
				src = RudeSolidBlock.activeImgS;
			else
				src = RudeSolidBlock.inactiveImgS;
		}
		else
		{
		if(state > 0)
			src = RudeSolidBlock.activeImg;
		else
			src = RudeSolidBlock.inactiveImg;
		}
		g.drawImage(src, xPos, yPos, null);
		}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{

		return false;
	}
	
	@Override
	public int toInt() {
		return Cell.RUDE_SOLID_BLOCK;
	}
}
