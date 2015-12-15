package cellsmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import cells.blocktypes.*;
import cells.celltypes.*;
import foundation.Game;

public class CellButton {
	public static BufferedImage nothingImg;
	Rectangle2D border;
	int cType;
	int bType;
	short state;
	
	public CellButton(int x, int y, int cType, int bType, short state)
	{
		border = new Rectangle2D.Double(x, y, Cell.WIDTH, Cell.HEIGHT);
		this.cType = cType;
		this.bType = bType;
		this.state = state;
	}
	
	public static void loadImage()
	{
		nothingImg = Game.getGame().robustLoadImage("rec/img/blocks/nothing.png", "Nothing (No Change) Image");
	}
	
	public static void draw(BufferedImage build, int offset,int xPos, int yPos, int myCType, int myBType, int myState)
	{
Graphics g = build.getGraphics();
		switch(myCType)
		{
		case -1:
			g.drawImage(CellButton.nothingImg, xPos, yPos + offset, null);;
			break;
		case Cell.ISOLATED_CELL:
			g.drawImage(IsolatedCell.img, xPos, yPos + offset, null);
			break;
		case Cell.COPY_LEFT_CELL:
			g.drawImage(CopyLeftCell.img, xPos, yPos + offset, null);
			break;
		case Cell.COPY_DOWN_CELL:
			g.drawImage(CopyDownCell.img, xPos, yPos + offset, null);
			break;
		case Cell.COPY_RIGHT_CELL:
			g.drawImage(CopyRightCell.img, xPos, yPos + offset, null);
			break;
		case Cell.COPY_UP_CELL:
			g.drawImage(CopyUpCell.img, xPos, yPos + offset, null);
			break;
		case Cell.LIFE_CELL:
			g.drawImage(LifeCell.img, xPos, yPos + offset, null);
			break;
		case Cell.TOGGLE_LEFT_CELL:
			g.drawImage(ToggleLeftCell.img, xPos, yPos + offset, null);
			break;
		case Cell.TOGGLE_DOWN_CELL:
			g.drawImage(ToggleDownCell.img, xPos, yPos + offset, null);
			break;
		case Cell.TOGGLE_RIGHT_CELL:
			g.drawImage(ToggleRightCell.img, xPos, yPos + offset, null);
			break;
		case Cell.TOGGLE_UP_CELL:
			g.drawImage(ToggleUpCell.img, xPos, yPos + offset, null);
			break;
		case Cell.DICE_1_CELL:
			g.drawImage(DiceCell_1.img[1], xPos, yPos + offset, null);
			break;
		case Cell.DICE_2_CELL:
			g.drawImage(DiceCell_2.img[2], xPos, yPos + offset, null);
			break;
		case Cell.DICE_3_CELL:
			g.drawImage(DiceCell_3.img[3], xPos, yPos + offset, null);
			break;
		case Cell.DICE_4_CELL:
			g.drawImage(DiceCell_4.img[4], xPos, yPos + offset, null);
			break;
		case Cell.DICE_5_CELL:
			g.drawImage(DiceCell_5.img[5], xPos, yPos + offset, null);
			break;
		case Cell.DICE_6_CELL:
			g.drawImage(DiceCell_6.img[6], xPos, yPos + offset, null);
			break;
		case Cell.BUTTON_CELL:
			g.drawImage(ButtonCell.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.WAIT_BUTTON_CELL:
			g.drawImage(WaitButtonCell.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.NOT_LEFT_CELL:
			g.drawImage(NotLeftCell.img, xPos, yPos + offset, null);
			break;
		case Cell.NOT_DOWN_CELL:
			g.drawImage(NotDownCell.img, xPos, yPos + offset, null);
			break;
		case Cell.NOT_RIGHT_CELL:
			g.drawImage(NotRightCell.img, xPos, yPos + offset, null);
			break;
		case Cell.NOT_UP_CELL:
			g.drawImage(NotUpCell.img, xPos, yPos + offset, null);
			break;
		case Cell.OR_VERT_CELL:
			g.drawImage(OrVertCell.img, xPos, yPos + offset, null);
			break;
		case Cell.OR_HORZ_CELL:
			g.drawImage(OrHorzCell.img, xPos, yPos + offset, null);
			break;
		case Cell.AND_VERT_CELL:
			g.drawImage(AndVertCell.img, xPos, yPos + offset, null);
			break;
		case Cell.AND_HORZ_CELL:
			g.drawImage(AndHorzCell.img, xPos, yPos + offset, null);
			break;
		case Cell.PLAYER_TOUCH_CELL:
			g.drawImage(PlayerTouchCell.img, xPos, yPos + offset, null);
			break;
		case Cell.PLAYER_TOUCH_NOT_CELL:
			g.drawImage(PlayerTouchNotCell.img, xPos, yPos + offset, null);
			break;
		case Cell.ONCE_ON_CELL:
			g.drawImage(OnceOnCell.img, xPos, yPos + offset, null);
			break;
		case Cell.ONCE_OFF_CELL:
			g.drawImage(OnceOffCell.img, xPos, yPos + offset, null);
			break;
		default:
			break;
		}
		switch(myBType)
		{
		case -1:
			g.drawImage(CellButton.nothingImg, xPos, yPos + offset, null);;
			break;
		case Cell.POLITE_SOLID_BLOCK:
			g.drawImage(PoliteSolidBlock.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.ALWAYS_SOLID_BLOCK:
			g.drawImage(AlwaysSolidBlock.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.NEVER_SOLID_BLOCK:
			g.drawImage(NeverSolidBlock.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.CHECKPOINT_BLOCK:
			g.drawImage(CheckpointBlock.inactiveImg[0], xPos, yPos + offset, null);
			break;
		case Cell.SOLID_BLOCK:
			g.drawImage(SolidBlock.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.DEATH_BLOCK:
			g.drawImage(DeathBlock.inactiveImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_RED:
			g.drawImage(TimeControlBlockRed.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_ORANGE:
			g.drawImage(TimeControlBlockOrange.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_YELLOW:
			g.drawImage(TimeControlBlockYellow.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_GREEN:
			g.drawImage(TimeControlBlockGreen.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_BLUE:
			g.drawImage(TimeControlBlockBlue.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.TIME_BLOCK_PURPLE:
			g.drawImage(TimeControlBlockPurple.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.SPLIT_BLOCK:
			g.drawImage(SplitBlock.activeImg[0], xPos, yPos + offset, null);
			break;
		case Cell.RUDE_SOLID_BLOCK:
			g.drawImage(RudeSolidBlock.activeImg, xPos, yPos + offset, null);
			break;
		case Cell.HATCH_BLOCK:
			g.drawImage(HatchBlock.activeImg[0], xPos, yPos + offset, null);
			break;
		default:
			break;
		}
		if(myState == -1)
			g.drawImage(CellButton.nothingImg, xPos, yPos + offset, null);;
		if(myState == 0)
			g.drawImage(PoliteSolidBlock.inactiveImg, xPos, yPos + offset, null);
		if(myState == 1)
			g.drawImage(PoliteSolidBlock.activeImg, xPos, yPos + offset, null);
		
	}
	
	public void draw(BufferedImage build, int offset, int cType, int bType, short state)
	{
		Graphics g = build.getGraphics();
		if(cType == this.cType || bType == this.bType || state == this.state)
		{
			g.setColor(Color.BLACK);
			g.drawRect((int)border.getX() - 1, (int)border.getY() - 1 + offset, (int)border.getWidth() + 2, (int)border.getHeight() + 2);;
		}
		draw(build, offset, (int)border.getX(), (int)border.getY(), this.cType, this.bType, this.state);
	}
	
	public boolean contains(Point2D.Double point){
		return border.contains(point);
	}
	
	public int getCellType()
	{
		return cType;
	}
	public int getBlockType()
	{
		return bType;
	}
	public short getState()
	{
		return state;
	}
	
}
