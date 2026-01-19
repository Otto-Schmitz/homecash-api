package com.app.homecash.repository;

import com.app.homecash.domain.HouseMember;
import com.app.homecash.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseMemberRepository extends JpaRepository<HouseMember, Long> {

    Optional<HouseMember> findByUserIdAndHouseId(Long userId, Long houseId);

    List<HouseMember> findByUserId(Long userId);

    List<HouseMember> findByHouseId(Long houseId);

    List<HouseMember> findByHouseIdAndRole(Long houseId, Role role);

    long countByHouseId(Long houseId);

    long countByHouseIdAndRole(Long houseId, Role role);
}

