package com.buapp.notificationservice.notification.abonnementnotification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abonnement-notifications")
@RequiredArgsConstructor
@Tag(name = "AbonnementNotifications", description = "CRUD for abonnement notifications")
public class AbonnementNotificationController {
    private final AbonnementNotificationService service;

    @GetMapping
    @Operation(summary = "List abonnement notifications")
    public List<AbonnementNotification> list() { return service.listAll(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get abonnement notification")
    public AbonnementNotification get(@PathVariable Long id) { return service.get(id); }
}
