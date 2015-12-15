package cells;

import foundation.Game;

//TODO - Only part of the Game class that can use this is using this.
//Determine if using threads here actually helps.  If it does, change
//the other parts of Game to use it.  If it doesn't, change the parts
//of Game that use it to not use it & delete this class.
public class LoadingThread extends Thread{
	public static final int SHIFT_LEFT = 1;
	public static final int SHIFT_RIGHT = 2;
	public static final int SHIFT_UP = 3;
	public static final int SHIFT_DOWN = 4;

	int direction;
	Game game;
	
	
	public LoadingThread(Sector s, int indexX, int indexY, Game game, int direction)
	{
	this.direction = direction;
	this.game = game;
	}
	
	
	private void shiftSectorsLeft() {
		game.loading = true;
		//
		if (game.autoSave)
			for (int j = 0; j < Game.SECT_COUNT_Y; j++) {
				game.saveSect(game.sectors[Game.SECT_COUNT_X - 1][j], game.sectIndexX
						+ Game.SECT_COUNT_X - 1, game.sectIndexY + j);
			}

		for (int i = Game.SECT_COUNT_X - 1; i > 0; i--) {
			for (int j = 0; j < Game.SECT_COUNT_Y; j++)
				game.sectors[i][j] = game.sectors[i - 1][j];
		}

		game.sectIndexX--;

		for (int j = 0; j < Game.SECT_COUNT_Y; j++) {
			game.sectors[0][j] = new Sector();
			game.loadSect(game.sectors[0][j], game.sectIndexX, game.sectIndexY + j);
		}
		
		game.loading = false;
	}

	private void shiftSectorsRight() {
		//
		game.loading = true;
		//
		if (game.autoSave)
			for (int j = 0; j < Game.SECT_COUNT_Y; j++) {
				game.saveSect(game.sectors[0][j], game.sectIndexX, game.sectIndexY + j);
			}
		for (int i = 0; i < Game.SECT_COUNT_X - 1; i++) {
			for (int j = 0; j < Game.SECT_COUNT_Y; j++)
				game.sectors[i][j] = game.sectors[i + 1][j];
		}

		game.sectIndexX++;

		for (int j = 0; j < Game.SECT_COUNT_Y; j++) {
			game.sectors[Game.SECT_COUNT_X - 1][j] = new Sector();
			game.loadSect(game.sectors[Game.SECT_COUNT_X - 1][j], game.sectIndexX + Game.SECT_COUNT_X
					- 1, game.sectIndexY + j);
		}
		
		game.loading = false;
	}

	private void shiftSectorsUp() {
		//
		game.loading = true;
		//
		if (game.autoSave)
			for (int i = 0; i < Game.SECT_COUNT_X; i++) {
				game.saveSect(game.sectors[i][Game.SECT_COUNT_Y - 1], game.sectIndexX + i,
						game.sectIndexY + Game.SECT_COUNT_Y - 1);
			}
		for (int j = Game.SECT_COUNT_Y - 1; j > 0; j--) {
			for (int i = 0; i < Game.SECT_COUNT_X; i++)
				game.sectors[i][j] = game.sectors[i][j - 1];
		}

		game.sectIndexY--;

		for (int i = 0; i < Game.SECT_COUNT_X; i++) {
			game.sectors[i][0] = new Sector();
			game.loadSect(game.sectors[i][0], game.sectIndexX + i, game.sectIndexY);
		}
		
		game.loading = false;
	}

	private void shiftSectorsDown() {

		game.loading = true;
		//
		if (game.autoSave)
			for (int i = 0; i < Game.SECT_COUNT_X; i++) {
				game.saveSect(game.sectors[i][0], game.sectIndexX + i, game.sectIndexY);
			}
		for (int j = 0; j < Game.SECT_COUNT_Y - 1; j++) {
			for (int i = 0; i < Game.SECT_COUNT_X; i++)
				game.sectors[i][j] = game.sectors[i][j + 1];
		}

		game.sectIndexY++;

		for (int i = 0; i < Game.SECT_COUNT_X; i++) {
			game.sectors[i][Game.SECT_COUNT_Y - 1] = new Sector();
			game.loadSect(game.sectors[i][Game.SECT_COUNT_Y - 1], game.sectIndexX + i, game.sectIndexY
					+ Game.SECT_COUNT_Y - 1);
		}
		
		game.loading = false;
	}
	
	public void run()
	{
		switch(direction)
		{
		case SHIFT_LEFT:
			shiftSectorsLeft();
			break;
		case SHIFT_RIGHT:
			shiftSectorsRight();
			break;
		case SHIFT_UP:
			shiftSectorsUp();
			break;
		case SHIFT_DOWN:
			shiftSectorsDown();
			break;
		default:
			break;
		}
		game.loading = false;
	}
}
