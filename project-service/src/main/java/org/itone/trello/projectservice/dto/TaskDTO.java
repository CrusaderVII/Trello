package org.itone.trello.projectservice.dto;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record TaskDTO(UUID id, String name, String description, String boardName) {
}
