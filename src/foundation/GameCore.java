package foundation;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

//import javax.swing.JFrame;

public class GameCore extends Applet implements KeyListener, MouseListener, MouseMotionListener{
	public static int NATIVE_X_RES = 800;
	public static int NATIVE_Y_RES = 600;
	
	
	Game game;
	Thread t;
	InputBlock ib;
	int kb_left = KeyEvent.VK_A;
	int kb_right = KeyEvent.VK_D;
	int kb_up = KeyEvent.VK_W;
	int kb_down = KeyEvent.VK_S;
	int kb_action1 = KeyEvent.VK_SPACE;
	int kb_suicide = KeyEvent.VK_T;
	int kb_quit = KeyEvent.VK_BACK_SPACE;
	
	public void init()
	{
		resize(NATIVE_X_RES, NATIVE_Y_RES);
		universalInit();
	}
	
	public void universalInit()
	{
		game = new Game(this);
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		ib = new InputBlock();
		t = new Thread(game);
		t.start();
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		game.stop();
	}

	public InputBlock getInput()
	{
		ib.updateClock();
		return new InputBlock(ib);
	}
	
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == kb_left)
			ib.keyLeft = true;
		else if(key.getKeyCode() == kb_right)
			ib.keyRight = true;
		else if(key.getKeyCode() == kb_up)
			ib.keyUp = true;
		else if(key.getKeyCode() == kb_down)
			ib.keyDown = true;
		else if(key.getKeyCode() == kb_action1)
			ib.keyAction1 = true;
		else if(key.getKeyCode() == kb_suicide)
			ib.keySuicide = true;
		else if(key.getKeyCode() == kb_quit)
			ib.keyQuit = true;

		
	}

	public void keyReleased(KeyEvent key) {
		if(key.getKeyCode() == kb_left)
			ib.keyLeft = false;
		else if(key.getKeyCode() == kb_right)
			ib.keyRight = false;
		else if(key.getKeyCode() == kb_up)
			ib.keyUp = false;
		else if(key.getKeyCode() == kb_down)
			ib.keyDown = false;
		else if(key.getKeyCode() == kb_action1)
			ib.keyAction1 = false;
		else if(key.getKeyCode() == kb_suicide)
			ib.keySuicide = false;
		else if(key.getKeyCode() == kb_quit)
			ib.keyQuit = false;
	}

	public void keyTyped(KeyEvent key) {
		
	}
	
	public static void main(String[] args)
	{
		GameCore gc = new GameCore();
		JFrame gameWindow = new JFrame("Slime Quest");
		gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameWindow.getContentPane().add(gc);
		gameWindow.pack();
		gameWindow.setSize(new Dimension(NATIVE_X_RES, NATIVE_Y_RES));
		gameWindow.setVisible(true);
		gc.universalInit();
		
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent me) {
		ib.mouseX = me.getX();
		ib.mouseY = me.getY();
	}

	public void mouseClicked(MouseEvent me) {
		if(me.getButton() == MouseEvent.BUTTON1)
			ib.mouseLCPrime = true;
		else if(me.getButton() == MouseEvent.BUTTON3)
			ib.mouseRCPrime = true;
	}

	public void mouseEntered(MouseEvent arg0) {
		
	}

	public void mouseExited(MouseEvent arg0) {
		
	}

	public void mousePressed(MouseEvent me) {
		if(me.getButton() == MouseEvent.BUTTON1)
			ib.mouseLC = true;
		else if(me.getButton() == MouseEvent.BUTTON3)
			ib.mouseRC = true;
	}

	public void mouseReleased(MouseEvent me) {
		if(me.getButton() == MouseEvent.BUTTON1)
			ib.mouseLC = false;
		else if(me.getButton() == MouseEvent.BUTTON3)
			ib.mouseRC = false;
	}
	
}
