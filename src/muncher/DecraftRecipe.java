package muncher;

import java.util.ArrayList;

import muncher.Inventory.Item;

public class DecraftRecipe {
	
	public Item item;
	Item[] results;
	public static ArrayList<DecraftRecipe> list = new ArrayList<DecraftRecipe>();
	public DecraftRecipe(Item item, Item[] results) {
		this.item = item;
		this.results = results;
		list.add(this);
	}
	
}
