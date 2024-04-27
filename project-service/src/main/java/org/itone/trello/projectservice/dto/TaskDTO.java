package org.itone.trello.projectservice.dto;

import java.util.List;
import java.util.Set;

public record TaskDTO(long id, String name, String description, String boardName) {
}
