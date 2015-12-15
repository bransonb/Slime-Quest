package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public abstract class TimeControlBlock extends BlockType {
	public static BufferedImage fillImg;
	public static long RED_DELAY = 250000000;
	public static long ORANGE_DELAY = 500000000;
	public static long YELLOW_DELAY = 1000000000;
	public static long GREEN_DELAY = 2000000000;
	public static long BLUE_DELAY = 4 * (long) 1000000000;
	public static long PURPLE_DELAY = 8 * (long) 1000000000;

	public static void loadImages() {
		fillImg = Game.getGame().robustLoadImage("rec/img/blocks/time_fill.png", "Time Fill Img");
	}

	public void draw(Graphics g, int xPos, int yPos, short state) {
		if (state > 0) {
			double percent = Game.getGame().getTimerPercent();
			percent = 1 - percent;
			if (percent > 0.5) {
				percent -= 0.5;
				percent = 0.5 - percent;
			}
			percent = percent * 2;
			double topH = (fillImg.getHeight() / 2) * (1 - percent);
			double botH = (fillImg.getHeight() / 2) * percent;

			g.drawImage(fillImg, xPos, yPos + (Cell.HEIGHT / 2) - (int) topH,
					xPos + Cell.WIDTH, yPos + (int) (Cell.HEIGHT / 2), 0,
					(fillImg.getHeight() / 2) - (int) topH, fillImg.getWidth(),
					fillImg.getHeight() / 2, null);
			g.drawImage(fillImg, xPos, yPos + Cell.HEIGHT - (int) botH, xPos
					+ Cell.WIDTH, yPos + Cell.HEIGHT, 0, fillImg.getHeight()
					- (int) botH, fillImg.getWidth(), fillImg.getHeight(), null);
		}
	}

	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state) {
		return false;
	}

}
