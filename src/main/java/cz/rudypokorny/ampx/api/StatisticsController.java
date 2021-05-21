package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.dto.StatisticsDto;
import cz.rudypokorny.ampx.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/statistics",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "return list of 15 minutes averages of time serie from first datapoint to current time. Matching device key",
            parameters = {@Parameter(name = "deviceId", description = "Device identifier", required = true)})
    @GetMapping("/devices/{deviceId}/avg")
    public List<StatisticsDto> getAverageForDevice(@PathVariable(name = "deviceId") final Long deviceId) {
        return statisticsService.getAveragesForDevice(deviceId);
    }

    @Operation(summary = "Return list of moving averages of 15 minutes average buckets. Matching device key",
            parameters = {@Parameter(name = "deviceId", description = "Device identifier", required = true),
                    @Parameter(name = "window_size", description = "size of the moving average window. Defaulted to 3")})
    @GetMapping("/devices/{deviceId}/moving_avg")
    public List<StatisticsDto> getMovingAverageForDevice(@PathVariable(name = "deviceId") final Long deviceId,
                                                         @RequestParam(name = "window_size", required = false,
                                                                 defaultValue = "3") final Integer windowSize) {
        return statisticsService.getMovingAveragesForDevice(deviceId, windowSize);
    }

    @Operation(summary = "return list of 15 minutes averages of time serie from first datapoint to current time. Matching user key",
            parameters = {@Parameter(name = "userId", description = "User identifier", required = true)})
    @GetMapping("/users/{userId}/avg")
    public List<StatisticsDto> getAverageForUser(@PathVariable(name = "userId") final Long userId) {
        return statisticsService.getAveragesForUser(userId);
    }

    @Operation(summary = "Return list of moving averages of 15 minutes average buckets. Matching user key",
            parameters = {@Parameter(name = "userId", description = "User identifier", required = true),
                    @Parameter(name = "window_size", description = "size of the moving average window. Defaulted to 3")})
    @GetMapping("/users/{userId}/moving_avg")
    public List<StatisticsDto> getMovingAverageForUser(@PathVariable(name = "userId") final Long userId,
                                                       @RequestParam(name = "window_size", required = false,
                                                               defaultValue = "3") final Integer windowSize) {
        return statisticsService.getMovingAveragesForUser(userId, windowSize);
    }
}
