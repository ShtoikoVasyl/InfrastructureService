package edu.shtoiko.infrastructureservice.service.implementation;

import edu.shtoiko.infrastructureservice.model.Terminal;
import edu.shtoiko.infrastructureservice.model.dto.TerminalRequest;
import edu.shtoiko.infrastructureservice.model.dto.TerminalResponse;
import edu.shtoiko.infrastructureservice.repository.TerminalRepository;
import edu.shtoiko.infrastructureservice.service.TerminalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        return modelMapper.map(terminalRepository.save(terminal), TerminalResponse.class);
    }

    @Override
    public TerminalResponse getTerminalResponse(long terminalId) {
        return modelMapper.map(getTerminalById(terminalId), TerminalResponse.class);
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
