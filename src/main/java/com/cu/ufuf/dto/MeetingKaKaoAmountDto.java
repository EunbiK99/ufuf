package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class MeetingKaKaoAmountDto {
    private int total;
	private int tax_free;
	private int vat;
	private int point;
	private int discount;
}
