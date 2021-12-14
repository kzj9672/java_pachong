package top.sellet.mapper;

import top.sellet.entity.ImageWeb;

import java.util.List;

/**
 * @author mo
 */

public interface WebMapper {
    /**
     * 爬到的数据插入数据库
     * @param  name
     * @param address
     */

    void insert(String name,String address);
    /**
     * 查询所有
     * @return
     */

    List<ImageWeb> findAll();
}
