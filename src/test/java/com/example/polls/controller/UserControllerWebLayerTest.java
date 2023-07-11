package com.example.polls.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;

import com.example.polls.config.AuditingConfig;
import com.example.polls.config.SecurityConfig;
import com.example.polls.payload.UserIdentityAvailability;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;
import com.example.polls.service.PollService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserController.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class}, 
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityConfig.class, AuditingConfig.class})
)
public class UserControllerWebLayerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;
    
    
    @MockBean
    PollRepository pollRepository;
    
    @MockBean
    VoteRepository voteRepository;
    
    @MockBean
    PollService pollService;
    
    
    @Test
    void testCheckUsernameAvailability_WhenNotAvailable_ReturnFalse() throws Exception{
    	when(userRepository.existsByEmail(anyString())).thenReturn(true);
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", "thomas.kang@kakao.com");
    	
    	MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
    	String responseBodyAsString = mvcResult.getResponse().getContentAsString();
    	UserIdentityAvailability userIdentityAvailability = new ObjectMapper()
                .readValue(responseBodyAsString, UserIdentityAvailability.class);
    	
    	Assertions.assertFalse(userIdentityAvailability.getAvailable(),"Shoud return true");
    }
    
    @Test
    void testCheckUsernameAvailability_WhenExceptiom_ReturnNestedServletException() throws Exception{
    	when(userRepository.existsByEmail(anyString())).thenThrow(new RuntimeException("ERROR"));
    	RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user/checkEmailAvailability").param("email", "thomas.kang@kakao.com");
    	
    	
    	Assertions.assertThrows(NestedServletException.class, ()->{
    		mockMvc.perform(requestBuilder).andReturn();
    	});
    	 	
    }
}
