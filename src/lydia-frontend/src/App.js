import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from "./Login";
import Register from './Register';
import ProductList from './components/ProductList';
import PrivateRoute from './components/PrivateRoute';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/products" element={
                        <PrivateRoute>
                            <ProductList />
                        </PrivateRoute>
                    } />
                    <Route path="/" element={<Login />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
