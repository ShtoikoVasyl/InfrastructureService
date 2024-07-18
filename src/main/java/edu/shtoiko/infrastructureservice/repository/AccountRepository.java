package edu.shtoiko.infrastructureservice.repository;

import edu.shtoiko.infrastructureservice.model.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<CurrentAccount, Long> {
    List<CurrentAccount> findAllByOwnerId(long id);

    boolean existsByAccountNumber(long accountNumber);

    Optional<CurrentAccount> findByAccountNumber(long accountNumber);
}
