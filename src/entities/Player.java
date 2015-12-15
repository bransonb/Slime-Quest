package entities;

import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.Cell;
import foundation.Game;
import foundation.InputBlock;
import entities.playerstates.*;

public class Player extends MobileEntity {
	public static final int FRAME_WIDTH = 48;
	public static final int FRAME_HEIGHT = 64;
	public static final int FRAME_COUNT = 8;
	public static int frame = 0;
	public static int frameDelay = 100000000;
	public static long nextFrame;

	// Player Static Values
	public static final double PLAYER_MIN_SPRINT_SPEED = 200;
	public static final double PLAYER_MAX_WALK_SPEED = PLAYER_MIN_SPRINT_SPEED;
	public static final double PLAYER_MAX_SPRINT_SPEED = 300;
	public static final double PLAYER_NATURAL_BRAKE_SPEED = 60;
	public static final double PLAYER_HARD_BRAKE_SPEED = 500;
	public static final double PLAYER_AIR_WALK_FORWARD_ACCEL = 250;
	public static final double PLAYER_AIR_WALK_BACK_ACCEL = 200;
	public static final double PLAYER_WALK_ACCEL = 300;
	public static final double PLAYER_WALK_LOW_ACCEL = 50;
	public static final double PLAYER_WALK_SLOW_ACCEL_MIN = 160;// Should be less than MIN_SPRINT_SPEED
	public static final double PLAYER_WALK_MIN_SPEED = 50;
	public static final double PLAYER_SPRINT_ACCEL = 100;
	public static final long PLAYER_SPRINT_PUNCH_FRAME_DELAY = 100000000;
	public static final double PLAYER_JUMP_VEL = -500;
	public static final double PLAYER_RUNNING_JUMP_VEL = -650;
	public static final double PLAYER_SPLAT_JUMP_VEL = -725;
	public static final double PLAYER_ANIM_JUMP_ARC_VEL = 200;
	public static final double PLAYER_FALL_ACCEL = 1200;
	public static final double PLAYER_MAX_FALL_SPEED = 800;
	public static final double PLAYER_RESIST_FALL_ACCEL = 600;
	public static final double PLAYER_DIMINISH_MIX_FALL_SPEED = 700;
	public static final double PLAYER_DIMINISH_FALL_ACCEL = 50;
	public static final long PLAYER_FALL_SPLAT_FRAME_DELAY = 50000000;
	public static final double PLAYER_MIN_FALL_SPLAT_SPEED = 700;
	public static final double PLAYER_WATER_FLOAT_SPEED = -2000;
	public static final double PLAYER_WATER_FLOAT_MAX_SPEED = -850;

