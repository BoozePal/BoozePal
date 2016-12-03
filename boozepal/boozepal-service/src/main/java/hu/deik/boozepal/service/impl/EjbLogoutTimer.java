package hu.deik.boozepal.service.impl;

import hu.deik.boozepal.common.entity.User;
import hu.deik.boozepal.core.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({SpringBeanAutowiringInterceptor.class})
public class EjbLogoutTimer {

    public static final int DURATION_OF_MINUTES = 5;

    @Autowired
    private UserRepository userDao;

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void doWork() {
        List<User> onlineUsers = userDao.findOnlineUsers();
        List<User> longLoggedinUser = onlineUsers.stream()
                .filter(user -> durationOfTwoDate(user))
                .collect(Collectors.toList());
        for (User user : longLoggedinUser) {
            user.setLoggedIn(false);
            userDao.saveAndFlush(user);
        }
        System.out.println("Online User : " + onlineUsers.size());
    }

    private boolean durationOfTwoDate(User actualUser) {
        if (actualUser.getLastLoggedinTime() == null) {
            actualUser.setLastLoggedinTime(new Date());
        }
        Instant instant = Instant.ofEpochMilli(actualUser.getLastLoggedinTime().getTime());
        Instant twentyMinutesAgo = Instant.now().minus(Duration.ofMinutes(DURATION_OF_MINUTES));
        return instant.isBefore(twentyMinutesAgo);
    }
}
