import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import ItemContent from "../mealsselectedcart/ItemContent";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router";
import { useEffect } from "react";
import { fetchMealsSelectedCart } from "../../store/actions";

const MealsSelected = () => {
    const dispatch = useDispatch();
    const { savedmealsselectedcart } = useSelector((state) => state.savedmealsselectedcarts);
    console.log("SAVED MEALS SELECTED: " + savedmealsselectedcart);

    useEffect(() => {
        dispatch(fetchMealsSelectedCart());
        console.log("SAVED MEALS SELECTED DISPATCH");
    }, [dispatch]);

    return (
        <div className="lg:px-14 sm:px-8 px-4 py-10">
            <div className="flex flex-col items-center mb-12">
                <h1 className="text-4xl font-bold text-gray-900 flex items-center gap-3">
                  <MdShoppingCart size={36} className="text-gray-700" />
                    Your Meals
                </h1>
                <p className="text-lg text-gray-600 mt-2">All your selected items</p>
            </div>

            <div className="grid md:grid-cols-5 grid-cols-3 gap-4 pb-2 font-semibold items-center">
                <div className="md:col-span-2 justify-self-start text-lg text-slate-800 lg:ps-4">
                    Recipe
                </div>

                <div className="justify-self-center text-lg text-slate-800">
                    Quantity
                </div>

                <div className="justify-self-center text-lg text-slate-800">
                    Date
                </div>
            </div>

            <div>
                {savedmealsselectedcart && savedmealsselectedcart.length > 0 &&
                    savedmealsselectedcart.map((item, i) => <ItemContent key={i} {...item}/>)}
            </div>

            <div className="border-t-[1.5px] border-slate-200 py-4 flex sm:flex-row sm:px-0 px-2 flex-col sm:justify-between gap-4">
                <div></div>
                <div className="flex text-sm gap-1 flex-col">

                    <Link className="flex gap-2 items-center w-full justify-center mt-2 text-slate-500" to="/recipes">
                        <MdArrowBack />
                        <span>Continue Browsing</span>
                    </Link>
                </div>
            </div>
        </div>
    );
}

export default MealsSelected;