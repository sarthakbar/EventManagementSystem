package com.sb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.PollOption;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
}
