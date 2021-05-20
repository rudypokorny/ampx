package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.User;

import java.util.Optional;

/**
 * Service managing {@link cz.rudypokorny.ampx.domain.User} entity
 */
public interface UserService {

    /**
     * Deletes all the {@link cz.rudypokorny.ampx.domain.Datapoint} for given userId
     *
     * @param userId id of the user whose datapoints should be deleted
     * @return count of deleted datapoints
     * @throws cz.rudypokorny.ampx.exceptions.UserNotFoundException if user with given id does not exists
     */
    Integer deleteDatapointsForUser(final Long userId);

    /**
     * Searches for user with given id
     *
     * @param userId
     * @return found user wrapped in Optional
     */
    Optional<User> findUser(final Long userId);
}
