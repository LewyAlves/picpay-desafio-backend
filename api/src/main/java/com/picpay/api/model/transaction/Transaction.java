package com.picpay.api.model.transaction;

import com.picpay.api.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal anount;
    @ManyToOne
    @JoinColumn(name = "sander_id")
    private User sander;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;
    private LocalDateTime timestamp;
}
