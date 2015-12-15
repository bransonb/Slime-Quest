package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class CopyLeftCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/copyLeftCell.png", "Copy Left Cell");
	}

	public short update(int x, int y, Game game) {
		return game.getCellLastState(x - 1, y);
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}
	

	@Override
	public int toInt() {
		return Cell.COPY_LEFT_CELL;
	}
}
