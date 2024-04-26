package org.itone.trello.projectservice.dto;

import java.util.List;
//TODO: add list of users assigned to this task
public record TaskDTO(long id, String name, String description, String boardName) {
}
