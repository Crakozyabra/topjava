package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = {"datajpa", "datajpa_jpa"})
public class UserServiceDataJpaTest extends UserServiceTest{
}
