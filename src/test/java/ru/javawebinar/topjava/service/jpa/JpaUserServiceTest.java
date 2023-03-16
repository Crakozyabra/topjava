package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;


@ActiveProfiles(profiles = {Profiles.JPA, Profiles.SECOND_LEVEL_CACHE_DISABLE})
public class JpaUserServiceTest extends AbstractUserServiceTest {
}