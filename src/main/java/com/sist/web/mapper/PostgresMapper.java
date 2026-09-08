package com.sist.web.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PostgresMapper {
	// <select id="findSimilarRecipes" resultType="hashmap">
	public List<Map<String, Object>> findSimilarRecipes(
			@Param("embedding") String embedding,
			@Param("limit") int limit);
}
