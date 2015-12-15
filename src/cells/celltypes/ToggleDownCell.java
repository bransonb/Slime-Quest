package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class ToggleDownCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/toggleFromDownCell.png", "Toggle Down Cell");
	}

	public short update(int x, int y, Game game) {
		short temp = game.getCellLastState(x, y);
		if(game.getCellLastState(x, y + 1) > 0)
			if(temp > 0)
				return 0;
			else
				return 1;
		else
			return temp;
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.TOGGLE_DOWN_CELL;
	}
}
