package cells.celltypes;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class PlayerTouchCell extends CellType{
	public static BufferedImage img;
	public static BufferedImage imgRC;
	
	public static void loadImage()
	{
		img = Game.getGame().robustLoadImage("rec/img/blocks/playerTouchCell.png", "Player Touch Cell");
	}

	public short update(int x, int y, Game game) {
		Rectangle2D.Double r = new Rectangle2D.Double(game.getSectCorner().x + x * Cell.WIDTH, game.getSectCorner().y + y * Cell.HEIGHT, Cell.WIDTH, Cell.HEIGHT);
		if(game.getPlayer() == null)
			return 0;
		if(game.getPlayer().getHitbox2D().intersects(r))
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
		return Cell.PLAYER_TOUCH_CELL;
	}
}
