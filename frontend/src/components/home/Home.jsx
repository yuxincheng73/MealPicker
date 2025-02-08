import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import { fetchRecipes } from "../../store/actions";
import RecipeCard from "../shared/RecipeCard";
import Loader from "../shared/Loader";
import { FaExclamationTriangle } from "react-icons/fa";

const Home = () => {
    const dispatch = useDispatch();
    const { recipes } = useSelector((state) => state.recipes);
    const { isLoading, errorMessage } = useSelector(
        (state) => state.errors
    );

    useEffect(() => {
        dispatch(fetchRecipes());
    }, [dispatch]);
    
    return (
        <div className="lg:px-14 sm:px-8 px-4">
            <div className="py-6">
                {/* Replace with grid of Carts if user is logged in */}
            </div>
            
            <div className="py-5">
                <div className="flex flex-col justify-center items-center space-y-2">
                    <h1 className="text-slate-800 text-4xl font-bold"> Recipes</h1>
                        <span className="text-slate-700">
                            Discover our handpicked selection of top-rated recipes just for you!
                        </span>
                    
                </div>

                {isLoading ? (
                    <Loader />
                ) : errorMessage ? (
                    <div className="flex justify-center items-center h-[200px]">
                        <FaExclamationTriangle className="text-slate-800 text-3xl mr-2"/>
                        <span className="text-slate-800 text-lg font-medium">
                            {errorMessage}
                        </span>
                    </div>
                ) : (
            <div className="pb-6 pt-14 grid 2xl:grid-cols-4 lg:grid-cols-3 sm:grid-cols-2 gap-y-6 gap-x-6">
                       {recipes && 
                       recipes?.slice(0,4)
                                .map((item, i) => <RecipeCard key={i} {...item} />
                        )}
                    </div>
                    )}
            </div>
        </div>
    )
}

export default Home;