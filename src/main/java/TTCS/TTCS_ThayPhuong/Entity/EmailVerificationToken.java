package TTCS.TTCS_ThayPhuong.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "EmailVerificationToken")
@Table(name = "email_verification_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id",nullable = false)
    @JsonBackReference
    private User user;

    @Column(name ="token",unique = true,nullable = false)
    private String token;

    @Column(name = "expiration_date",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
}
