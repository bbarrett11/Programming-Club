package RPG;

public class Equipment {
	public String name;
	public int attack;
	private int range;
	public int accuracy;
	
	public Equipment(String nameInput){
		name = nameInput;
		
		if(name.substring(0,5).equals("sword")){
			range = 1;
			attack = 4;
			accuracy = 110;
			name = nameInput.substring(5);
		}
		else if(name.substring(0,3).equals("bow")){
			range = -2;
			attack = 2;
			accuracy = 90;
			name = nameInput.substring(3);
		}
		else if(name.substring(0,5).equals("throw")){
			range = 3;
			attack = 2;
			accuracy = 95;
			name = nameInput.substring(5);
		}
		else{
			range = 1;
			attack = 0;
			accuracy = 100;
			name = nameInput;
		}
	}
	
	public int getRange(){
		return range;
	}

}
