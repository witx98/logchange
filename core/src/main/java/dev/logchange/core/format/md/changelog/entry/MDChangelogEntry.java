package dev.logchange.core.format.md.changelog.entry;

import dev.logchange.core.format.md.MD;
import dev.logchange.core.model.ChangelogEntry;
import net.steppschuh.markdowngenerator.list.UnorderedListItem;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class MDChangelogEntry implements MD {

    private static final String entryFormat = "${title} ${merge_request} ${issues} ${links} ${authors}";

    private final ChangelogEntry entry;

    private final MDChangelogEntryMergeRequest mdMergeRequest;
    private final MDChangelogEntryIssues mdIssues;
    private final MDChangelogEntryLinks mdLinks;
    private final MDChangelogEntryAuthors mdAuthors;

    public MDChangelogEntry(ChangelogEntry entry) {
        this.entry = entry;
        this.mdMergeRequest = new MDChangelogEntryMergeRequest(entry.getMergeRequest());
        this.mdIssues = new MDChangelogEntryIssues(entry.getIssues());
        this.mdLinks = new MDChangelogEntryLinks(entry.getLinks());
        this.mdAuthors = new MDChangelogEntryAuthors(entry.getAuthors());
    }

    @Override
    public String toString() {
        return new UnorderedListItem(getEntry()).toString();
    }

    private String getEntry() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("title", entry.getTitle());
        valuesMap.put("merge_request", mdMergeRequest.toMD());
        valuesMap.put("issues", mdIssues.toMD());
        valuesMap.put("links", mdLinks.toMD());
        valuesMap.put("authors", mdAuthors.toMD());

        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        return sub.replace(entryFormat).replaceAll("\\s{2,}", " ");
    }
}