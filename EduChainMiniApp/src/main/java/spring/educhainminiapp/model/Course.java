package spring.educhainminiapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "courses")
@Data
public class Course { // Новый
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "required_level", nullable = false)
    private int requiredLevel;

    @Column(name = "exp_reward", nullable = false)
    private int expReward;

    @Column(name = "reward_tokens", nullable = false)
    private int rewardTokens;

    @ElementCollection
    @CollectionTable(name = "course_section_ids", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "section_id")
    private List<Long> sectionIds;
}

