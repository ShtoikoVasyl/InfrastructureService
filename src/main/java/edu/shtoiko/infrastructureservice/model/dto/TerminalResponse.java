package edu.shtoiko.infrastructureservice.model.dto;

import edu.shtoiko.infrastructureservice.model.Terminal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalResponse {
    private long id;
    private String name;
    private String address;
    private String signature;

    public TerminalResponse(Terminal terminal) {
        this.name = terminal.getName();
        this.id = terminal.getId();
        this.address = terminal.getAddress();
        this.signature = terminal.getSignature();
    }
}
