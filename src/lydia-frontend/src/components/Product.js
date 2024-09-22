import React, { useState } from 'react';
import axios from 'axios';
import './Product.css';

function Product({ product, onPurchaseSuccess }) {
    const [quantity, setQuantity] = useState(1);

    const handlePurchase = () => {
        const token = localStorage.getItem('token');
        if (quantity <= 0) {
            alert('Please enter a valid quantity.');
            return;
        }

        axios.post('http://localhost:8080/api/purchase', {
            productId: product.id,
            quantity: quantity,
            price: product.price
        }, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(response => {
                alert(`Successfully purchased ${quantity} of ${product.name}!`);
                onPurchaseSuccess();  // Call the callback function to trigger product list refresh
            })
            .catch(error => {
                console.error('Error details:', error);
                if (error.response) {
                    alert(`Failed to purchase ${product.name}: ${error.response.data.message}`);
                } else if (error.request) {
                    alert(`No response from the server.`);
                } else {
                    alert(`Error in setting up the purchase request.`);
                }
            });
    };

    return (
        <li className="product-card">
            <h3>{product.name}</h3>
            <p>{product.description}</p>
            <p className="product-price">Price: ${product.price}</p>
            <p className="product-stock">Stock: {product.stock}</p>
            <div className="product-actions">
                <input
                    className="product-quantity"
                    type="number"
                    value={quantity}
                    min="1"
                    max={product.stock}
                    onChange={e => setQuantity(Math.max(1, parseInt(e.target.value)))}
                />
                <button className="buy-button" onClick={handlePurchase}>Buy</button>
            </div>
        </li>
    );
}

export default Product;
