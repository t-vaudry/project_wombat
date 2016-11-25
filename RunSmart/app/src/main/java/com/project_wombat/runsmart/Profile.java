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
	private boolean useFacebook;
	private boolean useGoogleMaps;

	public Profile(){ this.name = "";}
	public Profile(String name, int age, boolean sex, int weight, int height, boolean facebook, boolean google_maps)
	{
		this.id = 1;
		this.name = name;
		this.age = age;
		this.sex = sex;
		this.weight = weight;
		this.height = height;
		this.useFacebook = facebook;
		this.useGoogleMaps = google_maps;
	}

	public void setName(String name) { this.name = name; }
	public void setAge(int age)	{ this.age = age; }
	public void setSex(boolean sex)	{ this.sex = sex; }
	public void setWeight(int weight) { this.weight = weight; }
	public void setHeight(int height) {	this.height = height; }
	public void setUseFacebook(boolean val) { useFacebook = val; }
	public void setUseGoogleMaps(boolean val) { useGoogleMaps = val; }

	public int getId() { return id; }
	public String getName()	{ return name; }
	public int getAge()	{ return age; }
	public boolean getSex() { return sex; }
	public int getWeight() { return weight; }
	public int getHeight() { return height; }
	public boolean getUseFacebook() { return useFacebook; }
	public boolean getUseGoogleMaps() { return useGoogleMaps; }
}