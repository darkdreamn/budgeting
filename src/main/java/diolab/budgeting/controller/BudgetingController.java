package diolab.budgeting.controller;

import diolab.budgeting.service.BudgetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
public class BudgetingController {
    private final BudgetingService service;

    public BudgetingController(BudgetingService service) {
        this.service = service;
    }

    @PostMapping("/command")
    public ResponseEntity<String> sendCommand(@RequestBody String textCommand) {
        String responseIA = service.processCommand(textCommand);
        return ResponseEntity.ok(responseIA);
    }
}
