package org.danikdev.weather_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.danikdev.weather_service.dto.MeasurementDto;
import org.danikdev.weather_service.dto.SensorDto;
import org.danikdev.weather_service.exception.ServiceException;
import org.danikdev.weather_service.service.SensorService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService sensorService;
    @Operation(description = "Get endpoint for getting all active sensors", summary = "Get all active sensors")
    @GetMapping("")
    public ResponseEntity<?> getActiveSensors() {
        List<SensorDto> sensors = sensorService.getAllActiveSensors();
        return ResponseEntity.ok(!sensors.isEmpty()
                ? sensors
                : "There are no active sensors");
    }

    @Operation(description = "Get endpoint for getting all registered sensors", summary = "Get all registered sensors")
    @GetMapping("/all")
    public ResponseEntity<?> getAllSensors() {
        List<SensorDto> sensors = sensorService.getAllSensors();
        return ResponseEntity.ok(!sensors.isEmpty()
                ? sensors
                : "There are no sensors registered yet");
    }

    @Operation(description = "Get endpoint for getting last measurement by sensor", summary = "Get last measurement by sensor")
    @GetMapping("/{sensor_name}")
    public ResponseEntity<?> getLastUpdateBySensor(@PathVariable String sensor_name) {
        try {
            Optional<MeasurementDto> conditionDto = sensorService.getLastUpdateBySensor(sensor_name);
            return ResponseEntity.ok(conditionDto.isPresent()
                    ? conditionDto
                    : "There are no measurements for " + sensor_name + " have been received yet");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(description = "Get endpoint for getting last 20 measurements by sensor", summary = "Get last 20 measurements by sensor")
    @GetMapping("/{key}/measurements")
    public ResponseEntity<?> getLast20UpdatesBySensor(@PathVariable String key) {
        try {
            List<MeasurementDto> measurements = sensorService.getLast20UpdatesBySensor(key);
            return ResponseEntity.ok(!measurements.isEmpty()
                    ? measurements
                    : "There are no measurements have been received yet");
        } catch (ServiceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(description = "Get endpoint for getting last measurements by all sensors", summary = "Get last measurements by all sensors")
    @GetMapping("/measurements")
    public ResponseEntity<?> getLastUpdatesOfAllActiveSensors() {
        List<MeasurementDto> measurements = sensorService.getLastUpdatesOfAllActiveSensors();
        return ResponseEntity.ok(!measurements.isEmpty()
                ? measurements
                : "There are no measurements have been received yet");
    }

    @Operation(
            description = "Post endpoint for registering a new sensor", summary = "Register new sensor",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Access-key: b31f60ec-9506-4e94-9f40-b5a6fb7fa046"))),
                    @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "'name' must not be null or you did not include it in json"))),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Sensor has been already registered")))
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody @Valid SensorDto sensorDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        try {
            return ResponseEntity.ok("Access-key: " + sensorService.register(sensorDto));
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(
            description = "Post endpoint for receiving measurements from sensors", summary = "Receive measurements from sensor",
            responses = {
                @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema())),
                @ApiResponse(description = "BadRequest", responseCode = "400", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "'value' must be in range from -100 to 100"))),
                @ApiResponse(description = "InternalServerError", responseCode = "500", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Server is down")))
            }
    )
    @PostMapping("/{key}/measurement")
    public ResponseEntity<?> receiveData(@PathVariable String key,
                                         @RequestBody @Valid MeasurementDto measurementDto,
                                         Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList());
        }

        try {
            sensorService.receiveData(key, measurementDto);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("The request must contain json");
    }


}
