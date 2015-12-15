package entities.playerstates;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSFallSplat extends PlayerState {

	int frame = 0;
	long nextFrame = 0;
	boolean directionRight;
	double xVel = 0;
	
	public PSFallSplat(boolean directionRight, double xVel) {
		this.directionRight = directionRight;
		this.xVel = xVel;
		if(xVel < 0)
			this.directionRight = false;
		else if (xVel > 0)
			this.directionRight = true;
	}

	@Override
	public void updateFrames(long currentTime, Player player) {
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + Player.PLAYER_FALL_SPLAT_FRAME_DELAY;
		if(currentTime > nextFrame)
		{
			frame++;
			if(frame >= Player.FRAME_COUNT)
				frame = 0;
			nextFrame += Player.PLAYER_FALL_SPLAT_FRAME_DELAY;
		}

	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if (!player.onGround(game)) {
			player.changeState(new PSFalling(player.getRight()));
		}
		if(frame >= Player.FRAME_COUNT - 1 && ib.keyAction1)
		{
			player.changeState(new PSJumping(directionRight, PSJumping.SPLAT_JUMP));
			player.setXVel(xVel);
		}
		else if(frame >= Player.FRAME_COUNT - 1)
			player.changeState(new PSGroundControl());

	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		super.update(ib, game, player);
		player.setXVel(0);
		//Produce Hitboxes
		if(frame >= 1 && frame <= 5)
		{
			double xPos = player.getXPos();
			double yPos = player.getYPos();
			double w = 0;
			double h = 0;
			if(frame == 1)
			{
				yPos += 50;
				w = 64;
				h = 10;
			}
			else if(frame == 2)//Perfect?
			{
				yPos += 46;
				w = 78;
				h = 14;
			}
			else if(frame == 3)
			{
				yPos += 45;
				w = 96;
				h = 15;
			}
			else if(frame == 4)
			{
				yPos += 45;
				w = 96;
				h = 15;
			}
			else if(frame == 5)
			{
				yPos += 46;
				w = 78;
				h = 14;
			}
			h -= 1;
			xPos += (player.getWidth() / 2);
			xPos -= w / 2;
			game.addPlayerHitbox(new Rectangle2D.Double(xPos, yPos, w, h));
		}
	}
	
	@Override
	public BufferedImage getFrame(Player player) {
		if(directionRight)
		{
			return Player.fallSplatRightImg[frame];
		}
		else
		{
			return Player.fallSplatLeftImg[frame];
		}
	}

}
