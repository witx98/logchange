package dev.logchange.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChangelogEntry {
    private final String title;
    private final String mergeRequest;
    private final List<String> issues;
    private final List<ChangelogEntryLink> links;
    private final List<ChangelogEntryAuthor> authors;
}
