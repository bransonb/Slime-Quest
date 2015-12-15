package cells;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import foundation.Game;

public class LoadingHelper {

	DataInput di;
	public static int RAF_MODE = 1;
	public static int DIS_MODE = 2;
	public int mode = 0;
	public LoadingHelper(String filename)
	{
		di = null;
		if(Game.getGame().jarMode)
		{
			String filename2 = filename.replaceFirst("rec\\/", "");
			di = new DataInputStream(this.getClass().getClassLoader().getResourceAsStream(filename2));
			mode = DIS_MODE;
		}
			if(di == null)
			{
			File f = new File(filename);
			try {
				di = new RandomAccessFile(f, "rw");
			} catch (FileNotFoundException e1) {
				System.out.println("SECTOR FILE NOT FOUND");
				e1.printStackTrace();
			}
			mode = RAF_MODE;
		}
	}
	public short readShort()
	{
		try {
			return di.readShort();
		} catch (IOException e) {
			System.out.println("FAILED TO LOAD DATA");
		}
		return -1;
	}
	
	public void writeShort(short s)
	{
		if(di instanceof RandomAccessFile)
			try {
				((RandomAccessFile)di).writeShort(s);
			} catch (IOException e) {
				//This will happen when in jar mode
			}
	}
	
	public void close()
	{
		if(di instanceof RandomAccessFile)
			try {
				((RandomAccessFile) di).close();
			} catch (IOException e) {
				//This will happen when in jar mode
			}
	}
	
}
