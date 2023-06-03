import java.io.*; 
public class Main {
	// We may need double typing rip
	public static void main(String[] args) throws IOException{
		System.out.println(PokeType.getTypeEffectiveness(4, 6));
		GameFunctions.importPokemon();
		GameFunctions.importMoves();
	}

}
