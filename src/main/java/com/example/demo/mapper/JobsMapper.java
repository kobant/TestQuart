package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: liaocongcong
 * @Date: 2020/12/22 15:57
 */
@Mapper
public interface JobsMapper {

	Map get(Long id);

	List<Map> list();

	int save (Map job);

	int update(Map task);

	int remove(Long id);

	int removeBatch(Long[] ids);

}
