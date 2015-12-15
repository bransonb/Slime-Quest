package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import foundation.Game;
import foundation.InputBlock;

public class PlayerDroplet extends MobileEntity {
	public static BufferedImage roundImg;
	public static BufferedImage flatImg;
	public static BufferedImage tallImg;
	public static BufferedImage wideImg;
	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	public static final int FLATTEN_SPEED = 100;
	boolean phantom = false;
	boolean drawArrows = false;

	public PlayerDroplet(double xPos, double yPos, double xVel, double yVel) {
		super(xPos, yPos);
		setXVel(xVel);
		setYVel(yVel);
		width = WIDTH;
		height = HEIGHT;
	}

	public static void loadImages() {
		roundImg = Game.getGame().robustLoadImage("rec/img/character/droplet_round.png", "Player - Round Droplet");
		flatImg = Game.getGame().robustLoadImage("rec/img/character/droplet_flat.png", "Player - Flat Droplet");
		tallImg = Game.getGame().robustLoadImage("rec/img/character/droplet_tall.png", "Player - Tall Droplet");
		wideImg = Game.getGame().robustLoadImage("rec/img/character/droplet_wide.png", "Player - Wide Droplet");
	}

	public void setDrawArrows(boolean b)
	{
		drawArrows = b;
	}
	
	public void setPhantom(boolean p) {
		phantom = p;
	}
	
	public boolean getPhantom()
	{
		return phantom;
	}
	@Override
	public void update(InputBlock ib, Game game) {
		if (phantom) {
			phantomUpdatePos(ib, game);
		} else {
			if (!onGround(game)) {
				double yVel = getYVel();
				yVel += Player.PLAYER_FALL_ACCEL * ib.delta;
				setYVel(yVel);

				if (ib.keyLeft && !ib.keyRight) {
					double xVel = getXVel();
					xVel -= Player.PLAYER_AIR_WALK_FORWARD_ACCEL * ib.delta;
					setXVel(xVel);
				} else if (ib.keyRight && !ib.keyLeft) {
					double xVel = getXVel();
					xVel += Player.PLAYER_AIR_WALK_FORWARD_ACCEL * ib.delta;
					setXVel(xVel);
				}

			} else {
				if(ib.keyAction1){
					Random r = new Random();
					double yMod = Math.min(5, r.nextGaussian());
					yMod = Math.max(-5, yMod);
					yMod = yMod * Player.PLAYER_JUMP_VEL * 0.1;
					setYVel(Player.PLAYER_JUMP_VEL + yMod);
				}
				if (ib.keyLeft && !ib.keyRight) {
					double xVel = getXVel();
					xVel -= Player.PLAYER_WALK_ACCEL * ib.delta;
					setXVel(xVel);
				} else if (ib.keyRight && !ib.keyLeft) {
					double xVel = getXVel();
					xVel += Player.PLAYER_WALK_ACCEL * ib.delta;
					setXVel(xVel);
				} else {
					double xVel = getXVel();

					if (Math.abs(xVel) <= Player.PLAYER_NATURAL_BRAKE_SPEED
							* ib.delta)
						xVel = 0;
					else {
						if (xVel > 0)
							xVel -= Player.PLAYER_NATURAL_BRAKE_SPEED
									* ib.delta;
						else
							xVel += Player.PLAYER_NATURAL_BRAKE_SPEED
									* ib.delta;
					}
					setXVel(xVel);
				}
			}
			updatePos(ib, game);
		}
	}

	@Override
	public void updateGraphics(BufferedImage build, Game game) {
		Point2D.Double d = game.worldToScreen(new Point2D.Double(xPos, yPos));
		Graphics g = build.getGraphics();
		
		if(d.x > 0 && d.x < build.getWidth() && d.y > 0 && d.y < build.getHeight())
		{
		
		BufferedImage src;
		if (onGround(game) && !phantom)
			src = flatImg;
		else
		{
			double t = Math.abs(getYVel()) - Math.abs(getXVel());
			if(t > FLATTEN_SPEED)
				if(Math.abs(getYVel()) > Math.abs(getXVel()))
					src = tallImg;
				else
					src = wideImg;
			else src = roundImg;
		}
		g.drawImage(src, (int) d.x - src.getWidth() / 2 + (int) width / 2,
				(int) d.y - src.getHeight() / 2 + (int) height / 2,
				(int) src.getWidth(), (int) src.getHeight(), null);
		
		}
		else if(drawArrows)
		{
			g.setColor(new Color(96, 238, 167));
			double ARROW_LENGTH = 64;
			double ARROW_BASE = build.getHeight() / 2 - ARROW_LENGTH - 2;
			double ARROW_WIDTH = ARROW_LENGTH / 3;
			Point2D.Double p1 = new Point2D.Double(build.getWidth() / 2, build.getHeight() / 2);
			Point2D.Double delt = new Point2D.Double(p1.x - d.x, p1.y - d.y);
			double angle = Math.atan(delt.y / delt.x);
			if(delt.x > 0)
				angle += Math.PI;
			Point2D.Double base = new Point2D.Double(Math.cos(angle) * ARROW_BASE + build.getWidth() / 2, Math.sin(angle) * ARROW_BASE + build.getHeight() / 2);
			Point2D.Double tip = new Point2D.Double(Math.cos(angle) * ARROW_LENGTH + base.x, Math.sin(angle) * ARROW_LENGTH + base.y);
			angle = angle + (Math.PI / 2);
			Point2D.Double side1 = new Point2D.Double(Math.cos(angle) * ARROW_WIDTH + base.x, Math.sin(angle) * ARROW_WIDTH + base.y);
			angle = angle + Math.PI;
			Point2D.Double side2 = new Point2D.Double(Math.cos(angle) * ARROW_WIDTH + base.x, Math.sin(angle) * ARROW_WIDTH + base.y);
			Polygon polygon = new Polygon();
			polygon.addPoint((int)tip.x, (int)tip.y);
			polygon.addPoint((int)side1.x, (int)side1.y);
			polygon.addPoint((int)side2.x, (int)side2.y);
			g.fillPolygon(polygon);
			g.setColor(new Color(81, 204, 143));
			g.drawPolygon(polygon);
		}

	}

}
