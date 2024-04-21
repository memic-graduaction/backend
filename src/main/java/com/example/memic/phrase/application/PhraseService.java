package com.example.memic.phrase.application;

import com.example.memic.member.domain.Member;
import com.example.memic.phrase.domain.Phrase;
import com.example.memic.phrase.domain.PhraseRepository;
import com.example.memic.phrase.domain.Tag;
import com.example.memic.phrase.domain.TagRepository;
import com.example.memic.phrase.dto.PhraseCreateRequest;
import com.example.memic.phrase.dto.PhraseCreatedResponse;
import com.example.memic.phrase.dto.TranscriptionPhraseResponse;
import com.example.memic.transcription.domain.TranscriptionSentence;
import com.example.memic.transcription.domain.TranscriptionSentenceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PhraseService {

    private final PhraseRepository phraseRepository;
    private final TranscriptionSentenceRepository transcriptionSentenceRepository;
    private final TagRepository tagRepository;

    public PhraseService(
            final PhraseRepository phraseRepository,
            final TranscriptionSentenceRepository transcriptionSentenceRepository,
            final TagRepository tagRepository
    ) {
        this.phraseRepository = phraseRepository;
        this.transcriptionSentenceRepository = transcriptionSentenceRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    public PhraseCreatedResponse createPhrase(final PhraseCreateRequest request, final Member member) {
        final TranscriptionSentence sentence = transcriptionSentenceRepository.getById(request.sentenceId());
        final Phrase newPhrase = new Phrase(
                request.meaning(),
                member,
                sentence,
                request.startIndex(),
                request.endIndex()
        );

        final List<Tag> tags = getTagsByIds(request.tagIds());
        tags.forEach(newPhrase::addTag);

        final Phrase saved = phraseRepository.save(newPhrase);
        return new PhraseCreatedResponse(saved.getId());
    }

    private List<Tag> getTagsByIds(final List<Long> requestTagIds) {
        final List<Tag> registeredTags = tagRepository.findByIdIn(requestTagIds);
        if (registeredTags.size() != requestTagIds.size()) {
            throw new EntityNotFoundException("태그 아이디에 해당하는 태그가 없습니다. 요청한 태그 아이디: " + requestTagIds);
        }
        return registeredTags;
    }

    public List<TranscriptionPhraseResponse> getTranscriptionPhrases(final Long transcriptionId, final Member member) {
        final List<IdWrapper> sentenceIdWrappers = transcriptionSentenceRepository.getIdsByTranscriptionId(transcriptionId);
        final List<Long> sentenceIds = sentenceIdWrappers.stream()
                                                         .map(IdWrapper::id)
                                                         .toList();
        final List<Phrase> phrases = phraseRepository.findByMemberAndSentenceIdIn(member, sentenceIds);
        return TranscriptionPhraseResponse.from(phrases);
    }
}
