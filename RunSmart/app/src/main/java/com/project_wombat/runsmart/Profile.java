package com.project_wombat.runsmart;

/**
 * Created by thoma on 10/23/16
 */

public class Profile {
	private int id;
	private String name;
	private int age;
	private boolean sex; // Male = true; Female = false;
	private int weight; //kg
	private int height; //cm

	public Profile(){}
	public Profile(String name, int age, boolean sex, int weight, int height)
	{
		this.id = 1;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.weight = weight;
		this.height = height;
	}

	public void setName(String name) { this.name = name; }
	public void setAge(int age)	{ this.age = age; }
	public void setSex(boolean sex)	{ this.sex = sex; }
	public void setWeight(int weight) { this.weight = weight; }
	public void setHeight(int height) {	this.height = height; }

	public int getId() { return id; }
	public String getName()	{ return name; }
	public int getAge()	{ return age; }
	public boolean getSex() { return sex; }
	public int getWeight() { return weight; }
	public int getHeight() { return height; }
}