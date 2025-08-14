package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.entity.BanPickActionType;
import com.lol.fearlessdraft.entity.BanRule;
import com.lol.fearlessdraft.entity.PickBan;
import com.lol.fearlessdraft.repository.PickBanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BanPickRestrictionService {

    private final PickBanRepository pickBanRepository;

    public BanPickRestrictionService(PickBanRepository pickBanRepository) {
        this.pickBanRepository = pickBanRepository;
    }

    public void validateChampionSelection(
            Long matchId,
            Long currentGameNumber,
            Long teamId,
            Long opponentTeamId,
            String championName,
            BanRule banRule
    ) {
        if (banRule == null) return;

        // 소프트 피어리스: 자기 팀의 이전 게임 픽/밴만 금지
        Set<String> myTeamChamps = getPreviousPicksAndBans(matchId, currentGameNumber, teamId);
        if (myTeamChamps.contains(championName)) {
            throw new IllegalArgumentException("이 챔피언은 이전 게임에서 이미 픽/밴하여 선택할 수 없습니다.");
        }

        // 하드 피어리스: 상대 팀의 이전 게임 픽/밴도 금지
        if (banRule == BanRule.HARDFEARLESS) {
            Set<String> opponentChamps = getPreviousPicksAndBans(matchId, currentGameNumber, opponentTeamId);
            if (opponentChamps.contains(championName)) {
                throw new IllegalArgumentException("상대 팀이 이전 게임에서 픽/밴한 챔피언은 선택할 수 없습니다.");
            }
        }
    }

    private Set<String> getPreviousPicksAndBans(Long matchId, Long currentGameNumber, Long teamId) {
        List<PickBan> records = pickBanRepository.findByMatchIdAndTeamIdAndGameNumberLessThanAndTypeIn(
                matchId,
                teamId,
                currentGameNumber,
                List.of(BanPickActionType.PICK, BanPickActionType.BAN)
        );
        return records.stream()
                .map(PickBan::getChampionName)
                .collect(Collectors.toSet());
    }
}
