package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class LifeCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/lifeCell.png", "Life Cell");
	}
	
	public short update(int x, int y, Game game)
	{
		int sum = 0;
		if(game.getCellLastState(x - 1, y - 1) > 0)
			sum++;
		if(game.getCellLastState(x - 1, y) > 0)
			sum++;
		if(game.getCellLastState(x - 1, y + 1) > 0)
			sum++;
		if(game.getCellLastState(x, y - 1) > 0)
			sum++;
		if(game.getCellLastState(x, y + 1) > 0)
			sum++;
		if(game.getCellLastState(x + 1, y - 1) > 0)
			sum++;
		if(game.getCellLastState(x + 1, y) > 0)
			sum++;
		if(game.getCellLastState(x + 1, y + 1) > 0)
			sum++;
		
		if(game.getCellLastState(x, y) > 0 && sum == 2)
			return 1;
		if(sum == 3)
			return 1;
		return 0;
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(imgRC, xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.LIFE_CELL;
	}
}
