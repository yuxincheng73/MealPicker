import React, { useState } from 'react'
import './App.css'

import { BrowserRouter as Router, Routes, Route } from 'react-router'
// import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Home from './components/home/Home'
import Navbar from './components/shared/Navbar'
import About from './components/About'
import { Toaster } from 'react-hot-toast'
import LogIn from './components/auth/LogIn'
import PrivateRoute from './components/PrivateRoute'
import Register from './components/auth/Register'
import Recipes from './components/recipes/Recipes'
import MealsSelectedCart from './components/mealsselectedcart/MealsSelectedCart'

function App() {
  return (
    <React.Fragment>
      <Router>
        <Navbar />
        <Routes>
          <Route path='/' element={ <Home />}/>
          <Route path='/recipes' element={ <Recipes />}/>
          <Route path='/about' element={ <About />}/>
          <Route path='/mealsselectedcart' element={ <MealsSelectedCart />}/>
        
          <Route path='/' element={<PrivateRoute publicPage />}>
            <Route path='/login' element={ <LogIn />}/>
            <Route path='/register' element={ <Register />}/>
          </Route>
        </Routes>
      </Router>
      <Toaster position='bottom-center'/>
    </React.Fragment>
  )
}

export default App;
