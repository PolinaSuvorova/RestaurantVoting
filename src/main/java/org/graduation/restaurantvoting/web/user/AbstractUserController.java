package org.graduation.restaurantvoting.web.user;

import lombok.extern.slf4j.Slf4j;
import org.graduation.restaurantvoting.model.User;
import org.graduation.restaurantvoting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Slf4j
public abstract class AbstractUserController {

    @Autowired
    protected UserService userService;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    public User get(int id) {
        log.info("get {}", id);
        return userService.get(id);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        userService.delete(id);
    }
}
