package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class NotLeftCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/notLeftCell.png", "NOT Left Cell");
	}

	public short update(int x, int y, Game game) {
		if(game.getCellLastState(x - 1, y) > 0)
			return 0;
		else
			return 1;
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}
	

	@Override
	public int toInt() {
		return Cell.NOT_LEFT_CELL;
	}
}
