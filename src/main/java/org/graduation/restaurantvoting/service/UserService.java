package org.graduation.restaurantvoting.service;

import org.graduation.restaurantvoting.model.User;
import org.graduation.restaurantvoting.repository.UserRepository;
import org.graduation.restaurantvoting.to.UserTo;
import org.graduation.restaurantvoting.util.UsersUtil;
import org.springframework.cache.annotation.CacheEvict;
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

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return repository.prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        repository.deleteExisted(id);
    }

    @Cacheable(value = "users")
    public User get(int id) {
        return repository.getExisted(id);
    }

    @Cacheable(value = "users")
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return repository.getExistedByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "email"));
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        repository.prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo) {
        User user = get(userTo.id());
        repository.prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        User user = repository.getExisted(id);
        user.setEnabled(enabled);
    }
}
