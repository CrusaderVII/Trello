package org.itone.trello.projectservice.dto;

import java.util.UUID;

public record ProjectDTO(UUID id, String name, String description) {
}
