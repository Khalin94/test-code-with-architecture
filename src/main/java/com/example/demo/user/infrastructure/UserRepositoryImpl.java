package com.example.demo.user.infrastructure;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return userJpaRepository.findByIdAndStatus(id, userStatus).map(userEntity -> userEntity.toModel());
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return userJpaRepository.findByEmailAndStatus(email, userStatus).map(userEntity -> userEntity.toModel());
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(userEntity -> userEntity.toModel());
    }

    @Override
    public User getById(long id) {
        return findById(id).orElseThrow(() -> new ResourceNotFoundException("Users", id));
    }
}
