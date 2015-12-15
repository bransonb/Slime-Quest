package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class AndHorzCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/andHorz.png", "Horizontal AND");
	}

	public short update(int x, int y, Game game) {
		if(game.getCellLastState(x - 1, y) > 0 && game.getCellLastState(x + 1, y) > 0)
			return 1;
		else
			return 0;
		}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}
	

	@Override
	public int toInt() {
		return Cell.AND_HORZ_CELL;
	}
}
