package org.graduation.restaurantvoting.service;

import org.graduation.restaurantvoting.model.User;
import org.graduation.restaurantvoting.repository.UserRepository;
import org.graduation.restaurantvoting.to.UserTo;
import org.graduation.restaurantvoting.util.UsersUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.prepareAndSave(user);
    }

    @CacheEvict(cacheNames =  "users", key="#id")
    public void delete(int id) {
        repository.deleteExisted(id);
    }

    @Cacheable(cacheNames =  "users", key="#id")
    public User get(int id) {
        return repository.getExisted(id);
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return repository.getExistedByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @CachePut(cacheNames = "users", key="#user.id")
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
       return repository.prepareAndSave(user);
    }

    @CachePut(value = "users", key="#userTo.id")
    @Transactional
    public User update(UserTo userTo) {
        User user = get(userTo.id());
       return repository.prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }

    @CachePut(value = "users", key="#id")
    @Transactional
    public User enable(int id, boolean enabled) {
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
        return user;
    }
}
