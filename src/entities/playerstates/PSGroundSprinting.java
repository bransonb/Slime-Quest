package entities.playerstates;

import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSGroundSprinting extends PlayerState {
	private boolean directionRight;
	int frame = 0;
	long nextFrame = 0;

	public PSGroundSprinting(boolean direction) {
		this.directionRight = direction;
	}

	@Override
	public void updateFrames(long currentTime, Player player) {
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + Player.frameDelay;
		if(currentTime > nextFrame)
		{
			frame++;
			if(frame >= Player.FRAME_COUNT)
				frame = 0;
			nextFrame += Player.frameDelay - (Math.abs(player.getXVel()) * 500000);
		}
	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if (!player.onGround(game)) {
			player.changeState(new PSFalling(player.getRight()));
		}
		if(ib.keyAction1)
			player.changeState(new PSJumping(player.getRight(), PSJumping.SPRINT_JUMP));
		double xVel = player.getXVel();
		if(Math.abs(xVel) < Player.PLAYER_MIN_SPRINT_SPEED)
		{
			player.changeState(new PSGroundControl());
		}
		

	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		super.update(ib, game, player);
		double xVel = player.getXVel();
		if(directionRight && ib.keyRight)
			xVel += Player.PLAYER_SPRINT_ACCEL * ib.delta;
		else if(!directionRight && ib.keyLeft)
			xVel -= Player.PLAYER_SPRINT_ACCEL * ib.delta;
		if(directionRight && ib.keyLeft)
			player.changeState(new PSGroundStopSprinting(directionRight, player.getXVel()));
		else if(!directionRight && ib.keyRight)
			player.changeState(new PSGroundStopSprinting(directionRight, player.getXVel()));

		
		
			
		if (!(ib.keyLeft || ib.keyRight) && xVel != 0) {
			if (Math.abs(xVel) <= Player.PLAYER_NATURAL_BRAKE_SPEED * ib.delta)
				xVel = 0;
			else {
				if (xVel > 0)
					xVel -= Player.PLAYER_NATURAL_BRAKE_SPEED * ib.delta;
				else
					xVel += Player.PLAYER_NATURAL_BRAKE_SPEED * ib.delta;
			}

		}
		
		player.setXVel(xVel);
	}

	@Override
	public BufferedImage getFrame(Player player) {
		if(player.getRight())
			return Player.runRightImg[frame];
		return Player.runLeftImg[frame];
	}

}
