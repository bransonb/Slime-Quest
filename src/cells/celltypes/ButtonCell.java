package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class ButtonCell extends CellType{
	public static BufferedImage activeImg;
	public static BufferedImage inactiveImg;
	public static BufferedImage pushSignImg;
	boolean pushed = false;
	boolean pushable = false;
	
	public static void loadImage()
	{
		activeImg = Game.getGame().robustLoadImage("rec/img/blocks/button_on.png", "Button ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/button_off.png", "Button OFF");
		pushSignImg = Game.getGame().robustLoadImage("rec/img/blocks/button_prompt.png", "Button SIGN");
	}

	public void pushButton(Cell c)
	{
		pushed = true;
		c.setState(1);
	}
	
	public void setPushable(boolean pushable)
	{
		this.pushable = pushable;
	}
	
	public short update(int x, int y, Game game) {
		pushed = false;
		return 0;//return game.getCellLastState(x, y);
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
		if(pushed)
			g.drawImage(activeImg, xPos, yPos, null);
		else
			g.drawImage(inactiveImg, xPos, yPos, null);
		if(pushable)
		{
			double yMod = System.nanoTime() % 1000000000;
			yMod = yMod / 1000000000;
			if(yMod > 0.5)
			{
				yMod = 0.5 - (yMod - 0.5);
			}
			yMod = yMod * 16;
			g.drawImage(pushSignImg, xPos + (int)(Cell.WIDTH / 2.0 - pushSignImg.getWidth() / 2.0), (int)(yPos - Cell.HEIGHT + yMod), null);
		}
			
		pushable = false;
			
	}

	@Override
	public int toInt() {
		return Cell.BUTTON_CELL;
	}
}
