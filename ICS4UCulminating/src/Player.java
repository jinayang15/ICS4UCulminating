// The Player class is used to store the user's Pokemon, items, and money.
// It will be also used in battle to determine what the user will do, such as attack, switch pokemon,
// or use items. 

public class Player {
//	private int money;\
//  something for items
	private Pokemon[] pokemonList = new Pokemon[3];
	
	public Player (String type) {
		if (type.equals("Grass")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(1));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(31));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(55));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
		else if (type.equals("Fire")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(4));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(27));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(40));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
		else if (type.equals("Water")) {
			pokemonList[0] = new Pokemon (Pokemon.pokeList.get(7));
			pokemonList[1] = new Pokemon (Pokemon.pokeList.get(38));
			pokemonList[2] = new Pokemon (Pokemon.pokeList.get(42));
			for (Pokemon a: pokemonList) {
				System.out.println(a);
			}
		}
	}
}
