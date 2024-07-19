package edu.shtoiko.infrastructureservice.model.dto;

import edu.shtoiko.infrastructureservice.model.Terminal;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TerminalRequest {
    private String address;
    private String password;
    private String name;
}
