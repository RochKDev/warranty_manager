import React, {useContext, useState} from "react";
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import './LoginModal.css';
import { useMutation } from 'react-query';
import {loginUser} from "../../api/authApi.ts";
import {AuthContext} from "../../contexts/AuthProvider.tsx";

interface LoginModalProps {
    visible: boolean;
    onClose: () => void;
    onSwitchToRegister: () => void;
}

export const LoginModal = ({ visible, onClose, onSwitchToRegister }: LoginModalProps) => {

    const { login } = useContext(AuthContext);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const mutation = useMutation(loginUser, {
        onSuccess: (data) => {
            login(data.token); // Use the login context method to store token
            onClose(); // Close the modal after successful login
        },
        onError: (error) => {
            console.error(error);
            // Handle error: Show an error message to the user
        }
    });

    const handleLogin = () => {
        mutation.mutate({ email, password });
    };

    return (
        <Dialog visible={visible} modal onHide={onClose} className="login-dialog" header="Login">
            <div className="login-container">
                <div className="input-group">
                    <label htmlFor="email" className="input-label">Email</label>
                    <InputText id="email" className="p-inputtext-sm" onChange={(e) => setEmail(e.target.value)}/>
                </div>
                <div className="input-group">
                    <label htmlFor="password" className="input-label">Password</label>
                    <InputText id="password" className="p-inputtext-sm" type="password" onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <Button label="Sign-In" className="button-signin" onClick={handleLogin} disabled={mutation.isLoading}/>
                <div className="register-link">
                    <p>
                        Don't have an account? <a onClick={onSwitchToRegister} className="link">Sign Up</a>
                    </p>
                </div>
            </div>
        </Dialog>
    );
};
