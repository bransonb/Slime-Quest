package cells.celltypes;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class PlayerTouchNotCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/playerTouchCellNOT.png", "Player Touch NOT Cell");
	}

	public short update(int x, int y, Game game) {
		Rectangle2D.Double r = new Rectangle2D.Double(game.getSectCorner().x + x * Cell.WIDTH, game.getSectCorner().y + y * Cell.HEIGHT, Cell.WIDTH, Cell.HEIGHT);
		if(game.getPlayer() == null)
			return 1;
		if(game.getPlayer().getHitbox2D().intersects(r))
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
		return Cell.PLAYER_TOUCH_NOT_CELL;
	}
}
