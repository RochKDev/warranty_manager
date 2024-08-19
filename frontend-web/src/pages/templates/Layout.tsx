import {Navbar} from "../../components/Navbar.tsx";
import {Outlet} from "react-router-dom";
import {Footer} from "../../components/Footer.tsx";

export const Layout = () => {
    return (
        <div>
            <Navbar />
            <Outlet />
            <Footer />
        </div>
    );
}