	public static BufferedImage[] idleLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] idleRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] walkLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] walkRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] runLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] runRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] stopLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] stopRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] jumpLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] jumpRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] jumpArcLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] jumpArcRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] fallLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] fallRightImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] fallSplatLeftImg = new BufferedImage[FRAME_COUNT];
	public static BufferedImage[] fallSplatRightImg = new BufferedImage[FRAME_COUNT];

	public static BufferedImage bi;
	private boolean facingRight = true;
	private PlayerState playerState;
	private PlayerState nextState;
	public double lastYVel = 0;

	public Player(double xPos, double yPos) {
		super(xPos, yPos);
		width = 40;
		height = 60;
		playerState = new PSSpawnState(this);
	}

	public static void loadImages() {
		for (int i = 0; i < FRAME_COUNT; i++)
		{
			idleLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/idle_left_" + (i + 1) + ".png", "Player Image");
			idleRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/idle_right_" + (i + 1) + ".png", "Player Image");
			walkLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/walk_left_" + (i + 1) + ".png", "Player Image");
			walkRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/walk_right_" + (i + 1) + ".png", "Player Image");
			runLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/run_left_" + (i + 1) + ".png", "Player Image");
			runRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/run_right_" + (i + 1) + ".png", "Player Image");
			stopLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/stop_sprint_left_" + (i + 1) + ".png", "Player Image");
			stopRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/stop_sprint_right_" + (i + 1) + ".png", "Player Image");
			jumpLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/jump_left_" + (i + 1) + ".png", "Player Image");
			jumpRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/jump_right_" + (i + 1) + ".png", "Player Image");
			jumpArcLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/jump_arc_left_" + (i + 1) + ".png", "Player Image");
			jumpArcRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/jump_arc_right_" + (i + 1) + ".png", "Player Image");
			fallLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/fall_left_" + (i + 1) + ".png", "Player Image");
			fallRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/fall_right_" + (i + 1) + ".png", "Player Image");
			fallSplatLeftImg[i] = Game.getGame().robustLoadImage("rec/img/character/fall_splat_left_" + (i + 1) + ".png", "Player Image");
			fallSplatRightImg[i] = Game.getGame().robustLoadImage("rec/img/character/fall_splat_right_" + (i + 1) + ".png", "Player Image");
			
		}

	}

	public void updateFrame(long currentTime) {
		if (nextFrame == 0)
			nextFrame = System.nanoTime() + frameDelay;
		if (currentTime > nextFrame) {
			frame++;
			if (frame >= FRAME_COUNT)
				frame = 0;
			nextFrame += frameDelay;
		}
		playerState.updateFrames(currentTime, this);
	}

	public Point2D.Double getDropletCenter() {
		if (playerState instanceof DropletState) {
			Point2D.Double cen = ((DropletState) playerState)
					.getDropletCenter();
			if (cen != null)
				return cen;
		}
		return new Point2D.Double(getXPos() + getWidth() / 2, getYPos()
				+ getHeight() / 2);
	}

	public void changeState(PlayerState state) {
		nextState = state;
	}

	public PlayerState getState()
	{
		return playerState;
	}
	
	public boolean getRight() {
		return facingRight;
	}

	public void hitboxCheck(Game game, ArrayList<Shape> ehb) {
		if (!(playerState instanceof PSDeathState))
			for (Shape hb : ehb)
				if (hb.intersects(getHitbox()))
					changeState(new PSDeathState(this));
	}

	@Override
	public void update(InputBlock ib, Game game) {
		playerState.updateState(ib, game, this);
		//Manual respawn
		if(ib.keySuicide && !(playerState instanceof PSDeathState) && !(playerState instanceof PSSpawnState))
		{
			if(playerState instanceof DropletState)
				changeState(new PSDeathState((DropletState)playerState));
			else
			changeState(new PSDeathState(this));
		}
		//Check collision with special blocks
		ArrayList<Integer> touchBlocks = getCollidingBlockTypes(game, true);
		if(!(playerState instanceof DropletState) && touchBlocks.contains(Cell.SPLIT_BLOCK))
			changeState(new PSDropletControl(this));
		
		lastYVel = yVel;

		if (nextState != null) {
			playerState = nextState;
			nextState = null;
		}

		if (xVel > 0)
			facingRight = true;
		else if (xVel < 0)
			facingRight = false;

		playerState.update(ib, game, this);

		if(touchBlocks.contains(Cell.RUDE_SOLID_BLOCK))
		{
			yVel += PLAYER_WATER_FLOAT_SPEED * ib.delta;
			yVel = Math.max(PLAYER_WATER_FLOAT_MAX_SPEED, yVel);
		}
		updatePos(ib, game);
	}

	@Override
	public void updateGraphics(BufferedImage build, Game game) {

		Point2D.Double d = game.worldToScreen(new Point2D.Double(xPos, yPos));
		Graphics g = build.getGraphics();
		BufferedImage biToDraw = playerState.getFrame(this);
		if (biToDraw != null)
			g.drawImage(biToDraw, (int) d.x - biToDraw.getWidth() / 2
					+ (int) width / 2, (int) d.y - biToDraw.getHeight() / 2
					+ (int) height / 2, (int) biToDraw.getWidth(),
					(int) biToDraw.getHeight(), null);

		if (playerState instanceof DropletState)
			((DropletState) playerState).updateGraphics(build, game);

		if(playerState instanceof PSDropletControl)
		{
			Point2D.Double p = ((DropletState)playerState).getDropletCenter();
			
			if(p != null){
				p = game.worldToScreen(p);
			g.drawRect((int)p.x - 1, (int)p.y - 1, 2, 2);
			}
		}

	}
	
	public boolean collisionCheck(Game game)
	{
		if(playerState instanceof DropletState)
			return false;
		else return super.collisionCheck(game);
	}
	
	public boolean dropletModeCollisionCheck(Game game)
	{
		return super.collisionCheck(game);
	}

}
