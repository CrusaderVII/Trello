package org.itone.trello.projectservice.dto.creation;

import java.util.UUID;

public record UserCreationDTO (String name, String email, String password) {
}
