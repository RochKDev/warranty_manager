import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {RouterProvider} from "react-router-dom";
import {router} from "./router.tsx";
import 'primereact/resources/themes/saga-blue/theme.css';  // or other theme
import 'primereact/resources/primereact.min.css';          // core css
import 'primeicons/primeicons.css';                        // icons
import 'primeflex/primeflex.css';

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
)
