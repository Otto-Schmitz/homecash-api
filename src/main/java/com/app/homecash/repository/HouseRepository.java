package com.app.homecash.repository;

import com.app.homecash.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByInviteCode(String inviteCode);
}

