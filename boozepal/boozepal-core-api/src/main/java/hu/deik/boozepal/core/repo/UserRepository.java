package hu.deik.boozepal.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.deik.boozepal.common.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String userName);
}
