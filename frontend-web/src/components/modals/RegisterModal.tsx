import React from "react";
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { InputText } from 'primereact/inputtext';
import './RegisterModal.css';

interface RegisterModalProps {
    visible: boolean;
    onClose: () => void;
    onSwitchToLogin: () => void;
}

export const RegisterModal = ({ visible, onClose, onSwitchToLogin }: RegisterModalProps) => {
    return (
        <Dialog visible={visible} modal onHide={onClose} className="register-dialog" header="Register">
            <div className="register-container">
                <div className="input-group">
                    <label htmlFor="full-name" className="input-label">Full Name</label>
                    <InputText id="full-name" className="p-inputtext-sm" />
                </div>
                <div className="input-group">
                    <label htmlFor="email" className="input-label">Email</label>
                    <InputText id="email" className="p-inputtext-sm" />
                </div>
                <div className="input-group">
                    <label htmlFor="password" className="input-label">Password</label>
                    <InputText id="password" className="p-inputtext-sm" type="password" />
                </div>
                <div className="input-group">
                    <label htmlFor="confirm-password" className="input-label">Confirm Password</label>
                    <InputText id="confirm-password" className="p-inputtext-sm" type="password" />
                </div>
                <Button label="Register" onClick={onClose} className="button-register" />
                <div className="login-link">
                    <p>
                        Already have an account? <a onClick={onSwitchToLogin} className="link">Sign In</a>
                    </p>
                </div>
            </div>
        </Dialog>
    );
};
