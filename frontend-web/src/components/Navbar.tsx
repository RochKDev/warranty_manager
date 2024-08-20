import {Menubar} from 'primereact/menubar';
import {MenuItem} from 'primereact/menuitem';
import {Button} from 'primereact/button';
import {PrimeIcons} from 'primereact/api';

import 'primeicons/primeicons.css';
import {useState} from "react";


export const Navbar = () => {
    const [receipt, setReceipt] = useState<string>("Receipts");
    const [product, setProduct] = useState<string>("Products");

    const items: MenuItem[] = [
        {
            label: 'Warranty Manager',
            icon: PrimeIcons.HOME,
        },
        {
            label: 'Dashboard',
            icon: 'pi pi-objects-column'
        },
        {
            label: 'Contact',
            icon: 'pi pi-envelope',
            disabled: true,
            visible : false
        }
    ];

    const start = (
        <div>
            <i className="pi pi-bars"></i>
        </div>
    );
    const end = (
        <div className="flex align-items-center gap-2">
            <Button label="Sign Up"/>
        </div>
    );

    return (
        <div className="w-full">
            <Menubar className="w-full justify-center" model={items} start={start} end={end}/>
        </div>
    )
}
