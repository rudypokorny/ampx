package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.UserCreator;
import cz.rudypokorny.ampx.exceptions.UserNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import cz.rudypokorny.ampx.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.Null;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private DatapointRepository datapointRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void givenNullAsId_whenDeleteDatapointsForUser_thenThrowsNPE(){
        assertThrows(NullPointerException.class, () ->userService.deleteDatapointsForUser(null));
    }

    @Test
    public void givenNonexistentId_whenDeleteDatapointsForUser_thenThrowsNotFoundException(){
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->userService.deleteDatapointsForUser(0L), "Device with id 0 has not been found.");
    }

    @Test
    public void givenExistingId_whenDeleteDatapointsForUser_thenReturnExpectedCount(){
        int expectedCount = 3;
        var user = UserCreator.createRandom(expectedCount);
        Mockito.when(userRepository.findById(Mockito.eq(user.getId()))).thenReturn(Optional.of(user));

        var actualCount = userService.deleteDatapointsForUser(user.getId());
        assertEquals(expectedCount, actualCount);
    }
}