package cells;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import cells.celltypes.*;
import cells.blocktypes.*;
import entities.Player;
import entities.playerstates.DropletState;
import foundation.Game;

public class Cell {
	public final static int WIDTH = 32;
	public final static int HEIGHT = 32;
	public final static int PREDICTION_FRAME_COUNT = 4;
	//Cell Values
	public final static int ISOLATED_CELL = 0;
	public final static int COPY_LEFT_CELL = 1;
	public final static int COPY_DOWN_CELL = 2;
	public final static int COPY_RIGHT_CELL = 3;
	public final static int COPY_UP_CELL = 4;
	public final static int LIFE_CELL = 5;
	public final static int TOGGLE_LEFT_CELL = 6;
	public final static int TOGGLE_DOWN_CELL = 7;
	public final static int TOGGLE_RIGHT_CELL = 8;
	public final static int TOGGLE_UP_CELL = 9;
	public final static int DICE_1_CELL = 10;
	public final static int DICE_2_CELL = 11;
	public final static int DICE_3_CELL = 12;
	public final static int DICE_4_CELL = 13;
	public final static int DICE_5_CELL = 14;
	public final static int DICE_6_CELL = 15;
	public final static int BUTTON_CELL = 16;
	public final static int WAIT_BUTTON_CELL = 17;
	public final static int NOT_LEFT_CELL = 18;
	public final static int NOT_DOWN_CELL = 19;
	public final static int NOT_RIGHT_CELL = 20;
	public final static int NOT_UP_CELL = 21;
	public final static int OR_VERT_CELL = 22;
	public final static int OR_HORZ_CELL = 23;
	public final static int AND_VERT_CELL = 24;
	public final static int AND_HORZ_CELL = 25;
	public final static int PLAYER_TOUCH_CELL = 26;
	public final static int PLAYER_TOUCH_NOT_CELL = 27;
	public final static int ONCE_ON_CELL = 28;
	public final static int ONCE_OFF_CELL = 29;
	//Block Values
	public final static int POLITE_SOLID_BLOCK = 0;
	public final static int ALWAYS_SOLID_BLOCK = 1;
	public final static int NEVER_SOLID_BLOCK = 2;
	public final static int CHECKPOINT_BLOCK = 3;
	public final static int SOLID_BLOCK = 4;
	public final static int DEATH_BLOCK = 5;
	public final static int TIME_BLOCK_RED = 6;
	public final static int TIME_BLOCK_ORANGE = 7;
	public final static int TIME_BLOCK_YELLOW = 8;
	public final static int TIME_BLOCK_GREEN = 9;
	public final static int TIME_BLOCK_BLUE = 10;
	public final static int TIME_BLOCK_PURPLE = 11;
	public final static int SPLIT_BLOCK = 12;
	public final static int RUDE_SOLID_BLOCK = 13;
	public final static int HATCH_BLOCK = 14;
	//
	public static long nextFrame;
	public static long FRAME_DELAY = 25000000;
	public static int frame = 0;
	public static BufferedImage[] glowOnImg = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] glowOffImg = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] shimmerOnImg = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] shimmerOffImg = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] glowOnImgRC = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] glowOffImgRC = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] shimmerOnImgRC = new BufferedImage[PREDICTION_FRAME_COUNT];
	public static BufferedImage[] shimmerOffImgRC = new BufferedImage[PREDICTION_FRAME_COUNT];

	private short state = 0;
	private short lastState = 0;
	private short nextState = 0;
	private CellType cellType;
	private BlockType blockType;
	
	public Cell(short state, CellType cellType, BlockType blockType)
	{
		this.state = (short)state;
		this.cellType = cellType;
		this.blockType = blockType;
		if(this.cellType.toInt() == ONCE_ON_CELL)
		{
			state = 1;
			if(this.blockType instanceof HatchBlock)
				((HatchBlock)blockType).setFreshSpawn(true);
		}
		else if(this.cellType.toInt() == ONCE_OFF_CELL)
			state = 0;
		nextState = state;
		lastState = state;
	}
	
	public static void loadImages()
	{
		for(int i = 0; i < PREDICTION_FRAME_COUNT; i++)
		{
			glowOnImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/pre_glow_on_" + (i + 1) + ".png", "Cell Prediction Image");
			glowOffImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/pre_glow_off_" + (i + 1) + ".png", "Cell Prediction Image");
			shimmerOnImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/pre_glow_on_shimmer_" + (i + 1) + ".png", "Cell Prediction Image");
			shimmerOffImg[i] = Game.getGame().robustLoadImage("rec/img/blocks/pre_glow_off_shimmer_" + (i + 1) + ".png", "Cell Prediction Image");
		}
	}
	

	public static void updateFrame(long currentTime)
	{
		if(nextFrame == 0)
			nextFrame = System.nanoTime() + FRAME_DELAY;
		if(currentTime > nextFrame)
		{
			frame++;
			if(frame >= PREDICTION_FRAME_COUNT)
				frame = 0;
			nextFrame += FRAME_DELAY;
		}
	}
	
	
	public void draw(Graphics g, int xPos, int yPos, Game game)
	{

		if(getCellTypeInt() == Cell.ONCE_ON_CELL && blockType instanceof HatchBlock)
		{
			Point2D.Double p = new Point2D.Double(xPos + Cell.WIDTH / 2, yPos - Cell.HEIGHT / 2);
			p = game.screenToWorld(p);
			p = game.worldToCell(p);
			
			Cell c = game.getCell((int)p.x, (int)p.y );
			c.getBlockType().draw(g, xPos, yPos, c.getState());
			c.getCellType().draw(g, xPos, yPos);
		}
		else
		{
		blockType.draw(g, xPos, yPos, state);
		cellType.draw(g, xPos, yPos);
		}
		
		double per = 1 - game.getTimerPercent();
		if(per <= 0.75)
		{
		if(nextState <= 0 && state > 0)
		{
			g.drawImage(glowOffImgRC[frame], xPos, yPos, null);
		}
		else if(nextState > 0 && state <= 0)
			g.drawImage(glowOnImgRC[frame], xPos, yPos, null);
		}
		else
		{
			int tFrame = (int)((per - 0.75)* 4 * PREDICTION_FRAME_COUNT);
			tFrame = Math.min(tFrame, 3);
			if(nextState <= 0 && state > 0)
				g.drawImage(shimmerOffImgRC[tFrame], xPos, yPos, null);
			else if(nextState > 0 && state <= 0)
				g.drawImage(shimmerOnImgRC[tFrame], xPos, yPos, null);
		}
	}
	
	public void predict(int x, int y, Game game)
	{
		nextState = (short) Math.max(0,cellType.update(x, y, game));
	}
	
	public void tickUpdate(Game game, double xPos, double yPos)
	{
		blockType.onTickAction(game, xPos, yPos, state);
	}
	
	public void update(int x, int y, Game game, Player p)
	{
		cellType.singularUpdate();
		state = (short) Math.max(0,cellType.update(x, y, game));
		blockType.singularUpdate(game, this, x, y);
		if(blockType instanceof PoliteSolidBlock && p != null)
		{
			if(!(p.getState() instanceof DropletState))
			{
			Point2D.Double tl = game.getSectCorner();
			if(blockType.collisionCheck(new Point2D.Double(x * WIDTH + tl.x, y * HEIGHT + tl.y), p.getHitbox(), state))
				{
				System.out.println("POP!");
				System.out.println("X: " + (x * WIDTH) + ", Y: " + (y * HEIGHT));
				state = 0;
				((PoliteSolidBlock)blockType).startPop();
				}
			}
		}
		if(blockType instanceof CheckpointBlock && cellType instanceof IsolatedCell)
		{
			Point2D.Double d = game.getSectCorner();
			int x2 = (int)(x + d.x / Cell.WIDTH);
			int y2 = (int)(y + d.y / Cell.HEIGHT);
			if(game.getRespawnPoint().x != x2 || game.getRespawnPoint().y != y2)
				state = 0;
		}
	}
	
	public void save(LoadingHelper lh) throws IOException
 {
		lh.writeShort((short)getCellTypeInt());
		lh.writeShort((short)getBlockTypeInt());
		lh.writeShort(state);
	}

	public void setState(int state) {
		this.state = (short) state;
		blockType.singularUpdate(Game.getGame(), this, 0, 0);
	}

	public void setStateSubtle(int state)
	{
		this.state = (short)state;
	}
	
	public short getState() {
		return state;
	}

	public void updateLastState() {
		lastState = state;
	}

	public short getLastState()
	{
		return lastState;
	}
	
	public int blockTypeCollisionCheck(Point2D.Double myLoc, Rectangle r)
	{
		Rectangle r2 = new Rectangle((int)myLoc.x, (int)myLoc.y, WIDTH, HEIGHT);
		if(r2.intersects(r))
			return blockType.toInt();
		else return -1;
	}
	
	public boolean collisionCheck(Point2D.Double myLoc, Rectangle r)
	{
		return blockType.collisionCheck(myLoc, r, state);
	}
	
	public CellType getCellType()
	{
		return cellType;
	}
	
	public BlockType getBlockType()
	{
		return blockType;
	}
	
	public static Cell typeToCell(int cType, int bType, short state)
	{
		Cell b;
		CellType ct;
		BlockType bt;
		switch(cType)
		{
		case ISOLATED_CELL:
			ct = new IsolatedCell();
			break;
		case COPY_LEFT_CELL:
			ct = new CopyLeftCell();
			break;
		case COPY_DOWN_CELL:
			ct = new CopyDownCell();
			break;
		case COPY_RIGHT_CELL:
			ct = new CopyRightCell();
			break;
		case COPY_UP_CELL:
			ct = new CopyUpCell();
			break;
		case LIFE_CELL:
			ct = new LifeCell();
			break;
		case TOGGLE_LEFT_CELL:
			ct = new ToggleLeftCell();
			break;
		case TOGGLE_DOWN_CELL:
			ct = new ToggleDownCell();
			break;
		case TOGGLE_RIGHT_CELL:
			ct = new ToggleRightCell();
			break;
		case TOGGLE_UP_CELL:
			ct = new ToggleUpCell();
			break;
		case DICE_1_CELL:
			ct = new DiceCell_1();
			break;
		case DICE_2_CELL:
			ct = new DiceCell_2();
			break;
		case DICE_3_CELL:
			ct = new DiceCell_3();
			break;
		case DICE_4_CELL:
			ct = new DiceCell_4();
			break;
		case DICE_5_CELL:
			ct = new DiceCell_5();
			break;
		case DICE_6_CELL:
			ct = new DiceCell_6();
			break;
		case BUTTON_CELL:
			ct = new ButtonCell();
			break;
		case WAIT_BUTTON_CELL:
			ct = new WaitButtonCell();
			break;
		case NOT_LEFT_CELL:
			ct = new NotLeftCell();
			break;
		case NOT_DOWN_CELL:
			ct = new NotDownCell();
			break;
		case NOT_RIGHT_CELL:
			ct = new NotRightCell();
			break;
		case NOT_UP_CELL:
			ct = new NotUpCell();
			break;
		case OR_VERT_CELL:
			ct = new OrVertCell();
			break;
		case OR_HORZ_CELL:
			ct = new OrHorzCell();
			break;
		case AND_VERT_CELL:
			ct = new AndVertCell();
			break;
		case AND_HORZ_CELL:
			ct = new AndHorzCell();
			break;
		case PLAYER_TOUCH_CELL:
			ct = new PlayerTouchCell();
			break;
		case PLAYER_TOUCH_NOT_CELL:
			ct = new PlayerTouchNotCell();
			break;
		case ONCE_ON_CELL:
			ct = new OnceOnCell();
			break;
		case ONCE_OFF_CELL:
			ct = new OnceOffCell();
			break;
		default:
			ct = new IsolatedCell();
			break;
		}
		switch(bType)
		{
		case POLITE_SOLID_BLOCK:
			bt = new PoliteSolidBlock();
			break;
		case ALWAYS_SOLID_BLOCK:
			bt = new AlwaysSolidBlock();
			break;
		case NEVER_SOLID_BLOCK:
			bt = new NeverSolidBlock();
			break;
		case CHECKPOINT_BLOCK:
			bt = new CheckpointBlock();
			break;
		case SOLID_BLOCK:
			bt = new SolidBlock();
			break;
		case DEATH_BLOCK:
			bt = new DeathBlock();
			break;
		case TIME_BLOCK_RED:
			bt = new TimeControlBlockRed();
			break;
		case TIME_BLOCK_ORANGE:
			bt = new TimeControlBlockOrange();
			break;
		case TIME_BLOCK_YELLOW:
			bt = new TimeControlBlockYellow();
			break;
		case TIME_BLOCK_GREEN:
			bt = new TimeControlBlockGreen();
			break;
		case TIME_BLOCK_BLUE:
			bt = new TimeControlBlockBlue();
			break;
		case TIME_BLOCK_PURPLE:
			bt = new TimeControlBlockPurple();
			break;
		case SPLIT_BLOCK:
			bt = new SplitBlock();
			break;
		case RUDE_SOLID_BLOCK:
			bt = new RudeSolidBlock();
			break;
		case HATCH_BLOCK:
			bt = new HatchBlock();
			break;
		default:
			bt = new PoliteSolidBlock();
			break;
		}
		if(cType == ONCE_ON_CELL)
			state = 1;
		else if(cType == ONCE_OFF_CELL)
			state = 0;
		b = new Cell(state, ct, bt);

		return b;
	}
	
	
	public int getCellTypeInt()
	{
		return cellType.toInt();
	}
	
	public int getBlockTypeInt()
	{
		return blockType.toInt();
	}
	
}
