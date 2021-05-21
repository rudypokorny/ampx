package cz.rudypokorny.ampx.repository;

import cz.rudypokorny.ampx.domain.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {
}
