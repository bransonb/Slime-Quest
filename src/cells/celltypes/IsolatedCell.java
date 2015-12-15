package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class IsolatedCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/isolatedCell.png", "Isolated Cell");
	}
	
	public short update(int x, int y, Game game)
	{
		return game.getCellLastState(x, y);
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.ISOLATED_CELL;
	}
}
