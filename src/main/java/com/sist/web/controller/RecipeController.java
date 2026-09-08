package com.sist.web.controller;

/*
 *    전체 구조 : HTML (재료 선택) => 사용자 
 *              |
 *         RecipeController : 화면 출력 
 *              |
 *            재료선택 
 *              |
 *          RecipeRestController 
 *              |
 *          ----------------------
 *            RecipeService 
 *            Mapper 
 *          ----------------------
 *              |
 *            EmbeddingModel 
 *            String -> float[]
 *              |
 *          ---------------------
 *             vector => [0.1,0.2....]
 *          ---------------------
 *              |
 *            Mapper 
 *              => findSimilarRecipe() => 유사도 
 *              |
 *         -------------------------
 *           PostgreSQL + pgVector 
 *           embedding => 검색 
 *           Cosine Distance => 가까운 거리 측정 (실수) 
 *                              String => float
 *         -------------------------
 *              |  => vectorDB
 *             distance => 작은 순 : 맛집 / 쇼핑몰 
 *              |
 *             ORDER BY => LIMIT 5
 *              |
 *          ---------------------
 *            재료 비교 
 *            |
 *            AVA (보유) / SHO (부족) / SUB => 대체 
 *          
 *          ---------------------
 *              |
 *             ingredientRate 
 *             ingredients 
 *             missingingredient
 *          ---------------------
 *              |
 *           ThymeLeaf 
 *             => 충족률 : 75%
 *             => 재료 .... (X) 
 *    
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sist.web.service.*;
import lombok.RequiredArgsConstructor;

/**
 * ============================================================ RecipeController
 * ============================================================
 *
 * URL
 *
 * GET /recipe/recommend
 *
 * POST /recipe/recommend
 *
 * 처리 순서
 *
 * HTML ↓ 선택 재료 ↓ AJAX POST ↓ Controller ↓ RecipeVectorService ↓ EmbeddingModel
 * ↓ PostgreSQL pgVector ↓ JSON ↓ HTML 결과 출력
 * ============================================================
 */
@Controller
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {

	/*
	 * Vector 검색 Service
	 */
	private final RecipeService recipeVectorService;

	/**
	 * ======================================================== 레시피 추천 화면
	 * ========================================================
	 *
	 * GET
	 *
	 * http://localhost:8080/recipe/recommend
	 */
	@GetMapping("/recommand")
	public String recommendPage(Model model) {

		/*
		 * 처음에는 검색 결과가 없도록 설정
		 */
		model.addAttribute("recipes", Collections.emptyList());

		return "recipe/recommand";
	}

	/**
	 * ======================================================== 레시피 Vector 검색
	 * ========================================================
	 *
	 * POST
	 *
	 * /recipe/recommend
	 *
	 * JSON
	 *
	 * { "ingredients": [ "김치", "돼지고기", "두부" ] }
	 */
	@PostMapping("/recommand")
	@ResponseBody
	public Map<String, Object> recommend(@RequestBody Map<String, Object> request) {

		Map<String, Object> response = new HashMap<>();

		try {

			/*
			 * JSON에서 ingredients 추출
			 */
			Object ingredientObject = request.get("ingredients");

			/*
			 * 재료가 없는 경우
			 */
			if (ingredientObject == null) {

				response.put("success", false);

				response.put("message", "재료를 선택해주세요.");

				response.put("recipes", Collections.emptyList());

				return response;
			}

			/*
			 * JSON 배열 → List<String>
			 */
			List<String> ingredients = new ArrayList<>();

			if (ingredientObject instanceof List<?>) {

				List<?> list = (List<?>) ingredientObject;

				for (Object value : list) {

					if (value != null) {

						String ingredient = value.toString().trim();

						if (!ingredient.isEmpty()) {

							ingredients.add(ingredient);
						}
					}
				}
			}

			/*
			 * 선택 재료가 없는 경우
			 */
			if (ingredients.isEmpty()) {

				response.put("success", false);

				response.put("message", "재료를 한 개 이상 선택해주세요.");

				response.put("recipes", Collections.emptyList());

				return response;
			}

			/*
			 * ================================================= Vector 검색
			 * =================================================
			 */
			List<Map<String, Object>> recipes = recipeVectorService.recommendRecipes(ingredients);

			/*
			 * 정상 응답
			 */
			response.put("success", true);

			response.put("message", recipes.isEmpty() ? "추천 레시피가 없습니다." : "레시피 추천이 완료되었습니다.");

			response.put("recipes", recipes);

			/*
			 * 사용자가 선택한 재료도 반환
			 */
			response.put("selectedIngredients", ingredients);

			return response;

		} catch (Exception e) {

			/*
			 * 서버 로그
			 */
			e.printStackTrace();

			/*
			 * 오류 응답
			 */
			response.put("success", false);

			response.put("message", "레시피 검색 중 오류가 발생했습니다.");

			response.put("recipes", Collections.emptyList());

			return response;
		}
	}
}
