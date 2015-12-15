package cellsmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import cells.Cell;
import entities.Enemy;
import foundation.Game;

public class EnemyButton {
	public static int BUTTON_WIDTH = 32;
	public static int BUTTON_HEIGHT = 32;
	BufferedImage bi = null;
	int eType;
	int x;
	int y;
	boolean active = false;
	public EnemyButton(int x, int y, int eType, EnemyMenu emenu)
	{
		this.eType = eType;
		this.x = x;
		this.y = y;
		//Enemy tempEnemy = emenu.makeEnemy(0, 0, null, eType);
		bi = new BufferedImage(BUTTON_WIDTH, BUTTON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		//TODO - Perhaps make an icon for each enemy because this solution is kinda bad.
		
		/*if(tempEnemy != null)
		{
			tempEnemy.updateGraphics(bi, Game.getGame());
		}
		else
		{
			Graphics g = bi.getGraphics();
			g.drawImage(CellButton.nothingImg, 0, 0, null);
		}*/
			bi = EnemyMenu.buttonIcons[eType];
	}
	public void draw(BufferedImage build, int offset)
	{
		Graphics g = build.getGraphics();
		g.drawImage(bi, x + offset, y, null);
		if(active)
		{
			g.setColor(Color.BLACK);
			g.drawRect(x + offset,  y, BUTTON_WIDTH, BUTTON_HEIGHT);
		}
	}
	public boolean contains(Point2D.Double p)
	{
		return (p.x > x && p.x < x + BUTTON_WIDTH && p.y > y && p.y < y + BUTTON_HEIGHT);
	}
	
}
