package org.danikdev.weather_service.repository;

import org.danikdev.weather_service.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    Measurement findTopBySensorIdOrderByFixedAtDesc(Long sensorId);

    List<Measurement> findTop20BySensorIdOrderByFixedAtDesc(Long sensorId);

    @Query("SELECT NEW org.springframework.data.util.Pair(c.sensor.id, MAX(c.fixedAt)) FROM Measurement c GROUP BY c.sensor.id")
    List<Pair<Long, LocalDateTime>> getLastMeasurementTimeByEachSensor();
}
