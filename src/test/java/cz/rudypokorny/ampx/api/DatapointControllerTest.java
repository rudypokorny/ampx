package cz.rudypokorny.ampx.api;

import cz.rudypokorny.ampx.AmpxApplication;
import cz.rudypokorny.ampx.config.AmpxConfiguration;
import cz.rudypokorny.ampx.config.ExceptionHandlerAdvice;
import cz.rudypokorny.ampx.dto.DatapointDtoCreator;
import cz.rudypokorny.ampx.service.DatapointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DatapointController.class)
@ContextConfiguration(classes = {AmpxApplication.class, AmpxConfiguration.class, ExceptionHandlerAdvice.class})
@AutoConfigureMockMvc
class DatapointControllerTest {

    private static final String MISSING_TIMESTAMP = "{\"user\": \"1\", \"device\": \"2\", \"value\": \"1\"}";
    private static final String MISSING_DEVICE = "{\"user\": \"1\", \"timestamp\": \"2021-05-19T12:34:12\", \"value\": \"1\"}";
    private static final String MISSING_USER = "{\"device\": \"2\", \"timestamp\": \"2021-05-19T12:34:12\", \"value\": \"1\"}";
    private static final String MISSING_VALUE = "{\"user\": \"1\", \"device\": \"2\", \"timestamp\": \"2021-05-19T12:34:12\"}";
    private static final String NULL_VALUE = "{\"user\": \"1\", \"device\": \"2\", \"timestamp\": \"2021-05-19T12:34:12\"}, \"value\":null }";
    private static final String COMPLETE_BODY = "{\"user\": \"1\", \"device\": \"2\", \"timestamp\": \"2021-05-19T12:34:12\", \"value\": \"1\"}";
    private static final String NO_ATTRIBUTES = "{}";
    private static final String TIMESTAMP_MALFORMED = "{\"user\": \"1\", \"device\": \"2\", \"timestamp\": \"2021-05-T12:34:12\", \"value\": \"1\"}";
    private static final String USER_ID_STRING = "{\"user\": \"x\", \"device\": \"2\", \"timestamp\": \"2021-05-19T12:34:12\", \"value\": \"1\"}";
    private static final String DEVICE_ID_DECIMAL = "{\"user\": \"1\", \"device\": \"1.1\", \"timestamp\": \"2021-05-19T12:34:12\", \"value\": \"1\"}";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private DatapointService datapointService;

    private static Stream<Arguments> invalidDatapointRequestsProvider() {
        return Stream.of(
                Arguments.of(NO_ATTRIBUTES, "timestamp", "must not be null"),
                Arguments.of(MISSING_DEVICE, "device", "must not be null"),
                Arguments.of(MISSING_TIMESTAMP, "timestamp", "must not be null"),
                Arguments.of(MISSING_USER, "user", "must not be null"),
                Arguments.of(NULL_VALUE, "value", "must not be null"),
                Arguments.of(MISSING_VALUE, "value", "must not be null")
        );
    }

    private static Stream<Arguments> mallformedDatapointRequestsProvider() {
        return Stream.of(
                Arguments.of(TIMESTAMP_MALFORMED),
                Arguments.of(DEVICE_ID_DECIMAL),
                Arguments.of(USER_ID_STRING)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDatapointRequestsProvider")
    public void givenInvalidBody_whenRequestingDatapoints_thenReturnBadRequest(final String requestBody, final String fieldName, final String errorMessage) throws Exception {
        mvc.perform(post("/datapoints")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation error")))
                .andExpect(jsonPath("$.errors." + fieldName, is(errorMessage)));
    }

    @ParameterizedTest
    @MethodSource("mallformedDatapointRequestsProvider")
    public void givenValidBodyButInvalidContent_whenRequestingDatapoints_thenReturnBadRequest(final String requestBody) throws Exception {
        var expectedDatapoint = DatapointDtoCreator.createRandom();
        Mockito.when(datapointService.createDatapointFromDto(ArgumentMatchers.any())).thenReturn(expectedDatapoint);

        mvc.perform(post("/datapoints")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request body is not valid")));
    }

    @Test
    public void givenValidBody_whenRequestingDatapoints_thenReturnAccepted() throws Exception {
        var expectedDatapoint = DatapointDtoCreator.createRandom();
        Mockito.when(datapointService.createDatapointFromDto(ArgumentMatchers.any())).thenReturn(expectedDatapoint);

        mvc.perform(post("/datapoints")
                .content(COMPLETE_BODY)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", is(expectedDatapoint.getId())));
    }
}