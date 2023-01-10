package org.micro.service;


import org.micro.dto.AuthorDTO;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.BookDTO;
import org.micro.dto.BookTypeDTO;
import org.micro.model.BookES;

import java.util.List;
import java.util.Map;

public interface BookESService {

    List<BookES> synchronize(List<BookDTO> books, List<AuthorDTO> authors,
                             List<BookTypeDTO> bookTypes);

    List<BookES> findByName(String name);

    AuthorizationDTO validateHeader(Map<String, String> headerParam);

    List<BookDTO> getAllOnLibrary(Map<String, String> headerParam);
    List<AuthorDTO> getAllAuthorOnLibrary(Map<String, String> headerParam);
    List<BookTypeDTO> getAllBookTypeOnLibrary(Map<String, String> headerParam);
}
