package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.dto.StatisticsDto;
import cz.rudypokorny.ampx.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/statistics",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "return list of 15 minutes averages of time serie from first datapoint to current time. Matching device key",
            parameters = {@Parameter(name = "deviceId", description = "Device identifier")})
    @GetMapping("/devices/{deviceId}/avg")
    public List<StatisticsDto> getAverageForDevice(@PathVariable(name = "deviceId") final Long deviceId) {
        return statisticsService.getAveragesForDevice(deviceId);
    }

    @Operation(summary = "return list of 15 minutes averages of time serie from first datapoint to current time. Matching user key",
            parameters = {@Parameter(name = "userId", description = "User identifier")})
    @GetMapping("/users/{userId}/avg")
    public List<StatisticsDto> getAverageForUser(@PathVariable(name = "userId") final Long userId) {
        return statisticsService.getAveragesForUser(userId);
    }
}
