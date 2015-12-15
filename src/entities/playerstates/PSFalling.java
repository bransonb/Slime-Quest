package entities.playerstates;

import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSFalling extends PlayerState{
	
	int frame = 0;
	long nextFrame = 0;
	boolean directionRight;

	public PSFalling(boolean directionRight)
	{
		this.directionRight = directionRight;
	}
	
	@Override
	public void updateFrames(long currentTime, Player player) {
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + 2 * Player.frameDelay;
		if(currentTime > nextFrame)
		{
			frame++;
			if(frame >= Player.FRAME_COUNT)
				frame = 0;
			nextFrame += 2 * Player.frameDelay - (Math.abs(player.getYVel()) * 500000);
		}

	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if(player.onGround(game))
		{
			if(player.lastYVel >= Player.PLAYER_MIN_FALL_SPLAT_SPEED)
				player.changeState(new PSFallSplat(directionRight, player.getXVel()));
			else
				player.changeState(new PSGroundControl());
		}
		if(player.lastYVel < 0){
			boolean tempD = directionRight;
			if(player.getXVel() > 0)
				tempD = true;
			else if(player.getXVel() < 0)
				tempD = false;
			player.changeState(new PSJumping(tempD, PSJumping.REG_JUMP));
		}
	}
	
	@Override
	public void update(InputBlock ib, Game game, Player player) {
		super.update(ib, game, player);
		double xVel = player.getXVel();
		double yVel = player.getYVel();
		double faccel = Player.PLAYER_AIR_WALK_FORWARD_ACCEL;
		double baccel = Player.PLAYER_AIR_WALK_BACK_ACCEL;
		
		if (ib.keyLeft)
		{
			if(directionRight)
			xVel -= baccel * ib.delta;
			else
				xVel -= faccel * ib.delta;
		}
		if (ib.keyRight)
		{
			if(directionRight)
			xVel += faccel * ib.delta;
			else
				xVel += baccel * ib.delta;
		}

		if(xVel > Player.PLAYER_MAX_WALK_SPEED)
			xVel = Player.PLAYER_MAX_WALK_SPEED;
		else if(xVel < -1 * Player.PLAYER_MAX_WALK_SPEED)
			xVel = -1 * Player.PLAYER_MAX_WALK_SPEED;
		player.setXVel(xVel);
		
		if(yVel < Player.PLAYER_MAX_FALL_SPEED)
		{
			if(yVel < Player.PLAYER_DIMINISH_MIX_FALL_SPEED)
				yVel += Player.PLAYER_FALL_ACCEL * ib.delta;
			else
				yVel += Player.PLAYER_DIMINISH_FALL_ACCEL * ib.delta;
			if(yVel > Player.PLAYER_MAX_FALL_SPEED)
				yVel = Player.PLAYER_MAX_FALL_SPEED;
		}
		player.setYVel(yVel);
		
	}

	@Override
	public BufferedImage getFrame(Player player) {
		double yVel = Math.abs(player.getYVel());
		if(yVel > Player.PLAYER_ANIM_JUMP_ARC_VEL)
		{
		if(directionRight)
			return Player.fallRightImg[frame];
		else
			return Player.fallLeftImg[frame];
		}
		else
		{
			int tFrame = (int) ((yVel / Player.PLAYER_ANIM_JUMP_ARC_VEL) * (Player.FRAME_COUNT / 2));
			tFrame += 4;
			if(directionRight)
				return Player.jumpArcRightImg[tFrame];
			else
				return Player.jumpArcLeftImg[tFrame];
		}
	}



}
