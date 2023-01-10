package org.micro.service;

import org.micro.dto.BookDTO;
import org.micro.dto.BorrowBookDTO;
import org.micro.dto.UserDTO;

import java.util.List;

public interface JobService {
    List<BorrowBookDTO> getAllBorrow();
    UserDTO getUserById(Integer id);
    BookDTO getBookById(Integer id);
}
