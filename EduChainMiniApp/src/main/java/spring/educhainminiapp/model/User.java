package spring.educhainminiapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String username;

    @Column(name = "photo_url")
    private String photoUrl;



    @Column(name = "tokens", nullable = false)
    private int tokens;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "exp", nullable = false)
    private int exp;

    @Column(name = "total_exp", nullable = false)
    private int totalExp;

    @ManyToMany
    @JoinTable(
            name = "user_achievements",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id")
    )
    private Set<Achievement> achievements;

    @ManyToMany
    @JoinTable(
            name = "user_completed_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> completedCourses;

    @ManyToMany
    @JoinTable(
            name = "user_completed_sections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> completedSections;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_enrolled_courses",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> enrolledCourses = new HashSet<>();
}
