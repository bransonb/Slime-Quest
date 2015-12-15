package entities.playerstates;

import java.awt.image.BufferedImage;

import entities.Player;
import foundation.Game;
import foundation.InputBlock;

public abstract class PlayerState {

	public abstract void updateFrames(long currentTime, Player player);
	
	public void update(InputBlock ib, Game game, Player player)
	{
		
	}
	
	public abstract void updateState(InputBlock ib, Game game, Player player);
	
	public abstract BufferedImage getFrame(Player player);
	
}
