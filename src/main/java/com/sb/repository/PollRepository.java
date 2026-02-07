package com.sb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Event;
import com.sb.entity.Poll;

public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByEvent(Event event);
}
