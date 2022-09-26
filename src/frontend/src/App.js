import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import {Home} from "./components/Home"
import {Box} from "@mui/material";
import {Navbar} from "./components/Navbar";
import {Login} from "./components/Login";
import {AddOffer} from "./components/AddOffer";
import {getCurrentUserRole} from "./services/auth";
import {Register} from "./components/Register";
import {PageNotFound} from "./components/PageNotFound";

function App() {
    const userRole = getCurrentUserRole();
    return (
        <Box>
            <Router>
                <Navbar/>
                <Routes>
                    <Route exact path="/" element={(userRole) ? <Home/> : <Login/>}></Route>
                    <Route path="/add-offer"
                           element={(userRole && userRole.includes("ROLE_ADMIN")) ?
                               <AddOffer/> : ((userRole) ?
                                   <Home/> : <Login/>)}>
                    </Route>
                    <Route path="/signup" element={(userRole) ? <Home/> : <Register/>}></Route>
                    <Route path="*" element={<PageNotFound/>}/>
                </Routes>
            </Router>
        </Box>

    );
}

export default App;
