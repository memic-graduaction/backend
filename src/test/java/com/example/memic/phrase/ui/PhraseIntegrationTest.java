package com.example.memic.phrase.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.memic.phrase.domain.PhraseRepository;
import com.example.memic.phrase.domain.TagRepository;
import com.example.memic.phrase.dto.PhraseCreateRequest;
import com.example.memic.phrase.dto.PhraseCreatedResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("nonAsciiCharacters")
@AutoConfigureMockMvc
@SpringBootTest
class PhraseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PhraseRepository phraseRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    void createPhrase() throws Exception {
        // disable foreign key check
        try (final var connection = dataSource.getConnection()) {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");
            connection.createStatement().execute("INSERT INTO transcription_sentence (id, start_point, content, transcription_id) VALUES (1, '00:00:00', 'hello world', 1)");
            connection.createStatement().execute("INSERT INTO tag (id, name) VALUES (1, 'tag1')");
            connection.createStatement().execute("INSERT INTO tag (id, name) VALUES (2, 'tag2')");
            connection.createStatement().execute("INSERT INTO member (id) VALUES (1)"); // TODO : 회원가입 기능 추가 후 수정
        }

        final var meaning = "세계";
        final var startIndex = 1;
        final var endIndex = 1;
        final var sentenceId = 1L;
        final var tagIds = List.of(1L, 2L);

        final var request = new PhraseCreateRequest(sentenceId, startIndex, endIndex, meaning, tagIds);

        final var mvcResponse = mockMvc.perform(post("/v1/phrases")
                                               .contentType(MediaType.APPLICATION_JSON)
                                               .content(objectMapper.writeValueAsString(request)))
                                       .andDo(print())
                                       .andExpect(status().isOk())
                                       .andReturn();

        final var response = objectMapper.readValue(mvcResponse.getResponse().getContentAsString(), PhraseCreatedResponse.class);

        // then
        final var phrase = phraseRepository.getById(response.id());

        assertEquals(meaning, phrase.getMeaning());
        assertEquals(sentenceId, phrase.getSentence().getId());
        assertEquals(startIndex, phrase.getStartIndex());
        assertEquals(endIndex, phrase.getEndIndex());
        assertEquals(tagIds.size(), phrase.getTags().size());
    }
}
