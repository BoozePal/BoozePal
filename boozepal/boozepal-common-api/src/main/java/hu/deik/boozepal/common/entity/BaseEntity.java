package hu.deik.boozepal.common.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Super class for Entities.
 * 
 * @author Nandi
 *
 */
@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

}
