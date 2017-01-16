package RPG;

import java.io.*;

public class Equipment implements Serializable{
	public String name;
	public int attack;
	private int[] range;
	public int accuracy;
	
	public Equipment(String nameInput){
		name = nameInput;
		
		if(name.substring(0,5).equals("sword")){
			range = new int[]{1};
			attack = 4;
			accuracy = 110;
			name = nameInput.substring(5);
		}
		else if(name.substring(0,3).equals("bow")){
			range = new int[]{-2, -3};
			attack = 2;
			accuracy = 90;
			name = nameInput.substring(3);
		}
		else if(name.substring(0,5).equals("throw")){
			range = new int[]{3};
			attack = 2;
			accuracy = 95;
			name = nameInput.substring(5);
		}
		else{
			range = new int[]{1};
			attack = 0;
			accuracy = 100;
			name = nameInput;
		}
	}
	
	public int[] getRange(){
		return range;
	}

}
