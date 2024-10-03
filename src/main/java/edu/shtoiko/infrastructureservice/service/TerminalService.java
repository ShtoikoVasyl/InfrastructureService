package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.model.dto.TerminalCreateRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;

import java.util.List;

public interface TerminalService {
    TerminalResponse create(TerminalCreateRequest terminalRequest);

    TerminalResponse getTerminalResponse(Long terminalId);

    boolean checkin(Long login, String password);

    List<TerminalResponse> getAllRegisteredTerminals();

    List<TerminalResponse> getAllTerminalsFromDiscovery();
}
