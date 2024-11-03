package dev.logchange.core;

import dev.logchange.core.application.changelog.repository.ChangelogRepository;
import dev.logchange.core.application.changelog.repository.VersionSummaryRepository;
import dev.logchange.core.application.changelog.service.generate.GenerateChangelogService;
import dev.logchange.core.domain.changelog.command.GenerateChangelogUseCase;
import dev.logchange.core.domain.changelog.command.GenerateChangelogUseCase.GenerateChangelogCommand;
import dev.logchange.core.domain.config.model.Config;
import dev.logchange.core.infrastructure.persistance.changelog.FileChangelogRepository;
import dev.logchange.core.infrastructure.persistance.changelog.FileVersionSummaryRepository;
import dev.logchange.core.infrastructure.persistance.file.FileRepository;
import dev.logchange.core.infrastructure.query.file.FileReader;
import org.codehaus.plexus.util.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class GenerateChangelogIntegrationTest {

    private static final String PATH = "src/test/resources/GenerateChangelogIntegrationTest/";

    @BeforeEach
    void init() throws IOException {
        new File(PATH + "CHANGELOG.md").createNewFile();
    }


    @AfterEach
    void cleanup() {
        new File(PATH + "CHANGELOG.md").delete();
    }

    @Test
    void shouldMatchExpectedChangelog() throws IOException {
        //given:
        File changelogInputDir = new File(PATH + "changelog");
        File changelogOutputFile = new File(PATH + "CHANGELOG.md");
        File expectedChangelogOutputFile = new File(PATH + "EXPECTED_CHANGELOG.md");

        FileRepository fr = FileRepository.of(changelogOutputFile);
        ChangelogRepository repository = new FileChangelogRepository(changelogInputDir, Config.EMPTY, new FileReader(), fr, fr);
        VersionSummaryRepository versionSummaryRepository = new FileVersionSummaryRepository(changelogInputDir, Config.EMPTY);
        GenerateChangelogUseCase generateChangelogUseCase = new GenerateChangelogService(repository, versionSummaryRepository);
        GenerateChangelogCommand command = GenerateChangelogCommand.of();

        //when:
        generateChangelogUseCase.handle(command);

        //then:
        String expectedContent = FileUtils.fileRead(expectedChangelogOutputFile);
        String actualContent = FileUtils.fileRead(changelogOutputFile);
        assertThat(actualContent).isEqualToIgnoringNewLines(expectedContent);
    }
}
