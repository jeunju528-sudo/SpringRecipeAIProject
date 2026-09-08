package com.sist.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostgresMapper {
	public List<Map<String, Object>> findSimilarRecipes(
			@Param("embedding") String embedding,
			@Param("limit") int limit);
}