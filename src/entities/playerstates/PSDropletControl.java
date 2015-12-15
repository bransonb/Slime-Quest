package entities.playerstates;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import entities.Player;
import entities.PlayerDroplet;
import foundation.Game;
import foundation.InputBlock;

public class PSDropletControl extends DropletState {

	long canReformTime;
	boolean canReform = false;
	public static final double RETURN_DISTANCE = 400;

	public PSDropletControl(Player player) {
		canReformTime = System.nanoTime() + 1000000000;
		droplets = new ArrayList<PlayerDroplet>();
		for (int i = 0; i < 15; i++) {
			Random r = new Random();
			double xMod = Math.min(5, r.nextGaussian());
			xMod = Math.max(-5, xMod);
			xMod = xMod * 100;
			double yMod = Math.min(5, r.nextGaussian());
			yMod = Math.max(-5, yMod);
			yMod = yMod * 100;

			droplets.add(new PlayerDroplet(player.getXPos()
					+ (player.getWidth() / 2.0), player.getYPos()
					+ (player.getHeight() / 2.0), player.getXVel() + xMod,
					player.getYVel() + yMod));
		}
		for (PlayerDroplet p : droplets)
			p.setDrawArrows(true);
	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if (canReform) {
			boolean flag = false;
			PlayerDroplet primePD = droplets.get(0);
			for (PlayerDroplet pd : droplets)
				if (!primePD.intersect(pd))
					flag = true;
			if (!flag) {
				Point2D.Double cen = getDropletCenter();
				double tX = player.getXPos();
				double tY = player.getYPos();
				player.teleport(cen.x - player.getWidth() / 2,
						cen.y - player.getHeight() / 2);
				if (player.dropletModeCollisionCheck(game)) {
					player.teleport(tX, tY);
					flag = true;
					if (droplets.get(0).onGround(game)) {
						player.teleport(cen.x - player.getWidth() / 2, cen.y
								- player.getHeight()
								+ droplets.get(0).getHeight() / 2);
						if (!player.dropletModeCollisionCheck(game))
							flag = false;
					}

				}
				if (!flag && ib.keyUp) {
					player.changeState(new PSGroundControl());
					player.setXVel(getDropletVelCenter().x);
					player.setYVel(getDropletVelCenter().y);
				}
			}
		}
	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		int GROUP_LIMIT = 0;
		double GROUP_ACCEL = 400;
		if (canReformTime < ib.currentTime)
			canReform = true;
		Point2D.Double p = getDropletCenter();

		for (PlayerDroplet pd : droplets) {
			double dist = Math.pow(pd.getXPos() - p.x, 2)
					+ Math.pow(pd.getYPos() - p.y, 2);
			dist = Math.pow(dist, 0.5);

			if (pd.collisionCheck(game)) {
				pd.setPhantom(true);
			}

			if (!pd.collisionCheck(game)) {
				if (pd.getPhantom()) {
					pd.setXVel(0);
					pd.setYVel(0);
				}
				pd.setPhantom(false);

			}
			if (pd.getPhantom()) {
				double yD = getDropletCenter().y
						- (pd.getYPos() + pd.getHeight() / 2);
				double xD = getDropletCenter().x
						- (pd.getXPos() + pd.getWidth() / 2);

				double hyp = Math.sqrt(Math.pow(yD, 2) + Math.pow(xD, 2));
				double yR = yD / hyp;
				double xR = xD / hyp;
				if (xR > 0 && pd.getXVel() < 0)
					xR = xR * PSSpawnState.GRAVITY_MULTIPLIER;
				else if (xR < 0 && pd.getXVel() > 0)
					xR = xR * PSSpawnState.GRAVITY_MULTIPLIER;
				if (yR > 0 && pd.getYVel() < 0)
					yR = yR * PSSpawnState.GRAVITY_MULTIPLIER;
				else if (yR < 0 && pd.getYVel() > 0)
					yR = yR * PSSpawnState.GRAVITY_MULTIPLIER;
				pd.adjXVel(xR * PSSpawnState.ADJUST_VEL);
				pd.adjYVel(yR * PSSpawnState.ADJUST_VEL);
			}
			if (ib.keyUp) {
				double adj = 0;
				if (pd.getXPos() < p.x - GROUP_LIMIT) {
					adj = GROUP_ACCEL * ib.delta;
					if (pd.getXVel() < 0)
						adj = adj * PSSpawnState.GRAVITY_MULTIPLIER;
				} else if (pd.getXPos() > p.x + GROUP_LIMIT) {
					adj = -1 * GROUP_ACCEL * ib.delta;
					if (pd.getXVel() > 0)
						adj = adj * PSSpawnState.GRAVITY_MULTIPLIER;
				}
				pd.adjXVel(adj);
				if (pd.getHitbox2D().contains(p)) {
					pd.setXVel(0);
				}
			}
			pd.update(ib, game);
		}
		player.setXVel(0);
		player.setYVel(0);

	}

	public Point2D.Double getDropletCenter() {
		return super.getDropletCenter();
	}

	@Override
	public void updateFrames(long currentTime, Player player) {

	}

}
