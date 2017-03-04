package RPG;

public class Buff {
	public String name;
	public int strength;
	public int turnLength;
	public Equipment takenEquipment;
	
	/**
	 * Class that can execute buffs on units
	 * @param name The name of the buff, if Bleed must contain :\d
	 * @param turnLength The number of turns (effected unit) the buff will be active
	 */
	public Buff(String name, int turnLength)
	{
		if(name.contains(":"))
		{
			strength = Integer.parseInt(name.split(":")[1]);
			name = name.split(":")[0];
		}
		this.name = name;
		this.turnLength = turnLength;
		takenEquipment = new Equipment("Fists:Fists","Weapon",1);
	}
	
	/**
	 * Method that executes at the start of the effected unit's turn
	 * @param u The unit the buff is on
	 */
	public void execute(Unit u)
	{
		switch(name)
		{
			case "Bleed":
				makeBleed(u);
				break;
			case "Disarm":
				makeDisarm(u);
				System.out.println("Disarmed");
				break;
			case "Stun":
				System.out.println("Stunned");
				makeStun(u);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Method that is executed at the beginning of the other team's turn to see if the buff expires
	 * @param u The unit effected by the buff
	 */
	public void checkEnd(Unit u)
	{
		turnLength--;
		if(turnLength == 0)
		{
			giveBack(u);
			u.buffList.remove(this);
		}
	}
	
	public void makeBleed(Unit u)
	{
		System.out.println(u.name + " Took "+ strength + " damage");
		u.enthusiasm-=strength;
		if(u.enthusiasm <=0) u.die();
	}
	
	public void makeStun(Unit u)
	{
		u.active = false;
		//u.occupiedSpace.placeInactive(u);
	}
	
	public void makeDisarm(Unit u)
	{
		if(takenEquipment.name.equals("Fists"))
		{
		takenEquipment = u.weapon;
		System.out.println(takenEquipment);
		u.equipWeapon(new Equipment("Fists:Fists","Weapon",u.level));
		}
	}
	
	public void giveBack(Unit u)
	{
		u.equipWeapon(takenEquipment);
		System.out.println("give" + takenEquipment.name);
	}

	public String toString()
	{
		return name+ ": "+turnLength+ " Turn"+(turnLength > 1 ? "s":"")+" Left";
	}
}
