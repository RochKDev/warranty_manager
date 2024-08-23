import {Navbar} from "../../components/Navbar.tsx";
import {Outlet} from "react-router-dom";
import {Footer} from "../../components/Footer.tsx";

export const Layout = () => {
    return (
        <div className="m-0">
            <Navbar />
            <Outlet />
            <Footer />
        </div>
    );
}