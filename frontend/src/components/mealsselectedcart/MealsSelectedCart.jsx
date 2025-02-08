import { MdArrowBack, MdShoppingCart } from "react-icons/md";
import { useDispatch, useSelector } from "react-redux";
import { Link } from "react-router";
import ItemContent from "./ItemContent";
import MealsSelectedCartEmpty from "./MealsSelectedCartEmpty";

const MealsSelectedCart = () => {
    const dispatch = useDispatch();
    const { mealsselectedcart } = useSelector((state) => state.mealsselectedcarts);
    const newmealsselectedcart = { ...mealsselectedcart };

    // newmealsselectedcart.totalPrice = mealsselectedcart?.reduce(
    //     (acc, cur) => acc + Number(cur?.specialPrice) * Number(cur?.quantity), 0
    // );

    if (!mealsselectedcart || mealsselectedcart.length === 0) return <CartEmpty />;

    return (
        <div className="lg:px-14 sm:px-8 px-4 py-10">
            <div className="flex flex-col items-center mb-12">
                <h1 className="text-4xl font-bold text-gray-900 flex items-center gap-3">
                  <MdShoppingCart size={36} className="text-gray-700" />
                    Your Meals
                </h1>
                <p className="text-lg text-gray-600 mt-2">All your selected items</p>
            </div>

            <div className="grid md:grid-cols-5 grid-cols-4 gap-4 pb-2 font-semibold items-center">
                <div className="md:col-span-2 justify-self-start text-lg text-slate-800 lg:ps-4">
                    Recipe
                </div>

                <div className="justify-self-center text-lg text-slate-800">
                    Quantity
                </div>
            </div>

            <div>
                {mealsselectedcart && mealsselectedcart.length > 0 &&
                    mealsselectedcart.map((item, i) => <ItemContent key={i} {...item}/>)}
            </div>

            <div className="border-t-[1.5px] border-slate-200 py-4 flex sm:flex-row sm:px-0 px-2 flex-col sm:justify-between gap-4">
                <div></div>
                <div className="flex text-sm gap-1 flex-col">
                    <Link className="w-full flex justify-end" to="/mealarrangement">
                    <button
                        onClick={() => {}}
                        className="font-semibold w-[300px] py-2 px-4 rounded-sm bg-customBlue text-white flex items-center justify-center gap-2 hover:text-gray-300 transition duration-500">
                        <MdShoppingCart size={20} />
                        Meal Arrangement
                    </button>
                    </Link>

                    <Link className="flex gap-2 items-center mt-2 text-slate-500" to="/recipes">
                        <MdArrowBack />
                        <span>Continue Browsing</span>
                    </Link>
                </div>
            </div>
        </div>
    );
};

export default MealsSelectedCart;