import {createBrowserRouter} from "react-router-dom";
import {HomePage} from "./pages/HomePage.tsx";
import {Layout} from "./pages/templates/Layout.tsx";
import {LoginPage} from "./pages/LoginPage.tsx";

export const router = createBrowserRouter([

    {
        path : '/',
        element : <Layout/>,
        children : [
            {
                path: '/',
                element: (
                        <HomePage />
                ),
            },
            {
                path: '/login',
                element: <LoginPage />,
            },
        ]
    }

])

