package cz.rudypokorny.ampx.dto.mapping;

/**
 * Interface for defining conversion api between DTO objects and domain objects. Conversions from domain should takes place inside transaction context, to avoid any lay loading issues.
 * @param <DTO>
 * @param <DOMAIN>
 */
public interface Mapper<DTO, DOMAIN> {

    /**
     * Conveerts the object from DTO to domain object
     * @param datapointDTO data to covert
     * @return  converted object
     */
    DOMAIN fromDto(final DTO datapointDTO);

    /**
     * Converts domain object to DTO
     * @param datapoint data to covert
     * @return converted object
     */
    DTO toDto(final DOMAIN datapoint);
}
