package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.DeviceCreator;
import cz.rudypokorny.ampx.dto.mapping.DeviceMapperImpl;
import cz.rudypokorny.ampx.exceptions.DeviceNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import cz.rudypokorny.ampx.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceImplTest {

    @InjectMocks
    private DeviceServiceImpl deviceService;
    @Mock
    private DeviceRepository deviceRepository;
    @Mock
    private DatapointRepository datapointRepository;
    @Spy
    private DeviceMapperImpl deviceMapper;

    @Test
    public void givenNull_whenFindDevices_thenThrowNPE() {
        assertThrows(NullPointerException.class, () -> deviceService.findDevice(null));
    }

    @Test
    public void givenNonexistentId_whenFindDevices_thenReturnEmptyOptional() {
        Mockito.when(deviceRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(DeviceNotFoundException.class, () -> deviceService.findDeviceAsDto(0l));
    }

    @Test
    public void givenExistingIdWithNoDatapints_whenFindDevices_thenReturnExpectedDevice() {
        var expectedDevice = DeviceCreator.createRandom();
        Mockito.when(deviceRepository.findById(Mockito.eq(expectedDevice.getId()))).thenReturn(Optional.of(expectedDevice));

        var actualDevice = deviceService.findDeviceAsDto(expectedDevice.getId());
        assertNotNull(actualDevice);
        assertEquals(expectedDevice.getId(), actualDevice.getId());
        assertEquals(0, actualDevice.getDatapointCount());
    }

    @Test
    public void givenExistingIdWithOneDatapoint_whenFindDevices_thenReturnExpectedDevice() {
        var expectedDevice = DeviceCreator.createRandom(1);
        Mockito.when(deviceRepository.findById(Mockito.eq(expectedDevice.getId()))).thenReturn(Optional.of(expectedDevice));

        var actualDevice = deviceService.findDeviceAsDto(expectedDevice.getId());
        assertNotNull(actualDevice);
        assertEquals(expectedDevice.getId(), actualDevice.getId());
        assertEquals(1, actualDevice.getDatapointCount());
    }

    @Test
    public void givenExistingId_whenDeleteDatapointsForDevice_thenReturnExpectedCount() {
        int expectedCount = 3;
        var device = DeviceCreator.createRandom(expectedCount);
        Mockito.when(deviceRepository.findById(Mockito.eq(device.getId()))).thenReturn(Optional.of(device));

        var actualCount = deviceService.deleteDatapointsForDevice(device.getId());
        assertEquals(expectedCount, actualCount);
    }
}