package org.itone.trello.taskservice.dto;

import java.util.UUID;

public record TaskDTO(UUID id, String name, String description, String boardName) {
}
