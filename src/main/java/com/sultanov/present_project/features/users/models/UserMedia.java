package com.sultanov.present_project.features.users.models;

import com.sultanov.present_project.core.models.Media;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("User")
public class UserMedia extends Media {
}