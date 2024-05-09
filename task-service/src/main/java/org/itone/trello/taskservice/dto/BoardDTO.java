package org.itone.trello.taskservice.dto;

import java.util.UUID;

public record BoardDTO(UUID id, String name, String deskName) {
}
