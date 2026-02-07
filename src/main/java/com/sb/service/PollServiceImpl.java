package com.sb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sb.entity.Event;
import com.sb.entity.Poll;
import com.sb.entity.PollOption;
import com.sb.entity.PollVote;
import com.sb.entity.User;
import com.sb.repository.PollOptionRepository;
import com.sb.repository.PollRepository;
import com.sb.repository.PollVoteRepository;

@Service
public class PollServiceImpl implements PollService {

    private final PollOptionRepository optionRepo;
    private final PollRepository pollRepo;
    private final PollVoteRepository voteRepo;
    private final EventService eventService; // ✅ added

    public PollServiceImpl(PollOptionRepository optionRepo,
                           PollRepository pollRepo,
                           PollVoteRepository voteRepo,
                           EventService eventService) { // ✅ added
        this.optionRepo = optionRepo;
        this.pollRepo = pollRepo;
        this.voteRepo = voteRepo;
        this.eventService = eventService;
    }

    @Override
    public List<Poll> getPollsByEvent(Event event) {
        return pollRepo.findByEvent(event);
    }

    @Override
    public void vote(Long optionId, User user) {

        PollOption option = optionRepo.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));

        Poll poll = option.getPoll();

        // 🚫 Prevent duplicate vote
        if (voteRepo.existsByPollAndUser(poll, user)) {
            throw new RuntimeException("You already voted in this poll");
        }

        PollVote vote = new PollVote();
        vote.setPoll(poll);
        vote.setOption(option);
        vote.setUser(user);

        voteRepo.save(vote);

        option.setVoteCount(option.getVoteCount() + 1);
        optionRepo.save(option);
    }

    @Override
    public void createPoll(Long eventId,
                           String question,
                           List<String> options,
                           User loggedInUser) {

        Event event = eventService.getEventById(eventId);

        // 🔒 SECURITY CHECK
        if (!event.getCreatedBy().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("Only event organizer can create polls");
        }

        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setEvent(event);

        List<PollOption> optionList = new ArrayList<>();

        for (String text : options) {
            if (text == null || text.trim().isEmpty()) continue;

            PollOption opt = new PollOption();
            opt.setOptionText(text);
            opt.setVoteCount(0);
            opt.setPoll(poll);
            optionList.add(opt);
        }

        poll.setOptions(optionList);
        pollRepo.save(poll);
    }

}
