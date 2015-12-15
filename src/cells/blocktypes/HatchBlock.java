package cells.blocktypes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.Cell;
import entities.Enemy;
import entities.MobileEntity;
import foundation.Game;

public class HatchBlock extends BlockType{
	public static BufferedImage activeImg[];
	public static BufferedImage inactiveImg;
	public static final int OPEN_FRAME_COUNT = 5;
	public static final int FRAME_DELAY = 25000000;
	public static int frame;
	static boolean reached = false;
	public static long nextFrame;
	boolean spawned;
	boolean freshSpawn = false;
	public int eType = 0;
	public static int SPAWN_LIMIT = 1;
	ArrayList<MobileEntity> spawns = new ArrayList<MobileEntity>();
	
	public HatchBlock()
	{
		spawned = true;
	}
	
	public HatchBlock(boolean freshSpawn)
	{
		this.freshSpawn = freshSpawn;
	}
	
	public void setEType(int eType)
	{
		this.eType = eType;
	}
	
	public static void loadImages()
	{
		activeImg = new BufferedImage[OPEN_FRAME_COUNT];
		for(int i = 0; i < OPEN_FRAME_COUNT; i++)
			activeImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/hatchON_" + (i + 1) + ".png", "Hatch ON");
		inactiveImg = Game.getGame().robustLoadImage("rec/img/blocks/hatchOFF.png", "Hatch OFF");
	}
	
	public void killEntity(MobileEntity me)
	{
		spawns.remove(me);
	}
	
	public static void updateFrame(long currentTime)
	{
		if(currentTime > nextFrame)
		{
			if(!reached)
				{
				frame++;
				if(frame > OPEN_FRAME_COUNT - 1)
				{
					reached = true;
					nextFrame = nextFrame + FRAME_DELAY * 2;
					frame--;
				}
				}
			else if(frame > 0)
					frame--;
			nextFrame = nextFrame + FRAME_DELAY;
		}
	}
	
	public void setFreshSpawn(boolean b)
	{
		freshSpawn = b;
	}
	
	@Override
	public void singularUpdate(Game game, Cell c, double x, double y)
	{
		if(c.getState() > 0)
		{
			reached = false;
			spawned = false;
		}
	}
	
	
	@Override
	public void onTickAction(Game game, double xPos, double yPos, short state)
	{

		if(((freshSpawn) ||(reached && !spawned)) && spawns.size() < SPAWN_LIMIT)
		{
			freshSpawn = false;
			MobileEntity me = game.makeEnemy(xPos + Cell.WIDTH / 2, yPos + Cell.HEIGHT / 2, this, eType);
			if(me != null)
			{
			game.addEnemy((Enemy)me);
			spawns.add(me);
			}
			spawned = true;
		}
				}
	
	public void draw(Graphics g, int xPos, int yPos, short state)
	{
		if(state > 0)
		{
			g.drawImage(HatchBlock.activeImg[frame], xPos, yPos, null);
		}
		else
			g.drawImage(HatchBlock.inactiveImg, xPos, yPos, null);
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r, short state)
	{
		return false;
	}
	
	@Override
	public int toInt() {
		return Cell.HATCH_BLOCK;
	}
}
