package foundation;

//This class is not being used.  Decide if it should be used again.  Consider deleting.
public class DrawThread implements Runnable{

	Game game;
	
	public DrawThread(Game game)
	{
		this.game = game;
	}
	
	public void run() {
		game.updateGraphics(game.gc.getGraphics());
		
	}

}
