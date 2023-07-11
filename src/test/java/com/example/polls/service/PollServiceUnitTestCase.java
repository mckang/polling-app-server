package com.example.polls.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.polls.model.Poll;
import com.example.polls.payload.PagedResponse;
import com.example.polls.payload.PollResponse;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;

@ExtendWith(MockitoExtension.class)
public class PollServiceUnitTestCase {

	PollService pollService;  
	
	@Mock
	PollRepository pollRepository;
	
	@Mock
	VoteRepository voteRepository;
	
	@Mock
	UserRepository userRepository;
	
	
    @BeforeEach 
    void init(){
    	pollService = new PollService(pollRepository, voteRepository, userRepository);
    }
    
    
    @Test
    void testGetAllPolls_WhenPollsZero_ReturnEmptyList() {
    	Pageable pageable = PageRequest.of(1, 10, Sort.Direction.DESC, "createdAt");
    	Page<Poll> polls = new PageImpl<>(new ArrayList<>(), pageable, 0);
    	when(pollRepository.findAll(any(Pageable.class))).thenReturn(polls);
    	
    	PagedResponse<PollResponse> pagedPollResponse = pollService.getAllPolls(null, 1, 10);
    	
    	Assertions.assertEquals(0, pagedPollResponse.getContent().size(),"Size should be zero");
    	Assertions.assertEquals(1, pagedPollResponse.getPage(),"Page should be one");

    }
}
