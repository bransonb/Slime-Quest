package cellsmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.Cell;
import foundation.Game;
import foundation.InputBlock;

public class CellMenu {
	public static final int NUM_OF_CELL_TYPES = 30;
	public static final int NUM_OF_BLOCK_TYPES = 15;

	Rectangle2D.Double enlargedBorder;
	Rectangle2D.Double hiddenBorder;
	Rectangle2D.Double activeBorder;
	Rectangle2D.Double saveButton;
	Rectangle2D.Double timeUpButton;
	Rectangle2D.Double timeDownButton;
	Rectangle2D.Double timeStopButton;
	Rectangle2D.Double lockCameraButton;
	Rectangle2D.Double toggleAutoSaveButton;
	ArrayList<CellButton> buttons;
	int cType = -1;
	int bType = -1;
	short state = -1;

	boolean hidden = true;

	public CellMenu(Rectangle2D.Double border) {
		this.enlargedBorder = border;
		hiddenBorder = new Rectangle2D.Double(enlargedBorder.x,
				enlargedBorder.y, enlargedBorder.width,
				enlargedBorder.height * 0.1);
		activeBorder = new Rectangle2D.Double(hiddenBorder.x, hiddenBorder.y,
				hiddenBorder.width, hiddenBorder.height);
		buttons = new ArrayList<CellButton>();

		
		double xPos = 0;
		double yPos = 0;
		for (int i = -1; i < NUM_OF_CELL_TYPES; i++) {
			xPos = enlargedBorder.x + 10;
			yPos = (enlargedBorder.y - 96) + (Cell.HEIGHT * -2 * i);
			while(yPos < enlargedBorder.y - enlargedBorder.height + 96 && i > 0)
			{
				System.out.println("YPOS: " + yPos);
				yPos += (enlargedBorder.height - 96 - (Cell.HEIGHT * 2));
				System.out.println("EB.y: " + enlargedBorder.y);
				xPos += Cell.WIDTH * 2;
				if(yPos - 4 % (Cell.HEIGHT * 2) != 0)
					yPos -= (yPos -4) % (Cell.HEIGHT * 2);
				
			}
			System.out.println("DONE: XPOS: " + xPos + " YPOS: " + yPos);
			buttons.add(new CellButton((int) (xPos),(int) (yPos), i, -2, (short) -2));

			//buttons.add(new CellButton((int) (enlargedBorder.x + 10),(int) (enlargedBorder.y - 96) + (Cell.HEIGHT * -2 * i), i, -2, (short) -2));
		}
		
		for (int i = -1; i < NUM_OF_BLOCK_TYPES; i++) {
			xPos = enlargedBorder.x + + enlargedBorder.width / 2 + 10;
			yPos = (enlargedBorder.y - 96) + (Cell.HEIGHT * -2 * i);
			while(yPos < enlargedBorder.y - enlargedBorder.height + 96 && i > 0)
			{
				System.out.println("YPOS: " + yPos);
				yPos += (enlargedBorder.height - 96 - (Cell.HEIGHT * 2));
				System.out.println("EB.y: " + enlargedBorder.y);
				xPos += Cell.WIDTH * 2;
				if(yPos - 4 % (Cell.HEIGHT * 2) != 0)
					yPos -= (yPos -4) % (Cell.HEIGHT * 2);
				
			}
			buttons.add(new CellButton((int) (xPos),(int) (yPos), -2, i, (short) -2));
		}
		for (int i = -1; i < 2; i++) {
			buttons.add(new CellButton((int) (enlargedBorder.x - 42 - (Cell.WIDTH * 1.5) + enlargedBorder.width),
					(int) (enlargedBorder.y - 48 * (2 + i)), -2, -2, (short) i));
		}
		for (int i = -1; i < 2; i++) {
			buttons.add(new CellButton((int) (enlargedBorder.x - 42 - (Cell.WIDTH * 1.5) + enlargedBorder.width),
					(int) (enlargedBorder.y - 48 * (2 + i)), -2, -2, (short) i));
		}
		saveButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48, Cell.WIDTH, Cell.HEIGHT);
		timeStopButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48 - (Cell.HEIGHT * 1), Cell.WIDTH, Cell.HEIGHT);
		timeDownButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48 - (Cell.HEIGHT * 2), Cell.WIDTH, Cell.HEIGHT);
		timeUpButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48 - (Cell.HEIGHT * 3), Cell.WIDTH, Cell.HEIGHT);
		lockCameraButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48 - (Cell.HEIGHT * 4), Cell.WIDTH, Cell.HEIGHT);
		toggleAutoSaveButton = new Rectangle2D.Double(enlargedBorder.x - 42 + enlargedBorder.width, enlargedBorder.y - 48 - (Cell.HEIGHT * 5), Cell.WIDTH, Cell.HEIGHT);
	}

	public boolean menuContains(Point2D.Double point) {
		return activeBorder.contains(point);
	}

	public void checkOpenCloseButtons(InputBlock ib)
	{
		// Clicking to open / close menu
		if (hidden && activeBorder.height <= hiddenBorder.height) {
			if (ib.mouseLCPrime && hiddenBorder.contains(ib.mousePos))
				hidden = false;
		}

		else if (!hidden && activeBorder.height >= enlargedBorder.height) {
			if (ib.mouseLCPrime && hiddenBorder.contains(ib.mousePos))
				hidden = true;
		}
	}
	
	public void checkSaveButton(Point2D.Double p, Game game)
	{
		if(saveButton.contains(p))
		{
			game.saveAll();
		}
	}
	
	public void checkTimeButtons(Point2D.Double p, Game game)
	{
			if(timeUpButton.contains(p))
				game.incrementFlipDelay();
			else if(timeDownButton.contains(p))
				game.decrementFlipDelay();
			else if(timeStopButton.contains(p))
				game.stopFlipDelay();
	}
	
	public void checkToggleAutoSaveButton(Point2D.Double p, Game game)
	{
		if(toggleAutoSaveButton.contains(p))
		{
			if(game.autoSave)
			{
				System.out.println("AUTO SAVE OFF");
				game.autoSave = false;
			}
			else
			{
				System.out.println("AUTO SAVE ON");
				game.autoSave = true;
			}
		}
	}
	
	public void checkLockCameraButton(Point2D.Double p, Game game)
	{
		if(lockCameraButton.contains(p))
		{
			if(game.freeCamera)
				game.freeCamera = false;
			else
				game.freeCamera = true;
		}
	}
	
	public void update(InputBlock ib, Game game) {
	
		checkOpenCloseButtons(ib);

		// Menu sliding
		if (hidden && activeBorder.height > hiddenBorder.height) {
			activeBorder.height -= 4000 * ib.delta;
			if (activeBorder.height < hiddenBorder.height)
				activeBorder.height = hiddenBorder.height;
		}
		if (!hidden && activeBorder.height < enlargedBorder.height) {
			activeBorder.height += 4000 * ib.delta;
			if (activeBorder.height > enlargedBorder.height)
				activeBorder.height = enlargedBorder.height;
		}
		if (ib.mouseLCPrime && activeBorder.height == enlargedBorder.height) {
			Point2D.Double offsetMousePos = new Point2D.Double(ib.mousePos.x,
					ib.mousePos.y);
			offsetMousePos.y -= activeBorder.y + activeBorder.height;
			for (CellButton cb : buttons) {
				if (cb.contains(offsetMousePos)) {
					if (cb.getCellType() >= -1)
						cType = cb.getCellType();
					if (cb.getBlockType() >= -1)
						bType = cb.getBlockType();
					if (cb.getState() >= -1)
						state = cb.getState();
					System.out.println("STATE: " + state);
					System.out.println("CTYPE: " + cType);
					System.out.println("BTYPE: " + bType);
				}

			}
			checkSaveButton(offsetMousePos, game);
			checkTimeButtons(offsetMousePos, game);
			checkToggleAutoSaveButton(offsetMousePos, game);
			checkLockCameraButton(offsetMousePos, game);
		}

	}

	public void draw(BufferedImage build) {
		Graphics g = build.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillRoundRect((int) activeBorder.x, (int) activeBorder.y,
				(int) activeBorder.width, (int) activeBorder.height, 30, 30);
		g.setColor(Color.YELLOW.darker());
		for (int i = 0; i < 5; i++)
			g.drawRoundRect((int) activeBorder.x + i, (int) activeBorder.y + i,
					(int) activeBorder.width - 2 * i, (int) activeBorder.height
							- 2 * i, 30, 30);
		
		int offset = (int)(activeBorder.y + activeBorder.height);
		for (CellButton cb : buttons)
			cb.draw(build, offset, cType, bType, state);
	
		
		g.setColor(Color.BLUE);
		g.fillRect((int)saveButton.x, (int)saveButton.y + offset, (int)saveButton.width, (int)saveButton.height);
		g.setColor(Color.RED);
		g.fillRect((int)timeStopButton.x, (int)timeStopButton.y + offset, (int)timeStopButton.width, (int)timeStopButton.height);
		g.setColor(Color.ORANGE);
		g.fillRect((int)timeDownButton.x, (int)timeDownButton.y + offset, (int)timeDownButton.width, (int)timeDownButton.height);
		g.setColor(Color.GREEN);
		g.fillRect((int)timeUpButton.x, (int)timeUpButton.y + offset, (int)timeUpButton.width, (int)timeUpButton.height);
		if(Game.getGame().freeCamera)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.white);
		g.fillRect((int)lockCameraButton.x, (int)lockCameraButton.y + offset, (int)lockCameraButton.width, (int)lockCameraButton.height);
		if(Game.getGame().autoSave)
			g.setColor(Color.black);
		else 
			g.setColor(Color.gray);
		g.fillRect((int)toggleAutoSaveButton.x, (int)toggleAutoSaveButton.y + offset, (int)toggleAutoSaveButton.width, (int)toggleAutoSaveButton.height);
		
		if(hidden)
		{
			CellButton.draw(build, 0, (int)(activeBorder.x + 0.5 * activeBorder.width - Cell.WIDTH * 2 - Cell.WIDTH * 0.5), (int)(activeBorder.y + activeBorder.height - Cell.HEIGHT * 1.0), cType, -2, -2);
			CellButton.draw(build, 0, (int)(activeBorder.x + 0.5 * activeBorder.width - Cell.WIDTH * 0.5), (int)(activeBorder.y + activeBorder.height - Cell.HEIGHT * 1.0), -2, bType, -2);
			CellButton.draw(build, 0, (int)(activeBorder.x + 0.5 * activeBorder.width - Cell.WIDTH * -2 - Cell.WIDTH * 0.5), (int)(activeBorder.y + activeBorder.height - Cell.HEIGHT * 1.0), -2, -2, state);
			
		}
	}

	public int getCellType() {
		return cType;
	}

	public int getBlockType() {
		return bType;
	}

	public short getState() {
		return state;
	}

}
