export const authHeader = () => {
    const currentUserToken = JSON.parse(localStorage.getItem("currentUser"));
    if (currentUserToken != null) {
        return currentUserToken;
    }
}