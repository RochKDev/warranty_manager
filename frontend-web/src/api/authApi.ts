import axiosClient from './axiosClient';

export interface AuthResponse {
    token: string;
    type: string;
}

export interface LoginData {
    email: string;
    password: string;
}

export interface RegisterData {
    email: string;
    name: string;
    password: string;
}

export const loginUser = async (data: LoginData): Promise<AuthResponse> => {
    const response = await axiosClient.post<AuthResponse>('/auth/login', data);
    return response.data;
};

export const registerUser = async (data: RegisterData): Promise<any> => {
    const response = await axiosClient.post('/auth/register', data);
    return response.data;
};
