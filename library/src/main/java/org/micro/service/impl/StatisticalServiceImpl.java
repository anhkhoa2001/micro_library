package org.micro.service.impl;

import org.micro.dto.StatisticalByAuthor;
import org.micro.dto.StatisticalByCharacter;
import org.micro.dto.StatisticalByType;
import org.micro.service.StatisticalService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<StatisticalByAuthor> getAllByAuthor() {
        try {
            String query = "select ra.name as author_name, b.author_id, count(b.name) as total from rest_book b\n" +
                    " inner join rest_author ra on b.author_id = ra.id\n" +
                    " group by b.author_id;";
            List<Object[]> result = entityManager.createNativeQuery(query).getResultList();
            List<StatisticalByAuthor> sbas = new ArrayList<>();
            for(int i=0; i<result.size(); i++) {
                Object[] objects = result.get(i);
                StatisticalByAuthor sba = new StatisticalByAuthor();
                sba.setAuthor_name(objects[0] == null ? null : (objects[0]).toString());
                sba.setAuthor_id(objects[1] == null ? 0 : Integer.parseInt((objects[1]).toString()));
                sba.setTotal(objects[2] == null ? null : Integer.parseInt((objects[2]).toString()));
                sbas.add(sba);
            }
            return sbas;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<StatisticalByType> getAllByType() {
        try {
            String query = "select b.book_type_id, rb.name, count(b.name) as total from rest_book b\n" +
                    " inner join rest_book_type rb on b.book_type_id = rb.type_id\n" +
                    " group by b.book_type_id;";
            List<Object[]> result = entityManager.createNativeQuery(query).getResultList();
            List<StatisticalByType> sbts = new ArrayList<>();
            for(int i=0; i<result.size(); i++) {
                Object[] objects = result.get(i);
                StatisticalByType sbt = new StatisticalByType();
                sbt.setBook_type_id(objects[0] == null ? 0 : Integer.parseInt((objects[0]).toString()));
                sbt.setBook_type_name(objects[1] == null ? null : (objects[1]).toString());
                sbt.setTotal(objects[2] == null ? 0 : Integer.parseInt((objects[2]).toString()));

                sbts.add(sbt);
            }
            return sbts;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public List<StatisticalByCharacter> getAllByFirstCharacter() {
        List<StatisticalByCharacter> result = new ArrayList<>();
        for(char c = 'A'; c <= 'Z'; ++c){
            String query = "select count(*) from rest_book b where upper(name) like '" + c + "%'";
            Object object = entityManager.createNativeQuery(query)
                    .getSingleResult();

            StatisticalByCharacter sbc = new StatisticalByCharacter();
            sbc.setBook_character(c);
            sbc.setTotal(object == null ? 0 : Integer.parseInt(object.toString()));

            result.add(sbc);
        }

        return result;
    }
}
