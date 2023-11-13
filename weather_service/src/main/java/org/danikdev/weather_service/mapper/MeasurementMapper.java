package org.danikdev.weather_service.mapper;

import org.danikdev.weather_service.dto.MeasurementDto;
import org.danikdev.weather_service.model.Measurement;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    MeasurementDto map(Measurement measurement);

    @InheritInverseConfiguration
    Measurement map(MeasurementDto measurementDto);

    @AfterMapping
    default void fillSensorName(@MappingTarget MeasurementDto measurementDto, Measurement measurement) {
        if (measurement != null && measurement.getSensor() != null) {
            measurementDto.setSensorName(measurement.getSensor().getName());
        }
    }

}