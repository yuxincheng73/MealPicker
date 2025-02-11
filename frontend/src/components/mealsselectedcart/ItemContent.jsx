import { useState } from "react";
import { HiOutlineTrash } from "react-icons/hi";
import SetQuantity from "./SetQuantity";
import { useDispatch } from "react-redux";
import { decreaseCartQuantity, increaseCartQuantity, removeFromCart } from "../../store/actions";
import toast from "react-hot-toast";
import truncateText from "../../utils/truncateText";
import { Button, FormControl, InputLabel, MenuItem, Select, Tooltip, Checkbox } from "@mui/material";

const ItemContent = ({
    recipeId,
    recipeName,
    image,
    description,
    quantity,
    mealsSelectedId,
  }) => {
    const [currentQuantity, setCurrentQuantity] = useState(quantity);
    const dispatch = useDispatch();
    const [date, setDate] = useState("");

    const handleQtyIncrease = (mealItems) => {
        dispatch(increaseCartQuantity(
            mealItems,
            toast,
            currentQuantity,
            setCurrentQuantity
        ));
    };

    const handleQtyDecrease = (mealItems) => {
        if (currentQuantity > 1) {
            const newQuantity = currentQuantity - 1;
            setCurrentQuantity(newQuantity);
            dispatch(decreaseCartQuantity(mealItems, newQuantity));
        }
    };

    const removeItemFromCart = (mealItems) => {
        dispatch(removeFromCart(mealItems, toast));
    };

    const handleDateChange = (event) => {
        const selectedDate = event.target.value;
        setDate(event.target.value);
    };

    const weekDates = [
        "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"
    ];
    
    return (
        <div className="grid md:grid-cols-5 grid-cols-3 md:text-md text-sm gap-4   items-center  border-[1px] border-slate-200  rounded-md  lg:px-4  py-4 p-2">
            <div className="md:col-span-2 justify-self-start flex  flex-col gap-2 ">
                <div className="flex md:flex-row flex-col lg:gap-4 sm:gap-3 gap-0 items-start ">
                   <h3 className="lg:text-[17px] text-sm font-semibold text-slate-600">
                    {truncateText(recipeName)}
                   </h3>
                </div>

                <div className="md:w-36 sm:w-24 w-12">
                    <img 
                        src={image}
                        alt={recipeName}
                        className="md:h-36 sm:h-24 h-12 w-full object-cover rounded-md"/>
                

                <div className="flex items-start gap-5 mt-3">
                    <button
                        onClick={() => removeItemFromCart({
                            image,
                            recipeName,
                            description,
                            recipeId,
                            quantity,
                        })}
                        className="flex items-center font-semibold space-x-2 px-4 py-1 text-xs border border-rose-600 text-rose-600 rounded-md hover:bg-red-50 transition-colors duration-200">
                        <HiOutlineTrash size={16} className="text-rose-600"/>
                        Remove
                    </button>
                    </div>
                </div>
            </div>

            <div className="justify-self-center">
                <SetQuantity 
                    quantity={currentQuantity}
                    cardCounter={true}
                    handeQtyIncrease={() => handleQtyIncrease({
                        image,
                        recipeName,
                        description,
                        recipeId,
                        quantity,
                    })}
                    handleQtyDecrease={() => {handleQtyDecrease({
                        image,
                        recipeName,
                        description,
                        recipeId,
                        quantity,
                    })}}/>
            </div>

            <div className="justify-self-center">
                <FormControl
                    className="text-slate-800 border-slate-700"
                    variant="outlined"
                    size="medium">
                        <InputLabel id="cuisine-select-label">Date</InputLabel>
                        <Select
                            labelId="cuisine-select-label"
                            value={date}
                            onChange={handleDateChange}
                            label="Date"
                            className="min-w-[120px] text-slate-800 border-slate-700"
                         >
                            {weekDates.map((date, index) => (
                                <MenuItem key={index} value={date}>
                                    {date}
                                </MenuItem>
                            ))}
                         </Select>
                </FormControl>
            </div>
        </div>
    )
};

export default ItemContent;