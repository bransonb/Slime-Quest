package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class SplitBlock extends BlockType {
	public static final int FRAME_COUNT = 4;
	public static BufferedImage[] activeImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] inactiveImg = new BufferedImage[FRAME_COUNT];
	public static long frameDelay = 50000000;
	public static long nextFrame = 0;
	public static int frame = 0;

	public static void loadImages() {
		for (int i = 0; i < FRAME_COUNT; i++)
		{
			activeImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/splitBlockOn_" + (i + 1) + ".png", "Split Block ON");
			inactiveImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/splitBlockOff_" + (i + 1) + ".png", "Split Block OFF");
		}
	}

	public static void updateFrame(long currentTime) {
		if (nextFrame == 0)
			nextFrame = System.nanoTime() + frameDelay;
		if (currentTime > nextFrame) {
			frame++;
			if (frame >= FRAME_COUNT)
				frame = 0;
			nextFrame += frameDelay;
		}
	}

	public void draw(Graphics g, int xPos, int yPos, short state) {
		if (state > 0)
			g.drawImage(SplitBlock.activeImg[frame], xPos, yPos, null);
		else
			g.drawImage(SplitBlock.inactiveImg[frame], xPos, yPos, null);
	}

	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state) {
		return false;
	}

	@Override
	public int toInt() {
		return Cell.SPLIT_BLOCK;
	}
}
