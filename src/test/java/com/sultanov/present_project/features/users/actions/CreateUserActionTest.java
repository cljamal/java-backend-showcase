package com.sultanov.present_project.features.users.actions;

import com.sultanov.present_project.core.actions.LocationActions;
import com.sultanov.present_project.core.actions.PasswordActions;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.regions.models.Region;
import com.sultanov.present_project.features.users.models.User;
import com.sultanov.present_project.features.users.repositories.UserRepository;
import com.sultanov.present_project.features.users.requests.UserCreateRequest;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserActionTest {
    @Mock
    UserRepository repository;

    @Mock
    PasswordActions passwordActions;

    @Mock
    LocationActions locationActions;

    @InjectMocks
    CreateUserAction action;

    private UserCreateRequest validRequest() {
        return new UserCreateRequest(
                "john",
                "password123",
                "+998912345678",
                null,
                "John",
                "Doe",
                null,
                null
        );
    }

    // ══════════════════════════════════════════════════════════════
    //  Блок 1: Валидация телефона
    // ══════════════════════════════════════════════════════════════

    @Test
    void shouldThrowWhenPhoneIsNull() {
        UserCreateRequest request = new UserCreateRequest(
                null, "password123", null, null, null, null, null, null
        );

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> action.handle(request)
        );
        assertEquals("Phone is required", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPhoneIsInvalid() {
        UserCreateRequest request = new UserCreateRequest(
                null, "password123", "invalid_phone", null, null, null, null, null
        );

        assertThrows(ValidationException.class, () -> action.handle(request));
    }

    @Test
    void shouldNormalizeShortPhone() {
        UserCreateRequest request = new UserCreateRequest(
                null, "password123", "912345678", null, null, null, null, null
        );

        User user = action.handle(request);

        assertEquals("+998912345678", user.getPhone());
    }

    // ══════════════════════════════════════════════════════════════
    //  Блок 2: Уникальность (username / phone)
    // ══════════════════════════════════════════════════════════════

    @Test
    void shouldThrowWhenUsernameAlreadyTaken() {
        UserCreateRequest request = validRequest();

        when(repository.existsByUsername("john")).thenReturn(true);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> action.handle(request)
        );
        assertEquals("Username already taken", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPhoneAlreadyTaken() {
        UserCreateRequest request = validRequest();
        when(repository.existsByPhone("+998912345678")).thenReturn(true);

        ValidationException ex = assertThrows(
                ValidationException.class,
                () -> action.handle(request)
        );
        assertEquals("Phone already taken", ex.getMessage());
    }

    // ══════════════════════════════════════════════════════════════
    //  Блок 3: Happy path — успешное создание пользователя
    // ══════════════════════════════════════════════════════════════

    @Test
    void shouldReturnUserWithCorrectFields() {
        UserCreateRequest request = validRequest();

        when(passwordActions.encode("password123")).thenReturn("hashed_password");

        User user = action.handle(request);

        assertEquals("john", user.getNickname());
        assertEquals("+998912345678", user.getPhone());
        assertEquals("hashed_password", user.getPassword());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertNull(user.getRegion());
        assertNull(user.getCity());
    }

    @Test
    void shouldSetRegionAndCopyCityFromIt() {
        City city = new City();
        Region region = new Region();
        region.setCity(city);

        UserCreateRequest request = new UserCreateRequest(
                null, "password123", "+998912345678",
                null, null, null, 1L, null  // regionId=1, cityId=null
        );

        when(locationActions.getRegionById(1L)).thenReturn(region);

        User user = action.handle(request);

        assertEquals(region, user.getRegion());
        assertEquals(city, user.getCity());

        verify(locationActions).getRegionById(1L);

        verify(locationActions, never()).getCityById(any());
    }

    @Test
    void shouldSetCityFromCityId() {
        City city = new City();

        UserCreateRequest request = new UserCreateRequest(
                null, "password123", "+998912345678",
                null, null, null, null, 2L  // regionId=null, cityId=2
        );

        when(locationActions.getCityById(2L)).thenReturn(city);

        User user = action.handle(request);

        assertEquals(city, user.getCity());
        assertNull(user.getRegion());

        verify(locationActions).getCityById(2L);
        verify(locationActions, never()).getRegionById(any());
    }
}
