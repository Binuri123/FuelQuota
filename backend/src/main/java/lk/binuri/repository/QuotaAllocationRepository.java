package lk.binuri.repository;

import lk.binuri.entity.QuotaAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotaAllocationRepository extends JpaRepository<QuotaAllocation,String> {
}
