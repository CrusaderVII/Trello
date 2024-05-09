package org.itone.trello.taskservice.dto;

import java.util.UUID;

public record ProjectDTO(UUID id, String name, String description) {
}
