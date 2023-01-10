package org.micro.service;

import org.micro.dto.StatisticalByAuthor;
import org.micro.dto.StatisticalByCharacter;
import org.micro.dto.StatisticalByType;

import java.util.List;

public interface StatisticalService {

    List<StatisticalByAuthor> getAllByAuthor();
    List<StatisticalByType> getAllByType();
    List<StatisticalByCharacter> getAllByFirstCharacter();

}
