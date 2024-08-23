import React from "react";
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import './LoginModal.css';

interface LoginModalProps {
    visible: boolean;
    onClose: () => void;
    onSwitchToRegister: () => void;
}

export const LoginModal = ({ visible, onClose, onSwitchToRegister }: LoginModalProps) => {
    return (
        <Dialog visible={visible} modal onHide={onClose} className="login-dialog" header="Login">
            <div className="login-container">
                <div className="input-group">
                    <label htmlFor="email" className="input-label">Email</label>
                    <InputText id="email" className="p-inputtext-sm" />
                </div>
                <div className="input-group">
                    <label htmlFor="password" className="input-label">Password</label>
                    <InputText id="password" className="p-inputtext-sm" type="password" />
                </div>
                <Button label="Sign-In" onClick={onClose} className="button-signin" />
                <div className="register-link">
                    <p>
                        Don't have an account? <a onClick={onSwitchToRegister} className="link">Sign Up</a>
                    </p>
                </div>
            </div>
        </Dialog>
    );
};
