package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class WaitButtonCell extends CellType{
	
	public final static int REPUSH_UPDATE_DELAY = 16;
	
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	boolean pushed = false;
	boolean pushable = false;
	int repushWaitTime = 0;
	
	public static void loadImage()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/red_button_on.png", "Wait Button ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/red_button_off.png", "Wait Button ON");
	}

	public void pushButton(Cell c)
	{
		if(repushWaitTime == 0)
		{
			repushWaitTime = REPUSH_UPDATE_DELAY;
		pushed = true;
		c.setState(1);
		}
	}
	
	public void setPushable(boolean pushable)
	{
		this.pushable = pushable;
	}
	@Override
	public void singularUpdate()
	{
		if(repushWaitTime > 0)
			repushWaitTime--;
		pushed = false;
	}
	
	public short update(int x, int y, Game game) {
		return 0;//return game.getCellLastState(x, y);
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
		if(pushed || repushWaitTime > 0)
			g.drawImage(activeImg, xPos, yPos, null);
		else
			g.drawImage(inactiveImg, xPos, yPos, null);
		if(pushable && repushWaitTime == 0)
		{
			double yMod = System.nanoTime() % 1000000000;
			yMod = yMod / 1000000000;
			if(yMod > 0.5)
			{
				yMod = 0.5 - (yMod - 0.5);
			}
			yMod = yMod * 16;
			g.drawImage(ButtonCell.pushSignImg, xPos + (int)(Cell.WIDTH / 2.0 - ButtonCell.pushSignImg.getWidth() / 2.0), (int)(yPos - Cell.HEIGHT + yMod), null);
		}
			
		pushable = false;
			
	}

	@Override
	public int toInt() {
		return Cell.WAIT_BUTTON_CELL;
	}
}
