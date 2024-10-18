package spring.educhainminiapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "assignments")
@Data
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question", nullable = false, length = 1000)
    private String question;

    @Column(name = "answer", nullable = false, length = 1000)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(name = "exp_reward", nullable = false)
    private int expReward;

    @Column(name = "reward_tokens", nullable = false)
    private int rewardTokens;
}
