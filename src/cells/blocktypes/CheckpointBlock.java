package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class CheckpointBlock extends BlockType {
	public static final int FRAME_COUNT = 4;
	public static BufferedImage[] activeImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] inactiveImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] activeImgRC = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] inactiveImgRC = new BufferedImage[FRAME_COUNT];
	public static long frameDelay = 50000000;
	public static long nextFrame = 0;
	public static int frame = 0;

	public static void loadImages() {
		for(int i = 0; i < FRAME_COUNT; i++)
		{
		activeImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/respawnblockON_" + (i + 1) + ".png", "Checkpoint Block ON");
		inactiveImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/respawnblockON_" + (i + 1) + ".png", "Checkpoint Block OFF");
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

	public void setCheckpoint(Game game, double xPos, double yPos, short state)
	{
		game.setRespawnPoint(new Point2D.Double(xPos / Cell.WIDTH, yPos / Cell.HEIGHT));
		Point2D.Double p = new Point2D.Double(xPos, yPos);
		p = game.worldToCell(p);
		game.getCell((int)p.x, (int)p.y).setState(1);
	}
	
	@Override
	public void onTickAction(Game game, double xPos, double yPos, short state) {
		if(game.getRespawnPoint() != null)
		if (game.getRespawnPoint().x != xPos
				|| game.getRespawnPoint().y != yPos) {
			if (game.getPlayer() != null)
				if (game.getPlayer().getHitbox2D()
						.intersects(xPos, yPos, Cell.WIDTH, Cell.HEIGHT)) {
					setCheckpoint(game, xPos, yPos, state);
				}
		}
	}

	public void draw(Graphics g, int xPos, int yPos, short state) {
		if (state > 0)
			g.drawImage(CheckpointBlock.activeImgRC[frame], xPos, yPos, null);
		else
			g.drawImage(CheckpointBlock.inactiveImgRC[frame], xPos, yPos, null);
	}

	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state) {
		return false;
	}

	@Override
	public int toInt() {
		return Cell.CHECKPOINT_BLOCK;
	}
}
