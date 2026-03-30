package com.sultanov.xojainka.features.users;

import com.sultanov.xojainka.core.contracts.DTOResource;
import com.sultanov.xojainka.core.exceptions.ResourceNotFoundException;
import com.sultanov.xojainka.core.utils.JsonResponse;

import com.sultanov.xojainka.features.users.dto.UserResource;
import com.sultanov.xojainka.features.users.models.User;
import com.sultanov.xojainka.features.users.repositories.UserRepository;
import com.sultanov.xojainka.features.users.requests.UserCreateRequest;
import com.sultanov.xojainka.features.users.services.UserService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.function.Function;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repository;
    private final Function<User, UserResource> mapper;
    private final UserService userService;

    public UserController(UserRepository repository, UserService userService) {
        this.mapper = UserResource::from;
        this.repository = repository;
        this.userService = userService;
    }

    @GetMapping
    public Map<String, Object> index(Pageable pageable) {
        var data = repository.findAll(pageable).map(mapper);
        return JsonResponse.collection(data);
    }

    @GetMapping("/{id}")
    public Map<String, DTOResource> show(@PathVariable Long id) {
        return repository.findById(id)
                .map(mapper) // Вызываем статику конкретного ресурса
                .map(JsonResponse::item)  // JsonResponse.item теперь должен принимать JsonResource
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, DTOResource> store(@Valid @RequestBody UserCreateRequest request)
    {
        userService.validateRegion(request.region_id());
        userService.validateCity(request.city_id());

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(userService.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        User savedUser = repository.save(user);
        return JsonResponse.item(mapper.apply(savedUser));
    }
}
