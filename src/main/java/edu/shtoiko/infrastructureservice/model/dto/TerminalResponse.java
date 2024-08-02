package edu.shtoiko.infrastructureservice.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TerminalResponse {
    private long id;
    private String name;
    private String address;
    private String signature;
}
