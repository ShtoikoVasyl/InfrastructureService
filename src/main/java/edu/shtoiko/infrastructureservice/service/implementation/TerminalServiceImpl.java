package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.exeptions.ResponseException;
import edu.shtoiko.infrastructureservice.model.Terminal;
import edu.shtoiko.infrastructureservice.model.dto.TerminalCreateRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.repository.TerminalRepository;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TerminalServiceImpl implements TerminalService {
    private final TerminalRepository terminalRepository;
    private final EncoderImpl encoder;
    private final ModelMapper modelMapper;
    private final DiscoveryClient discoveryClient;

    @Override
    public TerminalResponse create(TerminalCreateRequest terminalRequest) {
        Terminal terminal = modelMapper.map(terminalRequest, Terminal.class);
        terminal.setSignature(encoder.generateKey());
        terminal = terminalRepository.save(terminal);
        log.info("Created new terminal id={}", terminal.getId());
        return modelMapper.map(terminal, TerminalResponse.class);
    }

    @Override
    public TerminalResponse getTerminalResponse(Long terminalId) {
        return modelMapper.map(getTerminalById(terminalId), TerminalResponse.class);
    }

    public Terminal getTerminalById(Long terminalId) {
        return terminalRepository.findById(terminalId).orElseThrow(() -> {
            log.error("Terminal with id={} not found", terminalId);
            return new ResponseException(HttpStatus.NOT_FOUND, "Terminal with id:" + terminalId + " not found");
        });
    }

    @Override
    public boolean checkin(Long login, String password) {
        Terminal terminal = getTerminalById(login);
        return terminal.getPassword().equals(password);
    }

    @Override
    public List<TerminalResponse> getAllRegisteredTerminals() {
        return terminalRepository.findAll().stream()
            .map(terminal -> modelMapper.map(terminal, TerminalResponse.class))
            .toList();
    }

    // todo
    @Override
    public List<TerminalResponse> getAllTerminalsFromDiscovery() {
        return null;
    }
}
