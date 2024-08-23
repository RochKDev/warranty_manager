import { Menubar } from 'primereact/menubar';
import { Button } from 'primereact/button';
import { useState } from 'react';
import { LoginModal } from './modals/LoginModal';
import { RegisterModal } from './modals/RegisterModal';

export const Navbar = () => {
    const [isLoginVisible, setLoginVisible] = useState(false);
    const [isRegisterVisible, setRegisterVisible] = useState(false);

    const items = [
        {
            label: 'Warranty Manager',
            icon: 'pi pi-home',
        },
        {
            label: 'Dashboard',
            icon: 'pi pi-objects-column',
        },
        {
            label: 'Contact',
            icon: 'pi pi-envelope',
            disabled: true,
            visible: false,
        },
    ];

    const start = <i className="pi pi-bars"></i>;
    const end = <Button label="Sign Up" onClick={() => setLoginVisible(true)} />;

    const switchToRegister = () => {
        setLoginVisible(false);
        setRegisterVisible(true);
    };

    const switchToLogin = () => {
        setRegisterVisible(false);
        setLoginVisible(true);
    };

    return (
        <div className="w-full">
            <Menubar className="w-full justify-center" model={items} start={start} end={end} />
            <LoginModal
                visible={isLoginVisible}
                onClose={() => setLoginVisible(false)}
                onSwitchToRegister={switchToRegister}
            />
            <RegisterModal
                visible={isRegisterVisible}
                onClose={() => setRegisterVisible(false)}
                onSwitchToLogin={switchToLogin}
            />
        </div>
    );
};
