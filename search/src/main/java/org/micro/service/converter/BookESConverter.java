package org.micro.service.converter;

import org.micro.dto.AuthorDTO;
import org.micro.dto.BookDTO;
import org.micro.dto.BookTypeDTO;
import org.micro.model.AuthorES;
import org.micro.model.BookES;
import org.micro.model.BookTypeES;
import org.springframework.stereotype.Component;

@Component
public class BookESConverter {

    public BookES convertModel2ES(BookDTO source) {
        if(source == null) {
            return null;
        }

        BookES target = new BookES();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setContent(source.getContent());
        target.setType_id(source.getBook_type().getType_id());
        target.setAuthor_id(source.getAuthor().getId());

        return target;
    }

    public AuthorES convertAuthorModel2AuthorES(AuthorDTO source) {
        if(source == null) {
            return null;
        }

        AuthorES target = new AuthorES();
        target.setId(source.getId());
        target.setName(source.getName());

        return target;
    }

    public BookTypeES convertBookTypeModel2BookTypeES(BookTypeDTO source) {
        if(source == null) {
            return null;
        }

        BookTypeES target = new BookTypeES();
        target.setId(source.getType_id());
        target.setName(source.getName());

        return target;
    }
}
