package com.hackerrank.sample.repository;

import com.hackerrank.sample.model.Question;
import java.util.List;

public interface QuestionRepository {
    List<Question> findQuestionsByItemId(String itemId);
}


