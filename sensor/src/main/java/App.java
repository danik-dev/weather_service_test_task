import sensor.Sensor;

import java.util.List;

public class App {
    public static void main(String[] args) throws InterruptedException {
        List<Sensor> sensors = List.of(
                new Sensor("Sensor 1", 1),
                new Sensor("Sensor 2", 90),
                new Sensor("Sensor 3", 1),
                new Sensor("Sensor 4", 70),
                new Sensor("Sensor 5", 50)
        );

        for (Sensor sensor: sensors) {
            sensor.start();
        }

        for (Sensor sensor: sensors) {
            sensor.join();
        }
    }
}