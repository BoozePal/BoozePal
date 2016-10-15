package hu.deik.boozepal.core.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hu.deik.boozepal.common.entity.Address;

/**
 * Lakóhelyeket kezelő adathozzáférési szolgáltatás.
 * 
 * @version 1.0
 *
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Lakóhely keresése város alapján.
     * 
     * @param town
     *            keresendő város.
     * @return a talált lakóhelyek listája.
     */
    List<Address> findByTown(String town);

    /**
     * Lakóhely keresése utca alapján.
     * 
     * @param street
     *            a keresendő utca.
     * @return a talált lakóhelyek listája.
     */
    List<Address> findByStreet(String street);

}
