package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class OnceOffCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/onceOFF.png", "Once Off Cell");
	}
	
	public short update(int x, int y, Game game) {
		return 1;
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.ONCE_OFF_CELL;
	}
}
