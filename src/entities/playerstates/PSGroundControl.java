package entities.playerstates;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.Cell;
import cells.celltypes.ButtonCell;
import cells.celltypes.WaitButtonCell;
import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public class PSGroundControl extends PlayerState {

	int frame = 0;
	long nextFrame = 0;

	public PSGroundControl() {

	}

	@Override
	public void updateFrames(long currentTime, Player player) {
		if (nextFrame == 0)
			nextFrame = System.nanoTime() + Player.frameDelay;
		if (currentTime > nextFrame) {
			frame++;
			if (frame >= Player.FRAME_COUNT)
				frame = 0;
			nextFrame += Player.frameDelay
					- (Math.abs(player.getXVel()) * 500000);
		}

	}

	@Override
	public void updateState(InputBlock ib, Game game, Player player) {
		if (!player.onGround(game)) {
			player.changeState(new PSFalling(player.getRight()));
		}
		if (ib.keyAction1)
			player.changeState(new PSJumping(player.getRight(),
					PSJumping.REG_JUMP));
		double xVel = player.getXVel();
		if (xVel >= Player.PLAYER_MIN_SPRINT_SPEED) {
			player.changeState(new PSGroundSprinting(true));
		} else if (xVel <= -1 * Player.PLAYER_MIN_SPRINT_SPEED) {
			player.changeState(new PSGroundSprinting(false));
		}
		/*
		if (ib.keyDown)
			player.changeState(new PSDropletControl(player));
			*/

	}

	@Override
	public void update(InputBlock ib, Game game, Player player) {
		super.update(ib, game, player);
		//Dealing with buttons
		ArrayList<Cell> cells = new ArrayList<Cell>();
		ArrayList<Point2D.Double> c = game.getCollidingCells(player);
		for(Point2D.Double p : c)
			cells.add(game.getCell((int)p.x, (int)p.y));
			
		for(Cell cell : cells)
		{
			try{
			if(cell.getCellTypeInt() == Cell.BUTTON_CELL)
			{
				
				if(ib.keyUp)
					((ButtonCell)(cell.getCellType())).pushButton(cell);
				else
					((ButtonCell)(cell.getCellType())).setPushable(true);
			}
			else if(cell.getCellTypeInt() == Cell.WAIT_BUTTON_CELL)
			{
				if(ib.keyUp)
					((WaitButtonCell)(cell.getCellType())).pushButton(cell);
				else
					((WaitButtonCell)(cell.getCellType())).setPushable(true);
			}
			}
			catch(NullPointerException e)
			{
				
			}
		}
		
		
		double xVel = player.getXVel();
		double accel = Player.PLAYER_WALK_ACCEL;
		if(Math.abs(xVel) >= Player.PLAYER_WALK_SLOW_ACCEL_MIN)
			accel = Player.PLAYER_WALK_LOW_ACCEL;
		
		if (ib.keyLeft)
			xVel -= accel * ib.delta;
		if (ib.keyRight)
			xVel += accel * ib.delta;
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
		if(xVel > Player.PLAYER_MAX_WALK_SPEED)
			xVel = Player.PLAYER_MAX_WALK_SPEED;
		else if(xVel < -1 * Player.PLAYER_MAX_WALK_SPEED)
			xVel = -1 * Player.PLAYER_MAX_WALK_SPEED;
		player.setXVel(xVel);

	}

	@Override
	public BufferedImage getFrame(Player player) {
		if (player.getRight()) {
			if (Math.abs(player.getXVel()) > Player.PLAYER_WALK_MIN_SPEED)
				return Player.walkRightImg[frame];
			return Player.idleRightImg[Player.frame];
		} else {
			if (Math.abs(player.getXVel()) > Player.PLAYER_WALK_MIN_SPEED)
				return Player.walkLeftImg[frame];
			return Player.idleLeftImg[Player.frame];
		}
	}

}
