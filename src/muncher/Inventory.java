package muncher;

import java.io.File;
import java.util.ArrayList;

public class Inventory {

	//Craft Recipes
	public static CraftingRecipe sledgehammer = 
		new CraftingRecipe(Item.METAL_BLOCK, Item.POLE, Item.EMPTY, Item.SLEDGEHAMMER);
	public static CraftingRecipe metalblock = 
		new CraftingRecipe(Item.METAL, Item.METAL, Item.METAL, Item.METAL_BLOCK);
	public static CraftingRecipe bread = 
		new CraftingRecipe(Item.BREAD_SLICE, Item.BREAD_SLICE, Item.BREAD_SLICE, Item.BREAD_LOAF);
	public static CraftingRecipe apple_pie =
		new CraftingRecipe(Item.APPLE, Item.PIE, Item.EMPTY, Item.APPLE_PIE);
	public static CraftingRecipe pole =
		new CraftingRecipe(Item.STICK, Item.STICK, Item.STICK, Item.POLE);
	public static CraftingRecipe bow =
		new CraftingRecipe(Item.STICK, Item.STICK, Item.STRING, Item.BOW);
	public static CraftingRecipe cleaver =
		new CraftingRecipe(Item.STICK, Item.METAL, Item.EMPTY, Item.CLEAVER);
	public static CraftingRecipe guillotine =
		new CraftingRecipe(Item.METAL, Item.METAL, Item.POLE, Item.GUILLOTINE);
	public static CraftingRecipe metalslice =
		new CraftingRecipe(Item.SCRAP, Item.SCRAP, Item.SCRAP, Item.METAL);
	public static CraftingRecipe can =
		new CraftingRecipe(Item.METAL, Item.SCRAP, Item.SCRAP, Item.CAN);
	public static CraftingRecipe hammer = 
		new CraftingRecipe(Item.STICK, Item.METAL_BLOCK, Item.EMPTY, Item.HAMMER);
	public static CraftingRecipe badge =
		new CraftingRecipe(Item.METAL, Item.STICK, Item.STICK, Item.BADGE);
	public static CraftingRecipe can_crest =
		new CraftingRecipe(Item.METAL, Item.CAN, Item.CAN, Item.CAN_CREST);
	public static CraftingRecipe badge_can =
		new CraftingRecipe(Item.BADGE, Item.CAN_CREST, Item.EMPTY, Item.BADGE_CAN);
	// Decraft Recipes
	public static DecraftRecipe dmetalblock =
		new DecraftRecipe(Item.METAL_BLOCK, new Item[]{Item.METAL, Item.METAL, Item.METAL});
	public static DecraftRecipe dpole = 
		new DecraftRecipe(Item.POLE, new Item[]{Item.STICK, Item.STICK, Item.STICK});
	public static DecraftRecipe dmetalslice =
		new DecraftRecipe(Item.METAL, new Item[]{Item.SCRAP, Item.SCRAP, Item.SCRAP});
	
	
	public enum Item {
		EMPTY("-", 0), FISH("Fish", 1),
		APPLE("Apple", 2), PIE("Pie", 3),
		BREAD_LOAF("BreadLoaf", 4), BREAD_SLICE("Bread", 5),
		POLE("WoodenPole", 6), METAL("MetalSlice", 7),
		METAL_BLOCK("MetalBlock", 8), SLEDGEHAMMER("Sledgehammer", 9),
		BLADE("Blade", 10), APPLE_PIE("ApplePie", 11),
		STICK("Stick", 12), STRING("String", 13),
		BOW("Bow", 14), CLEAVER("Cleaver", 15),
		GUILLOTINE("Guillotine", 16), SCRAP("ScrapMetal", 17),
		CAN("Can", 18), HAMMER("Hammer", 19),
		BADGE("Badge", 20), CAN_CREST("CanCrest", 21),
		BADGE_CAN("The Badge of Cans", 22);
		
		public String name;
		public int id;
		Item(String name, int id) {
			this.name = name;
			this.id = id;
		}
		
		public static String getNameFromID(int id) {
			for (Item i : values()) {
				if (i.id == id) {
					return i.name;
				}
			}
			return "--";
		}
		
		public static Item nameToItem(String item) {
			for (Item i : values()) {
				if (item.equalsIgnoreCase(i.name)) {
					return i;
				}
			}
			return EMPTY;
		}
	}

	int[] items = new int[10];
	String username;
	
	public Inventory(String username) {
		this.username = username;
		ArrayList<String> data = 
				IOTools2.readFile(new File("data/inventories/" + CommandHandler._user + ".txt"));
		
		for (int i = 0; i < data.get(0).split(",").length; i++) {
			items[i] = Integer.parseInt(data.get(0).split(",")[i]);
		}
	}
	
	public void add(Item item) {
		int space = 0;
		for (int i = 0; i < items.length; i++) {
			if ((Item.nameToItem(Item.getNameFromID(items[i])) == Item.EMPTY)) {
				space = i;
				break;
			}
			if (i == items.length-1) {
				return;
			}
		}
		
		items[space] = item.id;
		System.out.println(space + " " + item.id);
		writeToFile();
	}
	
	public void remove(Item item) {
		int spaceToRemove = 0;
		for (int i = 0; i < items.length; i++) {
			if ((Item.nameToItem(Item.getNameFromID(items[i])).equals(item))) {
				items[i] = 0;
				spaceToRemove = i;
				break;
			}
		}
		writeToFile();
		
		System.out.println(spaceToRemove + "  " + item);
		System.out.println(items[spaceToRemove]);
	}
	
	public boolean contains(Item i) {
		if (i == Item.EMPTY) {
			return true;
		}
		for (int c : items) {
			if (Item.nameToItem(Item.getNameFromID(c)) == i) {
				return true;
			}
		}
		return false;
	}
	
	public void writeToFile() {
		String n = "";
		for (int i = 0; i < items.length; i++) {
			if (i != items.length - 1) {
				n = n.concat(items[i] + ",");
			} else {
				n = n.concat(""+items[i]);
			}
		}
		ArrayList<String> data = new ArrayList<String>();
		data.add(n);
		IOTools2.writeToFile(new File("data/inventories/" + username + ".txt"), data);
	}
}
