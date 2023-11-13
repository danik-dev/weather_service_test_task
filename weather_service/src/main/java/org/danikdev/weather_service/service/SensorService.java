package org.danikdev.weather_service.service;

import org.danikdev.weather_service.dto.MeasurementDto;
import org.danikdev.weather_service.dto.SensorDto;
import org.danikdev.weather_service.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface SensorService {
    List<SensorDto> getAllActiveSensors();
    List<SensorDto> getAllSensors();
    Optional<MeasurementDto> getLastUpdateBySensor(String sensor_name) throws ServiceException;
    List<MeasurementDto> getLast20UpdatesBySensor(String sensor_key) throws ServiceException;
    List<MeasurementDto> getLastUpdatesOfAllActiveSensors();
    String register(SensorDto sensorDto) throws ServiceException;
    void receiveData(String accessKey, MeasurementDto condition) throws ServiceException;

}
