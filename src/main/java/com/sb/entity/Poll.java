package com.sb.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne
    @JoinColumn(name = "event_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PollOption> options = new ArrayList<>();




    @Transient
    public int getTotalVotes() {
        if (options == null) return 0;
        return options.stream()
                      .mapToInt(PollOption::getVoteCount)
                      .sum();
    }





    @Transient
    public int getPercentage(PollOption option) {
        int total = getTotalVotes();
        return total == 0 ? 0 : (int) ((option.getVoteCount() * 100.0) / total);
    }
}
