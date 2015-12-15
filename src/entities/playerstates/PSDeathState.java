package entities.playerstates;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import entities.Player;
import entities.PlayerDroplet;
import foundation.Game;
import foundation.InputBlock;

public class PSDeathState extends DropletState {
	long floatUpTime;
	public static final long POP_FLOAT_DELAY = 2000000000;
	boolean phantomMode = false;
	
	
	public PSDeathState(Player player) {
		floatUpTime = System.nanoTime() + POP_FLOAT_DELAY;
		droplets = new ArrayList<PlayerDroplet>();
	for(int i = 0; i < 15; i++)
	{
		Random r = new Random();
		double xMod = Math.min(5, r.nextGaussian());
		xMod = Math.max(-5, xMod);
		xMod = xMod * 100;
		double yMod = Math.min(5, r.nextGaussian());
		yMod = Math.max(-5, yMod);
		yMod = yMod * 100;
		
		droplets.add(new PlayerDroplet(player.getXPos() + (player.getWidth() / 2.0), player.getYPos() + (player.getHeight() / 2.0), player.getXVel() + xMod, player.getYVel() + yMod));
	}
	}

	public PSDeathState(DropletState ds)
	{
		floatUpTime = System.nanoTime() + POP_FLOAT_DELAY;
		droplets = ds.droplets;
		for(PlayerDroplet pd : droplets)
		{
			Random r = new Random();
			double xMod = Math.min(5, r.nextGaussian());
			xMod = Math.max(-5, xMod);
			xMod = xMod * 100;
			double yMod = Math.min(5, r.nextGaussian());
			yMod = Math.max(-5, yMod);
			yMod = yMod * 100 - 250;
			pd.setXVel(xMod);
			pd.setYVel(yMod);
			pd.setDrawArrows(false);
		}
	}
	
	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		boolean flag = false;
		for(PlayerDroplet pd : droplets)
			if(pd.getYPos() > game.getSectCorner().y - PlayerDroplet.HEIGHT)
				flag = true;
		if(!flag)
			game.killEntity(player);

	}
	

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		if(!phantomMode && System.nanoTime() > floatUpTime)
		{
			phantomMode = true;
			for(PlayerDroplet pd: droplets)
			{
				double yv = Math.random() * 500 + 600;
				yv = yv * -1;
				pd.setYVel(yv);
				pd.setPhantom(true);
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
