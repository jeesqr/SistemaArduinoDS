package client;

import java.util.Random;

public class SerialSimulator {

    private Random random = new Random();

    public String readData() {
        int x = random.nextInt(20) - 10;
        int y = random.nextInt(20) - 10;
        int z = random.nextInt(20) - 10;
        return x + "," + y + "," + z;
    }
}
