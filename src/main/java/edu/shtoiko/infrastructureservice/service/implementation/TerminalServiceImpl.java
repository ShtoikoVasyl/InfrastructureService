package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.Terminal;
import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.repository.TerminalRepository;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private final TerminalRepository terminalRepository;
    private final EncoderImpl encoder;


    @Override
    public TerminalResponse create(TerminalRequest terminalRequest) {
        Terminal terminal = terminalRequest.convertToEntity();
        terminal.setSignature(encoder.generateKey());
        return new TerminalResponse(terminalRepository.save(terminal));
    }

    @Override
    public TerminalResponse getTerminalResponse(long terminalId) {
        return new TerminalResponse(getTerminalById(terminalId));
    }

    public Terminal getTerminalById(long terminalId){
        return terminalRepository.findById(terminalId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public boolean checkin(long login, String password){
        Terminal terminal = getTerminalById(login);
        return terminal.getPassword().equals(password);
    }


}
