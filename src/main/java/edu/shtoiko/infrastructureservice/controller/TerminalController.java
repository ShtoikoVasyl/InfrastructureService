package edu.shtoiko.infrastructureservice.controller;

import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.service.TerminalService;
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
public class TerminalController {
    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @PostMapping("/{user_id}/")
    public ResponseEntity<TerminalResponse> createTerminal(@PathVariable("user_id") long accountId, @RequestBody TerminalRequest terminalRequest){
        return new ResponseEntity<>(terminalService.create(terminalRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{terminal_id}/")
    public ResponseEntity<TerminalResponse> getTerminal(@PathVariable("terminal_id") long terminalId){
        return new ResponseEntity<>(terminalService.getTerminalResponse(terminalId), HttpStatus.OK);
    }


}
