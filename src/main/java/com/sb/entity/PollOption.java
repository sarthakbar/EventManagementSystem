package com.sb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class PollOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String optionText;

    @Transient
    public int getVotePercentage() {
        if (poll == null || poll.getTotalVotes() == 0) {
            return 0;
        }
        return (voteCount * 100) / poll.getTotalVotes();
    }

    
    private int voteCount = 0;

    @ManyToOne
    private Poll poll;
}

