package hu.deik.boozepal.core.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.deik.boozepal.common.entity.UserVO;

public interface UserRepository extends JpaRepository<UserVO, Long> {
	UserVO findByUsername(String userName);
}
