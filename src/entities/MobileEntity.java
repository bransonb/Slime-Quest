package entities;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import foundation.Game;
import foundation.InputBlock;

public abstract class MobileEntity {
	protected double xPos;
	protected double yPos;
	protected double width;
	protected double height;
	protected Rectangle2D.Double hitbox;
	protected double xVel;
	protected double yVel;

	public MobileEntity(double xPos, double yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public void setXVel(double newXVel)
	{
		xVel = newXVel;
	}
	
	public void setYVel(double newYVel)
	{
		yVel = newYVel;
	}
	
	public void teleport(double xPos, double yPos)
	{
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public double getXVel()
	{
		return xVel;
	}
	
	public double getYVel()
	{
		return yVel;
	}
	
	public Rectangle2D.Double getHitbox2D()
	{
		return new Rectangle2D.Double(xPos, yPos, width, height);
	}
	
	public boolean intersect(MobileEntity me)
	{
		return getHitbox().intersects(me.getHitbox());
	}
	
	public Rectangle getHitbox()
	{
		return new Rectangle((int) xPos, (int) yPos, (int) width,
				(int) height);
	}
	
	public void adjXVel(double change)
	{
		xVel += change;
	}
	
	public void adjYVel(double change)
	{
		yVel += change;
	}
	
	public void phantomUpdatePos(InputBlock ib, Game game)
	{
		double tXVel = (int) (xVel * ib.delta);
		double tYVel = (int) (yVel * ib.delta);
		xPos += tXVel;
		yPos += tYVel;
	}
	
	public void updatePos(InputBlock ib, Game game) {
		boolean startedOnGround = onGround(game);
		double tXVel = (int) (xVel * ib.delta);
		double tYVel = (int) (yVel * ib.delta);
		xPos += tXVel;
		yPos += tYVel;
		int most = 1;
		if (Math.abs(tXVel) > width || Math.abs(tYVel) > height) {
			if(Math.abs(tXVel) / width > Math.abs(tYVel) / height)//added
				most = (int) (Math.abs(tXVel) / width);
			else
				most = (int) (Math.abs(tYVel) / height);
			tXVel = tXVel / most;
			tYVel = tYVel / most;
		}

		for (int countToMost = 0; countToMost < most; countToMost++) {
			if(startedOnGround)
			{
				double yChange = 0;
				double yChangeMax = Math.abs(tXVel);
				while(yChange < yChangeMax && !onGround(game) && yVel >= 0)
				{
						yPos += 1;
						yChange++;
				}
			}
			if (collisionCheck(game)) {
				xPos -= tXVel;
				boolean vOK = !collisionCheck(game);
				yPos -= tYVel;
				xPos += tXVel;
				boolean hOK = !collisionCheck(game);
				xPos -= tXVel;
				
				// RAMPS
				
				boolean stairsOK = false;
				int sYVel = -1;
				if (vOK && !hOK && onGround(game)) {
					int sYVelFin = -1 * (int) Math.abs(tXVel) - 1;
					xPos += tXVel;
					while (sYVel > sYVelFin && !stairsOK) {
						yPos += sYVel;
						if (!collisionCheck(game))
							stairsOK = true;
						yPos -= sYVel;
						sYVel--;
					}
					xPos -= tXVel;
				}
				if (stairsOK) {
					yPos += sYVel + 1;
					xPos += tXVel;
				} else {
					tXVel = (int) (tXVel + 0.5);
					tYVel = (int) (tYVel + 0.5);
					xPos += tXVel;
					yPos += tYVel;
					while ((Math.abs(tXVel) > 0 || Math.abs(tYVel) > 0)
							&& collisionCheck(game)) {
						xPos -= tXVel;
						yPos -= tYVel;
						if (tXVel > 0)
							tXVel--;
						else if (tXVel < 0)
							tXVel++;
						if (tYVel > 0)
							tYVel--;
						else if (tYVel < 0)
							tYVel++;
						xPos += tXVel;
						yPos += tYVel;
					}
					if (!hOK)
						xVel = 0;
					if (!vOK)
						yVel = 0;
				}
				
			}
		}
	}

	public abstract void update(InputBlock ib, Game game);

	public boolean onGround(Game game) {
		yPos = yPos + 1;
		boolean rValue = collisionCheck(game);
		yPos = yPos - 1;
		return rValue;
	}

	public ArrayList<Integer> getCollidingBlockTypes(Game game, boolean mustBeOn)
	{
		ArrayList<Integer> a = new ArrayList<Integer>();
		Point2D.Double cctl = game.worldToCell(new Point2D.Double(xPos, yPos));
		Point2D.Double ccbr = game.worldToCell(new Point2D.Double(
				xPos + width, yPos + height));
		boolean flag = false;
		int i = (int) cctl.x;
		while (i < ccbr.x + 1 && !flag) {
			int j = (int) cctl.y;
			while (j < ccbr.y + 1 && !flag) {
				try {
					if(!mustBeOn || game.getCell(i, j).getState() > 0)
					{
					int toAdd = game.getCell(i, j).blockTypeCollisionCheck(
							game.cellToWorld(new Point2D.Double(i, j)),
							new Rectangle((int) xPos, (int) yPos, (int) width,
									(int) height));
						if(!a.contains(toAdd))
							a.add(toAdd);
					}
				} catch (NullPointerException e) {
					flag = true;
				}
				j++;
			}
			i++;
		}
		return a;
	}
	
	public boolean collisionCheck(Game game) {
		Point2D.Double cctl = game.worldToCell(new Point2D.Double(xPos, yPos));
		Point2D.Double ccbr = game.worldToCell(new Point2D.Double(
				xPos + width, yPos + height));
		boolean flag = false;
		int i = (int) cctl.x;
		while (i < ccbr.x + 1 && !flag) {
			int j = (int) cctl.y;
			while (j < ccbr.y + 1 && !flag) {
				try {
					if (game.getCell(i, j).collisionCheck(
							game.cellToWorld(new Point2D.Double(i, j)),
							new Rectangle((int) xPos, (int) yPos, (int) width,
									(int) height)))
						flag = true;
				} catch (NullPointerException e) {
					flag = true;
				}
				j++;
			}
			i++;
		}
		return flag;
	}

	public abstract void updateGraphics(BufferedImage bi, Game game);
	public double getXPos()
	{
		return xPos;
	}
	public double getYPos()
	{
		return yPos;
	}
	public double getWidth()
	{
		return width;
	}
	public double getHeight()
	{
		return height;
	}
}
