package de.afb.authorization;

import de.afb.authorization.controller.AuthorizationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AuthorizationControllerTest {

    @InjectMocks
    AuthorizationController authController;

    @Mock
    KeyProvider keyProvider;

    @Mock
    RefreshTokenCreator refreshTokenCreator;

    private StaticKeyProvider staticKeyProvider;

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        staticKeyProvider = new StaticKeyProvider();
        mvc = MockMvcBuilders.standaloneSetup(authController).build();

        when(keyProvider.getPublicKey()).thenReturn(staticKeyProvider.getPublicKey());
        when(keyProvider.getPrivateKey()).thenReturn(staticKeyProvider.getPrivateKey());
        when(refreshTokenCreator.create()).thenReturn("12345");
    }

    @Test
    public void testAuthenticate() {
        AuthSession session = authController.authenticate("test", "test");
        assertEquals("test", session.getName());
        assertEquals("12345", session.getRefreshToken());
        assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaXNzIjoic2FsZXMifQ.nTcmpiR_CMrNGHazJ-tBUMmvQe2AV4rzPgv08isBrE36z5FRfj0--M6ORu5xAkqDfmCjb7JUq53VD1jjtDrDrJ-k0q7OUDdhNGYziiJdk7qczLJO3aSXjbyi_cp9AXO1uRbwhOv1wa7Pbys8nOz4mZ3AMEkK7hDF0Xqyahct2YI",
                session.getAccessToken());
    }

    @Test
    public void testValidationMissingJWT() throws Exception {
        mvc.perform(get("/authentication/validate").header("authorization", "test"))
                .andExpect(status().is(403))
                .andExpect(header().string("Content-Type", "application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message", is("missing authorization token")));
    }

    @Test
    public void testValidation() throws Exception {
        mvc.perform(get("/authentication/validate")
                .header("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaXNzIjoic2FsZXMifQ.nTcmpiR_CMrNGHazJ-tBUMmvQe2AV4rzPgv08isBrE36z5FRfj0--M6ORu5xAkqDfmCjb7JUq53VD1jjtDrDrJ-k0q7OUDdhNGYziiJdk7qczLJO3aSXjbyi_cp9AXO1uRbwhOv1wa7Pbys8nOz4mZ3AMEkK7hDF0Xqyahct2YI")
        )
                .andExpect(status().is(200));
    }

    @Test
    public void testValidationWrongToken() throws Exception {
        mvc.perform(get("/authentication/validate")
                .header("authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.wrongtoken.nTcmpiR_CMrNGHazJ-tBUMmvQe2AV4rzPgv08isBrE36z5FRfj0--M6ORu5xAkqDfmCjb7JUq53VD1jjtDrDrJ-k0q7OUDdhNGYziiJdk7qczLJO3aSXjbyi_cp9AXO1uRbwhOv1wa7Pbys8nOz4mZ3AMEkK7hDF0Xqyahct2YI")
        )
                .andExpect(status().is(403));
    }
}
