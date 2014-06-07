package dev.blacksheep.trif.classes;

public class Item {
	String name, image;

	public Item(String name, String image) {
		this.name = name;
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public String getImage() {
		return image;
	}
}
