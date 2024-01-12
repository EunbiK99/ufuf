package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class CardInfoDto {
    private int card_id;
    private String bin_ ;
	private String card_type;
	private String install_month;
	private String approved_id;
}
