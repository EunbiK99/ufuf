package com.cu.ufuf.dto;

import lombok.Data;

@Data
public class MeetingVoteBestMemberDto {

    private int bestMemberVoteId;
    private int groupMemberIdFrom;
    private int groupMemberIdTo;
    private int bestMemberVote;

}
