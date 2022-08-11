package ee.ria.riha.domain;

import ee.ria.riha.client.StorageClient;
import ee.ria.riha.domain.model.Comment;
import ee.ria.riha.service.util.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Makes calls against RIHA-Storage and performs various operations on {@link Comment} entities.
 *
 * @author Valentin Suhnjov
 */
public class CommentRepository implements StorageRepository<Long, Comment> {

    private static final String COMMENT_PATH = "db/comment";
    private static final String COMMENT_TYPE_ISSUE_VIEW_PATH = "db/comment_type_issue_view";
    private static final String DASHBOARD_COMMENT_PATH = "/comment";

    private final StorageClient storageClient;

    public CommentRepository(StorageClient storageClient) {
        Assert.notNull(storageClient, "Storage client must be provided");
        this.storageClient = storageClient;
    }

    @Override
    @Cacheable("listComment")
    public PagedResponse<Comment> list(Pageable pageable, Filterable filterable) {
        return storageClient.list(COMMENT_PATH, pageable, filterable, Comment.class);
    }

    @Cacheable("listIssues")
    public PagedResponse<Comment> listIssues(Pageable pageable, Filterable filterable) {
        return storageClient.list(COMMENT_TYPE_ISSUE_VIEW_PATH, pageable, filterable, Comment.class);
    }

    @Cacheable("listDashboardIssues")
    public PagedGridResponse<Comment> listDashboardIssues(Pageable pageable, CompositeFilterRequest filterRequest) {
        return storageClient.list(DASHBOARD_COMMENT_PATH, filterRequest, pageable);
    }

    @Override
    @Cacheable("getComment")
    public Comment get(Long id) {
        return storageClient.get(COMMENT_PATH, id, Comment.class);
    }

    @Override
    @Cacheable("findComment")
    public List<Comment> find(Filterable filterable) {
        return storageClient.find(COMMENT_PATH, filterable, Comment.class);
    }

    @Override
    @CacheEvict(value={"listComment", "listIssues", "listDashboardIssues", "getComment", "findComment"}, allEntries = true)
    public List<Long> add(Comment entity) {
        return storageClient.create(COMMENT_PATH, entity);
    }

    @Override
    @CacheEvict(value={"listComment", "listIssues", "listDashboardIssues", "getComment", "findComment"}, allEntries = true)
    public void update(Long id, Comment entity) {
        storageClient.update(COMMENT_PATH, id, entity);
    }

    @Override
    @CacheEvict(value={"listComment", "listIssues", "listDashboardIssues", "getComment", "findComment"}, allEntries = true)
    public void remove(Long id) {
        storageClient.remove(COMMENT_PATH, id);
    }

}
