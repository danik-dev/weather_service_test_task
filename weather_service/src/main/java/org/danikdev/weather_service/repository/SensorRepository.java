package org.danikdev.weather_service.repository;

import org.danikdev.weather_service.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, String> {
    List<Sensor> findSensorsByEnabled(boolean enabled);

    Sensor findByName(String name);

    Sensor findByAccessKey(String accessKey);

    @Modifying
    @Query("UPDATE Sensor s SET s.enabled = false WHERE s.id IN :ids")
    void disableSensors(@Param("ids") Collection<Long> ids);
}
