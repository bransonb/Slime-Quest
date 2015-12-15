package cells;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.RandomAccessFile;

import cells.blocktypes.HatchBlock;
import foundation.Game;


public class Sector {
	public final static int NUMBER_OF_ROWS = 4;
	public final static int NUMBER_OF_COLS = 4;
	public final static int LOADING_TICKS = 10;
	public int loadingProgress = 0;
	RandomAccessFile myRAF;

	private Cell[][] cells;

	public Sector() {
		cells = new Cell[NUMBER_OF_COLS][NUMBER_OF_ROWS];
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
				cells[i][j] = Cell.typeToCell(0, 1, (short)1);
	}
	
	public Sector(Sector q)
	{
		cells = new Cell[NUMBER_OF_COLS][NUMBER_OF_ROWS];
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
			{
				cells[i][j] = Cell.typeToCell(q.cells[i][j].getCellTypeInt(), q.cells[i][j].getBlockTypeInt(), q.cells[i][j].getState());
			}
	}
	
	public Sector(LoadingHelper lh) throws IOException
	{
		cells = new Cell[NUMBER_OF_COLS][NUMBER_OF_ROWS];
		load(lh);
	}
	//Enter -1 for a type to have it remain the same
	public void changeCell(int x, int y, int cType, int bType, short state)
	{
		System.out.println("DEBUG - X: " + x + ", Y: " + y);
		cells[x][y] = Cell.typeToCell(cType, bType, state);
	}
	
	public void changeCell(int x, int y, Cell newCell)
	{
		cells[x][y] = newCell;
	}
	
	public void setState(int x, int y, short type)
	{
		cells[x][y].setState(type);
	}
	
	public void drawCells(BufferedImage build, Point2D.Double offset, BufferedImage tileset, Color themeColor, Game game) {
		Graphics g = build.getGraphics();
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
			{
				int blockX = (int) (offset.x + i * Cell.WIDTH);
				int blockY = (int) (offset.y + j * Cell.HEIGHT);
				if (!(blockX < -1 * Cell.WIDTH || blockX > build.getWidth()
						|| blockY < -1 * Cell.HEIGHT || blockY > build.getHeight()))
				{
					cells[i][j].draw(g, blockX, blockY, game);
				}
			}
	}
	
	public void clear()
	{
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
				cells[i][j] = Cell.typeToCell(0, 1, (short)1);
	}
	public void load(LoadingHelper lh) throws IOException
	{
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
			{
				short ct = lh.readShort();
				if(ct == -1)
					ct = Cell.ISOLATED_CELL;
				short bt = lh.readShort();
				if(bt == -1)
					bt = Cell.ALWAYS_SOLID_BLOCK;
				short st = lh.readShort();
					if(st == -1)
						st = 0;
				cells[i][j] = Cell.typeToCell(ct, bt, st);
				if(cells[i][j].getBlockType() instanceof HatchBlock)
				{
					((HatchBlock)cells[i][j].getBlockType()).setEType(lh.readShort());
				}
			}
	}
	public boolean partialLoad(int progress)
	{
		int i = progress - 1;
		if (i >= NUMBER_OF_COLS || myRAF == null)
		{
			if(myRAF == null)
				System.out.println("BUMMER");
			myRAF = null;
			System.out.println("i > COLS: " + i);
			return true;
		}
		try{
		for (int j = 0; j < NUMBER_OF_ROWS; j++)
		{
			cells[i][j] = Cell.typeToCell(myRAF.readShort(), myRAF.readShort(), myRAF.readShort());
		}
		}catch(IOException e)
		{
			System.out.println("ERROR - Partial Load failed.");
			e.printStackTrace();
		}
		return false;
	}
	public void startPartialLoad(RandomAccessFile raf)
	{
		myRAF = raf;
	}
	public void save(LoadingHelper lh) throws IOException
	{
		if(myRAF != null)
		{
			System.out.println("Not fully loaded.  Could not save");
			return;
		}
		for (int i = 0; i < NUMBER_OF_COLS; i++)
			for (int j = 0; j < NUMBER_OF_ROWS; j++)
			{
				cells[i][j].save(lh);
				if(cells[i][j].getBlockType() instanceof HatchBlock)
				{
					lh.writeShort((short)((HatchBlock)cells[i][j].getBlockType()).eType);
				}
			}
	}
	public Cell getCell(int x, int y)
	{
		return cells[x][y];
	}
	
	public void updateCellLastState()
	{
		for(int x = 0; x < NUMBER_OF_COLS; x++)
			for(int y = 0; y < NUMBER_OF_ROWS; y++)
				cells[x][y].updateLastState();
	}
	
	public void updateCells(int xIndex, int yIndex, Game game)
	{
		for(int x = 0; x < NUMBER_OF_COLS; x++)
			for(int y = 0; y < NUMBER_OF_ROWS; y++)
				if(cells[x + xIndex][y + yIndex] != null)
					cells[x + xIndex][y + yIndex].updateLastState();
	}
	
}
