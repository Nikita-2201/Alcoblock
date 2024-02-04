package ru.gknsv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlcoHistoryDto extends AlcoDto {
    private String alcoId;
    private String volume;
}
