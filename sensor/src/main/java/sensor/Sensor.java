package sensor;

import java.util.Random;

import static utils.Service.register;
import static utils.Service.send_data;

public class Sensor extends Thread {
    private final String name;
    private final int faultChancePercentage;
    private final Random random = new Random();

    public Sensor(String name, int faultChancePercentage) {
        this.name = name;
        this.faultChancePercentage = 100 - faultChancePercentage;
    }

    @Override
    public void run() {
        double value;
        boolean raining;
        String accessKey = register(name);
        if (accessKey == null) {
            return;
        }
        while(random.nextInt(faultChancePercentage) != 0) {
            value = (double) Math.round((random.nextDouble(200) - 100) * 1000) / 1000;

            raining = random.nextBoolean();
            send_data(name, accessKey, value, raining);
            try {
                sleep((random.nextInt(12) + 3) * 100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(name + " - stop working");


    }
}
