package com.ejemplo.practica2.service;

import com.ejemplo.practica2.model.MockEndpoint;
import com.ejemplo.practica2.model.User;
import com.ejemplo.practica2.repository.MockEndpointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MockEndpointService {

    private final MockEndpointRepository repository;

    public MockEndpoint createMock(MockEndpoint mock, User owner) {
        mock.setOwner(owner);
        mock.setExpirationDate(mock.getExpirationDate() != null ? mock.getExpirationDate() : LocalDateTime.now().plusYears(1));
        return repository.save(mock);
    }

    public List<MockEndpoint> getMocksByUser(User user) {
        return repository.findByOwner(user);
    }

    public List<MockEndpoint> getAllMocks() {
        return repository.findByExpirationDateAfter(LocalDateTime.now());
    }

    public Optional<MockEndpoint> getMockById(Long id) {
        return repository.findById(id);
    }

    public void deleteMock(Long id) {
        repository.deleteById(id);
    }

    public Optional<MockEndpoint> findByPath(String path) {
        return repository.findByPath(path);
    }
}