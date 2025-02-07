import { useState } from "react";
import { FaShoppingCart } from "react-icons/fa";
import RecipeViewModal from "./RecipeViewModal";
import truncateText from "../../utils/truncateText";
import { useDispatch } from "react-redux";
import { addToCart } from "../../store/actions";
import toast from "react-hot-toast";

const RecipeCard = ({
        recipeId,
        recipeName,
        image,
        description,
        quantity,
        about = false,
}) => {
    const [openRecipeViewModal, setOpenRecipeViewModal] = useState(false);
    const btnLoader = false;
    const [selectedViewRecipe, setSelectedViewRecipe] = useState("");
    const isAvailable = quantity && Number(quantity) > 0;
    const dispatch = useDispatch();

    const handleRecipeView = (recipe) => {
        if (!about) {
            setSelectedViewRecipe(recipe);
            setOpenRecipeViewModal(true);
        }
    };

    const addToCartHandler = (mealItems) => {
        dispatch(addToCart(mealItems, 1, toast));
    };

    return (
        <div className="border rounded-lg shadow-xl overflow-hidden transition-shadow duration-300">
            <div onClick={() => {
                handleRecipeView({
                    id: recipeId,
                    recipeName,
                    image,
                    description,
                    quantity,
                })
            }} 
                    className="w-full overflow-hidden aspect-[3/2]">
                <img 
                className="w-full h-full cursor-pointer transition-transform duration-300 transform hover:scale-105"
                src={image}
                alt={recipeName}>
                </img>
            </div>
            <div className="p-4">
                <h2 onClick={() => {
                handleRecipeView({
                    id: recipeId,
                    recipeName,
                    image,
                    description,
                    quantity,
                })
            }}
                    className="text-lg font-semibold mb-2 cursor-pointer">
                    {truncateText(recipeName, 50)}
                </h2>
                
                <div className="min-h-20 max-h-20">
                    <p className="text-gray-600 text-sm">
                        {truncateText(description, 80)}
                    </p>
                </div>

            { !about && (
                <div className="flex items-center justify-between">
                {/* {specialPrice ? (
                    <div className="flex flex-col">
                        <span className="text-gray-400 line-through">
                            ${Number(price).toFixed(2)}
                        </span>
                        <span className="text-xl font-bold text-slate-700">
                            ${Number(specialPrice).toFixed(2)}
                        </span>
                    </div>
                ) : (
                    <span className="text-xl font-bold text-slate-700">
                        {"  "}
                        ${Number(price).toFixed(2)}
                    </span>
                )} */}

                <button
                    disabled={!isAvailable || btnLoader}
                    onClick={() => addToCartHandler({
                        recipeId,
                        recipeName,
                        image,
                        description,
                        quantity,
                    })}
                    className="bg-blue-500 opacity-100 hover:bg-blue-600 text-white py-2 px-3 rounded-lg items-center transition-colors duration-300 w-36 flex justify-center">
                    <FaShoppingCart className="mr-2"/>
                    Add to Cart
                </button>
                </div>
            )}
                
            </div>
            <RecipeViewModal 
                open={openRecipeViewModal}
                setOpen={setOpenRecipeViewModal}
                recipe={selectedViewRecipe}
            />
        </div>
    )
}

export default RecipeCard;