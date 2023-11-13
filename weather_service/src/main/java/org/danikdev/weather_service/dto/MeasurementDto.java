package org.danikdev.weather_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDto {
    @NotNull(message = "'value' must not be null or you didn't include it in json")
    @DecimalMin(value = "-100.0", inclusive = true, message = "'value' must be in range from -100 to 100")
    @DecimalMax(value = "100.0", inclusive = true, message = "'value' must be in range from -100 to 100")
    private Double value;
    @NotNull(message = "'raining' must not be null or you didn't include it in json")
    private boolean raining;
    @JsonIgnore
    private LocalDateTime fixedAt;
    @JsonIgnore
    private String sensorName;
}
