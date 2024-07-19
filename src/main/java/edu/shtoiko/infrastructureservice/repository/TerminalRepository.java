package edu.shtoiko.infrastructureservice.repository;

import edu.shtoiko.infrastructureservice.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, Long> {
}