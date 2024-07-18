package edu.shtoiko.infrastructureservice.model;

import edu.shtoiko.infrastructureservice.utils.SixDigitIdGenerator;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Table(name = "terminals")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Terminal {
    public Terminal() {
    }

    @Id
//    @IdGeneratorType(SixDigitIdGenerator.class)
    @GeneratedValue(generator = "six-digit-id-generator")
    @GenericGenerator(
            name = "six-digit-id-generator",
            strategy = "edu.shtoiko.infrastructureservice.utils.SixDigitIdGenerator"
    )
    private Long id;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String password;

    @Column
    private String signature;

    @Column
    private Instant lastActivity;
//    private Map<String, Map<Integer, Integer>> banknotesQuantity;
}
