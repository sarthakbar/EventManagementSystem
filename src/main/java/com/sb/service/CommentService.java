package com.sb.service;

import java.util.List;

import com.sb.entity.Comment;
import com.sb.entity.Event;
import com.sb.entity.User;

public interface CommentService {

    // Add a comment to an event
    Comment addComment(Event event, User loggedInUser, String message);

    // Get all comments for an event
    List<Comment> getCommentsByEvent(Event event);
}
