package com.buapp.notificationservice.notification.achatnotification;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "CRUD operations for notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "List notifications", description = "Retrieve all notifications")
    public List<Notification> list() {
        return notificationService.listAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification", description = "Retrieve a notification by id")
    public Notification get(@PathVariable Long id) {
        return notificationService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create notification", description = "Create a new notification")
    public Notification create(@RequestBody Notification request) {
        return notificationService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update notification", description = "Update an existing notification by id")
    public Notification update(@PathVariable Long id, @RequestBody Notification request) {
        return notificationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete notification", description = "Delete a notification by id")
    public void delete(@PathVariable Long id) {
        notificationService.delete(id);
    }
}
