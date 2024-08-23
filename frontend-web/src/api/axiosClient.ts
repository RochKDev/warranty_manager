import axios, { AxiosError } from 'axios';
import { useContext } from 'react';
import { AuthContext } from '../contexts/AuthProvider';

const axiosClient = axios.create({
    baseURL: '',
});

axiosClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

axiosClient.interceptors.response.use(
    (response) => response,
    (error: AxiosError) => {
        if (error.response?.status === 401) {
            const { logout } = useContext(AuthContext);
            logout();
        }
        return Promise.reject(error);
    }
);

export default axiosClient;
