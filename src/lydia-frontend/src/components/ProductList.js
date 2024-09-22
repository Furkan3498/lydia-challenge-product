import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Product from './Product';
import './ProductList.css';

function ProductList() {
    const [products, setProducts] = useState([]);

    // Fetch the products from the backend API
    const fetchProducts = () => {
        axios.get('http://localhost:8080/api/product', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then((response) => setProducts(response.data))
            .catch((error) => console.error("Error fetching products:", error));
    };

    useEffect(() => {
        fetchProducts();  // Fetch the products on component mount
    }, []);

    // This function will be called after a purchase to refresh the product list
    const handlePurchaseSuccess = () => {
        fetchProducts();  // Re-fetch the products to update stock
    };

    return (
        <div className="product-list">
            <h1>Products</h1>
            <ul>
                {products.map((product) => (
                    <Product key={product.id} product={product} onPurchaseSuccess={handlePurchaseSuccess} />
                ))}
            </ul>
        </div>
    );
}

export default ProductList;
