package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.DatapointCreator;
import cz.rudypokorny.ampx.domain.DeviceCreator;
import cz.rudypokorny.ampx.domain.UserCreator;
import cz.rudypokorny.ampx.dto.DatapointDto;
import cz.rudypokorny.ampx.dto.DatapointDtoCreator;
import cz.rudypokorny.ampx.dto.mapping.DatapointMapperImpl;
import cz.rudypokorny.ampx.exceptions.AbstractInvalidInputException;
import cz.rudypokorny.ampx.exceptions.DatapointNotUniqueException;
import cz.rudypokorny.ampx.exceptions.DeviceNotFoundException;
import cz.rudypokorny.ampx.exceptions.UserNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatapointServiceImplTest {


    @InjectMocks
    private DatapointServiceImpl datapointService;
    @Mock
    private DatapointRepository datapointRepository;
    @Mock
    private UserService userService;
    @Mock
    private DeviceService deviceService;
    @Spy
    private DatapointMapperImpl mapper;
    @Captor
    private ArgumentCaptor<Datapoint> datapointArgumentCaptor;

    private DatapointDto DATAPOINT_DTO;

    @BeforeEach
    public void setup(){
        DATAPOINT_DTO = DatapointDtoCreator.createRandom();
    }

    @Test
    public void givenNulldDatapointDto_thenThrowException(){
        //bypass common when
        lenient().when(mapper.fromDto(DATAPOINT_DTO)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> datapointService.createDatapointFromDto(null), "datapoint cannot be null");
    }

    @Test
    public void givenValidDatapointDto_whenCreateDatapoint_thenSaveIsCalled() {
        when(userService.findUser(eq(DATAPOINT_DTO.getUser()))).thenReturn(Optional.of(UserCreator.createWithId(DATAPOINT_DTO.getUser())));
        when(deviceService.findDevice(eq(DATAPOINT_DTO.getDevice()))).thenReturn(Optional.of(DeviceCreator.createWithId(DATAPOINT_DTO.getDevice())));
        when(datapointRepository.findDatapointByUserAndDeviceAndTimestamp(any(), any(), any())).thenReturn(Collections.emptyList());

        //ve do not care about the save, just don't want conversion to fail
        when(datapointRepository.save(any())).thenReturn(DatapointCreator.createRandom());

        datapointService.createDatapointFromDto(DATAPOINT_DTO);

        verify(datapointRepository).save(datapointArgumentCaptor.capture());
        var capturedDatapoint = datapointArgumentCaptor.getValue();

        assertEquals(DATAPOINT_DTO.getValue(), capturedDatapoint.getValue());
        assertEquals(DATAPOINT_DTO.getTimestamp(), capturedDatapoint.getTimestamp());
        assertEquals(DATAPOINT_DTO.getUser(), capturedDatapoint.getUser().getId());
        assertEquals(DATAPOINT_DTO.getDevice(), capturedDatapoint.getDevice().getId());
    }

    @Test
    public void givenValidDatapointDto_whenCreateDatapointThatHasBeenAlreadySaved_thenThrowDuplicateException() {
        when(userService.findUser(eq(DATAPOINT_DTO.getUser()))).thenReturn(Optional.of(UserCreator.createWithId(DATAPOINT_DTO.getUser())));
        when(deviceService.findDevice(eq(DATAPOINT_DTO.getDevice()))).thenReturn(Optional.of(DeviceCreator.createWithId(DATAPOINT_DTO.getDevice())));
        when(datapointRepository.findDatapointByUserAndDeviceAndTimestamp(any(), any(), any())).thenReturn(Collections.singletonList(DatapointCreator.createRandom()));

        assertThrows(DatapointNotUniqueException.class, () -> datapointService.createDatapointFromDto(DATAPOINT_DTO),
                String.format(DatapointService.DATAPOINT_ALREADY_EXISTS_MESSAGE, DATAPOINT_DTO.getUser(), DATAPOINT_DTO.getDevice(), DATAPOINT_DTO.getTimestamp()));
    }

    @Test
    public void givenValidDatapointDto_whenCreateDatapointWithNonExistentUser_thenThrowUserNotFoundException() {
        when(userService.findUser(eq(DATAPOINT_DTO.getUser()))).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> datapointService.createDatapointFromDto(DATAPOINT_DTO),
                String.format(AbstractInvalidInputException.NOT_FOUND_MESSAGE_TEMPLATE, "User", DATAPOINT_DTO.getUser()));
    }

    @Test
    public void givenValidDatapoint_whenCreateDatapointWithNonExistingDevice_thenThrowDeviceNotFoundException() {
        when(userService.findUser(eq(DATAPOINT_DTO.getUser()))).thenReturn(Optional.of(UserCreator.createWithId(DATAPOINT_DTO.getUser())));
        when(deviceService.findDevice(eq(DATAPOINT_DTO.getDevice()))).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> datapointService.createDatapointFromDto(DATAPOINT_DTO),
                String.format(AbstractInvalidInputException.NOT_FOUND_MESSAGE_TEMPLATE, "Device", DATAPOINT_DTO.getDevice()));
    }
}