package com.sb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sb.entity.Comment;
import com.sb.entity.Event;
import com.sb.entity.User;
import com.sb.repository.CommentRepository;

@Service
public class CommentServiceImpl implements CommentService{

	private CommentRepository commentRepository;
	
	public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
	
	@Override
	public Comment addComment(Event event, User loggedInUser, String message) {
		
		Comment comment=new Comment();
		comment.setEvent(event);
		comment.setUser(loggedInUser);
		comment.setMessage(message);
		
		return commentRepository.save(comment);
	}

	@Override
	public List<Comment> getCommentsByEvent(Event event) {
		
		return commentRepository.findByEvent(event);
	}

}
