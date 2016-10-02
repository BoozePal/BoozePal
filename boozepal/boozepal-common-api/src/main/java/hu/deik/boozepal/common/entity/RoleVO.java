package hu.deik.boozepal.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Super class for Entities.
 * 
 * @author Viktor
 *
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class RoleVO extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 64)
	private String roleName;
}
