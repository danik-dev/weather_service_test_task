package org.danikdev.weather_service.service;


import lombok.RequiredArgsConstructor;
import org.danikdev.weather_service.dto.MeasurementDto;
import org.danikdev.weather_service.dto.SensorDto;
import org.danikdev.weather_service.exception.ServiceException;
import org.danikdev.weather_service.mapper.MeasurementMapper;
import org.danikdev.weather_service.mapper.SensorMapper;
import org.danikdev.weather_service.model.Measurement;
import org.danikdev.weather_service.model.Sensor;
import org.danikdev.weather_service.repository.MeasurementRepository;
import org.danikdev.weather_service.repository.SensorRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final SensorMapper sensorMapper;
    private final MeasurementMapper measurementMapper;

    @Override
    public List<SensorDto> getAllActiveSensors() {
        updateSensorsState();
        return sensorRepository.findSensorsByEnabled(true)
                .stream().map(sensorMapper::map).toList();
    }

    @Override
    public List<SensorDto> getAllSensors() {
        return sensorRepository.findAll()
                .stream().map(sensorMapper::map).toList();
    }

    @Override
    public Optional<MeasurementDto> getLastUpdateBySensor(String sensor_name) throws ServiceException {
        return Optional.of(measurementMapper.map(
                measurementRepository.findTopBySensorIdOrderByFixedAtDesc(
                        Optional.ofNullable(sensorRepository.findByName(sensor_name))
                                .orElseThrow(() -> new ServiceException("No such sensor")).getId())
        ));
    }

    @Override
    public List<MeasurementDto> getLast20UpdatesBySensor(String sensor_key) throws ServiceException {
        return measurementRepository.findTop20BySensorIdOrderByFixedAtDesc(
                        Optional.ofNullable(sensorRepository.findByAccessKey(sensor_key))
                                .orElseThrow(() -> new ServiceException("No sensor by received key found")).getId())
                .stream().map(measurementMapper::map).toList();
    }

    @Override
    public List<MeasurementDto> getLastUpdatesOfAllActiveSensors() {
        updateSensorsState();
        return sensorRepository.findSensorsByEnabled(true)
                .stream().map(Sensor::getId).map(measurementRepository::findTopBySensorIdOrderByFixedAtDesc)
                .map(measurementMapper::map).toList();

    }

    @Override
    public String register(SensorDto sensorDto) throws ServiceException {
        if (sensorRepository.findByName(sensorDto.getName()) == null) {
            sensorDto.setEnabled(true);
            Sensor s = sensorMapper.map(sensorDto);
            s.setAccessKey(UUID.randomUUID().toString());
            sensorRepository.save(s);
            return s.getAccessKey();
        } else {
            throw new ServiceException("Sensor has been already registered");
        }
    }

    @Override
    public void receiveData(String accessKey, MeasurementDto measurementDto) throws ServiceException {
        Sensor sensor = sensorRepository.findByAccessKey(accessKey);
        if (sensor != null) {
            Measurement measurement = measurementMapper.map(measurementDto);
            measurement.setSensor(sensor);
            measurement.setFixedAt(LocalDateTime.now());
            measurementRepository.save(measurement);
        } else {
            throw new ServiceException("Error saving condition");
        }
    }

    private void updateSensorsState() {
        List<Pair<Long, LocalDateTime>> pairs =  measurementRepository.getLastMeasurementTimeByEachSensor();
        List<Long> sensorsToDisable =
                pairs.stream()
                        .filter(pair -> LocalDateTime.now().minusMinutes(1).isAfter(pair.getSecond()))
                        .map(Pair::getFirst).toList();
        sensorRepository.disableSensors(sensorsToDisable);
    }
}