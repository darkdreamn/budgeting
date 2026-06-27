package diolab.budgeting.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String description;

    @Column(name = "value_transaction")
    private BigDecimal value;
    private String type;

    public Transaction(String description, BigDecimal value, String type) {
        this.description = description;
        this.value = value;
        this.type = type;
    }
}
