package org.itone.trello.taskservice.dto;

import java.util.UUID;

public record DeskDTO(UUID id, String name, UUID projectId) {
}
