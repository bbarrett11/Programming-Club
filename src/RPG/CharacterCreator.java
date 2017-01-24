package RPG;

import java.io.*;

public class CharacterCreator implements Serializable {
	public static void main(String[] args) throws IOException {
		Unit hope = new Unit("axeBandit", "bandit", 1, "swordSword", 15, 4, 2, 0, 8, 2, -5, 4);

		FileOutputStream outStream = new FileOutputStream("Units\\Test" + hope.name + ".dat");// "Test"
																								// will
																								// be
																								// replaced
																								// by
																								// level
																								// name
		ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);

		objectOutputFile.writeObject(hope);

		outStream.close();
	}

}
