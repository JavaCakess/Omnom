package muncher;

import java.util.ArrayList;

import muncher.Inventory.Item;

public class CraftingRecipe {

	public Item item1, item2, item3, result;
	public static ArrayList<CraftingRecipe> list = new ArrayList<CraftingRecipe>();
	public CraftingRecipe(Item item1, Item item2, Item item3, Item result) {
		this.item1 = item1;
		this.item2 = item2;
		this.item3 = item3;
		this.result = result;
		list.add(this);
	}
	
	public boolean contains(Item i) {
		if (item1 == i || item2 == i || item3 == i) {
			return true;
		}
		return false;
	}
}
