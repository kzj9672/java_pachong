package top.sellet.mapper;

import top.sellet.entity.Movie;

import java.util.List;

/**
 * @author mo
 */
public interface MovieMapper {
    /**
     * 爬到的数据插入数据库
     * @param movie
     */
    void insert(Movie movie);

    /**
     * 查询所有
     * @return
     */
    List<Movie> findAll();
}
