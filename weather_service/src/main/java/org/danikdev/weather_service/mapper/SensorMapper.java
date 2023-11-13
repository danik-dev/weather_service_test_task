package org.danikdev.weather_service.mapper;

import org.danikdev.weather_service.dto.SensorDto;
import org.danikdev.weather_service.model.Sensor;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SensorMapper {
        SensorDto map(Sensor sensor);
        @InheritInverseConfiguration
        Sensor map(SensorDto sensorDto);
}
