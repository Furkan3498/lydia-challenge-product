import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Register() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            await axios.post('http://localhost:8080/api/register', {
                username,
                password
            });

            // After registration, redirect to the login page
            alert('Registration successful! Please login.');
            navigate('/login');
        } catch (error) {
            alert('Registration failed: ' + (error.response ? error.response.data.message : 'Unknown error'));
        }
    };

    return (
        <div className="register-container">
            <h2>Register</h2>
            <form onSubmit={handleRegister}>
                <div>
                    <label>Username</label>
                    <input type="text" value={username} onChange={e => setUsername(e.target.value)} required />
                </div>
                <div>
                    <label>Password</label>
                    <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
                </div>
                <button type="submit">Register</button>
            </form>
            <p>Already have an account? <a href="/login">Login here</a></p>
        </div>
    );
}

export default Register;
