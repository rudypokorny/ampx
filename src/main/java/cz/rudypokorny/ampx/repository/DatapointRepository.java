package cz.rudypokorny.ampx.repository;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.domain.Statistics;
import cz.rudypokorny.ampx.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DatapointRepository extends CrudRepository<Datapoint, Long> {

    /**
     * Searches for datapoints according to given criterias
     *
     * @param user
     * @param device
     * @param timestamp
     * @return list of found datapoints
     */
    List<Datapoint> findDatapointByUserAndDeviceAndTimestamp(final User user, final Device device, final Date timestamp);

    /**
     * Deletes all datapoints joined with given device
     *
     * @param device
     */
    void deleteByDevice(final Device device);

    /**
     * Deletes all datapoints joined with given user
     *
     * @param user
     */
    void deleteByUser(final User user);

    /**
     * Searches for datapoints by given device and within given timestamp range
     *
     * @param device to search by
     * @param from
     * @param to
     * @return list of found datapoints
     */
    @Query("SELECT new cz.rudypokorny.ampx.domain.Statistics(AVG(d.value), count(*)) FROM Datapoint d WHERE d.device.id = :deviceId AND d.timestamp BETWEEN :from AND :to")
    Statistics findAverageForDevice(@Param("deviceId") final Long deviceId, @Param("from") Date from, @Param("to") Date to);

    /**
     * Searches for the oldest time for given device
     *
     * @param device
     * @return
     */
    @Query("SELECT MIN(d.timestamp) FROM Datapoint d WHERE d.device = :device")
    Date findMinDateForDevice(@Param("device") final Device device);

    /**
     * Searches for datapoints by given user and within given timestamp range
     *
     * @param user to search by
     * @param from
     * @param to
     * @return list of found datapoints
     */
    @Query("SELECT new cz.rudypokorny.ampx.domain.Statistics(AVG(d.value), count(*)) FROM Datapoint d WHERE d.user.id = :userId AND d.timestamp BETWEEN :from AND :to")
    Statistics findAverageForUser(@Param("userId") final Long userId, @Param("from") Date from, @Param("to") Date to);

    /**
     * Searches for the oldest time for given user
     *
     * @param user
     * @return
     */
    @Query("SELECT MIN(d.timestamp) FROM Datapoint d WHERE d.user = :user")
    Date findMinDateForUser(@Param("user") final User user);

}
