package RPG;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Unit implements Serializable {
	final String CHARACTER_ART_PATH = "Art\\"; // put before character name to
												// get through the folders

	public transient ImageIcon graphic;
	public transient ImageIcon portrait;
	public transient ImageIcon inactiveGraphic;
	public transient ImageIcon attackAnimation;
	public boolean placed; 
	public transient Tile occupiedSpace;
	public String name;
	public String type;
	public int moveRange;//number of squares can move
	public int[] attackRange;
	public int allignment; //team
	public boolean active = false;
	public Equipment weapon;
	public Equipment armorSet;
	
	public int maxEnthusiasm; //max health
	public int enthusiasm; //current health
	public int state; //how happy one is
	public int toughness; // armor
	public int maxFocus; // max mana
	public int focus; //current mana
	public int diligence; //magic stat
	public int strength; //physical damage stat
	public int speed; //if attacks first
	public int avoidance; // extra chance to dodge
	public int bleedThreshold; //threshold required to overcome in order to gain health on hit 
	public int glory; // amount of hp recovered
	public int critToughness; // crit armor
	public String battleCry; // want an array, index corresponds to state
	// level, exp, skills, buffs
	public int level=1;
	
	public ArrayList<Buff> buffList;

	public Unit(String unitName, int allignmentInput) {
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + unitName + "Inactive.png");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		moveRange = 6;
		attackRange = new int[] { 1 };
		weapon = new Equipment("Fists","Weapon",level);
		armorSet = new Equipment("Cloth:Cloth Armor","ArmorSet",level);
		allignment = allignmentInput;
	}

	public Unit(String unitName, int allignmentInput, String weaponName,String armorSetName) {
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + unitName + "Inactive.png");
		attackAnimation = new ImageIcon(CHARACTER_ART_PATH + unitName + "Attack.gif");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		moveRange = 6;
		allignment = allignmentInput;

		maxEnthusiasm = 30;
		enthusiasm = maxEnthusiasm;
		state = 1;
		toughness = 4;
		maxFocus = 5;
		focus = maxFocus;
		diligence = 7;
		strength = 3;
		bleedThreshold = toughness / 2 + 1;
		glory = 2;
		avoidance = 5;
		speed = 5;
		battleCry = "I will defeat you!";
		
		buffList = new ArrayList<Buff>();
		
		weapon = new Equipment(weaponName,"Weapon",level);
		attackRange = weapon.getRange();

		equipArmor(new Equipment(armorSetName,"ArmorSet",level));
	}

	public Unit(String unitName, String unitType, int allignmentInput, String weaponName, int maxEnthusiasmInput,
			int toughnessInput, int maxFocusInput, int dilligenceInput, int strengthInput, int gloryInput,
			int avoidanceInput, int speedInput, String armorSetName) {
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + "Inactive\\" + unitName + "Inactive.png");
		attackAnimation = new ImageIcon(CHARACTER_ART_PATH + unitName + "Attack.gif");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		type = unitType;
		moveRange = 6;
		allignment = allignmentInput;

		maxEnthusiasm = maxEnthusiasmInput;
		enthusiasm = maxEnthusiasm;
		state = 1;
		toughness = toughnessInput;
		maxFocus = maxFocusInput;
		focus = maxFocus;
		diligence = dilligenceInput;
		strength = strengthInput;
		bleedThreshold = toughness / 2 + 1;
		glory = gloryInput;
		avoidance = avoidanceInput;
		speed = speedInput;
		battleCry = "I will defeat you!";

		buffList = new ArrayList<Buff>();
		
		weapon = new Equipment(weaponName,"Weapon",level);
		attackRange = weapon.getRange();

		equipArmor(new Equipment(armorSetName,"ArmorSet",level));

	}

	public void findImages() {
		graphic = new ImageIcon(CHARACTER_ART_PATH + type + "\\" + name + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + "Inactive\\" + name + "Inactive.png");
		attackAnimation = new ImageIcon(CHARACTER_ART_PATH + name + "Attack.gif");
		portrait = new ImageIcon(CHARACTER_ART_PATH + "\\portrait"+name + "Portrait.png");
	}

	public void setPlaced(boolean input) {
		placed = input;
	}

	public void calculateState() {
		double hpPercent = (double) enthusiasm / (double) maxEnthusiasm;
		if (hpPercent >= .66) {
			state = 1;
		} else if (hpPercent >= .33) {
			state = 0;
		} else {
			state = 2;
		}
	}

	private int calculateAttack() {
		calculateState();

		int totalDamage;
		
		totalDamage = strength + weapon.getAttack();
		totalDamage *= Math.pow(1.3, state);

		return totalDamage;
	}

	public int receiveAttack(Unit attacker, Tile space) {
		bleedThreshold = maxEnthusiasm/3;
		/*if (damageReceived > bleedThreshold) {
			damageReceived *= 1.3;
		}*/
		
		int damageReceived = attacker.calculateAttack() - toughness - space.defenseMod;
		//System.out.println(effects);
		
		calculateState();

		return damageReceived;
	}

	public void die() {
		// save to file if no perma-death
		occupiedSpace.remove(this);
	}

	public void equipWeapon(Equipment newWeapon) {
		weapon = newWeapon;
		attackRange = newWeapon.getRange();
	}
	
	public void equipArmor(Equipment newArmor) {
		if(armorSet != null)
		{
			deEquip(armorSet);
		}
		armorSet = newArmor;
		armorSet.getArmorEffects(this);
	}
	
	public void equipAccessory(Equipment newAccessory) {
		
	}

	public void deEquip(Equipment oldEquipment)
	{
		if(oldEquipment.type.equals("armorSet"))
		{
			oldEquipment.removeArmorEffects(this);
		}
	}

	public void apply(Buff theBigCheese) {

	}

	public void expire(Buff smallerCheese) {

	}

	public ImageIcon createAttackAnimation() {
		return new ImageIcon(CHARACTER_ART_PATH + name + "Attack.gif");
	}

}
