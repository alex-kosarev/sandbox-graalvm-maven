package pro.akosarev.sandbox.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/todos")
public class TodosRestController implements RowMapper<TodoPresentationV1> {

    private final JdbcTemplate jdbcTemplate;

    public TodosRestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TodoPresentationV1 mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new TodoPresentationV1(resultSet.getObject("id", UUID.class),
                resultSet.getBoolean("c_completed"),
                resultSet.getString("c_task"),
                resultSet.getTimestamp("c_date_created").toInstant());
    }

    @GetMapping
    public ResponseEntity<List<TodoPresentationV1>> getAllTodos() {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/vnd.sandbox.todos.v1+json"))
                .body(this.jdbcTemplate.query("select * from todo.t_todo", this));
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoPresentationV1> getTodo(@PathVariable UUID id) {
        return this.jdbcTemplate.query("select * from todo.t_todo where id = ?", this, id)
                .stream()
                .findFirst()
                .map(todo -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("application/vnd.sandbox.todo.v1+json"))
                        .body(todo))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping(consumes = "application/vnd.sandbox.new-todo.v1+json")
    public ResponseEntity<TodoPresentationV1> createTodo(@RequestBody NewTodoPayloadV1 payload,
                                                         UriComponentsBuilder uriComponentsBuilder) {
        var id = UUID.randomUUID();
        var now = Instant.now();
        this.jdbcTemplate.update("insert into todo.t_todo(id, c_task, c_date_created) values (?, ?, ?)",
                id, payload.task(), Timestamp.from(now));
        return ResponseEntity.created(uriComponentsBuilder.pathSegment(id.toString()).build(Map.of()))
                .contentType(MediaType.valueOf("application/vnd.sandbox.todo.v1+json"))
                .body(new TodoPresentationV1(id, false, payload.task(), now));
    }
}
