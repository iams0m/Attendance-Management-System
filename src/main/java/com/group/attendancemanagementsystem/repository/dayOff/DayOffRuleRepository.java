package com.group.attendancemanagementsystem.repository.dayOff;

import com.group.attendancemanagementsystem.domain.dayOff.DayOffRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOffRuleRepository extends JpaRepository<DayOffRule, Long> {
}
