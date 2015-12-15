package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class OnceOnCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;

	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/onceON.png", "Once On Cell");
	}

	public short update(int x, int y, Game game) {
		return 0;
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.ONCE_ON_CELL;
	}
}
