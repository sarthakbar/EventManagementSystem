package com.sb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Poll;
import com.sb.entity.PollVote;
import com.sb.entity.User;

public interface PollVoteRepository extends JpaRepository<PollVote, Long> {

    boolean existsByPollAndUser(Poll poll, User user);
}
