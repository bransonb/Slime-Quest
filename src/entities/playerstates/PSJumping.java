package entities.playerstates;

import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSJumping extends PlayerState {

	int frame = 0;
	long nextFrame = 0;
	boolean directionRight;
	int jumpType;
	public static final int REG_JUMP = 0;
	public static final int SPRINT_JUMP = 1;
	public static final int SPLAT_JUMP = 2;
	
	public PSJumping(boolean directionRight, int jumpType) {
		this.directionRight = directionRight;
		this.jumpType = jumpType;
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
		if (player.getYVel() >= 0) {
			player.changeState(new PSFalling(directionRight));
		}


	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		if(player.onGround(game))
		{
			if(jumpType == 0)
				player.setYVel(Player.PLAYER_JUMP_VEL);
			else if(jumpType == 1)
				player.setYVel(Player.PLAYER_RUNNING_JUMP_VEL);
			else if(jumpType == 2)
				player.setYVel(Player.PLAYER_SPLAT_JUMP_VEL);
		}
		
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

		if(ib.keyAction1)
			yVel += Player.PLAYER_RESIST_FALL_ACCEL * ib.delta;
		else
			yVel += Player.PLAYER_FALL_ACCEL * ib.delta;
		if(yVel > Player.PLAYER_MAX_FALL_SPEED)
			yVel = Player.PLAYER_MAX_FALL_SPEED;
		player.setYVel(yVel);
	}
	
	@Override
	public BufferedImage getFrame(Player player) {
		double yVel = Math.abs(player.getYVel());
		if(yVel > Player.PLAYER_ANIM_JUMP_ARC_VEL)
		{
		if(directionRight)
			return Player.jumpRightImg[frame];
		else
			return Player.jumpLeftImg[frame];
		}
		else
		{
			int tFrame = (int) ((1 - yVel / Player.PLAYER_ANIM_JUMP_ARC_VEL) * (Player.FRAME_COUNT / 2.0));
			if(directionRight)
				return Player.jumpArcRightImg[tFrame];
			else
				return Player.jumpArcLeftImg[tFrame];
		}
	}

}
