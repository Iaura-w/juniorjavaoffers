import fetch from "unfetch";
import jwtDecode from "jwt-decode";

const checkStatusLogin = response => {
    if (response.ok) {
        localStorage.setItem("currentUser", JSON.stringify(response.headers.get("Authorization")));
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(response.statusText);
    error.message = response;
    console.log(response)
    return Promise.reject(error);
}

const checkStatus = response => {
    if (response.ok) {
        return response;
    }
    // convert non-2xx HTTP responses into errors:
    const error = new Error(response.statusText);
    error.message = response;
    console.log(response)
    return Promise.reject(error);
}

export const login = (username, password) =>
    fetch("/login", {
            headers: {
                "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({username, password})
        }
    ).then(checkStatusLogin);

export const register = (username, password) =>
    fetch("/register", {
            headers: {
                "Content-Type": "application/json"
            },
            method: "POST",
            body: JSON.stringify({username, password})
        }
    ).then(checkStatus);

export const getCurrentUser = () => {
    if (localStorage.getItem("currentUser") != null) {
        return localStorage.getItem("currentUser")
            .replace("Bearer ", "")
            .replaceAll("\"", "");
    }
}
export const getCurrentUserRole = () => {
    if (getCurrentUser() != null) {
        return jwtDecode(getCurrentUser()).authorities[0].authority;
    }
}

export const logout = () =>
    localStorage.removeItem("currentUser");