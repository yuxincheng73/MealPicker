import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { generateRecipe } from "../../store/actions";

const RecipeGenerator = () => {
    const dispatch = useDispatch();
    const [ingredients, setIngredients] = useState('');
    const [cuisine, setCuisine] = useState('any');
    const [categoryRecipe, setCategoryRecipe] = useState('');
    const [recipe, setRecipe] = useState('');
    const { generatedrecipe } = useSelector((state) => state.recipegenerator );


    const createRecipe = async () => {
        console.log("GENERATING RECIPE");
        dispatch(generateRecipe({ingredients: ingredients, categoryRecipe: categoryRecipe, cuisine: cuisine}));
        // try {
        //     const response = await fetch(`http://localhost:8080/api/recipe_generator?ingredients=${ingredients}&cuisine=${cuisine}&categoryRecipe=${categoryRecipe}`)
        //     const data = await response.text();
        //     console.log(data);
        //     setRecipe(data);
        // } catch (error) {
        //     console.error("Error generating recipe : ", error)
        // }
    };
    
    return (
        <div className=" m-0 p-20 items-center flex justify-center">
            <div className="flex flex-col items-center ">
                <div className="flex text-center justify-items-center">
                    <h1 className="font-bold text-2xl ">Recipe Generator</h1>
                </div>
                <div className="flex flex-col justify-items-center items-center">
                    <div>
                        <h2 className="text-center font-semibold mb-10">Generate a Recipe</h2>
                    </div>
                    <input
                        type="text"
                        value={ingredients}
                        onChange={(e) => setIngredients(e.target.value)}
                        placeholder="Enter ingredients (comma seperated)"
                        className="p-10 w-2xl mb-5 box-border border rounded-md"
                    />

                    <input
                        type="text"
                        value={cuisine}
                        onChange={(e) => setCuisine(e.target.value)}
                        placeholder="Enter cuisine type"
                        className="p-10 w-2xl mb-5 box-border border rounded-md"
                    />

                    <input
                        type="text"
                        value={categoryRecipe}
                        onChange={(e) => setCategoryRecipe(e.target.value)}
                        placeholder="Enter dietary restrictions"
                        className="p-10 w-2xl mb-5 box-border border rounded-md"
                    />
                    <div className="p-40 pt-0 pb-0 flex items-center">
                    <button onClick={createRecipe} className="px-20 py-5 cursor-pointer bg-emerald-400 font-bold rounded-md text-neutral-900">Create Recipe</button>
                    </div>
                    <div className="mt-20 text-left bg-[#f4f4f4] rounded-xl w-full">
                        <pre className="w-full p-10 box-border rounded-md overflow-y-auto text-black text-wrap text-xs md:text-md lg:text-xl leading-[1.6] border border-solid border-neutral-500 mb-20">{generatedrecipe}</pre>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RecipeGenerator;