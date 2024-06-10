package com.group.attendancemanagementsystem.dto.commute.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

@ToString
@Getter
@NoArgsConstructor
public class YearMonthRequest {

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
    private YearMonth yearMonth;
}
