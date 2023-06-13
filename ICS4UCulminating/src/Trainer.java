// The trainer class is used to store the trainer's Pokemon
// Opponents will inherit this class. 

public class Trainer {
	private Pokemon[] pokemonList = new Pokemon[3];
	
	// Constructor 
	public Trainer() {
		// Randomly generates 3 Pokemon for the opposing trainer. 
		// Repeats are allowed. 
		int random;
		for (int i = 0; i<3; i++) {
			random = (int)(Math.random()*(59)) + 1;
			pokemonList[i] = Pokemon.pokeList.get(random);
			System.out.println(pokemonList[i]);
		}
	}
	
	public Pokemon[] getPokemonList() {
		return pokemonList;
	}
}
