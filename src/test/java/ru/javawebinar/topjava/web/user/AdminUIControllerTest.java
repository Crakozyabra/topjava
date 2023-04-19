package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
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
        ResultActions resultActions = perform(MockMvcRequestBuilders
                .post(UI_URL)
                .with(
                        user("admin@gmail.com")
                                .password("admin")
                                .roles("ADMIN", "USER")
                )
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonWithPasswordTo(userTo, userTo.getPassword()))
        ).andDo(print());
    }
}
