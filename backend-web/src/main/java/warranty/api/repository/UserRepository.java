package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import warranty.api.model.User;
import warranty.api.model.dto.UserDto;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT new warranty.api.model.dto.UserDto(u.name, u.email)
        FROM User u
        WHERE u.id = :id
       """)
    UserDto findByIdDto(Long Id);


}
