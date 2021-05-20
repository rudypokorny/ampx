package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.User;
import cz.rudypokorny.ampx.exceptions.UserNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import cz.rudypokorny.ampx.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatapointRepository datapointRepository;


    @Transactional
    @Override
    public Integer deleteDatapointsForUser(final Long userId) {
        return findUser(userId)
                .map(this::deleteDatapoints)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public Optional<User> findUser(Long userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        return userRepository.findById(userId);
    }

    private Integer deleteDatapoints(final User user) {
        var count = user.getDatapoints().size();
        datapointRepository.deleteByUser(user);
        logger.debug("Deleted all datapoints ({}) for user {}", count, user.getId());
        return count;
    }

}
