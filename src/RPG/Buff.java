package RPG;

public class Buff {
	public String name;
	public int strength;
	public int turnLength;
	
	public Buff(String name, int turnLength)
	{
		if(name.contains(":"))
		{
			strength = Integer.parseInt(name.split(":")[1]);
			name = name.split(":")[0];
		}
		this.name = name;
		this.turnLength = turnLength;
	}
	
	public void execute(Unit u)
	{
		switch(name)
		{
			case "Bleed":
				makeBleed(u);
				break;
			case "Disarm":
				System.out.println("Disarmed");
				break;
			case "Stun":
				System.out.println("Stunned");
				makeStun(u);
				break;
			default:
				break;
		}
		turnLength--;
		if(turnLength == 0)
			u.buffList.remove(this);
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
	}
	
	public String toString()
	{
		return name+ ": "+turnLength+ " Turn"+(turnLength > 1 ? "s":"")+" Left";
	}
}
