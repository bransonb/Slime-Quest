package cells.celltypes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cells.Cell;
import foundation.Game;

public class DiceCell_6 extends DiceCell{
	public static final int SIDE_COUNT = 6;
	public static BufferedImage[] img = new BufferedImage[SIDE_COUNT + 1];
	
	public DiceCell_6()
	{
		number_of_sides = SIDE_COUNT;
	}
	
	public static void loadImages()
	{

		for(int i = 0; i <= SIDE_COUNT; i++)
			img[i] = Game.getGame().robustLoadImage("rec/img/blocks/dice_" + i + "_of_" + SIDE_COUNT + ".png", "Dice Cell");
	}
	public void singularUpdate()
	{
		singularUpdate(SIDE_COUNT);
	}
	public short update(int x, int y, Game game) {
		return update(x, y, game, SIDE_COUNT);
	}

	public void draw(Graphics g, int xPos, int yPos)
	{
			g.drawImage(img[counter], xPos, yPos, null);
	}

	@Override
	public int toInt() {
		return Cell.DICE_6_CELL;
	}
}
