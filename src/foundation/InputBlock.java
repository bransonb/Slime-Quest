package foundation;

import java.awt.geom.Point2D;

public class InputBlock {
	public boolean keyUp;
	public boolean keyDown;
	public boolean keyRight;
	public boolean keyLeft;
	public boolean keyAction1;
	public boolean keySuicide;
	public boolean keyQuit;
	public int mouseX;
	public int mouseY;
	public Point2D.Double mousePos;
	public boolean mouseLC;
	public boolean mouseRC;
	public boolean mouseLCPrime;
	public boolean mouseRCPrime;
	public long lastTime;
	public long currentTime;
	public double delta;
	public static double TIMESCALE = 1.0/1000000000;
	
	public InputBlock()
	{
		keyUp = false;
		keyDown = false;
		keyLeft = false;
		keyRight = false;
		keyAction1 = false;
		keySuicide = false;
		keyQuit = false;
		mouseX = 0;
		mouseY = 0;
		mouseLC = false;
		mouseRC = false;
		mouseLCPrime = false;
		mouseRCPrime = false;
		currentTime = System.nanoTime();
	}
	
	public InputBlock(InputBlock ib)
	{
		this.keyUp = ib.keyUp;
		this.keyLeft = ib.keyLeft;
		this.keyRight = ib.keyRight;
		this.keyDown = ib.keyDown;
		this.keyAction1 = ib.keyAction1;
		this.keySuicide = ib.keySuicide;
		this.keyQuit = ib.keyQuit;
		this.mouseX = ib.mouseX;
		this.mouseY = ib.mouseY;
		this.mousePos = new Point2D.Double(mouseX, mouseY);
		this.mouseLC = ib.mouseLC;
		this.mouseRC = ib.mouseRC;
		this.mouseLCPrime = ib.mouseLCPrime;
		ib.mouseLCPrime = false;
		this.mouseRCPrime = ib.mouseRCPrime;
		ib.mouseRCPrime = false;
		
		this.lastTime = ib.lastTime;
		this.currentTime = ib.currentTime;
		this.delta = ib.delta;
	}
	
	public void updateClock()
	{
		lastTime = currentTime;
		currentTime = System.nanoTime();
		delta = (currentTime - lastTime)*TIMESCALE;
	}
	
}
