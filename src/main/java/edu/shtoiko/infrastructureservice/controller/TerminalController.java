package edu.shtoiko.infrastructureservice.controller;

import edu.shtoiko.infrastructureservice.model.dto.TerminalCreateRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/terminal/")
@Tag(name = "Terminal Controller", description = "Controller for managing terminals")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @Operation(summary = "Create a new terminal",
        description = "Creates a new terminal for the specified user. Requires TERMINAL_WRITE authority.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Terminal successfully created",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TerminalResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('TERMINAL_WRITE')")
    public ResponseEntity<?> createTerminal(
        @Parameter(description = "Details of the terminal to create",
            required = true) @Valid @RequestBody TerminalCreateRequest terminalRequest) {
        return new ResponseEntity<>(terminalService.create(terminalRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get terminal information",
        description = "Retrieves information about a terminal by its ID. Requires TERMINAL_READ authority.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminal information successfully retrieved",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TerminalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Terminal not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{terminal_id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('TERMINAL_READ')")
    public ResponseEntity<?> getTerminal(
        @Parameter(description = "ID of the terminal to retrieve",
            required = true) @PathVariable("terminal_id") long terminalId) {
        return new ResponseEntity<>(terminalService.getTerminalResponse(terminalId), HttpStatus.OK);
    }

    @Operation(summary = "Get all registered in DB terminals information",
        description = "Retrieves information about a all terminals in data base. Requires TERMINAL_READ authority.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminals information successfully retrieved",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TerminalResponse.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('TERMINAL_READ')")
    public ResponseEntity<?> getAllRegisteredTerminals() {
        return new ResponseEntity<>(terminalService.getAllRegisteredTerminals(), HttpStatus.OK);
    }

    @Operation(summary = "Get all registered in system terminals information",
        description = "Retrieves information about a all terminals from discovery server. Requires TERMINAL_READ authority.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Terminals information successfully retrieved",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TerminalResponse.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/online")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAuthority('TERMINAL_READ')")
    public ResponseEntity<?> getAllTerminalsInSystem() {
        return new ResponseEntity<>(terminalService.getAllTerminalsFromDiscovery(), HttpStatus.OK);
    }
}