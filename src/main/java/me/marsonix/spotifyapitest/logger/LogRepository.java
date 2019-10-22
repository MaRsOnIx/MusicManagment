package me.marsonix.spotifyapitest.logger;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface LogRepository extends CrudRepository<Log, Long> {

}
