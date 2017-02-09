package RPG;

import java.io.*;

public class CharacterCreator implements Serializable {
	public static void main(String[] args) throws IOException {
		Unit hope = new Unit("axeBandit", "bandit", 1, "swordSword", 15, 4, 2, 0, 8, 2, -5, 4);

		Unit senorSavesTheDay = new Unit("SenorSavesTheDay", "Hero", 0, "swordSword of Legends", 15, 4, 2, 0, 8, 2, -5, 4);
		makeCharacter(senorSavesTheDay, "Saves\\Roster\\");
	}
	
	public static void makeCharacter(Unit person, String destination) throws FileNotFoundException, IOException{
		
		FileOutputStream outStream = new FileOutputStream(destination + person.name + ".dat");
		
		ObjectOutputStream objectOutputFile = new ObjectOutputStream(outStream);

		objectOutputFile.writeObject(person);

		outStream.close();
	}

}
