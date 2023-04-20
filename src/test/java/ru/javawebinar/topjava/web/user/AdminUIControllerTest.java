package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static ru.javawebinar.topjava.UserTestData.jsonWithPasswordTo;

public class AdminUIControllerTest extends AbstractControllerTest {

    static final String UI_URL = "/admin/users";

    @Test
    public void createOrUpdate() throws Exception {
        User newUser = UserTestData.getNew();
        UserTo userTo = UsersUtil.asTo(newUser);
        String contents = jsonWithPasswordTo(userTo, userTo.getPassword());
        perform(MockMvcRequestBuilders
                .post(UI_URL)
                .with(
                        user("amin@gmail.com")
                                .password("amin")
                                .roles("ADMIN", "USER")
                )
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                //.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(contents)
        ).andDo(print());
    }
}
