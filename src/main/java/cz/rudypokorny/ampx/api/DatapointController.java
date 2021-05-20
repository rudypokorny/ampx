package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.dto.DatapointDto;
import cz.rudypokorny.ampx.service.DatapointService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/datapoints",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DatapointController {

    @Autowired
    private DatapointService datapointService;

    @Operation(summary = "add new datapoint to time series. Tuple {timestamp, device, user} is unique. Adding the same point should cause bad request response and not change the data.")
    @PostMapping
    public ResponseEntity<DatapointDto> createDatapoint(@RequestBody @Validated final DatapointDto datapointDto) {
        var resultedDatapoint = datapointService.createDatapointFromDto(datapointDto);

        return ResponseEntity.accepted().body(resultedDatapoint);
    }
}
