package com.example.cms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "brief")
    private String brief;

    @Column(name = "content")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "sort")
    private String sort;

    @PrePersist
    protected void onCreate() {
        createDate = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id", referencedColumnName = "member_id")
    private Member author;  
}
