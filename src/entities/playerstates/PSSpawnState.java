package entities.playerstates;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import entities.Player;
import entities.PlayerDroplet;
import foundation.Game;
import foundation.GameCore;
import foundation.InputBlock;

public class PSSpawnState extends DropletState {
	long floatUpTime;
	public static final long SPAWN_FLOAT_DELAY = 250000000;
	public static final double ADJUST_VEL = 1000;
	public static final double GRAVITY_MULTIPLIER = 2;
	boolean phantomMode = true;
	boolean phantomMode2 = false;
	
	
	public PSSpawnState(Player player) {
		droplets = new ArrayList<PlayerDroplet>();
	for(int i = 0; i < 15; i++)
	{
		Random r = new Random();
		double xMod = Math.min(1, r.nextGaussian());
		xMod = Math.max(-1, xMod);
		xMod = xMod * 100;
		double yMod = Math.min(5, r.nextGaussian());
		yMod = Math.max(0, yMod);
		yMod = yMod * 300;
		yMod += 600;
		droplets.add(new PlayerDroplet(player.getXPos() + (player.getWidth() / 2.0), player.getYPos() + (player.getHeight() / 2.0) - GameCore.NATIVE_Y_RES * 1.2, player.getXVel() + xMod, player.getYVel() + yMod));
	}
	for(PlayerDroplet pd : droplets)
		pd.setPhantom(true);
	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if(phantomMode2)
		{
		boolean flag = false;
		PlayerDroplet primePD = droplets.get(0);
		for(PlayerDroplet pd : droplets)
			if(!primePD.intersect(pd))
				flag = true;
		if(!primePD.intersect(player))
			flag = true;
		if(!flag)
			player.changeState(new PSGroundControl());
		}

	}
	

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		if(phantomMode)
		{
			double yP = player.getYPos() + player.getHeight() / 2;
			boolean flag = false;
			for(PlayerDroplet pd : droplets)
			{
				if(pd.getYPos() + pd.getHeight() / 2 > yP)
					pd.setPhantom(false);
				if(pd.getPhantom())
					flag = true;
				
				
			}
			if(!flag)
			{
				phantomMode = false;
				floatUpTime = System.nanoTime() + SPAWN_FLOAT_DELAY;
			}
		}
		
		
		else if(!phantomMode && !phantomMode2)
		{
			if(System.nanoTime() > floatUpTime)
			{
				phantomMode2 = true;
				for(PlayerDroplet pd : droplets)
				{
					pd.setXVel(0);
					pd.setYVel(0);
					pd.setPhantom(true);
				}
			}
		}
		else if(phantomMode2)
		{
			double playX = player.getXPos() + player.getWidth() / 2.0;
			double playY = player.getYPos() + player.getHeight() / 2.0;
			for(PlayerDroplet pd : droplets)
			{
				double yD = playY - (pd.getYPos() + pd.getHeight() / 2);
				double xD = playX - (pd.getXPos() + pd.getWidth() / 2);
				double hyp = Math.sqrt(Math.pow(yD, 2) + Math.pow(xD, 2));
				double yR = yD / hyp;
				double xR = xD / hyp;
				if(xR > 0 && pd.getXVel() < 0)
					xR = xR * GRAVITY_MULTIPLIER;
				else if(xR < 0 && pd.getXVel() > 0)
					xR = xR * GRAVITY_MULTIPLIER;
				if(yR > 0 && pd.getYVel() < 0)
					yR = yR * GRAVITY_MULTIPLIER;
				else if(yR < 0 && pd.getYVel() > 0)
					yR = yR * GRAVITY_MULTIPLIER;
				pd.adjXVel(xR * ADJUST_VEL * ib.delta);
				pd.adjYVel(yR * ADJUST_VEL * ib.delta);
				
			}
		}
	
		
		for(PlayerDroplet pd: droplets)
			pd.update(ib, game);
		player.setXVel(0);
		player.setYVel(0);


	}

	public Point2D.Double getDropletCenter()
	{
		if(!phantomMode)
			return super.getDropletCenter();
		else
			return lastDropletCenter;
	}
	
	@Override
	public void updateFrames(long currentTime, Player player) {
		
		
	}
	
	
}
