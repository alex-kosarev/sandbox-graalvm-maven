package pro.akosarev.sandbox.controller;

import java.time.Instant;
import java.util.UUID;

public record TodoPresentationV1(UUID id, boolean completed, String task, Instant dateCreated) {
}
