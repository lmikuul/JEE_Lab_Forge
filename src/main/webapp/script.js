window.addEventListener('load', () => {
    //const loginForm = document.getElementById('loginForm');
    //const logoutForm = document.getElementById('logoutForm');

    //loginForm.addEventListener('submit', event => loginAction(event));
    //logoutForm.addEventListener('submit', event => logoutAction(event));
});

/**
 * Clears all children of the provided element
 * @param {HTMLElement} element parent element
 */
function clearElementChildren(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}
/**
 *
 * @returns {string} context root (application name) to be used as a root for rest requests
 */
function getContextRoot() {
    return '/' + location.pathname.split('/')[1];
}