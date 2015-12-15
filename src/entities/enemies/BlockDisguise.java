package entities.enemies;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import cells.blocktypes.HatchBlock;
import entities.Enemy;
import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class BlockDisguise extends Enemy{
	public static final int HORZ_VIEW_DISTANCE = 150;
	public static final int VERT_VIEW_DISTANCE = 50;
	public static final double DASH_SPEED = 600;
	public static final double BRAKE_SPEED = 600;
	public static final double FALL_ACCEL = 500;
	public static final double MAX_FALL_SPEED = 4000;
	public static final long DASH_DELAY = 1000000000;
	public static final int WIDTH = Cell.WIDTH;
	public static final int HEIGHT = Cell.HEIGHT;
	public static final int PANT_FRAME_COUNT = 4;
	public static final int WAKE_FRAME_COUNT = 4;
	public static final int DS_CAN_DASH = 0;
	public static final int DS_DASHING = 1;
	public static final int DS_RESTING = 2;
	public static final long FRAME_DELAY = 200000000;
	public static final long EYE_OPEN_RATE = 1000000000;
	public static final long EYE_OPEN_AMMOUNT = 500000000;
	public static BufferedImage[] wakeImgL = new BufferedImage[WAKE_FRAME_COUNT];
	public static BufferedImage[] pantImgL = new BufferedImage[PANT_FRAME_COUNT];
	public static BufferedImage dashImgL;
	public static BufferedImage[] wakeImgR = new BufferedImage[WAKE_FRAME_COUNT];
	public static BufferedImage[] pantImgR = new BufferedImage[PANT_FRAME_COUNT];
	public static BufferedImage dashImgR;
	public static long nextFrame = 0;
	public long dashFinish;
	boolean directionRight = false;
	static int pantFrame = 0;
	int dashState = 0;
	double eyeOpenness = 0;
	
	
	BufferedImage disguise;

	public BlockDisguise(double xPos, double yPos, HatchBlock hb) {
		super(xPos - WIDTH / 2, yPos - HEIGHT / 2, hb);
		width = WIDTH;
		height = HEIGHT;
	}

	public static void loadImages()
	{
			for(int i = 0; i < WAKE_FRAME_COUNT; i++)
			{
				wakeImgL[i] = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/wake_" + (i + 1) + "_L.png", "Block Disguise Image");
				wakeImgR[i] = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/wake_" + (i + 1) + "_R.png", "Block Disguise Image");
			}
			for(int i = 0; i < PANT_FRAME_COUNT; i++)
			{
				pantImgL[i] = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/pant_" + (i + 1) + "_L.png", "Block Disguise Image");
				pantImgR[i] = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/pant_" + (i + 1) + "_R.png", "Block Disguise Image");			
			}
			dashImgL = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/dash_L.png", "Block Disguise Image");
			dashImgR = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/dash_R.png", "Block Disguise Image");
			
	}
	
	public static void updateFrame(long currentTime)
	{
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + FRAME_DELAY;
		if(currentTime > nextFrame)
		{
			nextFrame += FRAME_DELAY;
			pantFrame++;
			if(pantFrame >= PANT_FRAME_COUNT)
				pantFrame = 0;
		}
	}
	
	@Override
	public void update(InputBlock ib, Game game) {
		super.update(ib, game);
		game.addEnemyHitbox(getHitbox2D());
		Player p = game.getPlayer();
		Rectangle2D.Double phb = null;
		if(p != null)
			phb = p.getHitbox2D();
		Rectangle2D.Double myhb = getHitbox2D();
		if(disguise == null && onGround(game))
		{
			
			Point2D.Double point = game.worldToCell(new Point2D.Double(myhb.x + myhb.width / 2, myhb.y + myhb.height + 1));
			Cell c = game.getCell((int)point.x, (int)point.y);
			Rectangle tempR = getHitbox();
			tempR.y++;
			Point2D.Double blockPos = game.cellToWorld(point);
			blockPos.x -= Cell.WIDTH / 2;
			blockPos.y -= Cell.HEIGHT / 2;
			if(c != null && c.getBlockType().collisionCheck(blockPos, tempR, c.getState()))
			{
			disguise = new BufferedImage(Cell.WIDTH, Cell.HEIGHT, BufferedImage.TYPE_INT_ARGB);
			c.draw(disguise.getGraphics(), 0, 0, game);
			}
		}
		if(phb != null)
		if(phb.x + phb.width > myhb.x - HORZ_VIEW_DISTANCE && phb.x < myhb.x + myhb.width + HORZ_VIEW_DISTANCE && phb.y + phb.height > myhb.y - VERT_VIEW_DISTANCE && phb.y < myhb.y + myhb.height + VERT_VIEW_DISTANCE)
		{
			if(dashState == DS_CAN_DASH || true)
			{
			if(phb.x + phb.width / 2 > myhb.x + myhb.width / 2)
				directionRight = true;
			else
				directionRight = false;
			}
			
			if(eyeOpenness < EYE_OPEN_AMMOUNT)
			{
				eyeOpenness += EYE_OPEN_RATE * ib.delta;
				eyeOpenness = Math.min(eyeOpenness, EYE_OPEN_AMMOUNT);
			}
			
			if(dashState == DS_CAN_DASH && onGround(game) && eyeOpenness >= EYE_OPEN_AMMOUNT)
			{
			

			if(phb.y + phb.height > myhb.y && phb.y < myhb.y + myhb.width)
				{
				if(dashState == 0)
					{
					dashState = DS_DASHING;
					if(directionRight)
						setXVel(DASH_SPEED);
					else
						setXVel(-1 * DASH_SPEED);
					}
				}
		}
	
		}
		else if(eyeOpenness > 0)
		{
			eyeOpenness -= EYE_OPEN_RATE * ib.delta;
			eyeOpenness = Math.max(eyeOpenness, 0);
		}
		double xVel = getXVel();
		if(xVel == 0)
		{
			if(dashState == DS_DASHING)
			{
				dashState = DS_RESTING;
				dashFinish = System.nanoTime() + DASH_DELAY;
			}
			if(dashState == DS_RESTING && System.nanoTime() > dashFinish)
			{
				dashState = 0;
			}
		}
		if(onGround(game))
		{
			if(Math.abs(xVel) < ib.delta * BRAKE_SPEED)
				setXVel(0);

			else if(xVel > 0)
				setXVel(xVel - ib.delta * BRAKE_SPEED);
			else
				setXVel(xVel + ib.delta * BRAKE_SPEED);
		}
		else
		{
			double yVel = getYVel();
			yVel += FALL_ACCEL * ib.delta;
			yVel = Math.min(yVel, MAX_FALL_SPEED);
			setYVel(yVel);
		}
		updatePos(ib, game);
	}

	@Override
	public void updateGraphics(BufferedImage build, Game game) {
		Graphics g = build.getGraphics();
		Point2D.Double p = game.worldToScreen(new Point2D.Double(getXPos(), getYPos()));
		if(disguise != null);
		g.drawImage(disguise, (int)p.x, (int)p.y, null);
		BufferedImage face = null;
		if(directionRight)
		{
		if(dashState == DS_DASHING)
			face = dashImgR;
		if(dashState == DS_RESTING)
			face = pantImgR[pantFrame];
		if(dashState == DS_CAN_DASH)
		{
			int frame = (int)(WAKE_FRAME_COUNT * (eyeOpenness / EYE_OPEN_AMMOUNT));
			if(frame == WAKE_FRAME_COUNT)
				frame--;
			face = wakeImgR[frame];
		}
		}
		else
		{
			if(dashState == DS_DASHING)
				face = dashImgL;
			if(dashState == DS_RESTING)
				face = pantImgL[pantFrame];
			if(dashState == DS_CAN_DASH)
			{
				int frame = (int)(WAKE_FRAME_COUNT * (eyeOpenness / EYE_OPEN_AMMOUNT));
				if(frame == WAKE_FRAME_COUNT)
					frame--;
				face = wakeImgL[frame];
			}
		}
		g.drawImage(face, (int)p.x, (int)p.y, null);
			
	}

	public int toInt()
	{
		return Enemy.EN_DISGUISE_BLOCK;
	}
	
}
