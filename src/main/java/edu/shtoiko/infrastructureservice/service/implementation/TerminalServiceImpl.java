package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.Terminal;
import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.repository.TerminalRepository;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private final TerminalRepository terminalRepository;
    private final EncoderImpl encoder;
    private final ModelMapper modelMapper;

    @Override
    public TerminalResponse create(TerminalRequest terminalRequest) {
        Terminal terminal = modelMapper.map(terminalRequest, Terminal.class);
        terminal.setSignature(encoder.generateKey());
        terminal = terminalRepository.save(terminal);
        log.info("Created new terminal id={}", terminal.getId());
        return modelMapper.map(terminal, TerminalResponse.class);
    }

    @Override
    public TerminalResponse getTerminalResponse(long terminalId) {
        return modelMapper.map(getTerminalById(terminalId), TerminalResponse.class);
    }

    public Terminal getTerminalById(long terminalId) {
        return terminalRepository.findById(terminalId).orElseThrow(() -> {
            log.error("Terminal with id={} not found", terminalId);
            return new EntityNotFoundException("Terminal with id=" + terminalId + " not found");
        });
    }

    @Override
    public boolean checkin(long login, String password) {
        Terminal terminal = getTerminalById(login);
        return terminal.getPassword().equals(password);
    }
}
