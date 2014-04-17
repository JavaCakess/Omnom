package muncher;

public enum ChickType {

	
	
	BW_IND_CHICK("Blue White Indonesian Chick", 2.5, 1.75, "Indonesia", ChickQuality.RARE,
					"A cyan small chick found in Indonesia. Walks quite quickly, and is usually hidden."),
	ENGLISH_FLUFFY("English Fluffy", 3.5, 2.15, "England", ChickQuality.COMMON, 
					"A small fluffy yellow chick in England. It is quite tall but slow."),
	ZMBAB_FISH_CHICK("Zimbabwean Fish Chick", 3, 2.85, "Zimbabwe", ChickQuality.RARE,
					"A Zimbabwean chick. It is found in Zimbabwe near ponds. It is quite fat."),
	MUNCHER_CHICK("Muncher Chick", 2.25, 2.25, "Japan", ChickQuality.VERY_RARE,
			"A black chick with yellow legs. It is extremely slow, and feasts on leaves on the ground.");
	
	public String name, foundIn, description;
	public double width, height;
	public ChickQuality quality;
	ChickType(String name, double height, double width, String foundIn, ChickQuality quality,
			String desc) {
		this.height = height;
		this.width = width;
		this.foundIn = foundIn;
		this.quality = quality;
		description = desc;
	}
	
	enum ChickQuality {
		COMMON("Common"), RARE("Rare"), VERY_RARE("Very Rare");
		
		public String name;
		ChickQuality(String name) {
			this.name = name;
		}
		
	}
}
