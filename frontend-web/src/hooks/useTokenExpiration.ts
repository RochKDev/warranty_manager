import { useEffect, useContext } from 'react';
import { jwtDecode, JwtPayload } from 'jwt-decode';
import { AuthContext } from '../contexts/AuthProvider';

const useTokenExpiration = () => {
    const { logout } = useContext(AuthContext)!;

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            const decodedToken: JwtPayload = jwtDecode(token);
            const expirationTime = decodedToken.exp ? decodedToken.exp * 1000 - Date.now() : 0;

            if (expirationTime <= 0) {
                logout();
            } else {
                const timer = setTimeout(() => {
                    logout();
                }, expirationTime);

                return () => clearTimeout(timer);
            }
        }
    }, [logout]);
};

export default useTokenExpiration;
