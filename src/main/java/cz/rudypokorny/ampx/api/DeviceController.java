package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.dto.DeviceDto;
import cz.rudypokorny.ampx.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/devices",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceDto> findDevice(@PathVariable(name = "deviceId") final Long deviceId) {
        return ResponseEntity.ok(deviceService.findDeviceAsDto(deviceId));
    }

    @Operation(summary = "delete all device datapoints", parameters = {@Parameter(name = "deviceId", description = "Device identifier")})
    @DeleteMapping("/{deviceId}/datapoints")
    public ResponseEntity deleteDatapoints(@PathVariable(name = "deviceId") final Long deviceId) {
        deviceService.deleteDatapointsForDevice(deviceId);
        return ResponseEntity.noContent().build();
    }
}
