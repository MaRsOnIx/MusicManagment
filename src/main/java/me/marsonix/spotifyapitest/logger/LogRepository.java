package me.marsonix.spotifyapitest.logger;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LogRepository extends CrudRepository<Log, Long> {

}
