package com.sb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.entity.Comment;
import com.sb.entity.Event;

public interface CommentRepository extends JpaRepository<Comment, Long>{

	List<Comment> findByEvent(Event event);
}
