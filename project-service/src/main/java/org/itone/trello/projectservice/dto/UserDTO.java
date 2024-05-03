package org.itone.trello.projectservice.dto;

import java.util.UUID;

public record UserDTO(UUID id, String name, String email) {
}
