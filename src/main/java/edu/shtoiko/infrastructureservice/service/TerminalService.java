package edu.shtoiko.infrastructureservice.service;

import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;

public interface TerminalService {
    TerminalResponse create(TerminalRequest terminalRequest);

    TerminalResponse getTerminalResponse(long terminalId);

    boolean checkin(long login, String password);
}
