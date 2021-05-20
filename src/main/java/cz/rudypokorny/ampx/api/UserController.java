package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users",
        consumes = MediaType.ALL_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "delete all users datapoints",
            parameters = {@Parameter(name = "userId", description = "User identifier")})
    @DeleteMapping("/{userId}/datapoints")
    public ResponseEntity deleteDatapoints(@PathVariable(name = "userId") final Long userId){
        userService.deleteDatapointsForUser(userId);
        return ResponseEntity.noContent().build();
    }
}
