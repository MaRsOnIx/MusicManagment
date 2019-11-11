package me.marsonix.spotifyapitest.repo;

import me.marsonix.spotifyapitest.models.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,String> {
}
