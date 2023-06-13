
import java.io.*;

public class RayTesting {

	public static void main(String[] args) throws IOException {
		Main.initialize();
		Player ray = new Player ("Fire");
		Trainer ash = new Trainer();
		Battle battle = new Battle(ray, ash);
	}

}
