import { createContext, ReactNode, useState } from 'react';
import  { JwtPayload, jwtDecode } from 'jwt-decode';

interface AuthContextProps {
    user: JwtPayload | null;
    login: (token: string) => void;
    logout: () => void;
}

export const AuthContext = createContext<AuthContextProps>({
    user: null,
    login: () => {},
    logout: () => {}
});


export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState<JwtPayload | null>(null);


    const login = (token: string) => {
        localStorage.setItem('token', token);
        const decodedToken: JwtPayload = jwtDecode(token);
        setUser(decodedToken);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
