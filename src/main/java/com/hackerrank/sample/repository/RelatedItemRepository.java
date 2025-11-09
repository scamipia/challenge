package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.RelatedItem;
import java.util.List;

public interface RelatedItemRepository {
    List<RelatedItem> findRelatedByItemId(String itemId);
}


