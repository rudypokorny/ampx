package cz.rudypokorny.ampx.dto.mapping;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.DatapointCreator;
import cz.rudypokorny.ampx.dto.DatapointDtoCreator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatapointMapperImplTest {

    private final DatapointMapper mapper = new DatapointMapperImpl();

    @Test
    public void givenNull_whenToDto_thenThrowNPE() {
        assertThrows(NullPointerException.class, () -> mapper.toDto(null));
    }

    @Test
    public void givenEmptyDatapoint_whenToDto_thenMappedToEmptyDto() {
        var actual = mapper.toDto(new Datapoint());
        assertNull(actual.getDevice());
        assertNull(actual.getUser());
        assertNull(actual.getTimestamp());
        assertNull(actual.getId());
        assertNull(actual.getValue());
    }

    @Test
    public void givenRandomDatapoint_whenToDto_thenMappedToDatapointDto() {
        var expected = DatapointCreator.createRandom();
        var actual = mapper.toDto(expected);

        assertEquals(expected.getDevice().getId(), actual.getDevice());
        assertEquals(expected.getUser().getId(), actual.getUser());
        assertEquals(expected.getTimestamp(), actual.getTimestamp());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getValue(), actual.getValue());
    }


    @Test
    public void givenValidDatapointDto_whenFromDto_thenMappedToDatapoint() {
        var expected = DatapointDtoCreator.createRandom();
        var actual = mapper.fromDto(expected);

        assertEquals(expected.getDevice(), actual.getDevice().getId());
        assertEquals(expected.getUser(), actual.getUser().getId());
        assertEquals(expected.getTimestamp(), actual.getTimestamp());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getValue(), actual.getValue());
    }
}