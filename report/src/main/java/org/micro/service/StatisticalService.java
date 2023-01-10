package org.micro.service;

import org.micro.dto.StatisticalByAuthor;
import org.micro.dto.StatisticalByCharacter;
import org.micro.dto.StatisticalByType;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticalService {
    List<StatisticalByAuthor> getAllByAuthor(Map<String, String> headerParam);
    List<StatisticalByType> getAllByType(Map<String, String> headerParam);
    List<StatisticalByCharacter> getAllByFirstCharacter(Map<String, String> headerParam);
    Long countByTimer(Map<String, String> headerParam, Map<String, Object> body);
    Integer getBookIdByTimer(Map<String, String> headerParam, Map<String, Object> body);

    Object getBookById(Integer id, Map<String, String> headerParam);
}
