package entities;

import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.Cell;
import cells.Sector;
import cells.blocktypes.HatchBlock;
import entities.enemies.BlockDisguise;
import foundation.Game;
import foundation.InputBlock;

public abstract class Enemy extends MobileEntity{
	public static final int NUM_OF_ENEMY_TYPES = 10;
	public static final int EN_DISGUISE_BLOCK = 1;
	HatchBlock owner;
	
	public Enemy(double xPos, double yPos, HatchBlock owner) {
		super(xPos, yPos);
		this.owner = owner;
	}

	@Override
	public void update(InputBlock ib, Game game)
	{
		if(xPos > game.getSectCorner().x + Sector.NUMBER_OF_COLS * Cell.WIDTH * Game.SECT_COUNT_X || xPos + width < game.getSectCorner().x || yPos > game.getSectCorner().y + Sector.NUMBER_OF_ROWS * Cell.HEIGHT * Game.SECT_COUNT_Y || yPos + height < game.getSectCorner().y)
				game.killEntity(this);
	}

	public void hitboxCheck(Game game, ArrayList<Shape> hitboxes)
	{
		boolean flag = false;
		for(Shape s : hitboxes)
		{
			if(s.intersects(getHitbox()))
			{
				flag = true;
				break;
			}
		}
		if(flag)
			game.killEntity(this);
	}
	
	public HatchBlock getOwner()
	{
		return owner;
	}
	
	@Override
	public abstract void updateGraphics(BufferedImage bi, Game game);

	public static Enemy makeEnemy(double xPos, double yPos, HatchBlock hb, int eType)
	{
		Enemy e = null;
		switch(eType)
		{
		case Enemy.EN_DISGUISE_BLOCK:
			e = new BlockDisguise(xPos, yPos, hb);
			break;
		default:
			break;
		}
		return e;
	}
	
}
