package org.itone.trello.taskservice.dto;

import java.util.UUID;

public record UserDTO(UUID id, String name, String email) {
}
