package hu.deik.boozepal.common.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "boozepal_user")
@Data
@ToString(exclude = { "password" })
public class UserVO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(unique = true, length = 64)
	private String username;

	@Column(nullable = false, length = 64)
	private String password;

	@Column(unique = true, length = 64)
	private String email;

	@Column(nullable = false)
	private boolean remove;

	@ManyToMany
	private List<RoleVO> roles;
	
}
