package de.afb.user.controller;

import de.afb.persistence.entities.User;
import de.afb.persistence.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    private List<User> users = new ArrayList<>();

    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        when(userRepository.save(any(User.class))).thenAnswer((Answer<User>) invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            User inputUser = (User) args[0];
            users.add(inputUser);
            inputUser.setId(users.size());
            return inputUser;
        });
        when(userRepository.findAll()).thenReturn(users);
    }

    @After
    public void tearDown() {
        users.clear();
    }

    @Test
    public void addNewUser() throws Exception {
        mvc.perform(get("/user/add?name=test&email=hier@da.de"))
                .andExpect(status().is(200))
                .andExpect(content().bytes("Saved 1".getBytes()));
    }

    @Test
    public void getAllUsers() throws Exception {
        mvc.perform(get("/user/add?name=test&email=hier@da.de"))
                .andExpect(status().is(200))
                .andExpect(content().bytes("Saved 1".getBytes()));
        mvc.perform(get("/user/add?name=test&email=hier@da.de"))
                .andExpect(status().is(200))
                .andExpect(content().bytes("Saved 2".getBytes()));
        mvc.perform(get("/user/all"))
                .andExpect(status().is(200))
//                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}