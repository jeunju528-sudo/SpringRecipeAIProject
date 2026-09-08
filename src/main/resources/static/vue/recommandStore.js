const {defineStore} = Pinia
const {ref} = Vue
const useRecipeStore = defineStore('recipe',
	()=>{
		const selectedIngredients = ref([])
		const selectedCategory = ref("all")
		const loading = ref(false)
		const errorMessage = ref('')
		const ingredients = 
		[
		    {
		        name:"돼지고기",
		        icon:"🥩",
		        category:"육류"
		    },
		    {
		        name:"소고기",
		        icon:"🥩",
		        category:"육류"
		    },
		    {
		        name:"닭고기",
		        icon:"🍗",
		        category:"육류"
		    },
		    {
		        name:"김치",
		        icon:"🥬",
		        category:"채소"
		    },
		    {
		        name:"대파",
		        icon:"🌱",
		        category:"채소"
		    },
		    {
		        name:"양파",
		        icon:"🧅",
		        category:"채소"
		    },
		    {
		        name:"감자",
		        icon:"🥔",
		        category:"채소"
		    },
		    {
		        name:"당근",
		        icon:"🥕",
		        category:"채소"
		    },
		    {
		        name:"마늘",
		        icon:"🧄",
		        category:"채소"
		    },
		    {
		        name:"계란",
		        icon:"🥚",
		        category:"계란/유제품"
		    },
		    {
		        name:"두부",
		        icon:"🧊",
		        category:"계란/유제품"
		    },
		    {
		        name:"우유",
		        icon:"🥛",
		        category:"계란/유제품"
		    },
		    {
		        name:"치즈",
		        icon:"🧀",
		        category:"계란/유제품"
		    },
		    {
		        name:"쌀",
		        icon:"🍚",
		        category:"곡류"
		    },
		    {
		        name:"밀가루",
		        icon:"🌾",
		        category:"곡류"
		    },
		    {
		        name:"라면",
		        icon:"🍜",
		        category:"곡류"
		    },
		    {
		        name:"고춧가루",
		        icon:"🌶️",
		        category:"양념"
		    },

		    {
		        name:"고추장",
		        icon:"🫙",
		        category:"양념"
		    },

		    {
		        name:"된장",
		        icon:"🫙",
		        category:"양념"
		    },
		    {
		        name:"간장",
		        icon:"🍶",
		        category:"양념"
		    },
		    {
		        name:"소금",
		        icon:"🧂",
		        category:"양념"
		    },
		    {
		        name:"참치",
		        icon:"🐟",
		        category:"수산물"
		    },
		    {
		        name:"고등어",
		        icon:"🐟",
		        category:"수산물"
		    },
		    {
		        name:"새우",
		        icon:"🦐",
		        category:"수산물"
		    }
		]
		const searchKeyword = ref('') // v-model 연결되는 검색어
		const filteredIngredients = compute(()=>{
			const keyword = searchKeyword.value.trim().toLowerCase()
			return ingredients.value.filter(ingredient => {
				const categoryMatch = selectedCategory.value === 'all' || selectedCategory.value === ingredient.category
				const searchMatch = ingredient.name.toLowerCase().includes(keyword)
				return categoryMatch && searchMatch
			})
		})
		
		function toggleIngredient(name){
			// 현재 선택 여부 확인
			const index = selectedIngredients.value.indexOf(name)
			if(index!==-1){ // 선택이 된 상태
				selectedIngredients.value.slice(index,1)
			}
			else{ // 선택이 안 됐을 때
				selectedIngredients.value.push(name)
			}
			
		}
	}
)