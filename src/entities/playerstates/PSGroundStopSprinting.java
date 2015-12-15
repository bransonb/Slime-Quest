package entities.playerstates;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSGroundStopSprinting extends PlayerState {
	private boolean directionRight;
	private double initialXVel;
	boolean stopped = false;
	int frame;
	long nextFrame = 0;

	public PSGroundStopSprinting(boolean direction, double xVel) {
		this.directionRight = direction;
		this.initialXVel = Math.abs(xVel);
	}

	@Override
	public void updateFrames(long currentTime, Player player) {
		if(stopped)
		{
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + Player.PLAYER_SPRINT_PUNCH_FRAME_DELAY;
		if(currentTime > nextFrame)
		{
			frame++;
			if(frame >= Player.FRAME_COUNT)
				frame = 0;
			nextFrame += Player.PLAYER_SPRINT_PUNCH_FRAME_DELAY;
		}
		}
	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if (!player.onGround(game)) {
			player.changeState(new PSFalling(player.getRight()));
		}
		if(frame >= 7)
		{
			player.changeState(new PSGroundControl());
		}

	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		super.update(ib, game, player);
		double xVel = player.getXVel();
		if (Math.abs(xVel) <= Player.PLAYER_HARD_BRAKE_SPEED * ib.delta)
			{
			stopped = true;
			xVel = 0;
			}
		else {
			if (xVel > 0)
				xVel -= Player.PLAYER_HARD_BRAKE_SPEED * ib.delta;
			else
				xVel += Player.PLAYER_HARD_BRAKE_SPEED * ib.delta;
		}
		player.setXVel(xVel);
		//Produce hitboxes
		if(!stopped)
			frame = 4 - (int)(3 * (Math.abs(player.getXVel()) / initialXVel));
		if(frame >= 4 && frame <= 5)
		{
			double xPos = player.getXPos();
			double yPos = player.getYPos();
			double w = 0;
			double h = 0;

			//TODO - Put these hitboxes into final variables rather than in-code numbers.
			if(frame == 5)
			{
				if(directionRight)
					xPos += 34;
				else
					xPos -= (34 + 10) - player.getWidth();
				yPos += 32;
				w = 10;
				h = 27;
			}
			else if(frame == 4)
			{
				if(directionRight)
					xPos += 39;
				else
					xPos -= (39 + 18) - player.getWidth();
				yPos += 28;
				w = 18;
				h = 31;
			}
			
			game.addPlayerHitbox(new Rectangle2D.Double(xPos, yPos, w, h));
		}
		
	}

	@Override
	public BufferedImage getFrame(Player player) {
		if(player.getRight())
			return Player.stopRightImg[frame];
		return Player.stopLeftImg[frame];
	}

}
