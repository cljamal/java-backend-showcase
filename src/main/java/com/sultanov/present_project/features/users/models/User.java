package com.sultanov.present_project.features.users.models;

import com.sultanov.present_project.core.abstractions.AbstractModel;
import com.sultanov.present_project.features.cities.models.City;
import com.sultanov.present_project.features.rbac.models.Permission;
import com.sultanov.present_project.features.rbac.models.Role;
import com.sultanov.present_project.features.regions.models.Region;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AbstractModel implements UserDetails {
    private String password;

    @Nullable
    private String username;

    @Nullable
    private String phone;

    @Nullable
    private String about;

    @Nullable
    @Column(name = "first_name")
    private String firstName;

    @Nullable
    @Column(name = "language")
    private String language;

    @Nullable
    @Column(name = "last_name")
    private String lastName;

    @Nullable
    @Setter(AccessLevel.NONE)
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "full_name", insertable = false, updatable = false)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToMany
    @JoinTable(
            name = "model_has_roles",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(
            name = "model_has_permissions",
            joinColumns = @JoinColumn(name = "model_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;


    public Set<Permission> getAllPermissions() {
        Set<Permission> all = new HashSet<>(permissions != null ? permissions : Set.of());

        if (roles != null) {
            for (Role role : roles) {
                if (role.getPermissions() != null) {
                    all.addAll(role.getPermissions());
                }
            }
        }

        return all;
    }

    public boolean hasPermission(String slug) {
        return getAllPermissions().stream()
                .anyMatch(p -> p.getSlug().equals(slug));
    }

    public boolean hasRole(String slug) {
        return roles != null && roles.stream()
                .anyMatch(r -> r.getSlug().equals(slug));
    }

    public boolean can(String slug) {
        return hasRole(slug) || hasPermission(slug);
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        Set<GrantedAuthority> authorities = getAllPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getSlug()))
                .collect(Collectors.toSet());

        if (roles != null) {
            roles.forEach(role ->
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getSlug()))
            );
        }
        return authorities;
    }

    @Override
    @NonNull
    public String getUsername() {
        return this.phone != null ? this.phone : "";
    }

    public String getNickname() {
        return this.username != null ? this.username : "";
    }

    @Override
    @NonNull
    public String getPassword() {
        return this.password != null ? this.password : "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
