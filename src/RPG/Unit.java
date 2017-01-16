package RPG;

import javax.swing.*;
import java.io.*;

public class Unit implements Serializable{
	final String CHARACTER_ART_PATH = "Art\\"; //put before character name to get through the folders
	
	public ImageIcon graphic;
	public ImageIcon portrait;
	public ImageIcon inactiveGraphic;
	public ImageIcon attackAnimation;
	public boolean placed;
	public transient Tile occupiedSpace;
	public String name;
	public int moveRange;
	public int[] attackRange;
	public int allignment;
	public boolean active = false;
	public Equipment weapon;
	
	public int maxEnthusiasm;
	public int enthusiasm;
	public int state;
	public int toughness;
	public int maxFocus;
	public int focus;
	public int dilligence;
	public int strength;
	public int speed;
	public int avoidance;
	public int bleedThreshold;
	public int glory; // amount of hp recovered
	public String battleCry;  //want an array, index corresponds to state
	//level, exp, skills, buffs
	
	public Unit(String unitName, int allignmentInput){
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + unitName + "Inactive.png");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		moveRange = 6;
		attackRange = new int[]{1};
		weapon = new Equipment("Fists");
		allignment = allignmentInput;
	}
	
	public Unit(String unitName, int allignmentInput, String weaponName){
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + unitName + "Inactive.png");
		attackAnimation = new ImageIcon(CHARACTER_ART_PATH + unitName + "Attack.gif");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		moveRange = 6;
		allignment = allignmentInput;
		weapon = new Equipment(weaponName);
		attackRange = weapon.getRange();
		
		maxEnthusiasm = 30;
		enthusiasm = maxEnthusiasm;
		state = 1;
		toughness = 4;
		maxFocus = 5;
		focus = maxFocus;
		dilligence = 7;
		strength = 3;
		bleedThreshold = toughness/2 + 1;
		glory = 2;
		avoidance = 5;
		speed = 5;
		battleCry = "I will defeat you!";
		
	}
	
	public Unit(String unitName, int allignmentInput, String weaponName, int maxEnthusiasmInput, int toughnessInput, int maxFocusInput,
			int dilligenceInput, int strengthInput, int gloryInput, int avoidanceInput, int speedInput){
		graphic = new ImageIcon(CHARACTER_ART_PATH + unitName + ".png");
		inactiveGraphic = new ImageIcon(CHARACTER_ART_PATH + unitName + "Inactive.png");
		attackAnimation = new ImageIcon(CHARACTER_ART_PATH + unitName + "Attack.gif");
		portrait = new ImageIcon(CHARACTER_ART_PATH + unitName + "Portrait.png");
		placed = false;
		name = unitName;
		moveRange = 6;
		allignment = allignmentInput;
		weapon = new Equipment(weaponName);
		attackRange = weapon.getRange();
		
		maxEnthusiasm = maxEnthusiasmInput;
		enthusiasm = maxEnthusiasm;
		state = 1;
		toughness = toughnessInput;
		maxFocus = maxFocusInput;
		focus = maxFocus;
		dilligence = dilligenceInput;
		strength = strengthInput;
		bleedThreshold = toughness/2 + 1;
		glory = gloryInput;
		avoidance = avoidanceInput;
		speed = speedInput;
		battleCry = "I will defeat you!";
		
	}
	
	public void setPlaced(boolean input){
		placed = input;
	}
	
	public void calculateState(){
		double hpPercent = (double)enthusiasm / (double)maxEnthusiasm;
		if(hpPercent>= .66){
			state = 1;
		}
		else if(hpPercent>= .33){
			state = 0;
		}
		else{
			state = 2;
		}
	}
	
	private int calculateAttack(){
		calculateState();
		
		int totalDamage;
		
		totalDamage = strength + weapon.attack;
		totalDamage *= Math.pow(1.3, state);
		
		return totalDamage;
	}
	
	public int receiveAttack(Unit attacker, Tile space){
		bleedThreshold = toughness/2 + 1;
		int damageReceived = attacker.calculateAttack() - toughness - space.defenseMod;
		
		if(damageReceived>bleedThreshold){
			damageReceived *= 1.3;
		}
		
		calculateState();
		
		return damageReceived;
	}
	
	public void die(){
		//save to file if no perma-death
		occupiedSpace.remove(this);
	}
	
	public void equip(Equipment newWeapon){
		weapon = newWeapon;
		attackRange = newWeapon.getRange();
	}
	
	public void apply(Buff theBigCheese){
		
	}
	
	public void expire(Buff smallerCheese){
		
	}
	
	public ImageIcon createAttackAnimation(){
		return new ImageIcon(CHARACTER_ART_PATH + name + "Attack.gif");
	}
	
	

}
