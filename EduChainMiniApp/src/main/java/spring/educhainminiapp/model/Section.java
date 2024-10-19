package spring.educhainminiapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sections")
@Data
public class Section { // Новый
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;
}
