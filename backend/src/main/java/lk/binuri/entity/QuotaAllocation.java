package lk.binuri.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "quota_allocation")
public class QuotaAllocation {
    @Id
    private String type;

    @Column(nullable = false,columnDefinition = "INT DEFAULT 0")
    private Integer amount;
}
