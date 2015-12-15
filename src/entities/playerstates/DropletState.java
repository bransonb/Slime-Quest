package entities.playerstates;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import entities.PlayerDroplet;
import foundation.Game;
import foundation.InputBlock;

public abstract class DropletState extends PlayerState {
	
	protected ArrayList<PlayerDroplet> droplets;
	protected Point2D.Double lastDropletCenter;
	protected Point2D.Double lastDropletVelCenter;
	
	public DropletState() {

	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		for(PlayerDroplet pd: droplets)
			pd.update(ib, game);
	}
	
	@Override
	public BufferedImage getFrame(Player player) {
		return null;
	}
	
	public Point2D.Double getDropletVelCenter()
	{
		double x = 0;
		double y = 0;
		for(PlayerDroplet pd : droplets)
		{

			x += pd.getXVel();
			y += pd.getYVel();
		}
		x = x / droplets.size();
		y = y / droplets.size();
		lastDropletVelCenter =  new Point2D.Double(x,y);
		return lastDropletVelCenter;
	}
	
	public Point2D.Double getDropletCenter()
	{
		double x = 0;
		double y = 0;
		for(PlayerDroplet pd : droplets)
		{

			x += (pd.getXPos() + pd.getWidth() / 2);
			y += (pd.getYPos() + pd.getHeight() / 2);
		}
		x = x / droplets.size();
		y = y / droplets.size();
		lastDropletCenter =  new Point2D.Double(x,y);
		return lastDropletCenter;
	}
	
	public void updateGraphics(BufferedImage build, Game game) {

		for(PlayerDroplet pd : droplets)
			pd.updateGraphics(build, game);
	}
	
}
