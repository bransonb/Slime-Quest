package cellsmenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import cells.blocktypes.HatchBlock;
import entities.Enemy;
import entities.enemies.*;
import foundation.Game;
import foundation.InputBlock;

public class EnemyMenu {
	Rectangle2D.Double closedHitbox;
	Rectangle2D.Double openHitbox;
	Rectangle2D.Double currentHitbox;
	int eType = 0;
	int openState = 0;
	public static final int OS_CLOSED = 0;
	public static final int OS_OPENING = 1;
	public static final int OS_OPEN = 2;
	public static final int OS_CLOSING = 3;
	public static final int OPEN_SPEED = 4000;
	public ArrayList<EnemyButton> buttons;
	public static BufferedImage[] buttonIcons;
	public EnemyButton activeButton;
	
	public EnemyMenu(Rectangle2D.Double activeBox)
	{
		this.openHitbox = activeBox;
		closedHitbox = new Rectangle2D.Double(openHitbox.x + openHitbox.width * 0.9, openHitbox.y, openHitbox.width * 0.1, openHitbox.height);
		currentHitbox = new Rectangle2D.Double(closedHitbox.x, openHitbox.y, openHitbox.width, openHitbox.height);
		eType = 0;
		buttons = new ArrayList<EnemyButton>();
		for(int i = 0; i <= Enemy.NUM_OF_ENEMY_TYPES; i++)
		{
			int x = (int)(openHitbox.x + EnemyButton.BUTTON_WIDTH);
			int y = (int)(EnemyButton.BUTTON_HEIGHT * i * 2);
			while(y > openHitbox.height)
			{
				y -= openHitbox.height;
				x += EnemyButton.BUTTON_WIDTH * 2;
			}
			y -= Math.abs((y % (EnemyButton.BUTTON_HEIGHT * 2)));
			y += openHitbox.y;
			buttons.add(new EnemyButton(x, y, i, this));
		}
		activeButton = buttons.get(0);
		activeButton.active = true;
	}
	
	public boolean menuContains(Point2D.Double point) {
		return currentHitbox.contains(point);
	}
	
	public static void loadImages()
	{
		buttonIcons = new BufferedImage[Enemy.NUM_OF_ENEMY_TYPES + 1];
		buttonIcons[0] = CellButton.nothingImg;
		buttonIcons[Enemy.EN_DISGUISE_BLOCK] = Game.getGame().robustLoadImage("rec/img/enemies/blockDisguise/icon.png", "BLOCK DISGUISE ICON");
		
	}
	
	public int getEnemyInt()
	{
		return eType;
	}

	public void update(InputBlock ib, Game game)
	{
		if(openState == OS_CLOSED && closedHitbox.contains(ib.mousePos) && ib.mouseLCPrime)
			openState = OS_OPENING;
		else if(openState == OS_OPEN && closedHitbox.contains(ib.mousePos)&& ib.mouseLCPrime)
			openState = OS_CLOSING;
		else if(openState == OS_OPENING && currentHitbox.x < openHitbox.x)
		{
			openState = OS_OPEN;
			currentHitbox.x = openHitbox.x;
		}
		else if(openState == OS_CLOSING && currentHitbox.x > closedHitbox.x)
		{
			openState = OS_CLOSED;
			currentHitbox.x = closedHitbox.x;
		}
		if(openState == OS_OPENING)
			currentHitbox.x -= ib.delta * OPEN_SPEED;
		else if(openState == OS_CLOSING)
			currentHitbox.x += ib.delta * OPEN_SPEED;
		if(openState == OS_OPEN && ib.mouseLCPrime)
		{
			Point2D.Double offsetMousePos = new Point2D.Double(ib.mousePos.x,
					ib.mousePos.y);
			offsetMousePos.x -= currentHitbox.x;
			for(EnemyButton eb : buttons)
			{
				if(eb.contains(offsetMousePos))
				{
					activeButton.active = false;
					eb.active = true;
					activeButton = eb;
					eType = eb.eType;
				}
			}
		}
	}
	
	public void draw(BufferedImage build)
	{
		Graphics g = build.getGraphics();
		g.setColor(Color.BLUE);
		g.fillRoundRect((int)currentHitbox.x, (int)currentHitbox.y, (int)currentHitbox.width, (int)currentHitbox.height, 30, 30);
		
		for(EnemyButton eb : buttons)
			eb.draw(build, (int)currentHitbox.x);
	}
	
	public Enemy makeEnemy(double xPos, double yPos, HatchBlock hb, int eType)
	{
		return Enemy.makeEnemy(xPos, yPos, hb, eType);
	}
}
