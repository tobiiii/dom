package com.itexc.dom.repository;

import com.itexc.dom.domain.Privilege;
import com.itexc.dom.domain.Profile;
import com.itexc.dom.domain.projection.ProfileView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository<T extends Profile> extends JpaRepository<T, Long> {

    boolean existsByCode(String code);

    boolean existsByName(String name);

    boolean existsByColor(String color);


    @Query("select p from Profile p where  p.code <> 'SUPERADMIN' ORDER BY p.id ASC ")
    List<ProfileView> findAllOrderByIdDesc ();
    
    Optional<T> findByCode(String code);

    List<T> findByPrivilegesIn(List<Privilege> privileges);
}
