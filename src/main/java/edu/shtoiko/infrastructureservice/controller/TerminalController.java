package edu.shtoiko.infrastructureservice.controller;

import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/terminal/")
@Tag(name = "Terminal Controller", description = "Controller for managing terminals")
public class TerminalController {
    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }
    @Operation(summary = "Create a new terminal", description = "Creates a new terminal for the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Terminal successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TerminalResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TerminalResponse> createTerminal(
            @Parameter(description = "ID of the user for whom the terminal is being created", required = true)
            @Valid @RequestBody TerminalRequest terminalRequest) {
        return new ResponseEntity<>(terminalService.create(terminalRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Get terminal information", description = "Retrieves information about a terminal by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Terminal information successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TerminalResponse.class))),
            @ApiResponse(responseCode = "404", description = "Terminal not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{terminal_id}/")
    public ResponseEntity<TerminalResponse> getTerminal(
            @Parameter(description = "ID of the terminal to retrieve", required = true)
            @PathVariable("terminal_id") long terminalId) {
        return new ResponseEntity<>(terminalService.getTerminalResponse(terminalId), HttpStatus.OK);
    }
}
