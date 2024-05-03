package org.itone.trello.projectservice.dto;

import java.util.UUID;

public record BoardDTO(UUID id, String name, String deskName) {
}
