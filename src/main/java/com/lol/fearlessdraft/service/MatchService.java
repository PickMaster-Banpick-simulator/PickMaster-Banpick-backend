package com.lol.fearlessdraft.service;

import com.lol.fearlessdraft.dto.match.CreateMatchRequest;
import com.lol.fearlessdraft.dto.match.MatchResponse;
import com.lol.fearlessdraft.entity.BanPickState;
import com.lol.fearlessdraft.entity.DraftMatch;
import com.lol.fearlessdraft.entity.Room;
import com.lol.fearlessdraft.repository.DraftMatchRepository;
import com.lol.fearlessdraft.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final DraftMatchRepository draftMatchRepository;
    private final RoomRepository roomRepository;
    private final BanPickService banPickService;

    public MatchResponse createMatch(CreateMatchRequest request) {
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        DraftMatch draftMatch = DraftMatch.builder()
                .room(room)
                .matchType(request.matchType())
                .blueTeamName(request.blueTeamName())
                .redTeamName(request.redTeamName())
                .build();

        DraftMatch savedMatch = draftMatchRepository.save(draftMatch);

        // When a match is created, the ban-pick state should also be initialized.
        BanPickState initialBanPickState = banPickService.getBanPickState(room.getId());

        return MatchResponse.of(savedMatch, initialBanPickState);
    }

    @Transactional(readOnly = true)
    public MatchResponse findMatchByRoomId(String roomId) {
        DraftMatch match = draftMatchRepository.findByRoom_Id(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found for this room"));

        BanPickState banPickState = banPickService.getBanPickState(roomId);

        return MatchResponse.of(match, banPickState);
    }
}
